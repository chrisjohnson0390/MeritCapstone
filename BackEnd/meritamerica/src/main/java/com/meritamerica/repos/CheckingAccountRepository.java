package com.meritamerica.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.models.CheckingAccount;

public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {
	
	List<CheckingAccount> findByAccountholder(long id);

}
