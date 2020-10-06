package br.senac.tads.dsw.exemplosspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
public class ExemplosSpringApplication extends SpringBootServletInitializer
		implements WebApplicationInitializer, CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ExemplosSpringApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ExemplosSpringApplication.class, args);
	}

	/**
	 * Chamada do Spring Boot através da classe SpringBootServletInitializer usada
	 * para executar o projeto em um servidor externo (Wildfly, JBoss, etc)<br>
	 * Mantido aqui como exemplo, pois não é necessário quando rodar com servidor
	 * embedded<br>
	 * Ver
	 * https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-traditional-deployment
	 */
	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(ExemplosSpringApplication.class);
	}

	/**
	 * Permite executar comandos após aplicação Spring Boot iniciar<br>
	 * Implementa interface CommandLineRunner
	 */
	@Override
	public void run(String... args) throws Exception {
		log.debug("***** CommandLineRunner running");
	}

}
