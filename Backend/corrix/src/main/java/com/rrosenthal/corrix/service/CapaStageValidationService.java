package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.entity.CapaStage;
import com.rrosenthal.corrix.exception.StageValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CapaStageValidationService {

    public void validateForStage(Capa capa, CapaStage targetStage) {
        List<String> errors = new ArrayList<>();

        switch (targetStage) {
            case TRIAGE -> validateTriage(capa, errors, targetStage);
            case INVESTIGATION -> {
                validateTriage(capa, errors, targetStage);
                requireOwner(capa, errors, targetStage);
            }
            case ACTION_PLAN, IMPLEMENTATION, EFFECTIVENESS_CHECK -> {
                validateTriage(capa, errors, targetStage);
                requireOwner(capa, errors, targetStage);
                requireDueDate(capa, errors, targetStage);
            }
            case CLOSED -> {
                validateTriage(capa, errors, targetStage);
                requireOwner(capa, errors, targetStage);
                requireDueDate(capa, errors, targetStage);
                requireClosedStatus(capa, errors);
            }
            case DRAFT -> {
                return;
            }
        }

        if (!errors.isEmpty()) {
            throw new StageValidationException(
                    "CAPA cannot move to " + formatStageName(targetStage) + " until the required fields are completed.",
                    errors
            );
        }
    }

    private void validateTriage(Capa capa, List<String> errors, CapaStage targetStage) {
        requireCapaNumber(capa, errors, targetStage);
        requireTitle(capa, errors, targetStage);
        requireDescription(capa, errors, targetStage);
        requireSeverity(capa, errors, targetStage);
        requireSourceType(capa, errors, targetStage);
        requireStatus(capa, errors, targetStage);
    }

    private void requireCapaNumber(Capa capa, List<String> errors, CapaStage targetStage) {
        if (isBlank(capa.getCapaNumber())) {
            errors.add("CAPA number is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireTitle(Capa capa, List<String> errors, CapaStage targetStage) {
        if (isBlank(capa.getTitle())) {
            errors.add("Title is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireDescription(Capa capa, List<String> errors, CapaStage targetStage) {
        if (isBlank(capa.getDescription())) {
            errors.add("Description is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireSeverity(Capa capa, List<String> errors, CapaStage targetStage) {
        if (capa.getSeverity() == null) {
            errors.add("Severity is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireSourceType(Capa capa, List<String> errors, CapaStage targetStage) {
        if (capa.getSourceType() == null) {
            errors.add("Source type is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireStatus(Capa capa, List<String> errors, CapaStage targetStage) {
        if (capa.getStatus() == null || isBlank(capa.getStatus().getName())) {
            errors.add("Status is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireOwner(Capa capa, List<String> errors, CapaStage targetStage) {
        if (capa.getOwner() == null) {
            errors.add("Owner is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireDueDate(Capa capa, List<String> errors, CapaStage targetStage) {
        if (capa.getDueDate() == null) {
            errors.add("Due date is required before moving to " + formatStageName(targetStage) + ".");
        }
    }

    private void requireClosedStatus(Capa capa, List<String> errors) {
        if (capa.getStatus() == null || capa.getStatus().getName() == null || !capa.getStatus().getName().equalsIgnoreCase("Closed")) {
            errors.add("Status must be set to Closed before moving to Closed.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String formatStageName(CapaStage stage) {
        return stage.name().replace('_', ' ');
    }
}
