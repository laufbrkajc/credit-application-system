package org.nothing.credit.application.system.service.impl

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.repository.CreditRepository
import org.nothing.credit.application.system.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }

        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
        return this.creditRepository.findAllByCustomerId(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = this.creditRepository.findByCreditCode(creditCode) ?: throw IllegalArgumentException("creditCode $creditCode nao encontrado")

        if (credit.customer?.id == customerId)
            return credit
        else
            throw RuntimeException("Fale com o admin")
    }
}