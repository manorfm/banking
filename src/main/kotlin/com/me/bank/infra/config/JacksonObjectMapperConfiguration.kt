package com.me.bank.infra.config

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
class JacksonObjectMapperConfiguration {

    @Autowired
    fun customize(objectMapper: ObjectMapper) =
        objectMapper
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true))
            .configOverride(BigDecimal::class.java).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING))
}