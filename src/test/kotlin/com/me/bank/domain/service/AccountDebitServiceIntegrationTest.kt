package com.me.bank.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.me.bank.domain.model.Operation
import com.me.bank.domain.model.exception.CheckInAccountException
import com.me.bank.domain.model.fake.AccountFake
import com.me.bank.domain.provider.model.OperationRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.math.BigDecimal

@DisplayName("Account debit service integration")
@WireMockTest(httpPort = 3002)
class AccountDebitServiceIntegrationTest(@Autowired private val accountService: AccountService): AbstractIntegrationTest() {

    private fun status(status: HttpStatus) = status.value()

    @Test
    fun `given an account when debit value and apply it then should send operation to check-in account`() {
        //setup
        val account = AccountFake.withPositiveBalance()
        val branch = AccountFake.BRANCH
        val number = AccountFake.ACCOUNT_NUMBER
        val value = AccountFake.BALANCE_VALUE

        accountService.save(account)

        val operationRequest = OperationRequest(Operation.DEBIT, value)
        val bodyJson = ObjectMapper().writeValueAsString(operationRequest)

        stubFor(
            post(urlEqualTo("""/balance/branch/${branch}/number/${number}"""))
                .withHeader("Content-Type", containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(bodyJson))
                .willReturn(
                    aResponse()
                        .withStatus(status(HttpStatus.CREATED))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
        )

        //action
        val accountUpdated = accountService.debit(branch, number, value)

        //assertion
        assertEquals(accountUpdated.balance(), BigDecimal("0.00"))
    }

    @Test
    fun `given an account when debit value and sent operation to check-in with issue then should throw check-in account exception`() {
        //setup
        val account = AccountFake.withSecondNumberAccountAndPositiveBalance()
        val branch = AccountFake.BRANCH
        val number = AccountFake.ACCOUNT_NUMBER_2
        val value = AccountFake.BALANCE_VALUE

        accountService.save(account)

        //action and assertion
        assertThrows<CheckInAccountException> {
            accountService.debit(branch, number, value)
        }
    }
}