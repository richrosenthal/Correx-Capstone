package com.rrosenthal.corrix.controller;

import com.rrosenthal.corrix.dto.ActionItemRequest;
import com.rrosenthal.corrix.dto.ActionItemResponse;
import com.rrosenthal.corrix.entity.ActionItem;
import com.rrosenthal.corrix.service.ActionItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/action-items")
public class ActionItemController {
    private final ActionItemService actionItemService;

    public ActionItemController(ActionItemService actionItemService) {
        this.actionItemService = actionItemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActionItemResponse create(@Valid @RequestBody ActionItemRequest actionItemRequest){
        return actionItemService.create(actionItemRequest);
    }

    @GetMapping("/capa/{capaId}")
    public List<ActionItemResponse> getByCapaId(@PathVariable UUID capaId){
        return actionItemService.getByCapaId(capaId);
    }
    @GetMapping("/{id}")
    public ActionItemResponse getById(@PathVariable UUID id){
        return actionItemService.getById(id);
    }
    @PutMapping("/{id}")
    public ActionItemResponse update(@PathVariable UUID id, @Valid @RequestBody ActionItemRequest actionItemRequest){
        return actionItemService.update(id, actionItemRequest);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        actionItemService.delete(id);
    }

}
