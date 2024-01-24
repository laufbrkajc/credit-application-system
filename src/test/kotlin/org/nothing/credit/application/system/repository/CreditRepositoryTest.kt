package org.nothing.credit.application.system.repository

import java.util.UUID

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles

import org.nothing.credit.application.system.domain.Credit
import org.nothing.credit.application.system.domain.Customer
import org.nothing.credit.application.system.util.SampleObjectsBuilder

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager
    private val sampleObjectsBuilder = SampleObjectsBuilder()

    private var creditCode0 = UUID.fromString("3f593bf8-5b50-438e-8ed6-94400cb5b22e")
    private var creditCode1 = UUID.fromString("9fe00619-31bc-4739-b1ca-fb8547c17b65")
    private lateinit var customer0: Customer
    private lateinit var customer1: Customer
    private lateinit var credit0: Credit
    private lateinit var credit1: Credit

    @BeforeEach fun setup() {
        customer0 = testEntityManager.persist(sampleObjectsBuilder.customerBuilder(cpf = "012345678901", email = "email-customer0@lugarnenhum.com.br"))
        customer1 = testEntityManager.persist(sampleObjectsBuilder.customerBuilder(cpf = "010101010101", email = "email-customer1@lugarnenhum.com.br"))
        credit0 = testEntityManager.persist(sampleObjectsBuilder.creditBuilder(creditCode = creditCode0, customer = customer0))
        credit1 = testEntityManager.persist(sampleObjectsBuilder.creditBuilder(creditCode = creditCode1, customer = customer1))
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
}