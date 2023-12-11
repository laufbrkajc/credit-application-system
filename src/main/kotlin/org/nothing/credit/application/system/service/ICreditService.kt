package org.nothing.credit.application.system.service

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.domain.Customer
import java.util.UUID

interface ICreditService {
    fun save(credit: Credit): Credit

    fun findAllByCustomer(customerId: Long): List<Credit>

    fun findByCreditCode(customerId: Long, creditCode: UUID): Credit
}