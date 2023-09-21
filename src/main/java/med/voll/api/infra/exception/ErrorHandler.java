package med.voll.api.infra.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ErrorHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handle404() {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handle400(MethodArgumentNotValidException e) {
		List<FieldError> errors = e.getFieldErrors();
		
		return ResponseEntity.badRequest().body(errors.stream().map(DadosErroValidacao::new).toList());
	}
	
	private record DadosErroValidacao(String campo, String mensagem) {
		public DadosErroValidacao(FieldError e) {
			this(e.getField(), e.getDefaultMessage());
		}
	}
	
}
