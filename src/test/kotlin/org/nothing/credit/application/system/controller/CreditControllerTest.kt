package org.nothing.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.nothing.credit.application.system.domain.Address
import org.nothing.credit.application.system.domain.Customer
import org.nothing.credit.application.system.dto.CreditDto
import org.nothing.credit.application.system.dto.CreditView
import org.nothing.credit.application.system.dto.CreditViewList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import org.nothing.credit.application.system.repository.CreditRepository
import org.nothing.credit.application.system.repository.CustomerRepository
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditControllerTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should save a credit and return a 201 status`() {
        //given
        val testCustomer = customerRepository.save(customerBuilder())
        val testCreditDto = creditDtoBuilder(testCustomer.id!!)

        //when
        //then
        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testCreditDto)
        }.andExpectAll {
            status { isCreated() }
            jsonPath("$.customerEmail") { value(testCustomer.email) }
            jsonPath("$.customerIncome") { value(testCustomer.income) }
            jsonPath("$.creditValue") { value(testCreditDto.creditValue) }
            jsonPath("$.firstInstallmentDate") { value(testCreditDto.firstInstallmentDate.toString()) }
            jsonPath("$.installmentsCount") { value(testCreditDto.installmentsCount) }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should find all credits by customerId and return a 200 status`() {
        //given
        val testCustomer = customerRepository.save(customerBuilder())
        val testCredit0 = creditRepository.save(creditDtoBuilder(testCustomer.id!!).toDomainEntity())
        val testCredit1 = creditRepository.save(
            creditDtoBuilder(
                testCustomer.id!!,
                creditValue = BigDecimal.valueOf(1001.02)
            ).toDomainEntity()
        )

        //when
        //then
        mockMvc.get("$URL?customerId=${testCustomer.id}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpectAll {
            status { isOk() }
            content {
                string(
                    objectMapper.writeValueAsString(
                        listOf(CreditViewList(testCredit0), CreditViewList(testCredit1))
                    )
                )
            }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should find a credit by its id and return a status 200`() {
        val testCustomer = customerRepository.save(customerBuilder())
        val testCredit0 = creditDtoBuilder(testCustomer.id!!).toDomainEntity()
        val testCredit1 = creditDtoBuilder(
            testCustomer.id!!,
            creditValue = BigDecimal.valueOf(1001.02)
        ).toDomainEntity()

        creditRepository.saveAll(listOf(testCredit0, testCredit1))
        // Manually setting these properties because the object returned from the repository on 'save()' does not have them yet
        testCredit1.apply {
            customer?.email = testCustomer.email
            customer?.income = testCustomer.income
        }

        //when
        //then
        mockMvc.get("$URL/${testCredit1.creditCode}?customerId=${testCustomer.id}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpectAll {
            status { isOk() }
            content { string(objectMapper.writeValueAsString(CreditView(testCredit1))) }
        }.andDo {
            print()
        }
    }

    private fun customerBuilder(
        firstName: String = "John",
        lastName: String = "Doe",
        cpf: String = "01010101010",
        email: String = "nao-tenho@algumemail.com",
        password: String = "thisIsUmaForte@Senha#098",
        income: BigDecimal = BigDecimal.valueOf(1.01),
        zipCode: String = "01234-567",
        street: String = "Baker Street 221B",
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        income = income,
        address = Address(zipCode, street),
    )

    private fun creditDtoBuilder(
        customerId: Long,
        creditValue: BigDecimal = BigDecimal.valueOf(10000.09),
        firstInstallmentDate: LocalDate = LocalDate.of(2024, Month.FEBRUARY, 20),
        installmentsCount: Int = 32,
    ) = CreditDto(
        creditValue = creditValue,
        firstInstallmentDate = firstInstallmentDate,
        installmentsCount = installmentsCount,
        customerId = customerId
    )
}