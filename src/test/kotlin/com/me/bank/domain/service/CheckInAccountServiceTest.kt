package com.me.bank.domain.service

import com.me.bank.domain.model.Operation
import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.model.fake.half
import com.me.bank.domain.provider.CheckInAccountProvider
import com.me.bank.domain.repository.AccountRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

@DisplayName("Check-in Account Service")
class CheckInAccountServiceTest {

    @Test
    fun `given an account when debit a value then should call check-in account to register operation`() {
        //setup
        val number = AccountFake.ACCOUNT_NUMBER
        val branch = AccountFake.BRANCH
        val halfBalanceValue = AccountFake.BALANCE_VALUE.half()
        val accountResponse = AccountFake.withPositiveBalance()

        val repository : AccountRepository = mock {
            on { findByBranchAndNumber(branch, number) } doReturn accountResponse
        }

        val checkInAccountProvider : CheckInAccountProvider = mock {
            on { balance(Operation.DEBIT, branch, number, halfBalanceValue) } doAnswer {}
        }
        val accountService = AccountService(repository, checkInAccountProvider)

        val account = accountService.debit(branch, number, halfBalanceValue)

        //assertions
        Assertions.assertEquals(halfBalanceValue, account.balance())

        //mock assertions
        verify(repository).findByBranchAndNumber(branch, number)
        verifyNoMoreInteractions(repository)

        verify(checkInAccountProvider).balance(Operation.DEBIT, branch, number, halfBalanceValue)
        verifyNoMoreInteractions(checkInAccountProvider)
    }
}