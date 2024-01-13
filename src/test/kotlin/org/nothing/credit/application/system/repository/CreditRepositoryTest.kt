package org.nothing.credit.application.system.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import org.nothing.credit.application.system.domain.Address
import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.domain.Customer
import java.time.LocalDate
import java.time.Month
import java.util.UUID

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private var creditCode0 = UUID.fromString("3f593bf8-5b50-438e-8ed6-94400cb5b22e")
    private var creditCode1 = UUID.fromString("9fe00619-31bc-4739-b1ca-fb8547c17b65")
    private lateinit var customer0: Customer
    private lateinit var customer1: Customer
    private lateinit var credit0: Credit
    private lateinit var credit1: Credit

    @BeforeEach fun setup() {
        customer0 = testEntityManager.persist(buildCustomer(cpf = "012345678901", email = "email-customer0@lugarnenhum.com.br"))
        customer1 = testEntityManager.persist(buildCustomer(cpf = "010101010101", email = "email-customer1@lugarnenhum.com.br"))
        credit0 = testEntityManager.persist(buildCredit(creditCode = creditCode0, customer = customer0))
        credit1 = testEntityManager.persist(buildCredit(creditCode = creditCode1, customer = customer1))
    }

    @Test
    fun `should find credit by credit code`() {
        //given
        val fakeCredit0 = creditRepository.findByCreditCode(creditCode0)
        val fakeCredit1 = creditRepository.findByCreditCode(creditCode1)

        //when

        //then
        Assertions.assertThat(fakeCredit0).isNotNull
        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit0).isSameAs(credit0)
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
    }

    @Test
    fun `should find all credits by customer id`() {
        //given
        val customerId = customer0.id ?: -1

        //when
        val creditList = creditRepository.findAllByCustomerId(customerId)

        //then
        Assertions.assertThat(creditList).isNotEmpty
        Assertions.assertThat(creditList.size).isEqualTo(1)
        Assertions.assertThat(creditList).contains(credit0)
    }

    private fun buildCredit(
        creditCode: UUID,
        creditValue:BigDecimal = BigDecimal.valueOf(616.0),
        firstInstallmentDay: LocalDate = LocalDate.of(2024, Month.MARCH, 13),
        installmentsCount: Int = 19,
        customer: Customer
    ): Credit = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        firstInstallmentDay = firstInstallmentDay,
        installmentsCount = installmentsCount,
        customer = customer
    )

    private fun buildCustomer(
        firstName: String = "Gabriel",
        lastName: String = "Martins",
        cpf: String = "01010101010",
        email: String = "nada@lugarnenhum.com.br",
        password: String = "senhaForte@123",
        zipCode: String = "01010101",
        street: String = "Rua dos Bobos",
        income: BigDecimal = BigDecimal.valueOf(1010101.1),
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(zipCode, street),
        income = income,
    )
}