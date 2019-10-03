package br.senac.tads.pi3.exemplosservlets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan(basePackages = {"br.senac.tads.pi3.exemplosservlets"})
public class ExemplosServletsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ExemplosServletsApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExemplosServletsApplication.class, args);
    }
}
