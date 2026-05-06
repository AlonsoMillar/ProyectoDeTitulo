package com.example.InicioSesion.Service;

import com.example.InicioSesion.Model.Usuario;
import com.example.InicioSesion.Repository.UsuarioRepository;
import com.example.InicioSesion.dto.OnboardingRequest; // Importación corregida
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Constructor manual para inyectar el repositorio
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(OnboardingRequest request) throws Exception {
        String storeCode = null;

        if ("admin".equalsIgnoreCase(request.getRole())) {
            storeCode = generarCodigoTienda();
        }

        // Creación manual del usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUid(request.getUid());
        nuevoUsuario.setAlias(request.getAlias());
        nuevoUsuario.setRole(request.getRole());
        nuevoUsuario.setStoreCode(storeCode);
        nuevoUsuario.setCreatedAt(System.currentTimeMillis());

        // Guardamos en Firestore
        usuarioRepository.saveUsuario(nuevoUsuario);

        return nuevoUsuario;
    }

   private String generarCodigoTienda() {
    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder codigo = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < 4; i++) {
        codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
    }

    return codigo.toString();
}
    public Usuario obtenerUsuario(String uid) throws Exception {
    return usuarioRepository.getUsuario(uid);
}
    
}