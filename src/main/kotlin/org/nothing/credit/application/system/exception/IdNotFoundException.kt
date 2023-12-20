package org.nothing.credit.application.system.exception

data class IdNotFoundException(override val message: String?): RuntimeException(message)
