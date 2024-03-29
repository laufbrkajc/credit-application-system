package org.nothing.credit.application.system

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {
    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("springcreditapplicationsystem")
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .build()
    }
}