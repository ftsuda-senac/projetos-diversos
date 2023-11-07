package br.senac.tads.dsw.exemplos.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

	private final byte[] jwtKey;

	public SecurityConfig(@Value("${jwt.chave-secreta}") String chaveSecreta) {
		try {
			jwtKey = MessageDigest.getInstance("SHA-256").digest(chaveSecreta.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

	@Bean
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authProvider);
	}

	@Bean
	JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey));
	}

	@Bean
	JwtDecoder jwtDecoder() {
		SecretKeySpec originalKey = new SecretKeySpec(jwtKey, 0, jwtKey.length, "RSA");
		return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS256).build();
	}

	@Scope("prototype")
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }


	// Remover
	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
		authConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return authConverter;
	}


	// https://stackoverflow.com/questions/77024398/spring-h2db-web-console-this-method-cannot-decide-whether-these-patterns-are
	// https://stackoverflow.com/questions/76809698/spring-security-method-cannot-decide-pattern-is-mvc-or-not-spring-boot-applicati
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

		MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);

		// @formatter:off
		return http
			.cors(cors -> Customizer.withDefaults())
			.csrf(csrf -> csrf
                    .ignoringRequestMatchers(toH2Console()).disable())
			.authorizeHttpRequests(
					authorize -> authorize
						.requestMatchers(
								antMatcher("/swagger-ui.html"),
								antMatcher("/swagger-ui/**"),
								antMatcher("/v3/api-docs/**"),
								antMatcher("/h2-console/**")).permitAll()
						.requestMatchers(antMatcher("/paginas/**"), antMatcher("/js/**"),antMatcher( "/img/**")).permitAll()
						.requestMatchers(mvc.pattern("/dados-pessoais")).hasAuthority("LORD_SITH")
						.requestMatchers(mvc.pattern("/conhecimentos")).hasAnyAuthority("JEDI")
						.requestMatchers(mvc.pattern("/login")).permitAll()
						.anyRequest().authenticated())
			.headers(headers -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.oauth2ResourceServer(
					oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
			.logout(logout -> logout
					.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)))
			.build();
		// @formatter:on
	}

    SecurityFilterChain filterChainOld(HttpSecurity http) throws Exception  {

        /*
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/paginas/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/dados-pessoais").hasAuthority("LORD_SITH")
                .requestMatchers("/conhecimentos").hasAnyAuthority("JEDI")
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Adicionando filtro no fluxo de autorização
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        */
        http.csrf().disable()
                .authorizeHttpRequests()
                .anyRequest().permitAll();

        return http.build();
    }

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
		configuration.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


}
