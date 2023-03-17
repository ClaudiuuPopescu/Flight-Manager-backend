package msg.project.flightmanager.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.service.TokenService;

@Data
@Component
public class JwtUtils {

	private static final String LOGIN_PATH = "/auth/login";
	private static final String REFRESH_PATH = "/auth/refresh";

	@Autowired
	private TokenService tokenService;

	private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;

	@Value("${spring.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${spring.app.jwtCookieName}")
	private String jwtCookie;

	@Value("${spring.app.jwtRefreshCookieName}")
	private String jwtRefreshCookie;

	private ResponseCookie generateCookie(String token, String cookieType, String path) {
		ResponseCookie cookie = ResponseCookie.from(cookieType, token).path(path).maxAge(jwtExpirationMs).httpOnly(true)
				.build();
		return cookie;
	}

	public ResponseCookie generateJwtCookie(User user) {
		String token = this.tokenService.getJWTToken(user.getUsername(), user.getRole().getTitle());
		return generateCookie(token, jwtCookie, LOGIN_PATH);
	}

	public ResponseCookie generateRefreshCookie(String refreshToken) {
		return generateCookie(refreshToken, jwtRefreshCookie, REFRESH_PATH);
	}

	private String getCookieValueByName(HttpServletRequest request, String name) {
		Cookie cookie = WebUtils.getCookie(request, name);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	public ResponseCookie getCleanJwtCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path(LOGIN_PATH).build();
		return cookie;
	}

	public ResponseCookie getCleanJwtRefreshCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null).path(REFRESH_PATH).build();
		return cookie;
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
	
	public String getJwtFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, jwtCookie);
	}

	public String getJwtRefreshFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, jwtRefreshCookie);
	}

}
