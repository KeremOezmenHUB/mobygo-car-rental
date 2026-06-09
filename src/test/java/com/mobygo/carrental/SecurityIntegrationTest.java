package com.mobygo.carrental;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies the authorization rules: public reads stay open, admin-only
 * endpoints reject anonymous (401) and non-admin (403) callers, and admins
 * are allowed through. Uses the seeded users from DataInitializer.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired private MockMvc mvc;

    @Test
    void publicCanBrowseCars() throws Exception {
        mvc.perform(get("/api/cars")).andExpect(status().isOk());
    }

    @Test
    void anonymousCannotCreateCars() throws Exception {
        mvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"Test\",\"model\":\"Car\",\"licensePlate\":\"ZH 999 ZZ\",\"category\":\"ECONOMY\",\"location\":{\"id\":1}}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void regularUserCannotCreateCars() throws Exception {
        mvc.perform(post("/api/cars").with(httpBasic("john", "user123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"Test\",\"model\":\"Car\",\"licensePlate\":\"ZH 999 ZZ\",\"category\":\"ECONOMY\",\"location\":{\"id\":1}}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreateCars() throws Exception {
        mvc.perform(post("/api/cars").with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"Test\",\"model\":\"Car\",\"licensePlate\":\"ZH 999 ZZ\",\"category\":\"ECONOMY\",\"location\":{\"id\":1}}"))
            .andExpect(status().isOk());
    }

    @Test
    void regularUserCannotListAllRentals() throws Exception {
        mvc.perform(get("/api/rentals").with(httpBasic("john", "user123")))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanListAllRentals() throws Exception {
        mvc.perform(get("/api/rentals").with(httpBasic("admin", "admin123")))
            .andExpect(status().isOk());
    }

    @Test
    void authenticatedUserCanReadOwnProfile() throws Exception {
        mvc.perform(get("/api/users/me").with(httpBasic("john", "user123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("john"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void anonymousCannotReadProfile() throws Exception {
        mvc.perform(get("/api/users/me")).andExpect(status().isUnauthorized());
    }
}
