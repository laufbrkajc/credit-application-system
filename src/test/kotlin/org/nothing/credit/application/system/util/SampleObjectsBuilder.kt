package org.nothing.credit.application.system.util

import org.nothing.credit.application.system.domain.Address
import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.domain.Customer
import org.nothing.credit.application.system.dto.CreditDto
import org.nothing.credit.application.system.dto.CustomerDto
import org.nothing.credit.application.system.dto.CustomerUpdateDto
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

class SampleObjectsBuilder {
    private val firstName = "John"
    private val lastName = "Doe"
    private val cpf = "12962624642"
    private val email = "john-doe@nowhere.net"
    private val password = "senhaFortE@000"
    private val income = BigDecimal.valueOf(12345.68)
    private val address = Address("01010101", "Baker Street")
    private val creditValue = BigDecimal.valueOf(11223.01)
    private val firstInstallmentDate = LocalDate.of(2024, Month.FEBRUARY, 22)
    private val installmentsCount = 24

    // Some tests need to set the id of this class, but objects with that are to be persisted cannot have that
    // so this Builder is used for tests that set the id and the next for tests that persist the object
    fun customerBuilder(
        id: Long,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        cpf: String = this.cpf,
        email: String = this.email,
        password: String = this.password,
        income: BigDecimal = this.income,
        address: Address = this.address
    ) = Customer(
        id = id,
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        income = income,
        address = address,
    )

    // Builder for tests that persist the object
    fun customerBuilder(
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        cpf: String = this.cpf,
        email: String = this.email,
        password: String = this.password,
        income: BigDecimal = this.income,
        address: Address = this.address
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        income = income,
        address = address,
    )

    fun customerDtoBuilder(
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        cpf: String = this.cpf,
        email: String = this.email,
        income: BigDecimal = this.income,
        password: String = this.password,
        zipCode: String = this.address.zipCode,
        street: String = this.address.street
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )

    fun creditBuilder(
        creditCode: UUID,
        creditValue:BigDecimal = this.creditValue,
        firstInstallmentDay: LocalDate = this.firstInstallmentDate,
        installmentsCount: Int = this.installmentsCount,
        customer: Customer
    ): Credit = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        firstInstallmentDay = firstInstallmentDay,
        installmentsCount = installmentsCount,
        customer = customer
    )

    fun customerUpdateDtoBuilder(
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        income: BigDecimal = this.income,
        zipCode: String = this.address.zipCode,
        street: String = this.address.street
    ) = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )

    fun creditDtoBuilder(
        customerId: Long,
        creditValue: BigDecimal = this.creditValue,
        firstInstallmentDate: LocalDate = this.firstInstallmentDate,
        installmentsCount: Int = this.installmentsCount,
    ) = CreditDto(
        creditValue = creditValue,
        firstInstallmentDate = firstInstallmentDate,
        installmentsCount = installmentsCount,
        customerId = customerId
    )
}