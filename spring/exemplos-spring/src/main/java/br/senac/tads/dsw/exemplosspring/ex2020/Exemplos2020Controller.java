package br.senac.tads.dsw.exemplosspring.ex2020;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/exemplos2020")
public class Exemplos2020Controller {

    @GetMapping("/ex1")
    public String exemplo1() {
        return "ex2020/exemplo1";
    }

    @GetMapping("/ex2")
    public String exemplo2(Model model) {
        model.addAttribute("mensagem", "Mensagem gerada no Controller");
        model.addAttribute("numero", 1122);
        model.addAttribute("dataHoraAcesso", LocalDateTime.now());
        return "ex2020/exemplo2";
    }

    @GetMapping("/ex2b")
    public ModelAndView exemplo2b() {
        ModelAndView mv = new ModelAndView("ex2020/exemplo2");
        mv.addObject("mensagem", "Mensagem gerada no Controller usando ModelAndView");
        mv.addObject("numero", 9876);
        mv.addObject("dataHoraAcesso", LocalDateTime.now());
        return mv;
    }

    @GetMapping("/ex3")
    public ModelAndView exemplo3() {
        List<Dados> lista = new ArrayList<>();
        lista.add(new Dados("Walter White", 145, LocalDate.of(1984, 4, 20), false));
        lista.add(new Dados("Jesse Pinkman", 566, LocalDate.of(2000, 10, 3), false));
        lista.add(new Dados("Seu Madruga", 72, LocalDate.of(1999, 1, 15), true));
        lista.add(new Dados("Luke Skywalker", 621, LocalDate.of(2003, 6, 10), false));
        
        ModelAndView mv = new ModelAndView("ex2020/exemplo3");
        mv.addObject("lista", lista);
        return mv;
    }
    
    @GetMapping("/ex4")
    public ModelAndView exemplo4(
            @RequestParam("nome") String nome,
            @RequestParam(value = "numero", defaultValue="0") int numero,
            @RequestParam(value = "data", required = false) String dataStr) {
        
        Dados dado = new Dados();
        dado.setNome(nome);
        dado.setNumero(numero);
        LocalDate data;
        if (dataStr != null) {
            data = LocalDate.parse(dataStr);
        } else {
            data = LocalDate.now();
        }
        dado.setData(data);
        
        ModelAndView mv = new ModelAndView("ex2020/exemplo4");
        mv.addObject("item", dado);
        return mv;
    }

    @GetMapping("/ex5/{apelido}")
    public ModelAndView exemplo5(
            @PathVariable("apelido") String apelido,
            @RequestParam(value = "numero", defaultValue="0") int numero,
            @RequestParam(value = "data", required = false) String dataStr) {
        String nome;
        if ("fulano".equals(apelido)) {
            nome = "Fulano da Silva";
        } else if ("ciclano".equals(apelido)) {
            nome = "Ciclano de Souza";
        } else if ("beltrana".equals(apelido)) {
            nome = "Beltrana dos Santos";
        } else {
            nome = "NOME INVÁLIDO";
        }

        Dados dado = new Dados();
        dado.setNome(nome);
        dado.setNumero(numero);
        LocalDate data;
        if (dataStr != null) {
            data = LocalDate.parse(dataStr);
        } else {
            data = LocalDate.now();
        }
        dado.setData(data);
        
        ModelAndView mv = new ModelAndView("ex2020/exemplo5");
        mv.addObject("item", dado);
        mv.addObject("apelido", apelido);
        return mv;
    }
 
    @GetMapping("/ex6")
    public ModelAndView exemplo6(
            @RequestHeader("user-agent") String userAgent) {
        
        boolean clientIsMobile = userAgent.toLowerCase().contains("mobile");
        
        ModelAndView mv = new ModelAndView("ex2020/exemplo6");
        mv.addObject("ua", userAgent);
        mv.addObject("mobile", clientIsMobile);
        return mv;
    }

    @GetMapping("/ex7")
    public ModelAndView exemplo7(@RequestHeader Map<String, String> cabecalhos) {
        ModelAndView mv = new ModelAndView("ex2020/exemplo7");
        mv.addObject("cabecalhos", cabecalhos);
        return mv;
    }
}
