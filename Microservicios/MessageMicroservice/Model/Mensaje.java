package com.example.MessageMicroservice.Model;

import com.google.cloud.firestore.annotation.PropertyName;

public class Mensaje {

    private String id;
    private String chatId;
    private String senderUid;
    private String senderAlias;
    private String senderRole;
    private String receiverUid;
    private String contenido;
    private long createdAt;

    public Mensaje() {}

    @PropertyName("id")
    public String getId() { return id; }
    @PropertyName("id")
    public void setId(String id) { this.id = id; }

    @PropertyName("chatId")
    public String getChatId() { return chatId; }
    @PropertyName("chatId")
    public void setChatId(String chatId) { this.chatId = chatId; }

    @PropertyName("senderUid")
    public String getSenderUid() { return senderUid; }
    @PropertyName("senderUid")
    public void setSenderUid(String senderUid) { this.senderUid = senderUid; }

    @PropertyName("senderAlias")
    public String getSenderAlias() { return senderAlias; }
    @PropertyName("senderAlias")
    public void setSenderAlias(String senderAlias) { this.senderAlias = senderAlias; }

    @PropertyName("senderRole")
    public String getSenderRole() { return senderRole; }
    @PropertyName("senderRole")
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }

    @PropertyName("receiverUid")
    public String getReceiverUid() { return receiverUid; }
    @PropertyName("receiverUid")
    public void setReceiverUid(String receiverUid) { this.receiverUid = receiverUid; }

    @PropertyName("contenido")
    public String getContenido() { return contenido; }
    @PropertyName("contenido")
    public void setContenido(String contenido) { this.contenido = contenido; }

    @PropertyName("createdAt")
    public long getCreatedAt() { return createdAt; }
    @PropertyName("createdAt")
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}