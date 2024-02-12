package org.nothing.credit.application.system.controller

import java.util.UUID
import java.util.stream.Collectors

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.dto.CreditDto
import org.nothing.credit.application.system.dto.CreditView
import org.nothing.credit.application.system.dto.CreditViewList
import org.nothing.credit.application.system.service.impl.CreditService

@RestController
@RequestMapping("/api/credits")
class CreditController(
    private val creditService: CreditService
) {
    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<CreditView> {
        val savedCredit = this.creditService.save(creditDto.toDomainEntity())

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CreditView(savedCredit))
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewList>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewList(credit) }
            .collect(Collectors.toList())

        return ResponseEntity.status(if (creditViewList.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK).body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long, @PathVariable creditCode: UUID): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.findByCreditCode(customerId, creditCode)

        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }
}