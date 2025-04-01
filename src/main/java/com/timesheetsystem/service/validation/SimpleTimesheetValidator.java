package com.timesheetsystem.service.validation;

import com.timesheetsystem.service.dto.TimesheetDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Validador simplificado de timesheet, sem dependências externas.
 */
@Component
public class SimpleTimesheetValidator {

    public static class ValidationResult {

        private final boolean valid;
        private final List<String> errors;

        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }
    }

    public ValidationResult validateTimesheet(TimesheetDTO timesheetDTO) {
        List<String> errors = new ArrayList<>();

        // Validações básicas
        if (timesheetDTO.getDate() == null) {
            errors.add("A data é obrigatória");
        } else if (timesheetDTO.getDate().isAfter(LocalDate.now())) {
            errors.add("A data do registro não pode ser no futuro");
        }

        if (timesheetDTO.getTotalHours() < 0) {
            errors.add("O total de horas não pode ser negativo");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }
}
