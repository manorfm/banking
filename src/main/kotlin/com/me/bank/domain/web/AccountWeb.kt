package com.me.bank.domain.web

import com.me.bank.domain.service.AccountService
import com.me.bank.domain.web.dto.*
import com.me.bank.infra.swagger.AccountWebSwagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountWeb(@Autowired private val accountService: AccountService): AccountWebSwagger {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun save(@RequestBody account: AccountIn): ResponseEntity<AccountOut> =
        accountService.save(account.toAccount())
            .run { ResponseEntity(toOut(), HttpStatus.CREATED) }

    @PostMapping("/branch/{branch}/number/{number}/debit",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun debit(@PathVariable branch: String,
    @PathVariable number: String,
    @RequestBody debitIn: DebitIn): ResponseEntity<AccountOut> =
        accountService.debit(branch, number, debitIn.value)
            .run { ResponseEntity(toOut(), HttpStatus.OK) }
}