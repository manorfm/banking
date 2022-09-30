package com.me.bank.domain.web

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.model.fake.HttpHeader
import com.me.bank.domain.service.AbstractIntegrationTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 3002)
class AccountWebIntegrationTest constructor(@LocalServerPort private val serverPort: Int): AbstractIntegrationTest() {

    private fun status(status: HttpStatus) = status.value()

    @Test
    fun `given a branch and number then should create an account`() {
        val branch = AccountFake.BRANCH
        val number = AccountFake.ACCOUNT_NUMBER

        val requestIn = object {
            val branch = branch
            val number = number
        }

        Given {
            port(serverPort)
            header(HttpHeader.CONTENT_TYE, MediaType.APPLICATION_JSON_VALUE)
            body(requestIn)
        } When {
            post("/accounts")
        } Then {
            statusCode(status(HttpStatus.CREATED))
            body("branch", equalTo(branch))
            body("number", equalTo(number))
            body("balance", equalTo("0"))
        }
    }
}
