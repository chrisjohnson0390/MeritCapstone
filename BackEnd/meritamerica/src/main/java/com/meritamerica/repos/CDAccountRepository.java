package com.meritamerica.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.models.CDAccount;

public interface CDAccountRepository extends JpaRepository<CDAccount, Long> {

	
	List<CDAccount> findByAccountholder(long id);
}
