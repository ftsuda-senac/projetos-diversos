package br.senac.tads.dsw.exemplosservlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ExemplosServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExemplosServletApplication.class, args);
	}

}
