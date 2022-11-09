/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/upload")
public class UploadController {
    
    @Value("${app.upload-path:C:/uploads/}")
    private String uploadPath;

    @Value("${app.upload-url-prefix:/uploads}")
    private String uploadUrlPrefix;


    @GetMapping
    public ModelAndView mostrarFormulario() {
        return new ModelAndView("upload/form");
    }

    @PostMapping("/salvar")
    public ModelAndView receberArquivo(
            @RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirAttr) {

        if (arquivo.isEmpty()) {
            // ERRO
            redirAttr.addFlashAttribute("msg", "Arquivo inválido");
            return new ModelAndView("redirect:/upload");
        }

        try {
            byte[] bytesArquivo = arquivo.getBytes();

            Path destino = Paths.get(uploadPath
                    + arquivo.getOriginalFilename());
            Files.write(destino, bytesArquivo);

            redirAttr.addFlashAttribute("msg", "Arquivo "
                    + arquivo.getOriginalFilename()
                    + " carregado com sucesso");
            redirAttr.addFlashAttribute("urlAcessoUpload", uploadUrlPrefix
                    + "/" + arquivo.getOriginalFilename());
            return new ModelAndView("redirect:/upload");
        } catch (IOException e) {
            // ERRO
            redirAttr.addFlashAttribute("msg", "Erro durante upload");
            return new ModelAndView("redirect:/upload");
        }
    }
    
    @GetMapping("/ver-imagens")
    public ModelAndView mostrarImagensUpload() {
        
        // Busca imagens carregadas no diretório de upload
        File diretorio = new File(uploadPath);
        File[] listaArquivos = diretorio.listFiles();
        
        List<InfoArquivo> arquivosImagem = new ArrayList<>();
        for (File arquivo : listaArquivos) {
            if (arquivo.getName().endsWith("jpg") || arquivo.getName().endsWith("png")) {
                arquivosImagem.add(new InfoArquivo(arquivo.getName(), uploadUrlPrefix + "/" + arquivo.getName()));
            }
        }
        
        ModelAndView mv = new ModelAndView("upload/ver-imagens");
        mv.addObject("arquivos", arquivosImagem);
        return mv;   
    }

}
