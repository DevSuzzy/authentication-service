package io.mosip.authentication.service.validator;


import io.mosip.authentication.service.dto.request.AuditLogRequestDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AuditLogRequestValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return AuditLogRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, @NotNull Errors errors) {
        AuditLogRequestDTO request = (AuditLogRequestDTO) target;
        if (request.getEventType().trim().isEmpty()) {
            errors.rejectValue("eventType", "IDA-MLC-008",
                    new Object[] {"eventType"}, "Missing input parameter - eventType");
        }
        if (request.getUserId().trim().isEmpty()) {
            errors.rejectValue("userId", "IDA-MLC-008",
                    new Object[] {"userId"}, "Missing input parameter - userId");
        }
    }
}