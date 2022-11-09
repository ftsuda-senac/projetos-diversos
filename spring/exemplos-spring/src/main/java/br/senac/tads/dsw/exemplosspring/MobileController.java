package br.senac.tads.dsw.exemplosspring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/exemplos-mobile")
public class MobileController {

    // Exemplo exibição dinâmica de interfaces móveis/desktop
    // Gerando informações diferenciadas no Controller e template comum
    @GetMapping("/exibicao-dinamica")
    public ModelAndView exExibicaoDinamica(
            @RequestHeader("user-agent") String userAgent) {

        String mensagem = "Acesso via dispositivo movel";
        String backgrondColor = "#2ecc71"; //Verde

        if (!userAgent.toLowerCase().contains("mobile")) {
            mensagem = "Acesso via desktop";
            backgrondColor = "#9b59b6"; // Roxo
        }

        ModelAndView mv = new ModelAndView("mobile/exibicao-dinamica");
        mv.addObject("mensagem", mensagem);
        mv.addObject("bgColor", backgrondColor);
        mv.addObject("userAgent", userAgent);
        return mv;
    }

    // Gerando informações diferenciadas já estão nos templates
    @GetMapping("/exibicao-dinamica-v2")
    public ModelAndView exExibicaoDinamicaV2(
            @RequestHeader("user-agent") String userAgent) {

        String viewTemplate = "mobile/exibicao-dinamica-mobile";
        if (!userAgent.toLowerCase().contains("mobile")) {
            viewTemplate = "mobile/exibicao-dinamica-desktop";
        }

        ModelAndView mv = new ModelAndView(viewTemplate);
        mv.addObject("userAgent", userAgent);
        return mv;
    }

    @GetMapping("/sites-separados/desktop")
    public ModelAndView exSiteSeparadoDesktop(
            @RequestHeader("user-agent") String userAgent) {
        if (userAgent.toLowerCase().contains("mobile")) {
            return new ModelAndView("redirect:/exemplos-mobile/sites-separados/mobile");
        }
        ModelAndView mv = new ModelAndView("mobile/sites-separados-desktop");
        mv.addObject("userAgent", userAgent);
        return mv;
    }

    @GetMapping("/sites-separados/mobile")
    public ModelAndView exSiteSeparadoMobile(
            @RequestHeader("user-agent") String userAgent) {
        if (!userAgent.toLowerCase().contains("mobile")) {
            return new ModelAndView("redirect:/exemplos-mobile/sites-separados/desktop");
        }
        ModelAndView mv = new ModelAndView("mobile/sites-separados-mobile");
        mv.addObject("userAgent", userAgent);
        return mv;
    }

}
