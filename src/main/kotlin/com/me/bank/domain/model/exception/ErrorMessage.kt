package com.me.bank.domain.model.exception

import io.swagger.v3.oas.annotations.media.Schema

data class ErrorMessage(
    @field:Schema(
        description = "Error status",
        type = "number"
    )
    val status: Int,
    @field:Schema(
        description = "Root cause of the error",
        type = "string"
    )
    val message: String
)
