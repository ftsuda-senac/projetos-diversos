/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Ver https://stackoverflow.com/questions/25356781/spring-boot-remove-whitelabel-error-page
 * Ver https://www.baeldung.com/spring-boot-custom-error-page
 * Ver https://www.mkyong.com/spring-mvc/spring-mvc-catch-the-exceptions-thrown-by-view-page/
 * Ver https://www.mkyong.com/spring-boot/spring-rest-error-handling-example/
 * @author ftsuda
 */
@ControllerAdvice
public class TratamentoErrosController {

    @ExceptionHandler(ErroException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView tratarErroException(HttpServletRequest request, ErroException exception) {
        return new ModelAndView("pagina-erro").addObject("msg", exception.getLocalizedMessage());
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView tratarErro404(HttpServletRequest request, Exception exception) {
        return new ModelAndView("pagina-erro").addObject("msg", "Página não encontrada (404)");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView tratarErro500(HttpServletRequest request, Exception exception) {
        return new ModelAndView("pagina-erro").addObject("msg", "Erro interno (500)");
    }
}
