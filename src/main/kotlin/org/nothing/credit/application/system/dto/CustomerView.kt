package org.nothing.credit.application.system.dto

import org.nothing.credit.application.system.domain.Customer
import java.math.BigDecimal

data class CustomerView(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipCode: String,
    val street: String,
    val id: Long?
) {

    constructor(domainCustomer: Customer): this (
        firstName = domainCustomer.firstName,
        lastName = domainCustomer.lastName,
        cpf = domainCustomer.cpf,
        income = domainCustomer.income,
        email = domainCustomer.email,
        zipCode = domainCustomer.address.zipCode,
        street = domainCustomer.address.street,
        id = domainCustomer.id
    )
}
