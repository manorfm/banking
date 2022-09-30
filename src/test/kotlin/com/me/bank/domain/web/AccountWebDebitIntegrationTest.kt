package com.me.bank.domain.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.me.bank.domain.model.Operation
import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.model.fake.HttpHeader
import com.me.bank.domain.model.fake.half
import com.me.bank.domain.provider.model.OperationRequest
import com.me.bank.domain.service.AbstractIntegrationTest
import com.me.bank.domain.service.AccountService
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 3002)
class AccountWebDebitIntegrationTest constructor(@LocalServerPort private val serverPort: Int,
                                        @Autowired private val accountService: AccountService): AbstractIntegrationTest() {

    private fun status(status: HttpStatus) = status.value()

    @BeforeEach
    fun setup() {
        val account = AccountFake.withPositiveBalance()
        accountService.save(account)

        val branch = AccountFake.BRANCH
        val number = AccountFake.ACCOUNT_NUMBER
        val halfOfValue = AccountFake.BALANCE_VALUE.half()

        val checkInOperation = OperationRequest(Operation.DEBIT, halfOfValue)
        val bodyJson = ObjectMapper().writeValueAsString(checkInOperation)

        stubFor(
            post(urlEqualTo("/balance/branch/${branch}/number/${number}"))
                .withHeader(HttpHeader.CONTENT_TYE, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(bodyJson))
                .willReturn(
                    aResponse()
                        .withStatus(status(HttpStatus.CREATED))
                        .withHeader(HttpHeader.CONTENT_TYE, MediaType.APPLICATION_JSON_VALUE)
                )
        )
    }

    @Test
    fun `given a branch and number and value then should debit it`() {
        val branch = AccountFake.BRANCH
        val number = AccountFake.ACCOUNT_NUMBER
        val halfOfValue = AccountFake.BALANCE_VALUE.half()

        val requestDebitIn = object {
            val value = halfOfValue
        }

        Given {
            port(serverPort)
            header(HttpHeader.CONTENT_TYE, MediaType.APPLICATION_JSON_VALUE)
            body(requestDebitIn)
        } When {
            post("/accounts/branch/{branch}/number/{number}/debit", branch, number)
        } Then {
            log()
            statusCode(status(HttpStatus.OK))
            body("branch", equalTo(branch))
            body("number", equalTo(number))
            body("balance", equalTo("500.00"))
        }
    }

}