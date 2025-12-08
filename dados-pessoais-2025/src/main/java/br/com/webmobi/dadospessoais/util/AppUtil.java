package br.com.webmobi.dadospessoais.util;

import java.text.Normalizer;
import java.util.Map;
import java.util.Map.Entry;

import org.jspecify.annotations.NonNull;

public class AppUtil {

	public static String normalizarString(@NonNull String input) {
		// Remove acentuações
		String normalized = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
		// Remove os diacríticos (caracteres de acento)
		normalized = normalized.replaceAll("\\p{M}", "");
		// Remove caracteres especiais, exceto hífen e underscore
		normalized = normalized.replaceAll("[^a-zA-Z0-9-_\\s]", "");
		// Substitui espaços por hífen
		normalized = normalized.replaceAll("\\s+", "-");
		// Converte para minúsculas
		return normalized.toLowerCase();
	}

	public static String normalizarStringComExcecoes(@NonNull String input, Map<String, String> excecoes) {
		if (excecoes != null && !excecoes.isEmpty()) {
			for (Entry<String, String> entry : excecoes.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(input)) {
					return entry.getValue().toLowerCase();
				}
			}
		}
		return normalizarString(input);
	}

}
