package com.meritamerica.repos;



import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.models.AccountHolder;

public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

	
	AccountHolder findById(long id);
	
	AccountHolder findByUsername(String username);
}
