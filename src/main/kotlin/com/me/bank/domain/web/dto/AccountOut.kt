package com.me.bank.domain.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Model for an account as response.")
data class AccountOut(
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
    @field:Schema(
        description = "The balance of that account",
        type = "double"
    )
    val balance: BigDecimal?,
)
