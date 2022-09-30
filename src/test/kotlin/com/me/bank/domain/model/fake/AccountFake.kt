package com.me.bank.domain.model.fake

import com.me.bank.domain.model.Account
import java.math.BigDecimal

fun BigDecimal.half(): BigDecimal = this.divide(BigDecimal(2))

class AccountFake private constructor() {

    companion object {
        const val ACCOUNT_NUMBER = "1214123-1"
        const val ACCOUNT_NUMBER_2 = "151232-2"
        const val ACCOUNT_ID = 3918L
        const val BRANCH = "0001"
        val BALANCE_VALUE: BigDecimal = BigDecimal.valueOf(1000L)

        private fun base(): Account.Builder =
            Account.Builder()
                .branch(BRANCH)
                .number(ACCOUNT_NUMBER)

        fun withCleanAccount(): Account = base().build()

        fun withCleanAccountPersisted(): Account = base()
            .id(ACCOUNT_ID)
            .build()

        fun withPositiveBalance(): Account =
            base()
                .current(BALANCE_VALUE)
                .build()

        fun withSecondNumberAccountAndPositiveBalance(): Account =
            base().current(BALANCE_VALUE)
                .number(ACCOUNT_NUMBER_2)
            .build()

        fun withHalfOfBalance(): Account =
            base()
                .current(BALANCE_VALUE.half())
            .build()
    }
}