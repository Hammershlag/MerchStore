package com.example.merchstore;

import com.example.merchstore.components.enums.Language;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.example.merchstore.components.utilities.Defaults.*;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 26.09.2024
 */

@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    @Autowired
    private HttpServletRequest request;

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(DEFAULT_LOCALE);
        return messageSource;
    }

    public Language getCurrentLanguage() {
        Locale locale = localeResolver().resolveLocale(request);
        return Language.fromCode(locale.getLanguage());
    }

    private static class CustomLocaleResolver extends SessionLocaleResolver {


        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String lang = request.getParameter("lang");
            if (lang == null || lang.isEmpty()) {
                return DEFAULT_LOCALE;
            }
            Locale locale = Locale.forLanguageTag(lang);
            for(Locale loc : ALLOWED_LOCALES) {
                if (loc.getLanguage().equals(locale.getLanguage())) {
                    return loc;
                }
            }
            return FALLBACK_LOCALE;
        }
    }

}
