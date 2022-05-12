package br.senac.tads.dsw.exemplospringsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import br.senac.tads.dsw.exemplospringsecurity.dominio.UsuarioSistema;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public ModelAndView mostrarHome(Authentication authentication) {
        UsuarioSistema usuario = null;
        if (authentication != null) {
            usuario = (UsuarioSistema) authentication.getPrincipal();
        }
        return new ModelAndView("home").addObject("usuario", usuario);
    }

}
