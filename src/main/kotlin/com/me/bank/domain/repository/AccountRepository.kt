package com.me.bank.domain.repository

import com.me.bank.domain.model.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {

    fun findByBranchAndNumber(branch: String, number: String): Account
}