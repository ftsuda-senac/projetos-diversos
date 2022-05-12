package br.senac.tads.dsw.exemplospringsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/protegido")
public class ProtegidoController {

    @GetMapping("/peao")
    @PreAuthorize("hasRole('PEAO')") // OBS: HABILITAR O @EnableGlobalMethodSecurity em SecurityConfig
    public ModelAndView mostrarPeaoPage() {
        return new ModelAndView("protegido")
                .addObject("titulo", "Página do PEAO")
                .addObject("msg", "Usuário logado possui papel \"PEAO\"");
    }

    @GetMapping("/fodon")
    @PreAuthorize("hasRole('FODON')")
    public ModelAndView mostrarFodonPage() {
        return new ModelAndView("protegido")
                .addObject("titulo", "Página do FODON")
                .addObject("msg", "Usuário logado possui papel \"FODON\"");
    }

    @GetMapping("/god")
    @PreAuthorize("hasRole('GOD')")
    public ModelAndView mostrarGodPage() {
        return new ModelAndView("protegido")
                .addObject("titulo", "Página do GOD")
                .addObject("msg", "Usuário logado possui papel \"GOD\"");
    }

}
