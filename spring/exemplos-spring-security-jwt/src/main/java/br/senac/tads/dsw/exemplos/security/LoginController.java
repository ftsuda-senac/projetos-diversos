package br.senac.tads.dsw.exemplos.security;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private  AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String senha) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, senha));
        UsuarioSistema usuario = (UsuarioSistema) auth.getPrincipal();
		String jwt = tokenService.generateToken(usuario, senha, Duration.ofMinutes(30));
		return jwt;
    }

}
