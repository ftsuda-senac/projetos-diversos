package br.senac.tads.dsw.exemplosspring;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@RequestMapping("/formulario")
public class FormularioController {

    @GetMapping
    public ModelAndView abrirFormulario() {
        ModelAndView mv = new ModelAndView("formulario");
        DadosPessoais dados = new DadosPessoais();
        dados.setId(999L);
        dados.setNome("Zezinho Silveira");
        dados.setEmail("zezinho@email.com");
        dados.setSenha("abcd1234");
        dados.setSenhaRepetida("abcd1234");
        dados.setDtNascimento(LocalDate.of(1998, 9, 30));
        dados.setSexo(1);
        dados.setNumeroSorte(72);
        dados.setPeso(BigDecimal.valueOf(90.7));
        dados.setAltura(BigDecimal.valueOf(1.72));
        dados.setDescricao("bla bla bla");
        dados.setInteresses(new String[]{"Tecnologia", "Viagens"});
        mv.addObject("dadosPessoais", dados);
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(
            @ModelAttribute("dadosPessoais") DadosPessoais dadosRecebidos) {
        ModelAndView mv = new ModelAndView("resultado-formulario");
        mv.addObject("dados", dadosRecebidos);
        return mv;
    }

    @PostMapping("/salvar-prg")
    public ModelAndView salvarComPostRedirectGet(
            @ModelAttribute("dadosPessoais") DadosPessoais dadosRecebidos,
            RedirectAttributes redirAttr) {
        ModelAndView mv = new ModelAndView("redirect:/formulario/resultado");
        redirAttr.addFlashAttribute("dados", dadosRecebidos);
        return mv;
    }

    @PostMapping("/salvar-prg-validacao")
    public ModelAndView salvarDadosComValidacao(
            @ModelAttribute("dadosPessoais") @Valid DadosPessoais dadosRecebidos,
            BindingResult bindingResult,
            RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("formulario-final");
        }
        ModelAndView mv = new ModelAndView("redirect:/formulario/resultado");
        redirAttr.addFlashAttribute("dados", dadosRecebidos);
        return mv;
    }

    @GetMapping("/resultado")
    public ModelAndView mostrarResultado() {
        return new ModelAndView("resultado-formulario");
    }

}
