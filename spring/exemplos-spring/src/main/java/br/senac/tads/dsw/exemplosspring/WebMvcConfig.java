/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 *
 * @author ftsuda
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean(name = "localeResolver")
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setDefaultLocale(new Locale("pt", "BR"));
        return clr;
    }

    // Interceptador que permite trocar manualmente o idioma usado
    @Bean(name = "localeChangeInterceptor")
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Configura as mensagens para serem usadas com Bean Validation<br>
     * Referência:
     * https://www.baeldung.com/spring-custom-validation-message-source
     */
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source
                = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:i18n/mensagens");
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(0);
        return source;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    /**
     * Define uma URL para acessar um diretório contendo as imagens<br>
     * Criar o diretório configurado no sistema.<br>
     * Referência: https://www.baeldung.com/spring-mvc-static-resources
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/teste-uploads/**")
                .addResourceLocations("file:///C:/uploads/");
    }

//    /**
//     * Redireciona uma requisição de / para uma tela espeficicada<br>
//     * Ver http://zetcode.com/springboot/viewcontrollerregistry/
//     *
//     * @param registry
//     */
//    @Override
//    public void addViewControllers(final ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("redirect:/home");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }
//
//    /**
//     * Disponibiliza um bean responsável pela formatação da data
//     *
//     * @return
//     */
//    @Bean
//    public WebDateFormatter dateFormatter() {
//        return new WebDateFormatter();
//    }
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatter(dateFormatter());
//    }
}
