package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // I donot want to go for the default security configuration so i want to implement it here.than it will go with this configuration (SecurityConfig.class).
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(customizer -> customizer.disable());
		http.authorizeHttpRequests(request -> request
				.requestMatchers("register", "login")
				.permitAll()
				.anyRequest().authenticated());
//		http.formLogin(Customizer.withDefaults()); // this is for the form login page
		http.httpBasic(Customizer.withDefaults()); // this is for working from the postman...
		// make http as stateless http
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	// instead of the hard coding i want to store the user details into the table so commenting this code.
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user1  = User
//				.withDefaultPasswordEncoder()
//				.username("Kiran")
//				.password("k@123")
//				.roles("USER")
//				.build();
//		
//		UserDetails user2  = User
//				.withDefaultPasswordEncoder()
//				.username("harsh")
//				.password("h@123")
//				.roles("ADMIN")
//				.build();
//		
//		return new InMemoryUserDetailsManager(user1, user2);
//	}
	
	
	// new BCryptPasswordEncoder(12) here we have used to varify the enduser normal password with the bcryptpassword for the validation.
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		return provider;
		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return   config.getAuthenticationManager();
	}
	
	
	

}
