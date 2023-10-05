package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import med.voll.api.domain.user.User;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			String token = JWT.create()
					.withIssuer("API Voll.med")
					.withSubject(user.getLogin())
					.withExpiresAt(dateExpires())
					.sign(algorithm);
			
			System.out.println(token);
			
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("erro ao gerar token jwt", e);
		}
	}
	
	public String getSubject(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.require(algorithm)
		        .withIssuer("API Voll.med")
		        .build()
		        .verify(token)
		        .getSubject();
		} catch (JWTVerificationException exception){
		    throw new RuntimeException("Token inválido ou expirado.");
		}
	}

	private Instant dateExpires() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
