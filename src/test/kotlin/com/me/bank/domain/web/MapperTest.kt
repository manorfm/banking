package com.me.bank.domain.web

import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.web.dto.AccountIn
import com.me.bank.domain.web.dto.toAccount
import com.me.bank.domain.web.dto.toOut
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@DisplayName("Web Mapper In/Out")
class MapperTest {

    @Test
    fun `given an accountIn then should mapper to account`() {
        val accountIn = AccountIn(AccountFake.BRANCH, AccountFake.ACCOUNT_NUMBER)
        val account = accountIn.toAccount()

        assertEquals(accountIn.branch, account.branch)
        assertEquals(accountIn.number, account.number)
        assertEquals(BigDecimal.ZERO, account.balance())
    }

    @Test
    fun `given an account then should mapper to accountOut`() {
        val account = AccountFake.withPositiveBalance()

        val accountOut = account.toOut()

        assertEquals(accountOut.branch, account.branch)
        assertEquals(accountOut.number, account.number)
        assertEquals(accountOut.balance, account.balance())
    }
}