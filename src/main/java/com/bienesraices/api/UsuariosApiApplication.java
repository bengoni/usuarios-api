package com.bienesraices.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.bienesraices.api.repository.UsuarioRepository;
import com.bienesraices.api.model.Usuario;
import com.bienesraices.api.model.Rol;

@OpenAPIDefinition(servers = { @Server(url = "/") })
@SpringBootApplication
public class UsuariosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuariosApiApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner seedAdmin(UsuarioRepository repo, BCryptPasswordEncoder encoder) {
        return args -> {
            final String email = "admin@demo.com";
            if (repo.findByEmail(email).isEmpty()) {
                Usuario u = new Usuario();
                u.setNombre("Admin");
                u.setEmail(email);
                u.setPassword(encoder.encode("Admin1234")); // <-- contraseña consistente
                u.setRol(Rol.ADMIN);
                u.setActivo(true);
                repo.save(u);
                System.out.println(">>> Usuario admin creado: " + email);
            } else {
                System.out.println(">>> Admin ya existe: " + email);
            }
        };
    }
}
