package org.nothing.credit.application.system.dto

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.domain.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    val creditValue: BigDecimal,
    val firstInstallmentDate: LocalDate,
    val installmentsCount: Int,
    val customerId: Long
) {
    fun toDomainEntity(): Credit {
        return Credit(
            creditValue = this.creditValue,
            firstInstallmentDay = this.firstInstallmentDate,
            installmentsCount = this.installmentsCount,
            customer = Customer(id = this.customerId)
        )
    }
}
