package org.nothing.credit.application.system.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.nothing.credit.application.system.domain.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "first name can not be empty.") val firstName: String,
    @field:NotEmpty(message = "last name can not be empty.") val lastName: String,
    @NotNull(message = "income can not be null.") val income: BigDecimal,
    @field:NotEmpty(message = "zip code can not be empty.") val zipCode: String,
    @field:NotEmpty(message = "street can not be empty.") val street: String
) {
    fun updateDomainEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street

        return customer
    }
}
