package com.bienesraices.api.config;


import com.bienesraices.api.model.Usuario;
import com.bienesraices.api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

  @Bean
  CommandLineRunner initUsers(UsuarioRepository repo) {
    return args -> {
      final String email = "admin@demo.com";
      if (repo.findByEmail(email).isEmpty()) {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

        Usuario u = new Usuario();
        u.setNombre("Admin");
        u.setEmail(email);
        u.setPassword(enc.encode("Admin1234")); // ¡BCrypt!

        // Campos opcionales si existen en tu entidad; si no existen, no pasa nada
        try { u.getClass().getMethod("setRol", String.class).invoke(u, "ADMIN"); } catch (Exception ignored) {}
        try { u.getClass().getMethod("setActivo", Boolean.class).invoke(u, Boolean.TRUE); } catch (Exception ignored) {}

        repo.save(u);
        System.out.println("[DataSeeder] Usuario admin creado: " + email);
      } else {
        System.out.println("[DataSeeder] Admin ya existe: " + email);
      }
    };
  }
}
