package com.rrosenthal.corrix.repository;

import com.rrosenthal.corrix.entity.Capa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CapaRepository extends JpaRepository<Capa, UUID>  {

    List<Capa> findByTitleContainingIgnoreCase(String title);
    List<Capa> findByCapaNumberContainingIgnoreCase(String capaNumber);
}
