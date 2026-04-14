package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.entity.ActionItem;
import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.entity.CapaStage;
import com.rrosenthal.corrix.entity.EscalationStatus;
import com.rrosenthal.corrix.entity.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OverdueEscalationServiceTest {

    private final OverdueEscalationService overdueEscalationService = new OverdueEscalationService();

    @Test
    void isCapaOverdue_shouldReturnTrueForPastDueOpenCapa() {
        Capa capa = new Capa();
        capa.setDueDate(LocalDate.now().minusDays(1));

        Status status = new Status();
        status.setName("Open");
        capa.setStatus(status);

        assertTrue(overdueEscalationService.isCapaOverdue(capa));
        assertEquals(EscalationStatus.NONE, overdueEscalationService.getCapaEscalationStatus(capa));
    }

    @Test
    void getCapaEscalationStatus_shouldEscalateAfterSevenDays() {
        Capa capa = new Capa();
        capa.setDueDate(LocalDate.now().minusDays(8));
        capa.setStage(CapaStage.INVESTIGATION);

        Status status = new Status();
        status.setName("Open");
        capa.setStatus(status);

        assertEquals(EscalationStatus.ESCALATED, overdueEscalationService.getCapaEscalationStatus(capa));
    }

    @Test
    void isActionItemOverdue_shouldIgnoreDoneItems() {
        ActionItem actionItem = new ActionItem();
        actionItem.setDueDate(LocalDate.now().minusDays(3));
        actionItem.setCompletedDate(LocalDate.now().minusDays(1));

        Status status = new Status();
        status.setName("Closed");
        actionItem.setStatus(status);

        assertFalse(overdueEscalationService.isActionItemOverdue(actionItem));
        assertEquals(EscalationStatus.NONE, overdueEscalationService.getActionItemEscalationStatus(actionItem));
    }

    @Test
    void getActionItemEscalationStatus_shouldEscalatePastDueOpenItems() {
        ActionItem actionItem = new ActionItem();
        actionItem.setDueDate(LocalDate.now().minusDays(9));

        Status status = new Status();
        status.setName("In Progress");
        actionItem.setStatus(status);

        assertTrue(overdueEscalationService.isActionItemOverdue(actionItem));
        assertEquals(EscalationStatus.ESCALATED, overdueEscalationService.getActionItemEscalationStatus(actionItem));
    }
}
