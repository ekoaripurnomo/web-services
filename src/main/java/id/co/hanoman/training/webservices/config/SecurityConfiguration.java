package id.co.hanoman.training.webservices.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("eko")
//				.password("eko123")
//					.roles("USER");
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("SELECT username, password, "
				+ "enabled FROM users WHERE username=?")
			.authoritiesByUsernameQuery("SELECT u.username, a.authority "
					+ "FROM users u INNER JOIN authorities a ON u.id = a.id_user WHERE u.username=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
			.defaultSuccessUrl("/alamat/list", true)
				.and()
			.logout()
				.and()
			.addFilterAfter(new CsrfAttributeToCookieFilter(), CsrfFilter.class)
			.csrf().csrfTokenRepository(csrfTokenRepository());
	}
	
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
	
	
	
	
}
