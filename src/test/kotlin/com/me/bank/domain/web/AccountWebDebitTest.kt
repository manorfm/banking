package com.me.bank.domain.web

import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.model.fake.half
import com.me.bank.domain.service.AccountService
import com.me.bank.domain.web.dto.DebitIn
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@DisplayName("Account Debit Web")
class AccountWebDebitTest {

    @Test
    fun `given a valid account then should save it and return an account out`() {
        //setup
        val account = AccountFake.withCleanAccount()
        val accountWithBalanceHalved = AccountFake.withHalfOfBalance()

        val branch = account.branch
        val number = account.number
        val halfOfBalance = AccountFake.BALANCE_VALUE.half()

        val accountService : AccountService = mock {
            on { debit(branch, number, halfOfBalance) } doReturn accountWithBalanceHalved
        }

        val accountWeb = AccountWeb(accountService)
        val debitIn = DebitIn(halfOfBalance)

        //action
        val response = accountWeb.debit(branch, number, debitIn)
        val accountOut = response.body!!

        //assertion
        assertEquals(halfOfBalance, accountOut.balance)
        assertEquals(branch, accountOut.branch)
        assertEquals(number, accountOut.number)

        verify(accountService).debit(branch, number, halfOfBalance)
        verifyNoMoreInteractions(accountService)
    }
}