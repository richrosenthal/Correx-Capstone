package com.rrosenthal.corrix.controller;

import com.rrosenthal.corrix.dto.CapaRequest;
import com.rrosenthal.corrix.dto.CapaResponse;
import com.rrosenthal.corrix.dto.CapaStageTransitionRequest;
import com.rrosenthal.corrix.service.CapaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/capas")
public class CapaController {

    private final CapaService capaService;

    public CapaController(CapaService capaService) {
        this.capaService = capaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CapaResponse create(@Valid @RequestBody CapaRequest request) {
        return capaService.create(request);
    }

    @GetMapping
    public List<CapaResponse> getAll(@RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()){
            return capaService.search(q);
        }
        return capaService.getAll();
    }

    @GetMapping("/{id}")
    public CapaResponse getById(@PathVariable UUID id) {
        return capaService.getById(id);
    }

    @PutMapping("/{id}")
    public CapaResponse update(@PathVariable UUID id, @RequestBody CapaRequest capaRequest) {
        return capaService.update(id, capaRequest);
    }

    @PatchMapping("/{id}/stage")
    public CapaResponse transitionStage(@PathVariable UUID id, @Valid @RequestBody CapaStageTransitionRequest request) {
        return capaService.transitionStage(id, request.getTargetStage());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        capaService.delete(id);
    }
}
