package mp.org.blip.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mp.org.blip.validator.annotationvalidator.CronValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CronValidator.class})
public @interface ValidCron {
    String message() default "Invalid cron expression";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
