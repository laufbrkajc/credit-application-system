package org.nothing.credit.application.system.service

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.domain.Customer
import java.util.UUID

interface ICreditService {
    fun save(credit: Credit): Credit

    fun findAllByCustomer(customer: Customer): List<Credit>

    fun findByCreditCode(creditCode: UUID): Credit
}