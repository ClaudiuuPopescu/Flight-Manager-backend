package msg.project.flightmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import msg.project.flightmanager.repository.RefreshTokenRepositoy;
import msg.project.flightmanager.repository.UserRepository;

@Configuration
public class WebSecurityConfig {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RefreshTokenRepositoy refreshTokenRepositoy;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(this.userRepository, this.refreshTokenRepositoy), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize                                  
            			.requestMatchers("/auth/login").permitAll());
        
        return http.build();
    }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

}
