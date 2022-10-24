package br.senac.tads.dsw.exemplorest;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

// https://spring.io/blog/2018/01/12/building-richer-hypermedia-with-spring-hateoas
// https://github.com/spring-projects/spring-hateoas-examples/tree/main/affordances
// https://docs.spring.io/spring-hateoas/docs/current/reference/html/#server.affordances
/**
 *
 * Configuração para incluir "affordances" relacionados a construção de
 * formulários HTML a partir das informações hipermidia
 * Habilitar as annotations abaixo para verificar o funcionamento do HAL+FORMS
 * TODO: Verificar como ordenar as properties
 */

//@Configuration
//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class HypermediaConfig {
    
}
