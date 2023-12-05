package org.nothing.credit.application.system.repository

import org.nothing.credit.application.system.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository: JpaRepository<Customer, Long> {
}