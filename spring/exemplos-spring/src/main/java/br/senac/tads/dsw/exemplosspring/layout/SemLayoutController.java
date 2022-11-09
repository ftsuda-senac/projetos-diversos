package br.senac.tads.dsw.exemplosspring.layout;

import br.senac.tads.dsw.exemplosspring.item.ItemService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sem-layout")
public class SemLayoutController {
    
    @Autowired
    private ItemService itemService;

    @GetMapping("/tela1")
    public ModelAndView mostrarTela1() {
        return new ModelAndView("layout/sem-layout1");
    }

    @GetMapping("/tela2")
    public ModelAndView mostrarTela2() {
        return new ModelAndView("layout/sem-layout2");
    }

    @GetMapping("/tela3")
    public ModelAndView mostrarTela3() {
        return new ModelAndView("layout/sem-layout3")
                .addObject("itens", itemService.findAll());
    }

    @GetMapping("/tela4")
    public ModelAndView mostrarTela4() {
        List<FotoInfo> fotos = new ArrayList<>();
        fotos.add(new FotoInfo("gato", "pexels-pixabay-45201.jpg"));
        fotos.add(new FotoInfo("cachorros", "pexels-chevanon-photography-1108099.jpg"));
        fotos.add(new FotoInfo("cachorro", "pexels-pixabay-39317.jpg"));
        fotos.add(new FotoInfo("leao", "pexels-mateusz-feliksik-13533634.jpg"));
        fotos.add(new FotoInfo("elefante", "pexels-magda-ehlers-1057366.jpg"));
        fotos.add(new FotoInfo("planta", "pexels-david-alberto-carmona-coto-1151418.jpg"));
        fotos.add(new FotoInfo("flor", "pexels-pixabay-33044.jpg"));
        fotos.add(new FotoInfo("praia", "pexels-maria-isabella-bernotti-1049298.jpg"));
        fotos.add(new FotoInfo("neve", "pexels-pixabay-235621.jpg"));
        fotos.add(new FotoInfo("montanha", "pexels-eberhard-grossgasteiger-1287145.jpg"));
        return new ModelAndView("layout/sem-layout4").addObject("fotos", fotos);
    }
}
