package com.example.MessageMicroservice.DTO;

public class MensajeRequest {

    private String chatId;

    private String senderUid;
    private String senderAlias;
    private String senderRole;

    private String receiverUid;
    private String storeCode;

    private String contenido;

    public MensajeRequest() {}

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderAlias() {
        return senderAlias;
    }

    public void setSenderAlias(String senderAlias) {
        this.senderAlias = senderAlias;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}