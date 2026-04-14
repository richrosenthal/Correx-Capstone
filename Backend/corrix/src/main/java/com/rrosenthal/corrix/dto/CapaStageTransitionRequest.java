package com.rrosenthal.corrix.dto;

import com.rrosenthal.corrix.entity.CapaStage;
import jakarta.validation.constraints.NotNull;

public class CapaStageTransitionRequest {

    @NotNull
    private CapaStage targetStage;

    public CapaStage getTargetStage() {
        return targetStage;
    }

    public void setTargetStage(CapaStage targetStage) {
        this.targetStage = targetStage;
    }
}
