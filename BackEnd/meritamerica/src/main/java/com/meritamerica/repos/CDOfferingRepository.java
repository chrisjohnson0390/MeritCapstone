package com.meritamerica.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.models.CDOffering;

public interface CDOfferingRepository extends JpaRepository<CDOffering, Long> {
	
	List<CDOffering> findById(long id);

}
