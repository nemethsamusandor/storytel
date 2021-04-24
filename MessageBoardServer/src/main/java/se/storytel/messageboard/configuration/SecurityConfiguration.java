package se.storytel.messageboard.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security configuration for MessageBoard service
 *
 * @author Sandor Nemeth
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private final DataSource dataSource;

    @Autowired
    public SecurityConfiguration(DataSource dataSource)
    {
        super();
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.sessionManagement()
            .sessionCreationPolicy((SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeRequests()
            .mvcMatchers("/api/health").permitAll()
            .mvcMatchers("/h2-console/**").permitAll()
            .mvcMatchers("/api/messages").hasAnyRole("USER")
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .csrf()
            .disable();

        httpSecurity.headers()
            .frameOptions()
            .sameOrigin();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(new BCryptPasswordEncoder())
            .usersByUsernameQuery("select username,password,enabled "
                + "from client "
                + "where username = ?");
    }
}
