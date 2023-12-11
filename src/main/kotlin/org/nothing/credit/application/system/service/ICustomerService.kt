package org.nothing.credit.application.system.service

import org.nothing.credit.application.system.domain.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer

    fun findById(id: Long): Customer

    fun delete(id: Long)
}