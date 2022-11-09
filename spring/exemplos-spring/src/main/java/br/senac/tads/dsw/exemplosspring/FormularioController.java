package br.senac.tads.dsw.exemplosspring;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DadosPessoaisService service;

    /**
     * Registra validadores e formatadores
     * @param binder 
     */
//    @InitBinder("dadosPessoais")
//    protected void initBinder(WebDataBinder binder) {
//        binder.addValidators(new SpringSenhasConfirmadasValidator());
//        binder.addCustomFormatter(new WebDateFormatter());
//    }

    private List<String> getInteresses() {
        return Arrays.asList("Tecnologia", "Gastronomia", "Viagens", "Esportes", "Investimentos");
    }

    @GetMapping("/lista")
    public ModelAndView listar() {
        return new ModelAndView("lista").addObject("dados", service.findAll());
    }

    @GetMapping
    public ModelAndView abrirFormularioVazio() {
        ModelAndView mv = new ModelAndView("formulario-validacao");
        mv.addObject("dadosPessoais", new DadosPessoais());
        mv.addObject("interesses", getInteresses());
        return mv;
    }
	
    @GetMapping("/preenchido")
    public ModelAndView abrirFormulario() {
        ModelAndView mv = new ModelAndView("formulario-validacao");
        DadosPessoais dados = new DadosPessoais();
        dados.setId(999);
        dados.setNome("Zezinho Silveira");
        dados.setDescricao("bla bla bla");
        dados.setEmail("zezinho@email.com");
        dados.setTelefone("(11) 98711-9988");
        dados.setSenha("abcd1234");
        dados.setSenhaRepetida("abcd1234");
        dados.setDataNascimento(LocalDate.of(1998, 9, 30));
        dados.setGenero(1);
        dados.setNumero(72);
        dados.setPeso(BigDecimal.valueOf(95.7));
        dados.setAltura(BigDecimal.valueOf(1.72));
        dados.setInteresses(Arrays.asList("Tecnologia", "Viagens"));
        mv.addObject("dadosPessoais", dados);
        mv.addObject("interesses", getInteresses());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(
            @ModelAttribute DadosPessoais dadosRecebidos) {
        service.save(dadosRecebidos);
        ModelAndView mv = new ModelAndView("resultado-formulario");
        mv.addObject("dados", dadosRecebidos);
        return mv;
    }

    @PostMapping("/salvar-prg")
    public ModelAndView salvarComPostRedirectGet(
            @ModelAttribute DadosPessoais dadosRecebidos,
            RedirectAttributes redirAttr) {
    	service.save(dadosRecebidos);
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
            ModelAndView mv = new ModelAndView("formulario-validacao");
            mv.addObject("interesses", getInteresses());
            return mv;
        }
        service.save(dadosRecebidos);
        ModelAndView mv = new ModelAndView("redirect:/formulario/resultado");
        redirAttr.addFlashAttribute("dados", dadosRecebidos);
        return mv;
    }

    @GetMapping("/resultado")
    public ModelAndView mostrarResultado() {
        return new ModelAndView("resultado-formulario");
    }

}
