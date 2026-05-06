package com.example.InicioSesion.Model;

public class Usuario {
    private String uid;
    private String alias;
    private String role;
    private String storeCode;
    private long createdAt;

    public Usuario() {}

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}