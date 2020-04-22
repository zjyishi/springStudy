package tacos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder encoder(){
		return new StandardPasswordEncoder("53cr3t");
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
		throws Exception{
		auth.inMemoryAuthentication().withUser("buzz")
			.password("infinity").authorities("ROLE_USER")
			.and().withUser("woody").password("bullseye")
			.authorities("ROLE_USER");
		
//		auth.jdbcAuthentication().dataSource(dataSource)
//			.usersByUsernameQuery(
//					"select username, password, enabled from Users" +
//					"where username=?")
//			.authoritiesByUsernameQuery(
//					"select username, authority from UserAuthorities" +
//					"where username=?")
//			.passwordEncoder(new StandardPasswordEncoder("53cr3t"));
		
//		auth.ldapAuthentication().userSearchFilter("(uid={0})").groupSearchFilter("member={0}");
		
//		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests().antMatchers("/design", "/orders")
			.access("hasRole('ROLE_USER')").antMatchers("/", "/**").access("permitAll")
			.and().formLogin().loginPage("/login").defaultSuccessUrl("/design", true)
			.loginProcessingUrl("/authenticate")
			.usernameParameter("user").passwordParameter("pwd")
			.and()
			.logout().logoutSuccessUrl("/");
		
	}
}
