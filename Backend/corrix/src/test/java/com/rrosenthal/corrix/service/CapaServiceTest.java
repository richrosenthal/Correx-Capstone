package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.dto.CapaRequest;
import com.rrosenthal.corrix.dto.CapaResponse;
import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.entity.CapaStage;
import com.rrosenthal.corrix.entity.Severity;
import com.rrosenthal.corrix.entity.SourceType;
import com.rrosenthal.corrix.entity.Status;
import com.rrosenthal.corrix.exception.InvalidStageTransitionException;
import com.rrosenthal.corrix.repository.CapaRepository;
import com.rrosenthal.corrix.repository.SeverityRepository;
import com.rrosenthal.corrix.repository.SourceTypeRepository;
import com.rrosenthal.corrix.repository.StatusRepository;
import com.rrosenthal.corrix.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapaServiceTest {

    @Mock
    private CapaRepository capaRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private SeverityRepository severityRepository;

    @Mock
    private SourceTypeRepository sourceTypeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CapaService capaService;

    @Test
    void create_shouldDefaultStageToDraftWhenMissing() {
        UUID statusId = UUID.randomUUID();
        UUID severityId = UUID.randomUUID();
        UUID sourceTypeId = UUID.randomUUID();

        CapaRequest request = buildRequest(statusId, severityId, sourceTypeId);

        Status status = new Status();
        status.setId(statusId);
        status.setName("Open");

        Severity severity = new Severity();
        severity.setId(severityId);
        severity.setName("High");

        SourceType sourceType = new SourceType();
        sourceType.setId(sourceTypeId);
        sourceType.setName("Deviation");

        when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        when(severityRepository.findById(severityId)).thenReturn(Optional.of(severity));
        when(sourceTypeRepository.findById(sourceTypeId)).thenReturn(Optional.of(sourceType));
        when(capaRepository.save(any(Capa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CapaResponse response = capaService.create(request);

        assertEquals("DRAFT", response.getStage());
    }

    @Test
    void update_shouldPreserveExistingStageWhenRequestStageMissing() {
        UUID capaId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        UUID severityId = UUID.randomUUID();
        UUID sourceTypeId = UUID.randomUUID();

        CapaRequest request = buildRequest(statusId, severityId, sourceTypeId);

        Status status = new Status();
        status.setId(statusId);
        status.setName("Open");

        Severity severity = new Severity();
        severity.setId(severityId);
        severity.setName("High");

        SourceType sourceType = new SourceType();
        sourceType.setId(sourceTypeId);
        sourceType.setName("Deviation");

        Capa existing = new Capa();
        existing.setId(capaId);
        existing.setStage(CapaStage.INVESTIGATION);

        when(capaRepository.findById(capaId)).thenReturn(Optional.of(existing));
        when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        when(severityRepository.findById(severityId)).thenReturn(Optional.of(severity));
        when(sourceTypeRepository.findById(sourceTypeId)).thenReturn(Optional.of(sourceType));
        when(capaRepository.save(any(Capa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CapaResponse response = capaService.update(capaId, request);

        assertEquals("INVESTIGATION", response.getStage());
    }

    @Test
    void transitionStage_shouldMoveToNextAllowedStage() {
        UUID capaId = UUID.randomUUID();

        Capa existing = new Capa();
        existing.setId(capaId);
        existing.setStage(CapaStage.TRIAGE);

        when(capaRepository.findById(capaId)).thenReturn(Optional.of(existing));
        when(capaRepository.save(any(Capa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CapaResponse response = capaService.transitionStage(capaId, CapaStage.INVESTIGATION);

        assertEquals("INVESTIGATION", response.getStage());
    }

    @Test
    void transitionStage_shouldRejectInvalidMove() {
        UUID capaId = UUID.randomUUID();

        Capa existing = new Capa();
        existing.setId(capaId);
        existing.setStage(CapaStage.DRAFT);

        when(capaRepository.findById(capaId)).thenReturn(Optional.of(existing));

        InvalidStageTransitionException ex = assertThrows(InvalidStageTransitionException.class,
                () -> capaService.transitionStage(capaId, CapaStage.ACTION_PLAN));

        assertEquals("Cannot transition CAPA from DRAFT to ACTION_PLAN. Allowed target stage(s): [TRIAGE]", ex.getMessage());
    }

    private CapaRequest buildRequest(UUID statusId, UUID severityId, UUID sourceTypeId) {
        CapaRequest request = new CapaRequest();
        request.setCapaNumber("CAPA-001");
        request.setTitle("Containment review");
        request.setDescription("Verify root cause and next actions");
        request.setStatusId(statusId);
        request.setSeverityId(severityId);
        request.setSourceTypeId(sourceTypeId);
        return request;
    }
}
