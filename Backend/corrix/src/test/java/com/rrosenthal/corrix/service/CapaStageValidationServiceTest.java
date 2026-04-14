package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.entity.CapaStage;
import com.rrosenthal.corrix.entity.Severity;
import com.rrosenthal.corrix.entity.SourceType;
import com.rrosenthal.corrix.entity.Status;
import com.rrosenthal.corrix.entity.User;
import com.rrosenthal.corrix.exception.StageValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapaStageValidationServiceTest {

    private final CapaStageValidationService validationService = new CapaStageValidationService();

    @Test
    void validateForStage_shouldReturnReadableErrorsForActionPlan() {
        Capa capa = new Capa();
        capa.setCapaNumber("CAPA-100");
        capa.setTitle("Supplier complaint");

        Severity severity = new Severity();
        severity.setName("High");
        capa.setSeverity(severity);

        StageValidationException ex = assertThrows(StageValidationException.class,
                () -> validationService.validateForStage(capa, CapaStage.ACTION_PLAN));

        assertEquals("CAPA cannot move to ACTION PLAN until the required fields are completed.", ex.getMessage());
        assertEquals(List.of(
                "Description is required before moving to ACTION PLAN.",
                "Source type is required before moving to ACTION PLAN.",
                "Status is required before moving to ACTION PLAN.",
                "Owner is required before moving to ACTION PLAN.",
                "Due date is required before moving to ACTION PLAN."
        ), ex.getErrors());
    }

    @Test
    void validateForStage_shouldAllowClosedWhenAllRequirementsAreMet() {
        Capa capa = new Capa();
        capa.setCapaNumber("CAPA-101");
        capa.setTitle("Calibration drift");
        capa.setDescription("Investigation completed and corrective actions verified.");
        capa.setDueDate(LocalDate.now().plusDays(7));

        Severity severity = new Severity();
        severity.setName("Medium");
        capa.setSeverity(severity);

        SourceType sourceType = new SourceType();
        sourceType.setName("Deviation");
        capa.setSourceType(sourceType);

        Status status = new Status();
        status.setName("Closed");
        capa.setStatus(status);

        User owner = new User();
        owner.setUsername("admin");
        capa.setOwner(owner);

        assertDoesNotThrow(() -> validationService.validateForStage(capa, CapaStage.CLOSED));
    }
}
