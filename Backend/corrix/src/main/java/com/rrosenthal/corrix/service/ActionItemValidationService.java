package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.dto.ActionItemRequest;
import com.rrosenthal.corrix.entity.Status;
import com.rrosenthal.corrix.exception.ActionItemValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionItemValidationService {

    public void validate(ActionItemRequest request, Status status) {
        List<String> errors = new ArrayList<>();

        if (request.getOwnerId() == null && request.getAssigneeId() == null) {
            errors.add("Owner is required.");
        }

        if (request.getDueDate() == null) {
            errors.add("Due date is required.");
        }

        if (status == null || status.getName() == null || status.getName().isBlank()) {
            errors.add("Status is required.");
        }

        boolean isClosed = status != null && status.getName() != null && status.getName().equalsIgnoreCase("Closed");
        if (isClosed && request.getCompletedDate() == null) {
            errors.add("Completed date is required when the action item status is Closed.");
        }

        if (isClosed && isBlank(request.getEvidenceNotes())) {
            errors.add("Evidence notes are required when the action item status is Closed.");
        }

        if (!errors.isEmpty()) {
            throw new ActionItemValidationException("Action item is missing required execution tracking fields.", errors);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
