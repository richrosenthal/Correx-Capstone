package com.rrosenthal.corrix.config;

import com.rrosenthal.corrix.entity.User;
import com.rrosenthal.corrix.entity.Status;
import com.rrosenthal.corrix.entity.Severity;
import com.rrosenthal.corrix.entity.SourceType;
import com.rrosenthal.corrix.repository.UserRepository;
import com.rrosenthal.corrix.repository.SeverityRepository;
import com.rrosenthal.corrix.repository.StatusRepository;
import com.rrosenthal.corrix.repository.SourceTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(StatusRepository statusRepository,
                                      SeverityRepository severityRepository,
                                      SourceTypeRepository sourceTypeRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            seedStatus(statusRepository, "Open");
            seedStatus(statusRepository, "In Progress");
            seedStatus(statusRepository, "Closed");
            seedStatus(statusRepository, "Overdue");

            seedSeverity(severityRepository, "Low");
            seedSeverity(severityRepository, "Medium");
            seedSeverity(severityRepository, "High");
            seedSeverity(severityRepository, "Critical");

            seedSourceType(sourceTypeRepository, "Internal Audit");
            seedSourceType(sourceTypeRepository, "Customer Complaint");
            seedSourceType(sourceTypeRepository, "Deviation");
            seedSourceType(sourceTypeRepository, "Nonconformance");

            seedUser(userRepository, passwordEncoder, "admin", "Password123!", "ADMIN");
            seedUser(userRepository, passwordEncoder, "helly.r", "Lumon123!", "USER");
            seedUser(userRepository, passwordEncoder, "mark.s", "Lumon123!", "USER");

            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPasswordHash(passwordEncoder.encode("Password123!"));
                user.setRole("ADMIN");
                user.setEnabled(true);
                userRepository.save(user);
            }
        };
    }
    private void seedStatus(StatusRepository repository, String name){
        if (repository.findByNameIgnoreCase(name).isEmpty()){
            Status status = new Status();
            status.setName(name);
            repository.save(status);
        }
    }
    private void seedSeverity(SeverityRepository repository, String name){
        if (repository.findByNameIgnoreCase(name).isEmpty()){
            Severity severity = new Severity();
            severity.setName(name);
            repository.save(severity);
        }
    }
    private void seedSourceType(SourceTypeRepository repository, String name){
        if (repository.findByNameIgnoreCase(name).isEmpty()){
            SourceType sourceType = new SourceType();
            sourceType.setName(name);
            repository.save(sourceType);
        }
    }
    private void seedUser(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          String username,
                          String rawPassword,
                          String role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}
