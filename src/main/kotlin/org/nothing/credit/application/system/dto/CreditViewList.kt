package org.nothing.credit.application.system.dto

import org.nothing.credit.application.system.domain.Credit
import java.math.BigDecimal
import java.util.UUID

data class CreditViewList(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val installmentsCount: Int
) {
    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        installmentsCount = credit.installmentsCount
    )
}
