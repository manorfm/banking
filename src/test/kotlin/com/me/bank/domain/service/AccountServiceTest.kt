package com.me.bank.domain.service

import com.me.bank.domain.model.exception.InsufficientBalanceException
import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.model.fake.half
import com.me.bank.domain.provider.CheckInAccountProvider
import com.me.bank.domain.repository.AccountRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@DisplayName("Account Service")
class AccountServiceTest {

    @Test
    fun `given an valid account then should persist it`() {
        //setup
        val account = AccountFake.withCleanAccount()
        val accountResponse = AccountFake.withCleanAccountPersisted()

        val repository : AccountRepository = mock {
            on { save(account) } doReturn accountResponse
        }

        val checkInAccountProvider : CheckInAccountProvider = mock()

        val accountService = AccountService(repository, checkInAccountProvider)

        //action
        val accountPersisted = accountService.save(account)

        //assertion
        assertNotNull(accountPersisted.id)
        assertEquals(accountResponse, accountPersisted)

        //mock assertions
        verify(repository).save(account)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `given a number and branch then should return an account`() {
        //setup
        val number = AccountFake.ACCOUNT_NUMBER
        val branch = AccountFake.BRANCH

        val accountResponse = AccountFake.withCleanAccountPersisted()

        val repository : AccountRepository = mock {
            on { findByBranchAndNumber(branch, number) } doReturn accountResponse
        }

        val checkInAccountProvider : CheckInAccountProvider = mock()
        val accountService = AccountService(repository, checkInAccountProvider)

        // action
        val account = accountService.get(branch, number)

        //assertions
        assertEquals(branch, account.branch)
        assertEquals(number, account.number)

        //mock assertions
        verify(repository).findByBranchAndNumber(branch, number)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `given an account when debit value and has balance then should apply it`() {
        //setup
        val number = AccountFake.ACCOUNT_NUMBER
        val branch = AccountFake.BRANCH
        val halfBalanceValue = AccountFake.BALANCE_VALUE.half()
        val accountResponse = AccountFake.withPositiveBalance()

        val repository : AccountRepository = mock {
            on { findByBranchAndNumber(branch, number) } doReturn accountResponse
        }

        val checkInAccountProvider : CheckInAccountProvider = mock()
        val accountService = AccountService(repository, checkInAccountProvider)

        val account = accountService.debit(branch, number, halfBalanceValue)

        //assertions
        assertEquals(halfBalanceValue, account.balance())

        //mock assertions
        verify(repository).findByBranchAndNumber(branch, number)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `given an account when debit value and has insufficient balance then should throw insufficient balance exception`() {
        //setup
        val number = AccountFake.ACCOUNT_NUMBER
        val branch = AccountFake.BRANCH
        val halfBalanceValue = AccountFake.BALANCE_VALUE.half()
        val accountResponse = AccountFake.withCleanAccountPersisted()

        val repository : AccountRepository = mock {
            on { findByBranchAndNumber(branch, number) } doReturn accountResponse
        }

        val checkInAccountProvider : CheckInAccountProvider = mock()
        val accountService = AccountService(repository, checkInAccountProvider)

        assertThrows<InsufficientBalanceException> {
            accountService.debit(branch, number, halfBalanceValue)
        }

        //mock assertions
        verify(repository).findByBranchAndNumber(branch, number)
        verifyNoMoreInteractions(repository)
    }
}