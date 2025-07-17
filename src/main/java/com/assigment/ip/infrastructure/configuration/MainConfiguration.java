package com.assigment.ip.infrastructure.configuration;

import com.assigment.ip.application.port.in.IpLookupUseCase;
import com.assigment.ip.application.port.in.IpValidateUseCase;
import com.assigment.ip.application.port.out.*;
import com.assigment.ip.application.service.IpLookupServiceImpl;
import com.assigment.ip.application.service.IpValidateServiceImpl;
import com.assigment.ip.application.service.ProviderService;
import com.assigment.ip.infrastructure.log.LoggerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class MainConfiguration {

    @Bean
    public IpLookupUseCase IpLookupService(IpLocationProvider ipLocationProvider,
                                           IpLocationRepository repository,
                                           IpRequestQueue ipRequestQueue,
                                           Notifier notifier,
                                           IpValidateUseCase ipValidateUseCase,
                                           LoggerImpl logger) {
        return new IpLookupServiceImpl(ipLocationProvider, repository, ipRequestQueue, notifier, ipValidateUseCase, logger);
    }

    @Bean
    public IpLocationProvider IpLocationProviderService(
            ProviderRepository repository,
            ProviderResultMapper mapper,
            LoggerImpl logger
    ) {
        return new ProviderService(repository, mapper, "FreeIPAPI", logger);
    }

    @Bean
    public IpValidateUseCase ipValidateUseCase(
            IpLocationRepository repository,
            LoggerImpl logger
    ) {
        return new IpValidateServiceImpl(repository, logger);
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // Your Angular app origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }

}
