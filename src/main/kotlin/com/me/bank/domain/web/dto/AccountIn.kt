package com.me.bank.domain.web.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Model for an account as request body.")
data class AccountIn(
    @field:Schema(
        description = "Which branch bank this account belong",
        type = "string"
    )
    val branch: String?,
    @field:Schema(
        description = "Client number account on this bank",
        type = "string"
    )
    val number: String?,
)