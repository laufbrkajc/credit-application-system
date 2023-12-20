package org.nothing.credit.application.system.dto

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.nothing.credit.application.system.dto.DateIsBefore.DateIsBeforeValidator
import java.time.LocalDate
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateIsBeforeValidator::class])
@MustBeDocumented
annotation class DateIsBefore(
    val message: String = "Date is out of range.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
    val value: String
) {
    class DateIsBeforeValidator : ConstraintValidator<DateIsBefore, LocalDate> {
        var value: String? = null

        override fun initialize(annotation: DateIsBefore) {
            this.value = annotation.value
        }

        override fun isValid(`object`: LocalDate, context: ConstraintValidatorContext): Boolean {
            if ("" == `object`.toString()) return true

            val endDate = LocalDate.parse(value)
            println("endDate: $endDate")
            println("value: $value")

            if (`object`.isBefore(endDate) || `object`.isEqual(endDate)) {
                return true
            }

            return false
        }
    }
}