package br.com.webmobi.dadospessoais.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;

@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Converte ConstraintViolationException (lançada pelo @Validated do service)
     * em uma resposta 400 no formato RFC 7807 (Problem Details).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex) {
        MultiValueMap<String, String> errors = new LinkedMultiValueMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = extractField(violation.getPropertyPath());
            errors.add(field.isEmpty() ? "global" : field, violation.getMessage());
        }

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "A requisição contém campos inválidos");
        problem.setTitle("Erro de validação");
        problem.setProperty("errors", errors);

        return ResponseEntity.of(problem).build();
    }

    /**
     * Extrai apenas os nós PROPERTY/CONTAINER_ELEMENT do caminho, ignorando
     * nós de método, parâmetro e bean (gerados pelo AOP do @Validated).
     */
    private String extractField(Path path) {
        StringBuilder sb = new StringBuilder();
        for (Path.Node node : path) {
            ElementKind kind = node.getKind();
            if (kind == ElementKind.PROPERTY || kind == ElementKind.CONTAINER_ELEMENT) {
                if (!sb.isEmpty()) sb.append('.');
                sb.append(node.getName());
            }
        }
        return sb.toString();
    }

}
