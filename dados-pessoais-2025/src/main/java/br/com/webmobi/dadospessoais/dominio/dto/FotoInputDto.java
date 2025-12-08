package br.com.webmobi.dadospessoais.dominio.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FotoInputDto {

	@NotNull
	private MultipartFile arquivo;

	@Size(max = 200)
	private String legenda;

	public MultipartFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(MultipartFile arquivo) {
		this.arquivo = arquivo;
	}

	public String getLegenda() {
		return legenda;
	}

	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}

}
