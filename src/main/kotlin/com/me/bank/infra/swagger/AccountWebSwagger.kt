package com.me.bank.infra.swagger

import com.me.bank.domain.model.exception.ErrorMessage
import com.me.bank.domain.web.dto.AccountIn
import com.me.bank.domain.web.dto.AccountOut
import com.me.bank.domain.web.dto.DebitIn
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity

interface AccountWebSwagger {

    @Operation(summary = "Create a new account based on branch and number",
        description = "Create a new account based on branch and number")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Successful operation")
        ]
    )
    fun save(account: AccountIn): ResponseEntity<AccountOut>

    @Operation(summary = "Debit an value on account by branch and number",
        description = "Debit an value on account by branch and number")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation"),
            ApiResponse(responseCode = "404", description = "Insufficient balance", content = [
                Content(mediaType = "application/json",
                    array = (
                        ArraySchema(schema = Schema(implementation = ErrorMessage::class))
                    )
                )
            ])
        ]
    )
    fun debit(branch: String, number: String, account: DebitIn): ResponseEntity<AccountOut>
}