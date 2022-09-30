package com.me.bank.domain.service

import com.me.bank.domain.model.Account
import com.me.bank.domain.model.Operation
import com.me.bank.domain.provider.CheckInAccountProvider
import com.me.bank.domain.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountService(@Autowired private val accountRepository: AccountRepository,
                    @Autowired private val checkInAccountProvider: CheckInAccountProvider) {

    fun save(account: Account): Account = accountRepository.save(account)

    fun debit(branch: String, number: String, value: BigDecimal): Account =
        get(branch, number)
            .apply { debit(value) }
            .also { this.checkInAccountProvider.balance(Operation.DEBIT, branch, number, value) }

    fun get(branch: String, number: String): Account = accountRepository.findByBranchAndNumber(branch, number)
}