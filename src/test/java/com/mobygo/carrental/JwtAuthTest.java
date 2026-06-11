package com.mobygo.carrental;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies the JWT flow: POST /api/auth/login issues a token, the token grants
 * the caller's role on protected endpoints, and bad credentials are rejected.
 */
@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    private String login(String username, String password) throws Exception {
        String body = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("token").asText();
    }

    @Test
    void loginReturnsTokenWithRole() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void adminTokenGrantsAccessToAdminEndpoint() throws Exception {
        String token = login("admin", "admin123");
        mvc.perform(post("/api/cars")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"Test\",\"model\":\"Car\",\"licensePlate\":\"ZH 999 ZZ\",\"category\":\"ECONOMY\",\"location\":{\"id\":1}}"))
            .andExpect(status().isOk());
    }

    @Test
    void userTokenIsForbiddenOnAdminEndpoint() throws Exception {
        String token = login("john", "user123");
        mvc.perform(get("/api/rentals").header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }

    @Test
    void invalidCredentialsRejected() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidTokenIsUnauthorized() throws Exception {
        mvc.perform(get("/api/rentals").header("Authorization", "Bearer not-a-real-token"))
            .andExpect(status().isUnauthorized());
    }
}
