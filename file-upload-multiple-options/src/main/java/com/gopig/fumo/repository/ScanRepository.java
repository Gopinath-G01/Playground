package com.gopig.fumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gopig.fumo.model.entities.ScanDetailsEntity;

@Repository
public interface ScanRepository extends JpaRepository<ScanDetailsEntity, String> {

}
