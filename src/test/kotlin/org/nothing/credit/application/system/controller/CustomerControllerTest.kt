package org.nothing.credit.application.system.controller

import java.math.BigDecimal

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import org.nothing.credit.application.system.dto.CustomerDto
import org.nothing.credit.application.system.dto.CustomerUpdateDto
import org.nothing.credit.application.system.repository.CustomerRepository

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CustomerControllerTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL = "/api/customers"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should save a customer and return a 201 status`() {
        //given
        val customerDto = customerDtoBuilder()
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Cami"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Cavalcante"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("012345678901"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("camil@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("00000-000"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua da Camila"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with duplicated cpf and return a 409 status`() {
        //given
        customerRepository.save(customerDtoBuilder().toDomainEntity())
        val customerDto = customerDtoBuilder()
        val valueAsString = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with firstName empty and return a 400 status`() {
        //given
        val customerDto = customerDtoBuilder(firstName = "")
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find a customer by id and return a 200 status`() {
        //given
        val testCustomerSaved = customerRepository.save(customerDtoBuilder().toDomainEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${testCustomerSaved.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Cami"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Cavalcante"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("012345678901"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("camil@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("00000-000"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua da Camila"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testCustomerSaved.id))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find a customer with an invalid id and return a 400 status`() {
        //given
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${Long.MIN_VALUE}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.nothing.credit.application.system.exception.IdNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should delete a customer by id`() {
        //given
        val savedCustomer = customerRepository.save(customerDtoBuilder().toDomainEntity())

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${savedCustomer.id}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete a customer with an invalid id and return a 400 status`() {
        //given
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${Long.MIN_VALUE}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.nothing.credit.application.system.exception.IdNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should update a customer and return a 200 status`() {
        //given
        val customer = customerRepository.save(customerDtoBuilder().toDomainEntity())
        val customerUpdateDto = customerUpdateDtoBuilder()
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Tony"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Montana"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(9999.99))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("11111-111"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Malibu Street"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update a customer with an invalid id and return a 400 status`() {
        //given
        val customerUpdateDto = customerUpdateDtoBuilder()
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${Long.MIN_VALUE}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.nothing.credit.application.system.exception.IdNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun customerDtoBuilder(
        firstName: String = "Cami",
        lastName: String = "Cavalcante",
        cpf: String = "012345678901",
        email: String = "camil@email.com",
        income: BigDecimal = BigDecimal.valueOf(1013.09),
        password: String = "minhaSenhaForte%091872",
        zipCode: String = "00000-000",
        street: String = "Rua da Camila"
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

    private fun customerUpdateDtoBuilder(
        firstName: String = "Tony",
        lastName: String = "Montana",
        income: BigDecimal = BigDecimal.valueOf(9999.99),
        zipCode: String = "11111-111",
        street: String = "Malibu Street"
    ) = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )
}