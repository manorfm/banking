package com.me.bank.domain.web

import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.service.AccountService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
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
class AccountWebAPITest (@Autowired private val mockMvc: MockMvc){

    @MockBean
    private lateinit var accountService: AccountService

    @Test
    fun `given a branch and number then should create an account`() {
        //setup
        val account = AccountFake.withCleanAccount()
        val branch = AccountFake.BRANCH
        val number = AccountFake.ACCOUNT_NUMBER

        given { accountService.save(account) } willReturn { account }

        val body = object {
            val branch = branch
            val number = number
        }

        val expected = object {
            val branch = branch
            val number = number
            val balance = 0
        }
        val mapper = ObjectMapper()

        mockMvc.post("/accounts") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(body)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expected)) }
        }
    }
}