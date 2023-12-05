package org.nothing.credit.application.system.repository

import org.nothing.credit.application.system.domain.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository: JpaRepository<Credit, Long> {
}