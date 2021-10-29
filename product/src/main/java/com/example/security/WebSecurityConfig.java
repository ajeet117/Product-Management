package com.example.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.example.utility.CustomLoginFailureHandler;
import com.example.utility.CustomLoginSucessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource datasource;

	private PersistentTokenRepository persistentTokenReposiroty() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(datasource);
		return tokenRepository;
	}

	@Autowired
	private CustomLoginFailureHandler failureHandler;
	@Autowired
	private CustomLoginSucessHandler successHandler;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**");
	}

	@Bean
	public UserDetailsService userDetailsSerive() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsSerive());
		auth.setPasswordEncoder(passwordEncoder());
		return auth;

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/oauth2/**","/resetpassword","/forgotpassword").permitAll()
		.antMatchers("/user/create", "/user/save").permitAll()
		.antMatchers("/**/delete/**").hasAuthority("ADMIN")
		.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.usernameParameter("email")
			.failureHandler(failureHandler)
			.successHandler(successHandler)
			.permitAll()
		.and()
		.oauth2Login()
			.loginPage("/login")
			.userInfoEndpoint().userService(oauth2UserService)
			.and()
		.successHandler(oauth2successHandler)
		.and()
		.logout().permitAll()
		.and()
		.rememberMe()
		.tokenRepository(persistentTokenReposiroty())
		.and()
		.exceptionHandling().accessDeniedPage("/403")
		;
	}

	@Autowired
	private CustomOAuth2UserService oauth2UserService;
	@Autowired
	private OAuth2SucessHandler oauth2successHandler;
}
