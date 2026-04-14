package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.entity.ActionItem;
import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.entity.CapaStage;
import com.rrosenthal.corrix.entity.EscalationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OverdueEscalationService {

    public boolean isCapaClosed(Capa capa) {
        if (capa.getStage() == CapaStage.CLOSED) {
            return true;
        }
        return capa.getStatus() != null
                && capa.getStatus().getName() != null
                && capa.getStatus().getName().equalsIgnoreCase("Closed");
    }

    public boolean isCapaOverdue(Capa capa) {
        return !isCapaClosed(capa) && isPastDue(capa.getDueDate());
    }

    public EscalationStatus getCapaEscalationStatus(Capa capa) {
        return isEscalated(capa.getDueDate(), !isCapaClosed(capa)) ? EscalationStatus.ESCALATED : EscalationStatus.NONE;
    }

    public boolean isActionItemDone(ActionItem actionItem) {
        if (actionItem.getCompletedDate() != null) {
            return true;
        }
        if (actionItem.getStatus() == null || actionItem.getStatus().getName() == null) {
            return false;
        }

        String statusName = actionItem.getStatus().getName();
        return statusName.equalsIgnoreCase("Closed")
                || statusName.equalsIgnoreCase("Done")
                || statusName.equalsIgnoreCase("Completed");
    }

    public boolean isActionItemOverdue(ActionItem actionItem) {
        return !isActionItemDone(actionItem) && isPastDue(actionItem.getDueDate());
    }

    public EscalationStatus getActionItemEscalationStatus(ActionItem actionItem) {
        return isEscalated(actionItem.getDueDate(), !isActionItemDone(actionItem)) ? EscalationStatus.ESCALATED : EscalationStatus.NONE;
    }

    private boolean isPastDue(LocalDate dueDate) {
        return dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    private boolean isEscalated(LocalDate dueDate, boolean eligible) {
        return eligible && dueDate != null && dueDate.isBefore(LocalDate.now().minusDays(7));
    }
}
