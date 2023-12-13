package org.nothing.credit.application.system.dto

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.enums.Status
import java.math.BigDecimal
import java.util.UUID

data class CreditView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val installmentsCount: Int,
    val status: Status,
    val customerEmail: String?,
    val customerIncome: BigDecimal?
) {
    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        installmentsCount = credit.installmentsCount,
        status = credit.status,
        customerEmail = credit.customer?.email,
        customerIncome = credit.customer?.income
    )
}
