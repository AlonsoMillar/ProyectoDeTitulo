package com.example.InicioSesion.dto;

public class OnboardingRequest {
    private String uid;
    private String alias;
    private String role;

    public OnboardingRequest() {}

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}