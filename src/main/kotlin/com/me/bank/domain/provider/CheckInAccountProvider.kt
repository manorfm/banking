package com.me.bank.domain.provider

import com.me.bank.domain.model.Operation
import com.me.bank.domain.model.exception.CheckInAccountException
import com.me.bank.domain.provider.model.OperationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class CheckInAccountProvider(@Autowired private val checkInAccount: CheckInAccountClient) {

    fun balance(operation: Operation, branch: String, number: String, value: BigDecimal) =
        OperationRequest(operation, value).let {
            checkInAccount.balance(branch, number, it).run {
                execute().run {
                    if (!isSuccessful) throw CheckInAccountException(errorBody()!!.string())
                }
            }
        }
}