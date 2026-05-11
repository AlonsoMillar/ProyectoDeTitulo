package com.example.MessageMicroservice.Service;

import com.example.MessageMicroservice.DTO.MensajeRequest;
import com.example.MessageMicroservice.Model.Mensaje;
import com.example.MessageMicroservice.Repository.MensajeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MensajeService {

    private final MensajeRepository repo;

    public MensajeService(MensajeRepository repo) {
        this.repo = repo;
    }

    /**
     * Procesa el envío de un mensaje, valida los datos obligatorios
     * y coordina la persistencia en el repositorio.
     */
    public Mensaje enviarMensaje(MensajeRequest r) throws Exception {
        // Validaciones de seguridad
        if (r.getContenido() == null || r.getContenido().trim().isEmpty()) {
            throw new Exception("El contenido del mensaje no puede estar vacío");
        }
        if (r.getChatId() == null || r.getChatId().isEmpty()) {
            throw new Exception("El chatId es requerido para procesar el mensaje");
        }
        if (r.getStoreCode() == null || r.getStoreCode().isEmpty()) {
            throw new Exception("El storeCode es obligatorio para indexar el chat");
        }

        // Mapeo del DTO al Modelo de base de datos
        Mensaje m = new Mensaje();
        m.setChatId(r.getChatId());
        m.setSenderUid(r.getSenderUid());
        m.setSenderRole(r.getSenderRole());
        m.setReceiverUid(r.getReceiverUid());
        m.setContenido(r.getContenido().trim());
        m.setCreatedAt(System.currentTimeMillis());

        // CAPTURA DEL ALIAS: 
        // Aseguramos que el alias se transporte al modelo. 
        // Si viene nulo del frontend (por error de sesión), ponemos un valor por defecto.
        String alias = (r.getSenderAlias() != null && !r.getSenderAlias().isEmpty()) 
                       ? r.getSenderAlias() 
                       : "Usuario";
        m.setSenderAlias(alias);

        // Enviamos al repo con el storeCode para actualizar la bandeja de entrada
        return repo.guardarMensaje(m, r.getStoreCode());
    }

    /**
     * Recupera el historial completo de mensajes de un chat específico.
     */
    public List<Mensaje> obtenerMensajes(String chatId) {
        try {
            return repo.obtenerMensajesPorChat(chatId);
        } catch (Exception e) {
            System.err.println("--- ERROR AL RECUPERAR HISTORIAL ---");
            System.err.println("ChatID solicitado: " + chatId);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene la lista de conversaciones (Bandeja de Entrada) para el Administrador.
     */
    public List<Map<String, Object>> obtenerChats(String storeCode) {
        try {
            // Limpiamos el código de tienda antes de la consulta
            String cleanCode = storeCode.trim().toUpperCase();
            return repo.obtenerChats(cleanCode);
        } catch (Exception e) {
            System.err.println("--- ERROR AL CARGAR BANDEJA DE ENTRADA ---");
            System.err.println("StoreCode solicitado: " + storeCode);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}