package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.dto.ActionItemRequest;
import com.rrosenthal.corrix.dto.ActionItemResponse;
import com.rrosenthal.corrix.entity.ActionItem;
import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.entity.Status;
import com.rrosenthal.corrix.entity.User;
import com.rrosenthal.corrix.exception.ResourceNotFoundException;
import com.rrosenthal.corrix.repository.ActionItemRepository;
import com.rrosenthal.corrix.repository.CapaRepository;
import com.rrosenthal.corrix.repository.StatusRepository;
import com.rrosenthal.corrix.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActionItemService {

    private final ActionItemRepository actionItemRepository;
    private final CapaRepository capaRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final ActionItemValidationService actionItemValidationService;

    public ActionItemService(ActionItemRepository actionItemRepository,
                             CapaRepository capaRepository,
                             UserRepository userRepository,
                             StatusRepository statusRepository,
                             ActionItemValidationService actionItemValidationService) {
        this.actionItemRepository = actionItemRepository;
        this.capaRepository = capaRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.actionItemValidationService = actionItemValidationService;
    }

    public ActionItemResponse create(ActionItemRequest actionItemRequest) {
        ActionItem actionItem = new ActionItem();
        mapRequestToEntity(actionItemRequest, actionItem);
        return toResponse(actionItemRepository.save(actionItem));
    }

    public List<ActionItemResponse> getByCapaId(UUID capaId){
        return actionItemRepository.findByCapa_Id(capaId).stream().map(this::toResponse).toList();
    }

    public ActionItemResponse getById(UUID id){
        ActionItem actionItem = actionItemRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Action Item not found"));
        return toResponse(actionItem);
    }

    public ActionItemResponse update(UUID id, ActionItemRequest actionItemRequest) {
        ActionItem actionItem = actionItemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Action Item not found"));
        mapRequestToEntity(actionItemRequest, actionItem);
        return toResponse(actionItemRepository.save(actionItem));
    }
    public void delete(UUID id) {
        ActionItem actionItem = actionItemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Action Item not found"));
        actionItemRepository.delete(actionItem);
    }

    private void mapRequestToEntity(ActionItemRequest actionItemRequest, ActionItem actionItem) {
        Capa capa = capaRepository.findById(actionItemRequest.getCapaId()).orElseThrow(()-> new ResourceNotFoundException("Capa not found"));
        Status status = statusRepository.findById(actionItemRequest.getStatusId()).orElseThrow(()-> new ResourceNotFoundException("Status not found"));
        actionItemValidationService.validate(actionItemRequest, status);

        actionItem.setCapa(capa);
        actionItem.setTitle(actionItemRequest.getTitle());
        actionItem.setDescription(actionItemRequest.getDescription());
        actionItem.setDueDate(actionItemRequest.getDueDate());
        actionItem.setCompletedDate(actionItemRequest.getCompletedDate());
        actionItem.setEvidenceNotes(actionItemRequest.getEvidenceNotes());
        actionItem.setStatus(status);

        UUID ownerId = actionItemRequest.getOwnerId() != null ? actionItemRequest.getOwnerId() : actionItemRequest.getAssigneeId();
        if (ownerId != null) {
            User assignee = userRepository.findById(ownerId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
            actionItem.setAssignee(assignee);
        } else {
            actionItem.setAssignee(null);
        }
    }
    private ActionItemResponse toResponse(ActionItem actionItem) {
        ActionItemResponse response = new ActionItemResponse();
        response.setId(actionItem.getId());
        response.setCapaId(actionItem.getCapa().getId());
        response.setTitle(actionItem.getTitle());
        response.setDescription(actionItem.getDescription());
        response.setStatus(actionItem.getStatus() != null ? actionItem.getStatus().getName() : null);
        response.setOwner(actionItem.getOwner() != null ? actionItem.getOwner().getUsername() : null);
        response.setAssignee(actionItem.getAssignee() != null ? actionItem.getAssignee().getUsername() : null);
        response.setDueDate(actionItem.getDueDate());
        response.setCompletedDate(actionItem.getCompletedDate());
        response.setEvidenceNotes(actionItem.getEvidenceNotes());
        response.setCreatedAt(actionItem.getCreatedAt());
        response.setUpdatedAt(actionItem.getUpdatedAt());
        return response;
    }
}
