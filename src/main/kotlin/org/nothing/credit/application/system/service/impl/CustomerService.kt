package org.nothing.credit.application.system.service.impl

import org.nothing.credit.application.system.domain.Customer
import org.nothing.credit.application.system.exception.IdNotFoundException
import org.nothing.credit.application.system.repository.CustomerRepository
import org.nothing.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository): ICustomerService {
    override fun save(customer: Customer): Customer {
        return this.customerRepository.save(customer)
    }

    override fun findById(id: Long): Customer {
        return this.customerRepository.findById(id).orElseThrow { IdNotFoundException("Id $id nao encontrado.") }
    }

    override fun delete(id: Long) {
        val customer = this.findById(id)
        this.customerRepository.delete(customer)
    }
}