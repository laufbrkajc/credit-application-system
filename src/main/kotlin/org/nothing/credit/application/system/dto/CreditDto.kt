package org.nothing.credit.application.system.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.domain.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "credit value can not be null.") val creditValue: BigDecimal,
    @field:DateIsBefore(value = "2024-01-31") @field:Future val firstInstallmentDate: LocalDate,
    @field:Min(value = 2) @field:Max(value = 48) val installmentsCount: Int,
    @field:NotNull(message = "customer id can not be null.") val customerId: Long
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
