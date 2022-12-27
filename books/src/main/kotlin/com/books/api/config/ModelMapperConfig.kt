package com.books.api.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper().apply {
            configuration.matchingStrategy = MatchingStrategies.STRICT
        }
    }
}