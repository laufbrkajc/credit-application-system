package org.nothing.credit.application.system.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    @Column(nullable = false) var zipCode: String = "",
    @Column(nullable = false) var street: String = ""
)
