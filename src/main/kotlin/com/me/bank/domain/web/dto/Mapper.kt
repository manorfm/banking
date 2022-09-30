package com.me.bank.domain.web.dto

import com.me.bank.domain.model.Account

fun Account.toOut(): AccountOut = AccountOut(branch, number, balance())

fun AccountIn.toAccount(): Account = Account
    .Builder()
        .branch(branch!!)
        .number(number!!)
    .build()