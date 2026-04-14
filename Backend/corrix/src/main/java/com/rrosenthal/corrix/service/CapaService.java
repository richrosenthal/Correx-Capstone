package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.dto.AgingReportResponse;
import com.rrosenthal.corrix.dto.AgingReportRowResponse;
import com.rrosenthal.corrix.dto.CapaRequest;
import com.rrosenthal.corrix.dto.CapaResponse;
import com.rrosenthal.corrix.dto.DashboardSummaryResponse;
import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.entity.CapaStage;
import com.rrosenthal.corrix.entity.User;
import com.rrosenthal.corrix.exception.InvalidStageTransitionException;
import com.rrosenthal.corrix.exception.ResourceNotFoundException;
import com.rrosenthal.corrix.repository.CapaRepository;
import com.rrosenthal.corrix.repository.SeverityRepository;
import com.rrosenthal.corrix.repository.SourceTypeRepository;
import com.rrosenthal.corrix.repository.StatusRepository;
import com.rrosenthal.corrix.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class CapaService {

    private static final Map<CapaStage, Set<CapaStage>> ALLOWED_STAGE_TRANSITIONS = Map.of(
            CapaStage.DRAFT, EnumSet.of(CapaStage.TRIAGE),
            CapaStage.TRIAGE, EnumSet.of(CapaStage.INVESTIGATION),
            CapaStage.INVESTIGATION, EnumSet.of(CapaStage.ACTION_PLAN),
            CapaStage.ACTION_PLAN, EnumSet.of(CapaStage.IMPLEMENTATION),
            CapaStage.IMPLEMENTATION, EnumSet.of(CapaStage.EFFECTIVENESS_CHECK),
            CapaStage.EFFECTIVENESS_CHECK, EnumSet.of(CapaStage.CLOSED),
            CapaStage.CLOSED, EnumSet.noneOf(CapaStage.class)
    );

    private final CapaRepository capaRepository;
    private final StatusRepository statusRepository;
    private final SeverityRepository severityRepository;
    private final SourceTypeRepository sourceTypeRepository;
    private final UserRepository userRepository;
    private final CapaStageValidationService capaStageValidationService;

    public CapaService(CapaRepository capaRepository,
                       StatusRepository statusRepository,
                       SeverityRepository severityRepository,
                       SourceTypeRepository sourceTypeRepository,
                       UserRepository userRepository,
                       CapaStageValidationService capaStageValidationService) {
        this.capaRepository = capaRepository;
        this.statusRepository = statusRepository;
        this.severityRepository = severityRepository;
        this.sourceTypeRepository = sourceTypeRepository;
        this.userRepository = userRepository;
        this.capaStageValidationService = capaStageValidationService;
    }

    public CapaResponse create(CapaRequest capaRequest) {
        Capa capa = new Capa();
        mapRequestToEntity(capaRequest, capa);
        return toResponse(capaRepository.save(capa));
    }

    public List<CapaResponse> getAll() {
        return capaRepository.findAll().stream().map(this::toResponse).toList();
    }
    public CapaResponse getById(UUID id) {
        Capa capa = capaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CAPA not found"));
        return toResponse(capa);
    }

    public CapaResponse update(UUID id, CapaRequest capaRequest) {
        Capa capa = capaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CAPA not found"));
        CapaStage currentStage = getCurrentStage(capa);
        mapRequestToEntity(capaRequest, capa);
        validateStageChange(capa, currentStage, getCurrentStage(capa));
        return toResponse(capaRepository.save(capa));
    }

    public CapaResponse transitionStage(UUID capaId, CapaStage targetStage) {
        Capa capa = capaRepository.findById(capaId).orElseThrow(() -> new ResourceNotFoundException("CAPA not found"));
        CapaStage currentStage = getCurrentStage(capa);
        if (currentStage == targetStage) {
            throw new InvalidStageTransitionException("CAPA is already in stage " + targetStage.name());
        }
        validateStageChange(capa, currentStage, targetStage);
        capa.setStage(targetStage);
        return toResponse(capaRepository.save(capa));
    }

    public void delete(UUID id) {
        Capa capa = capaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CAPA not found"));
        capaRepository.delete(capa);
    }

    public List<CapaResponse> search(String q){
        Map<UUID, Capa> deduped = new LinkedHashMap<>();

        capaRepository.findByTitleContainingIgnoreCase(q).forEach(capa -> deduped.put(capa.getId(), capa));

        capaRepository.findByCapaNumberContainingIgnoreCase(q).forEach(capa -> deduped.put(capa.getId(), capa));

        return deduped.values().stream().map(this::toResponse).toList();
    }

    public DashboardSummaryResponse getDashboardSummary() {
        List<Capa> capas = capaRepository.findAll();
        LocalDate today = LocalDate.now();

        long openCount = capas.stream().filter(c -> !isClosed(c)).count();

        long overdueCount = capas.stream().filter(c -> !isClosed(c)).filter(c -> c.getDueDate() != null && c.getDueDate().isBefore(today)).count();

        long dueSoonCount = capas.stream()
                .filter(c -> !isClosed(c))
                .filter(c -> c.getDueDate() != null
                && !c.getDueDate().isBefore(today)
                && !c.getDueDate().isAfter(today.plusDays(7))).count();


        long closedCount = capas.stream().filter(this::isClosed).count();

        DashboardSummaryResponse dashboardSummaryResponse = new DashboardSummaryResponse();
        dashboardSummaryResponse.setOpenCount(openCount);
        dashboardSummaryResponse.setOverdueCount(overdueCount);
        dashboardSummaryResponse.setClosedCount(closedCount);
        dashboardSummaryResponse.setDueSoonCount(dueSoonCount);
        return dashboardSummaryResponse;
    }

    public AgingReportResponse getOpenCapaAgingReport(){
        LocalDate today = LocalDate.now();

        List<AgingReportRowResponse> rows = capaRepository.findAll().stream()
                .filter(c -> !isClosed(c))
                .map(c ->{
            AgingReportRowResponse row = new AgingReportRowResponse();
            row.setCapaNumber(c.getCapaNumber());
            row.setTitle(c.getTitle());
            row.setStatus(c.getStatus() != null ? c.getStatus().getName() : null);
            row.setSeverity(c.getSeverity() != null ? c.getSeverity().getName() : null);
            row.setOwner(c.getOwner() != null ? c.getOwner().getUsername() : null);
            row.setDueDate(c.getDueDate());

            long daysOpen = 0;
            if (c.getCreatedAt() != null){
                daysOpen = ChronoUnit.DAYS.between(c.getCreatedAt().toLocalDate(), today);
            }
            row.setDaysOpen(daysOpen);
            return row;
        }).sorted((a, b) -> Long.compare(b.getDaysOpen(), a.getDaysOpen())).toList();

        AgingReportResponse rowResponse = new AgingReportResponse();
        rowResponse.setTitle("Open CAPA Aging Report");
        rowResponse.setGeneratedAt(OffsetDateTime.now());
        rowResponse.setRows(rows);
        return rowResponse;
    }

    private boolean isClosed(Capa capa){
        return capa.getStatus() != null && capa.getStatus().getName() != null && capa.getStatus().getName().equalsIgnoreCase("Closed");
    }

    private CapaStage getCurrentStage(Capa capa) {
        return capa.getStage() != null ? capa.getStage() : CapaStage.DRAFT;
    }

    private String buildInvalidTransitionMessage(CapaStage currentStage, CapaStage targetStage, Set<CapaStage> allowedTargets) {
        if (allowedTargets.isEmpty()) {
            return "Cannot transition CAPA from " + currentStage.name() + " to " + targetStage.name() + ". " + currentStage.name() + " is a terminal stage.";
        }
        return "Cannot transition CAPA from " + currentStage.name() + " to " + targetStage.name() + ". Allowed target stage(s): "
                + allowedTargets.stream().map(Enum::name).toList();
    }

    private void validateStageChange(Capa capa, CapaStage currentStage, CapaStage targetStage) {
        if (currentStage == targetStage) {
            return;
        }

        Set<CapaStage> allowedTargets = ALLOWED_STAGE_TRANSITIONS.getOrDefault(currentStage, EnumSet.noneOf(CapaStage.class));
        if (!allowedTargets.contains(targetStage)) {
            throw new InvalidStageTransitionException(buildInvalidTransitionMessage(currentStage, targetStage, allowedTargets));
        }

        capaStageValidationService.validateForStage(capa, targetStage);
    }

    private void mapRequestToEntity(CapaRequest capaRequest, Capa capa) {
        capa.setCapaNumber(capaRequest.getCapaNumber());
        capa.setTitle(capaRequest.getTitle());
        capa.setDescription(capaRequest.getDescription());
        capa.setDueDate(capaRequest.getDueDate());
        if (capaRequest.getStage() != null) {
            capa.setStage(capaRequest.getStage());
        } else if (capa.getStage() == null) {
            capa.setStage(CapaStage.DRAFT);
        }

        capa.setStatus(statusRepository.findById(capaRequest.getStatusId()).orElseThrow(() -> new ResourceNotFoundException("Status not found")));

        capa.setSeverity(severityRepository.findById(capaRequest.getSeverityId()).orElseThrow(() -> new ResourceNotFoundException("Severity not found")));

        capa.setSourceType(sourceTypeRepository.findById(capaRequest.getSourceTypeId()).orElseThrow(() -> new ResourceNotFoundException("Source type not found")));

        if(capaRequest.getOwnerId() != null) {
            User owner = userRepository.findById(capaRequest.getOwnerId()).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
            capa.setOwner(owner);
        } else {
            capa.setOwner(null);
        }
    }
    private CapaResponse toResponse(Capa capa) {
        CapaResponse response = new CapaResponse();
        response.setId(capa.getId());
        response.setCapaNumber(capa.getCapaNumber());
        response.setTitle(capa.getTitle());
        response.setDescription(capa.getDescription());
        response.setStage(getCurrentStage(capa).name());
        response.setStatus(capa.getStatus() != null ? capa.getStatus().getName() : null);
        response.setSeverity(capa.getSeverity() != null ? capa.getSeverity().getName() : null);
        response.setSourceType(capa.getSourceType() != null ? capa.getSourceType().getName(): null);
        response.setOwner(capa.getOwner() != null ? capa.getOwner().getUsername() : null);
        response.setDueDate(capa.getDueDate());
        response.setCreatedAt(capa.getCreatedAt());
        response.setUpdatedAt(capa.getUpdatedAt());
        return response;
    }
}
