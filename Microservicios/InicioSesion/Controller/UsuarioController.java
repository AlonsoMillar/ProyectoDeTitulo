        package com.example.InicioSesion.Controller;

        import com.example.InicioSesion.Model.Usuario;
        import com.example.InicioSesion.Service.UsuarioService;
        import com.example.InicioSesion.dto.OnboardingRequest; // Importación corregida
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        @RestController
        @RequestMapping("/api/usuarios")
        @CrossOrigin(origins = "*", allowedHeaders = "*")
        public class UsuarioController {

            private final UsuarioService usuarioService;

            // Constructor manual para inyectar el servicio
            public UsuarioController(UsuarioService usuarioService) {
                this.usuarioService = usuarioService;
            }

            @PostMapping("/onboarding")
            public ResponseEntity<?> registrarOnboarding(@RequestBody OnboardingRequest request) {
                try {
                    Usuario usuario = usuarioService.registrarUsuario(request);
                    return ResponseEntity.ok(usuario);
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().body("Error al registrar: " + e.getMessage());
                }
            }
            @GetMapping("/test")
        public ResponseEntity<String> test() {
            System.out.println("--> ¡El celular se ha conectado con éxito!");
            return ResponseEntity.ok("Conexión exitosa desde el microservicio");
        }
            @GetMapping("/{uid}")
        public ResponseEntity<?> obtenerUsuario(@PathVariable String uid) {
            try {
                Usuario usuario = usuarioService.obtenerUsuario(uid);

                if (usuario == null) {
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(usuario);

            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body("Error al obtener usuario: " + e.getMessage());
            }
        }
        }