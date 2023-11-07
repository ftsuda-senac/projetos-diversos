/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.tads.dsw.exemplos.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

	@Autowired
	private JwtEncoder encoder;

	public String generateToken(UsuarioSistema usuario, String clientKey, Duration duration) {
		Instant now = Instant.now();
		
		// Scope é string separada com espaços
		// https://stackoverflow.com/a/62477585
		String scope = usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		// @formatter:off
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.audience(Arrays.asList(clientKey))
				.issuedAt(now)
				.expiresAt(now.plus(duration))
				.subject(usuario.getUsername())
				.claim("email", usuario.getEmail())
				.claim("scope", scope).build();
		// @formatter:on
		JwtEncoderParameters encoderParameters =
				JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return this.encoder.encode(encoderParameters).getTokenValue();
	}

}
