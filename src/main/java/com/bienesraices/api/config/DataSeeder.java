package com.bienesraices.api.config;

import com.bienesraices.api.model.Rol;
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
        u.setNombre("Admin Demo");
        u.setEmail(email);
        u.setPassword(enc.encode("Admin1234")); // hash
        u.setRol(Rol.ADMIN);
        u.setActivo(true);
        u.setNumeroCasa("101");

        repo.save(u);
        System.out.println("[DataSeeder] Admin creado: " + email + " / Admin1234");
      } else {
        System.out.println("[DataSeeder] Admin ya existe: " + email);
      }
    };
  }
}

