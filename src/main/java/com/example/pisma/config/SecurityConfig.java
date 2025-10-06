package com.example.pisma.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Value("${app.admin.username}") String adminUser;
	@Value("${app.admin.password}") String adminPass;
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails admin = User.withUsername(adminUser)
				.password("{noop}"+adminPass)
				.roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(admin);
	}
	
	@Bean
	public SecurityFilterChain filter(HttpSecurity http) throws Exception {
		http.csrf(cs -> cs.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/public/**","/files/**","/v3/api-docs/**","/swagger-ui/**").permitAll()
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.httpBasic(Customizer.withDefaults());
		return http.build();
	}
}
