package no.steras.opensamlSamples.opensaml4WebprofileDemo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.steras.opensamlSamples.opensaml4WebprofileDemo.app.ApplicationServlet;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.idp.ArtifactResolutionServlet;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.idp.SingleSignOnServlet;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.AccessFilter;
import no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.ConsumerServlet;

/**
 * Spring Boot Web Configuration
 * Servlet/Filter registration for SAML processing
 */
@Configuration
public class WebConfig {

    /**
     * AccessFilter - protects /app/* endpoints
     */
    @Bean
    public FilterRegistrationBean<AccessFilter> accessFilterRegistration() {
        FilterRegistrationBean<AccessFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AccessFilter());
        registration.addUrlPatterns("/app/*");
        registration.setName("AccessFilter");
        registration.setOrder(1);
        return registration;
    }

    /**
     * ApplicationServlet - protected resource
     */
    @Bean
    public ServletRegistrationBean<ApplicationServlet> applicationServletRegistration() {
        ServletRegistrationBean<ApplicationServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new ApplicationServlet());
        registration.addUrlMappings("/app/appservlet");
        registration.setName("ApplicationServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    /**
     * SingleSignOnServlet - IdP SSO endpoint
     */
    @Bean
    public ServletRegistrationBean<SingleSignOnServlet> singleSignOnServletRegistration() {
        ServletRegistrationBean<SingleSignOnServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new SingleSignOnServlet());
        registration.addUrlMappings("/idp/singleSignOnService");
        registration.setName("SingleSignOnService");
        registration.setLoadOnStartup(1);
        return registration;
    }

    /**
     * ConsumerServlet - SP Assertion Consumer Service
     */
    @Bean
    public ServletRegistrationBean<ConsumerServlet> consumerServletRegistration() {
        ServletRegistrationBean<ConsumerServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new ConsumerServlet());
        registration.addUrlMappings("/sp/consumer");
        registration.setName("ConsumerServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    /**
     * ArtifactResolutionServlet - IdP Artifact Resolution endpoint
     */
    @Bean
    public ServletRegistrationBean<ArtifactResolutionServlet> artifactResolutionServletRegistration() {
        ServletRegistrationBean<ArtifactResolutionServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new ArtifactResolutionServlet());
        registration.addUrlMappings("/idp/artifactResolutionService");
        registration.setName("ArtifactResolutionServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
