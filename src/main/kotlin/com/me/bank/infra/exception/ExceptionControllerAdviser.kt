package com.me.bank.infra.exception

import com.me.bank.domain.model.exception.CheckInAccountException
import com.me.bank.domain.model.exception.ErrorMessage
import com.me.bank.domain.model.exception.InsufficientBalanceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdviser {

    private fun toError(status: HttpStatus, message: String?): ResponseEntity<ErrorMessage> =
        ResponseEntity(ErrorMessage(status.value(), message!!), status)

    @ExceptionHandler
    fun handlerError(exception: InsufficientBalanceException): ResponseEntity<ErrorMessage> =
        toError(HttpStatus.NOT_FOUND, exception.message)


    @ExceptionHandler
    fun handlerError(exception: CheckInAccountException): ResponseEntity<ErrorMessage> =
        toError(HttpStatus.BAD_REQUEST, exception.message)
}