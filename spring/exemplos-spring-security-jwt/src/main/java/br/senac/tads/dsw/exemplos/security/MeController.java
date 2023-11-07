package br.senac.tads.dsw.exemplos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @Autowired
    private UsuarioSistemaService usuarioService;

    @GetMapping("/me")
    public UsuarioDto buscarUsuario(Authentication authentication) {
        String username = (String) authentication.getName();

        UsuarioSistema usuario = usuarioService.loadUserByUsername(username);
        UsuarioDto dto = new UsuarioDto();
        dto.setNome(usuario.getNomeCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setUrlFoto("http://localhost:8080/img/" + usuario.getArquivoFoto());
        return dto;
    }
}
