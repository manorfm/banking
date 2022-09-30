package com.me.bank.domain.service

import com.me.bank.domain.model.fake.AccountFake
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("Account Service Integration")
class AccountServiceIntegrationTest constructor(@Autowired val accountService: AccountService): AbstractIntegrationTest() {

    @Test
    fun `given an account then should persist it`() {
        val account = AccountFake.withCleanAccount()
        val accountPersisted = accountService.save(account)

        assertNotNull(accountPersisted.id)
        assertEquals(accountPersisted, account)
    }
}