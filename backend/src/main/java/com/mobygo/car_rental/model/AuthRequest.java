package com.mobygo.car_rental.model;

public class AuthRequest {
    private String email;
    private String password;

    // Getter und Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}