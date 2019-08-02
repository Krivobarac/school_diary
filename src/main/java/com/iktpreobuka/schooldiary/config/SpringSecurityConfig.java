package com.iktpreobuka.schooldiary.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.iktpreobuka.schooldiary.entities.SuperAdminEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.repositories.SuperAdminRepository;
import com.iktpreobuka.schooldiary.repositories.UserRepository;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AuthenticationEntryPoint entryPoint;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		if( ((List<UserEntity>) userRepository.findAll()).size() == 0 ) {
			usersQuery = "select 'superadmin@fake.com' as principal, '$2a$10$/Lz0Do..XaE5c4S1NtHq4eesy9WoDZrt9UYAVJIcuQmwrKffNi322' as credentials, true where 'superadmin@fake.com' = ?";
			rolesQuery = "select 'superadmin@fake.com', 'ROLE_SUPER_ADMIN' where 'superadmin@fake.com' = ?";
		} 
		auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery).dataSource(dataSource).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic().authenticationEntryPoint(entryPoint);
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	return encoder;
	}

}
