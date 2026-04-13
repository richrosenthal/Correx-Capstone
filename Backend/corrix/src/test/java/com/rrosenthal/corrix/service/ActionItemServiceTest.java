package com.rrosenthal.corrix.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActionItemServiceTest {

    @Mock
    private ActionItemRepository actionItemRepository;

    @Mock
    private CapaRepository capaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private ActionItemService actionItemService;

    // tests go here
    @Test
    void getByCapaId_shouldReturnMappedActionItems() {
        UUID capaId = UUID.randomUUID();

        Capa capa = new Capa();
        capa.setId(capaId);

        Status status = new Status();
        status.setName("Open");

        User assignee = new User();
        assignee.setUsername("admin");

        ActionItem actionItem = new ActionItem();
        actionItem.setCapa(capa);
        actionItem.setTitle("Review welding SOP");
        actionItem.setStatus(status);
        actionItem.setAssignee(assignee);

        when(actionItemRepository.findByCapa_Id(capaId))
                .thenReturn(List.of(actionItem));

        List<ActionItemResponse> results = actionItemService.getByCapaId(capaId);

        assertEquals(1, results.size());
        assertEquals("Review welding SOP", results.get(0).getTitle());
        assertEquals("Open", results.get(0).getStatus());
        assertEquals("admin", results.get(0).getAssignee());
    }
    @Test
    void getById_shouldThrowExceptionWhenNotFound() {
        UUID actionItemId = UUID.randomUUID();

        when(actionItemRepository.findById(actionItemId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            actionItemService.getById(actionItemId);
        });
    }

}
