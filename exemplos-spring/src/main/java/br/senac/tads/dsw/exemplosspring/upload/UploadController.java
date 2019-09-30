/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * @author ftsuda
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

    private static final String DIRETORIO_UPLOAD = "C:/uploads/";

    private static final String CONTEXTO_ACESSO_UPLOAD = "/teste-uploads";

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

            Path destino = Paths.get(DIRETORIO_UPLOAD
                    + arquivo.getOriginalFilename());
            Files.write(destino, bytesArquivo);

            redirAttr.addFlashAttribute("msg", "Arquivo "
                    + arquivo.getOriginalFilename()
                    + " carregado com sucesso");
            redirAttr.addFlashAttribute("urlAcessoUpload", CONTEXTO_ACESSO_UPLOAD
                    + "/" + arquivo.getOriginalFilename());
            return new ModelAndView("redirect:/upload");
        } catch (IOException e) {
            // ERRO
            redirAttr.addFlashAttribute("msg", "Erro durante upload");
            return new ModelAndView("redirect:/upload");
        }
    }

}
