package br.senac.tads.dsw.exemplosspring.i18n;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/i18n")
public class InternacionalizacaoController {

    @GetMapping
    public ModelAndView mostrarTela() {
        return new ModelAndView("i18n/tela")
                .addObject("info", new Info());
    }

    @PostMapping
    public ModelAndView salvar(@ModelAttribute("info") @Valid Info info,
            BindingResult bindingResult, RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("i18n/tela");
        }
        return new ModelAndView("redirect:/i18n");
    }

}
