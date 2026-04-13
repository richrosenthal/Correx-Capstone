package com.rrosenthal.corrix.controller;
import com.rrosenthal.corrix.entity.User;
import com.rrosenthal.corrix.entity.Status;
import com.rrosenthal.corrix.entity.Severity;
import com.rrosenthal.corrix.entity.SourceType;
import com.rrosenthal.corrix.repository.UserRepository;
import com.rrosenthal.corrix.repository.SeverityRepository;
import com.rrosenthal.corrix.repository.StatusRepository;
import com.rrosenthal.corrix.repository.SourceTypeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LookupController {

    private final StatusRepository statusRepository;
    private final SeverityRepository severityRepository;
    private final SourceTypeRepository sourceTypeRepository;
    private final UserRepository userRepository;

    public LookupController(StatusRepository statusRepository,
                            SeverityRepository severityRepository,
                            SourceTypeRepository sourceTypeRepository,
                            UserRepository userRepository) {
        this.statusRepository = statusRepository;
        this.severityRepository = severityRepository;
        this.sourceTypeRepository = sourceTypeRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/api/statuses")
    public List<Status> getStatuses() {
        return statusRepository.findAll();
    }
    @GetMapping("/api/severities")
    public List<Severity> getSeverities() {
        return severityRepository.findAll();
    }
    @GetMapping("/api/source-types")
    public List<SourceType> getSourceTypes() {
        return sourceTypeRepository.findAll();
    }
    @GetMapping("/api/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
