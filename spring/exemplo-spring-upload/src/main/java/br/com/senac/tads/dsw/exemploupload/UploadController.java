/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.tads.dsw.exemploupload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author fernando.tsuda
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
    
    private static final String DIRETORIO_UPLOAD = "C:/uploads/";
    
    private static final String CONTEXTO_ACESSO_UPLOAD = "/arquivos-upload";

    @GetMapping
    public ModelAndView mostrarFormulario() {
        return new ModelAndView("upload");
    }

    // TODO: Ver uso do RedirectView ao invés do ModelAndView
    @PostMapping("/salvar")
    public ModelAndView receberArquivo(
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirectAttributes) {

        if (arquivo.isEmpty()) {
            // ERRO
            redirectAttributes.addFlashAttribute("msg", "Arquivo inválido");
            return new ModelAndView("redirect:/upload");
        }

        try {
            byte[] bytesArquivo = arquivo.getBytes();

            Path destino = Paths.get(DIRETORIO_UPLOAD
                    + arquivo.getOriginalFilename());
            Files.write(destino, bytesArquivo);

            redirectAttributes.addFlashAttribute("msg", "Arquivo "
                    + arquivo.getOriginalFilename()
                    + " carregado com sucesso");
            redirectAttributes.addFlashAttribute("urlAcessoUpload", CONTEXTO_ACESSO_UPLOAD
                    + "/" + arquivo.getOriginalFilename());
            return new ModelAndView("redirect:/upload");
        } catch (IOException e) {
            // ERRO
            redirectAttributes.addFlashAttribute("msg", "Erro durante upload");
            return new ModelAndView("redirect:/upload");
        }
    }

    @GetMapping("/ver-imagens")
    public ModelAndView mostrarImagensUpload() {
        
        // Busca imagens carregadas no diretório de upload
        File diretorio = new File(DIRETORIO_UPLOAD);
        File[] listaArquivos = diretorio.listFiles();
        
        List<String> arquivosImagem = new ArrayList<>();
        for (File arquivo : listaArquivos) {
            if (arquivo.getName().endsWith("jpg") || arquivo.getName().endsWith("png")) {
                arquivosImagem.add(arquivo.getName());
            }
        }
        
        ModelAndView mv = new ModelAndView("ver-imagens");
        mv.addObject("arquivos", arquivosImagem);
        return mv;   
    }
    
}
