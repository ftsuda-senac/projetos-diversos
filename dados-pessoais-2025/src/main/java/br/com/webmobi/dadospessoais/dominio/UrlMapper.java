package br.com.webmobi.dadospessoais.dominio;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlMapper {

	@Value("${app.upload-url-prefix}")
	private String uploadUrlPrefix;

	public String getImagemUrlPath(UUID pessoaId, String nomeArquivo) {
		return uploadUrlPrefix + "/" + pessoaId + "/fotos/" + nomeArquivo;
	}

}
