package org.nothing.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import org.nothing.credit.application.system.domain.Address
import org.nothing.credit.application.system.domain.Customer
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "first name can not be empty.") val firstName: String,
    @field:NotEmpty(message = "last name can not be empty.") val lastName: String,
    @field:NotEmpty(message = "cpf can not be empty.") @CPF(message = "invalid cpf.") val cpf: String,
    @NotNull(message = "income can not be null.") val income: BigDecimal,
    @field:NotEmpty(message = "email can not be empty.") @Email(message = "invalid email.") val email: String,
    @field:NotEmpty(message = "password can not be empty.") val password: String,
    @field:NotEmpty(message = "zip code can not be empty.") val zipCode: String,
    @field:NotEmpty(message = "street can not be empty.") val street: String
) {

    fun toDomainEntity(): Customer {
        return Customer(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = this.cpf,
            income = this.income,
            email = this.email,
            password = this.password,
            address = Address(this.zipCode, this.street)
        )
    }

}