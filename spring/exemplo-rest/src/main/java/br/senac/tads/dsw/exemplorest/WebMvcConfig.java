package br.senac.tads.dsw.exemplorest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.imagem-path}")
    private String imagemPath;

    @Value("${app.imagem-url-prefix}")
    private String imagemUrlPrefix;

    
    /**
     * Define uma URL para acessar um diretório contendo as imagens<br>
     * Criar o diretório configurado no sistema.<br>
     * Referência: https://www.baeldung.com/spring-mvc-static-resources
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(imagemUrlPrefix + "/**")
                .addResourceLocations("file:///" + imagemPath);   
    }
    
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
