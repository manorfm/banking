package com.me.bank.domain.model

import com.me.bank.domain.model.exception.InsufficientBalanceException
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    private var balance: BigDecimal = BigDecimal.ZERO,
    val branch: String,
    val number: String,
) {
    fun balance() : BigDecimal = balance

    fun debit(value: BigDecimal) {
        val current = balance.subtract(value)
        if (current.signum() < 0) {
            throw InsufficientBalanceException()
        }
        this.balance = current;
    }

    data class Builder(
        var id: Long? = null,
        var current: BigDecimal = BigDecimal.ZERO,
        var branch: String? = null,
        var number: String? = null,
    ) {
        fun current(current: BigDecimal) = apply { this.current = current }
        fun branch(branch: String) = apply { this.branch = branch }
        fun number(number: String) = apply { this.number = number }
        fun id(id: Long) = apply { this.id = id }
        fun build() = Account(this.id, this.current!!, this.branch!!, this.number!!)
    }
}
