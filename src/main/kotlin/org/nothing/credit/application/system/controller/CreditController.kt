package org.nothing.credit.application.system.controller

import org.nothing.credit.application.system.dto.CreditDto
import org.nothing.credit.application.system.service.impl.CreditService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/credits")
class CreditController(
    private val creditService: CreditService
) {
    @PostMapping
    fun saveCredit(@RequestBody creditDto: CreditDto): String {
        val credit = this.creditService.save(creditDto.toDomainEntity())

        return "Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!"
    }
}