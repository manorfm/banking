package com.me.bank.domain.web

import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.service.AccountService
import com.me.bank.domain.web.dto.AccountIn
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@DisplayName("Account Web")
class AccountWebTest {

    @Test
    fun `given a valid account thenm should save it and return account response`() {
        //setup
        val account = AccountFake.withCleanAccount()
        val accountResponse = AccountFake.withCleanAccountPersisted()

        val accountService : AccountService = mock {
            on { save(account) } doReturn accountResponse
        }

        val accountWeb = AccountWeb(accountService)
        val accountIn = AccountIn(AccountFake.BRANCH, AccountFake.ACCOUNT_NUMBER)

        //action
        val response = accountWeb.save(accountIn)
        val accountOut = response.body!!

        //assertions
        assertEquals(account.balance(), accountOut.balance)
        assertEquals(account.branch, accountOut.branch)
        assertEquals(account.number, accountOut.number)

        //mock assertions
        verify(accountService).save(account)
        verifyNoMoreInteractions(accountService)
    }
}