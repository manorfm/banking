package com.me.bank.domain.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class DebitIn(
    @field:Schema(
        description = "The value to debit on account",
        type = "double"
    )
    val value: BigDecimal,
)
