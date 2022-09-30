package com.me.bank.domain.web

import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.model.fake.half
import com.me.bank.domain.service.AccountService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.willReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@WebMvcTest(AccountWeb::class)
@DisplayName("Account API")
class AccountWebDebitAPITest (@Autowired private val mockMvc: MockMvc){

    @MockBean
    private lateinit var accountService: AccountService

    @Test
    fun `given a branch and number and a value then should debit`() {
        //setup
        val account = AccountFake.withHalfOfBalance()
        val halfOfBalance = AccountFake.BALANCE_VALUE.half()

        val branch = AccountFake.BRANCH
        val number = AccountFake.ACCOUNT_NUMBER

        given { accountService.debit(branch, number, halfOfBalance) } willReturn { account }

        val body = object {
            val value = halfOfBalance
        }

        val expected = object {
            val branch = branch
            val number = number
            val balance = halfOfBalance
        }
        val mapper = ObjectMapper()

        mockMvc.post("/accounts/branch/$branch/number/$number/debit") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(body)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expected)) }
        }

        verify(accountService).debit(branch, number, halfOfBalance)
        verifyNoMoreInteractions(accountService)
    }
}