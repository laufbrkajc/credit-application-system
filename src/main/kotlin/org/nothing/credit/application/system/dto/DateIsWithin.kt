package org.nothing.credit.application.system.dto

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.constraints.Min
import org.nothing.credit.application.system.dto.DateIsWithin.DateIsBeforeValidator
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
annotation class DateIsWithin(
    val message: String = "Date is out of range.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
    @Min(value = 0) val years: Int = 0,
    @Min(value = 0) val months: Int = 0,
    @Min(value = 0) val days: Int = 0
) {
    class DateIsBeforeValidator : ConstraintValidator<DateIsWithin, LocalDate> {
        private var years: Long = 0
        private var months: Long = 0
        private var days: Long = 0

        override fun initialize(annotation: DateIsWithin) {
            this.years = annotation.years.toLong()
            this.months = annotation.months.toLong()
            this.days = annotation.days.toLong()
        }

        override fun isValid(dateToValidate: LocalDate, context: ConstraintValidatorContext): Boolean {
            if ("" == dateToValidate.toString()) return true

            var endDate = LocalDate.now()
                .plusYears(years)
                .plusMonths(months)
                .plusDays(days)

            if (dateToValidate.isBefore(endDate) || dateToValidate.isEqual(endDate)) {
                return true
            }

            return false
        }
    }
}