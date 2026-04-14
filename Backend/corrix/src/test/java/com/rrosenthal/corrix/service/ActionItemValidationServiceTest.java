package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.dto.ActionItemRequest;
import com.rrosenthal.corrix.entity.Status;
import com.rrosenthal.corrix.exception.ActionItemValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActionItemValidationServiceTest {

    private final ActionItemValidationService validationService = new ActionItemValidationService();

    @Test
    void validate_shouldRequireExecutionTrackingFields() {
        ActionItemRequest request = new ActionItemRequest();
        request.setCapaId(UUID.randomUUID());
        request.setTitle("Verify operator retraining");

        Status status = new Status();
        status.setName("Open");

        ActionItemValidationException ex = assertThrows(ActionItemValidationException.class,
                () -> validationService.validate(request, status));

        assertEquals("Action item is missing required execution tracking fields.", ex.getMessage());
        assertEquals(List.of(
                "Owner is required.",
                "Due date is required."
        ), ex.getErrors());
    }

    @Test
    void validate_shouldRequireCompletionEvidenceForClosedStatus() {
        ActionItemRequest request = new ActionItemRequest();
        request.setCapaId(UUID.randomUUID());
        request.setTitle("Confirm process change");
        request.setOwnerId(UUID.randomUUID());
        request.setDueDate(LocalDate.now().plusDays(3));

        Status status = new Status();
        status.setName("Closed");

        ActionItemValidationException ex = assertThrows(ActionItemValidationException.class,
                () -> validationService.validate(request, status));

        assertEquals(List.of(
                "Completed date is required when the action item status is Closed.",
                "Evidence notes are required when the action item status is Closed."
        ), ex.getErrors());
    }

    @Test
    void validate_shouldAllowValidClosedActionItem() {
        ActionItemRequest request = new ActionItemRequest();
        request.setCapaId(UUID.randomUUID());
        request.setTitle("Confirm process change");
        request.setOwnerId(UUID.randomUUID());
        request.setDueDate(LocalDate.now().plusDays(3));
        request.setCompletedDate(LocalDate.now());
        request.setEvidenceNotes("Training record reviewed and updated SOP attached.");

        Status status = new Status();
        status.setName("Closed");

        assertDoesNotThrow(() -> validationService.validate(request, status));
    }
}
