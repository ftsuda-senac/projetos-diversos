package br.senac.tads.dsw.exemplospringsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    /**
     * Redireciona uma requisição de / ou /index.html para um template
     * espeficicado<br>
     * Referência: http://zetcode.com/springboot/viewcontrollerregistry/
     *
     * @param registry
     */
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index-template.html");
        registry.addViewController("/index.html").setViewName("index-template.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
    
}
