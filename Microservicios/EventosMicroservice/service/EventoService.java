package com.example.eventomicroservice.service;

import com.example.eventomicroservice.dto.EventoRequestDTO;
import com.example.eventomicroservice.dto.EventoResponseDTO;
import com.example.eventomicroservice.model.EstadoEvento;
import com.example.eventomicroservice.model.Evento;
import com.example.eventomicroservice.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public EventoResponseDTO crearEvento(EventoRequestDTO dto) throws ExecutionException, InterruptedException {
        Evento evento = Evento.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .fechaEvento(dto.getFechaEvento())
                .duracionMinutos(dto.getDuracionMinutos())
                .maxParticipantes(dto.getMaxParticipantes())
                .requiereJuez(dto.isRequiereJuez())
                .storeCode(dto.getStoreCode())
                .adminUid(dto.getAdminUid())
                .adminAlias(dto.getAdminAlias())
                .estado(EstadoEvento.BORRADOR)
                .createdAt(new Date())
                .build();

        evento = eventoRepository.save(evento);
        return mapToDTO(evento);
    }

    public EventoResponseDTO actualizarEvento(String id, EventoRequestDTO dto) throws ExecutionException, InterruptedException {
        Evento evento = eventoRepository.findById(id);
        if (evento == null) {
            throw new RuntimeException("Evento no encontrado");
        }

        evento.setNombre(dto.getNombre());
        evento.setDescripcion(dto.getDescripcion());
        evento.setFechaEvento(dto.getFechaEvento());
        evento.setDuracionMinutos(dto.getDuracionMinutos());
        evento.setMaxParticipantes(dto.getMaxParticipantes());
        evento.setRequiereJuez(dto.isRequiereJuez());
        evento.setStoreCode(dto.getStoreCode());
        // adminUid y adminAlias no deberían cambiar, pero se pueden actualizar si es necesario

        evento = eventoRepository.save(evento);
        return mapToDTO(evento);
    }

    public void eliminarEvento(String id) throws ExecutionException, InterruptedException {
        Evento evento = eventoRepository.findById(id);
        if (evento == null) {
            throw new RuntimeException("Evento no encontrado");
        }
        eventoRepository.delete(id);
    }

    public EventoResponseDTO publicarEvento(String id) throws ExecutionException, InterruptedException {
        Evento evento = eventoRepository.findById(id);
        if (evento == null) {
            throw new RuntimeException("Evento no encontrado");
        }
        if (evento.getEstado() != EstadoEvento.BORRADOR) {
            throw new RuntimeException("Solo se pueden publicar eventos en estado BORRADOR");
        }
        evento.setEstado(EstadoEvento.PUBLICADO);
        evento = eventoRepository.save(evento);
        return mapToDTO(evento);
    }

    public EventoResponseDTO iniciarEvento(String id) throws ExecutionException, InterruptedException {
        Evento evento = eventoRepository.findById(id);
        if (evento == null) {
            throw new RuntimeException("Evento no encontrado");
        }
        if (evento.getEstado() != EstadoEvento.PUBLICADO) {
            throw new RuntimeException("Solo se pueden iniciar eventos en estado PUBLICADO");
        }
        evento.setEstado(EstadoEvento.EN_CURSO);
        evento = eventoRepository.save(evento);
        return mapToDTO(evento);
    }

    public EventoResponseDTO finalizarEvento(String id) throws ExecutionException, InterruptedException {
        Evento evento = eventoRepository.findById(id);
        if (evento == null) {
            throw new RuntimeException("Evento no encontrado");
        }
        if (evento.getEstado() != EstadoEvento.EN_CURSO) {
            throw new RuntimeException("Solo se pueden finalizar eventos en estado EN_CURSO");
        }
        evento.setEstado(EstadoEvento.FINALIZADO);
        evento = eventoRepository.save(evento);
        return mapToDTO(evento);
    }

    public List<EventoResponseDTO> obtenerEventosPorAdmin(String adminUid) throws ExecutionException, InterruptedException {
        List<Evento> eventos = eventoRepository.findByAdminUid(adminUid);
        return eventos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<EventoResponseDTO> obtenerEventosPublicados() throws ExecutionException, InterruptedException {
        List<Evento> eventos = eventoRepository.findByEstado(EstadoEvento.PUBLICADO.name());
        return eventos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public EventoResponseDTO obtenerEventoPorId(String id) throws ExecutionException, InterruptedException {
        Evento evento = eventoRepository.findById(id);
        if (evento == null) {
            throw new RuntimeException("Evento no encontrado");
        }
        return mapToDTO(evento);
    }

    private EventoResponseDTO mapToDTO(Evento evento) {
        EventoResponseDTO dto = new EventoResponseDTO();
        dto.setId(evento.getId());
        dto.setNombre(evento.getNombre());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaEvento(evento.getFechaEvento());
        dto.setDuracionMinutos(evento.getDuracionMinutos());
        dto.setMaxParticipantes(evento.getMaxParticipantes());
        dto.setRequiereJuez(evento.isRequiereJuez());
        dto.setStoreCode(evento.getStoreCode());
        dto.setAdminUid(evento.getAdminUid());
        dto.setAdminAlias(evento.getAdminAlias());
        dto.setEstado(evento.getEstado());
        dto.setCreatedAt(evento.getCreatedAt());
        return dto;
    }
}
