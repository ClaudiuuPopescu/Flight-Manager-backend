package msg.project.flightmanager.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import msg.project.flightmanager.model.RefreshToken;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.RefreshTokenRepositoy;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.TokenService;

@AllArgsConstructor
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private static final String AUTHORITIES = "authorities";

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RefreshTokenRepositoy refreshTokenRepositoy;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            if (TokenService.checkJWTToken(request)) {

                Claims claims = TokenService.validateToken(request);

                String userName = claims.getSubject();
                User user = this.userRepository.findByUsername(userName).get();
                
                Optional<RefreshToken> refreshToken = this.refreshTokenRepositoy.getRefreshTokenByUser(user);
                
                if (refreshToken.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (claims.get(AUTHORITIES) != null) {
                    TokenService.setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }
}
