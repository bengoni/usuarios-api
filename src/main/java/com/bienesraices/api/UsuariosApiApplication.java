package com.bienesraices.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bienesraices.api.repository.UsuarioRepository;
import com.bienesraices.api.model.Usuario;
import com.bienesraices.api.model.Rol;   // <-- tu enum

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
			String email = "admin@demo.com";
			if (repo.findByEmail(email).isEmpty()) {
				Usuario u = new Usuario();
				u.setNombre("Admin");
				u.setEmail(email);
				u.setPassword(encoder.encode("admin123")); // BCrypt
				u.setRol(Rol.ADMIN);
				u.setActivo(true);
				repo.save(u);
				System.out.println(">>> Usuario admin creado: " + email + " / admin123");
			}
		};
	}
}
