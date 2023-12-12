package org.nothing.credit.application.system.controller

import org.nothing.credit.application.system.domain.Customer
import org.nothing.credit.application.system.dto.CustomerDto
import org.nothing.credit.application.system.dto.CustomerUpdateDto
import org.nothing.credit.application.system.dto.CustomerView
import org.nothing.credit.application.system.service.impl.CustomerService
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
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping
    fun saveCustomer(@RequestBody customerDto: CustomerDto): String {
        val savedCustomer = this.customerService.save(customerDto.toDomainEntity())

        return "Customer ${savedCustomer.email} saved!"
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): CustomerView {
        val customer = this.customerService.findById(id)

        return CustomerView(customer)
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long) {
        this.customerService.delete(id)
    }

    @PatchMapping
    fun updateCustomer(
        @RequestParam(value = "customerId") id: Long, @RequestBody customerUpdateDto: CustomerUpdateDto
    ): CustomerView {
        val customer = this.customerService.findById(id)
        val customerToUpdate = customerUpdateDto.updateDomainEntity(customer)
        val customerUpdated = this.customerService.save(customerToUpdate)

        return CustomerView(customerUpdated)
    }
}
