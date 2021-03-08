package edu.kit.typicalc;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class configures server properties related to HTTP.
 */
@Configuration
public class TypicalcConfiguration implements WebMvcConfigurer {

    /**
     * Sets SameSite=Strict on all cookies.
     *
     * @return a customizer object that does the cookie modification
     */
    @Bean
    public TomcatContextCustomizer sameSiteCookiesConfig() {
        return context -> {
            Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
            cookieProcessor.setSameSiteCookies(SameSiteCookies.STRICT.getValue());
            context.setCookieProcessor(cookieProcessor);
        };
    }
}

