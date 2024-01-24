package org.nothing.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.nothing.credit.application.system.dto.CustomerView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import org.springframework.web.servlet.function.RequestPredicates.contentType

import org.nothing.credit.application.system.repository.CustomerRepository
import org.nothing.credit.application.system.util.SampleObjectsBuilder

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
    private val sampleObjectsBuilder = SampleObjectsBuilder()

    companion object {
        const val URL = "/api/customers"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    fun `should save a customer and return a 201 status`() {
        //given
        val customerDto = sampleObjectsBuilder.customerDtoBuilder()
        val expectedContentResponse = objectMapper.writeValueAsString(
            CustomerView(customerDto.toDomainEntity())
        ).run { this.replace("null", "1") }
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        //when
        //then
        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = valueAsString
        }.andExpectAll {
            status { isCreated() }
            content { string(expectedContentResponse) }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should not save a customer with duplicated cpf and return a 409 status`() {
        //given
        customerRepository.save(sampleObjectsBuilder.customerDtoBuilder().toDomainEntity())
        val customerDto = sampleObjectsBuilder.customerDtoBuilder()
        val valueAsString = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = valueAsString
        }.andExpectAll {
            status { isConflict() }
            jsonPath("$.title") { value("Conflict! Consult the documentation") }
            jsonPath("$.timestamp") { exists() }
            jsonPath("$.status") { value(409) }
            jsonPath("$.exception") { value("class org.springframework.dao.DataIntegrityViolationException") }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should not save a customer with firstName empty and return a 400 status`() {
        //given
        val customerDto = sampleObjectsBuilder.customerDtoBuilder(firstName = "")
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        //when
        //then
        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = valueAsString
        }.andExpectAll {
            status { isBadRequest() }
            jsonPath("$.title") { value("Bad Request! Consult the documentation") }
            jsonPath("$.timestamp") { exists() }
            jsonPath("$.status") { value(400) }
            jsonPath("$.exception") { value("class org.springframework.web.bind.MethodArgumentNotValidException") }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should find a customer by id and return a 200 status`() {
        //given
        val testCustomerSaved = customerRepository.save(sampleObjectsBuilder.customerDtoBuilder().toDomainEntity())
        val expectedContentResponse = objectMapper.writeValueAsString(
            CustomerView(testCustomerSaved)
        )
            .run { this.replace("null", "1") }
        //when
        //then
        mockMvc.get("$URL/${testCustomerSaved.id}") {
            contentType(MediaType.APPLICATION_JSON)
            accept(MediaType.APPLICATION_JSON)
        }.andExpectAll {
            status() { isOk() }
            content() { string(expectedContentResponse) }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should not find a customer with an invalid id and return a 400 status`() {
        //given
        //when
        //then
        mockMvc.get("$URL/${Long.MIN_VALUE}") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpectAll {
            status { isBadRequest() }
            jsonPath("$.title") { value("Bad Request! Consult the documentation") }
            jsonPath("$.timestamp") { exists() }
            jsonPath("$.status") { value(400) }
            jsonPath("$.exception") { value("class org.nothing.credit.application.system.exception.IdNotFoundException") }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should delete a customer by id`() {
        //given
        val savedCustomer = customerRepository.save(sampleObjectsBuilder.customerDtoBuilder().toDomainEntity())

        //when
        //then
        mockMvc.delete("$URL/${savedCustomer.id}") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should not delete a customer with an invalid id and return a 400 status`() {
        //given
        //when
        //then
        mockMvc.delete("$URL/${Long.MIN_VALUE}") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpectAll {
            status { isBadRequest() }
            jsonPath("$.title") { value("Bad Request! Consult the documentation") }
            jsonPath("$.timestamp") { exists() }
            jsonPath("$.status") { value(400) }
            jsonPath("$.exception") { value("class org.nothing.credit.application.system.exception.IdNotFoundException") }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should update a customer and return a 200 status`() {
        //given
        val customer = customerRepository.save(sampleObjectsBuilder.customerBuilder())
        val customerUpdateDto = sampleObjectsBuilder.customerUpdateDtoBuilder()
        val expectedContentResponse = objectMapper.writeValueAsString(
            CustomerView(customer)
            ).run {
                this.replace("null", "1")
            }
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        //when
        //then
        mockMvc.patch("$URL?customerId=${customer.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = valueAsString
        }.andExpectAll {
            status { isOk() }
            content { string(expectedContentResponse) }
        }.andDo {
            print()
        }
    }

    @Test
    fun `should not update a customer with an invalid id and return a 400 status`() {
        //given
        val customerUpdateDto = sampleObjectsBuilder.customerUpdateDtoBuilder()
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        //when
        //then
        mockMvc.patch("$URL?customerId=${Long.MIN_VALUE}") {
            contentType = MediaType.APPLICATION_JSON
            content = valueAsString
        }.andExpectAll {
            status { isBadRequest() }
            jsonPath("$.title") { value("Bad Request! Consult the documentation") }
            jsonPath("$.timestamp") { exists() }
            jsonPath("$.status") { value(400) }
            jsonPath("$.exception") { value("class org.nothing.credit.application.system.exception.IdNotFoundException") }
            jsonPath("$.details[*]") { isNotEmpty() }
        }.andDo {
            print()
        }
    }
}