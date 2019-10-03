package br.senac.tads.dsw.exemplosspring;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExemploController {

    @GetMapping("/ex1")
    public String exemplo1() {
        return "exemplo1";
    }

    @GetMapping("/ex2")
    public String exemplo2(Model model) {
        model.addAttribute("mensagem", "Mensagem gerada no Controller");
        model.addAttribute("numeroSorte", 2140);
        model.addAttribute("dataAcesso", LocalDateTime.now());
        return "exemplo2";
    }

    @GetMapping("/ex2b")
    public ModelAndView exemplo2b() {
        ModelAndView resposta = new ModelAndView("exemplo2");
        resposta.addObject("mensagem", "Mensagem gerada no Controller usando ModelAndView");
        resposta.addObject("numeroSorte", 2204);
        resposta.addObject("dataAcesso", LocalDateTime.now());
        return resposta;
    }

    @GetMapping("/ex3")
    public ModelAndView exemplo3() {

        List<String> valores = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            valores.add("Item " + i);
        }
        boolean par = false; // impar

        ModelAndView resposta = new ModelAndView("exemplo3");
        resposta.addObject("lista", valores);
        resposta.addObject("par", par);
        return resposta;
    }

    @GetMapping("/ex4")
    public ModelAndView exemplo4(
            @RequestParam("nome") String nome,
            @RequestParam(value = "genero", defaultValue = "0") int codGenero,
            @RequestParam(value = "dtnasc", required = false) String dtNascimento) {
        ModelAndView resposta = new ModelAndView("exemplo4");
        resposta.addObject("nomeParam", nome);
        resposta.addObject("generoParam", codGenero);
        resposta.addObject("dtNascParam", dtNascimento);
        return resposta;
    }

    @GetMapping("/ex5/{apelido}")
    public ModelAndView exemplo5(
            @PathVariable("apelido") String apelido,
            @RequestParam(value = "genero", defaultValue = "0") int codGenero,
            @RequestParam(value = "dtnasc", required = false) String dtNascimento) {
        ModelAndView resposta = new ModelAndView("exemplo5");
        resposta.addObject("apelidoParam", apelido);
        resposta.addObject("generoParam", codGenero);
        resposta.addObject("dtNascParam", dtNascimento);
        return resposta;
    }

    @GetMapping("/ex5b/{apelido}/{numeroSorte}")
    public ModelAndView exemplo5b(
            @PathVariable("apelido") String apelido,
            @PathVariable("numeroSorte") int numeroSorte) {
        ModelAndView resposta = new ModelAndView("exemplo5b");
        resposta.addObject("apelidoParam", apelido);
        resposta.addObject("numeroSorteParam", numeroSorte);
        return resposta;
    }

    @GetMapping("/ex6")
    public ModelAndView exemplo6(
            @RequestHeader("user-agent") String userAgent) {
        boolean mobile = userAgent.toLowerCase().contains("mobile");
        
        ModelAndView resposta = new ModelAndView("exemplo6");
        resposta.addObject("ua", userAgent);
        resposta.addObject("mobile", mobile);
        return resposta;
    }

    @GetMapping("/ex7")
    public ModelAndView exemplo7(
            @RequestHeader Map<String, String> cabecalhos) {
        ModelAndView resposta = new ModelAndView("exemplo7");
        resposta.addObject("cabecalhos", cabecalhos);
        return resposta;
    }

}
