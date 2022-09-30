package com.me.bank.domain.provider.model

import com.me.bank.domain.model.Operation
import java.math.BigDecimal

data class OperationRequest(
    val operation: Operation,
    val value: BigDecimal
)
