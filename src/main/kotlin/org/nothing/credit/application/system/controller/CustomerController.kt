package org.nothing.credit.application.system.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

import org.nothing.credit.application.system.dto.CustomerDto
import org.nothing.credit.application.system.dto.CustomerUpdateDto
import org.nothing.credit.application.system.dto.CustomerView
import org.nothing.credit.application.system.service.impl.CustomerService

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<CustomerView> {
        val savedCustomer = this.customerService.save(customerDto.toDomainEntity())

        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(savedCustomer))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer = this.customerService.findById(id)

        return ResponseEntity.status(HttpStatus.OK).body(CustomerView(customer))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long): ResponseEntity<String> {
        val customer = this.customerService.findById(id)
        this.customerService.delete(id)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("${customer.email} deleted.")
    }

    @PatchMapping
    fun updateCustomer(
        @RequestParam(value = "customerId") id: Long, @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerView> {
        val customer = this.customerService.findById(id)
        val customerToUpdate = customerUpdateDto.updateDomainEntity(customer)
        val customerUpdated = this.customerService.save(customerToUpdate)

        return ResponseEntity.status(HttpStatus.OK).body(CustomerView(customerUpdated))
    }
}
