package com.meritamerica.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.models.AccountHolderContactDetails;

public interface AccountHolderContactDetailsRepository extends JpaRepository<AccountHolderContactDetails, Long> {

	//AccountHolderContactDetails findById(long id);
}
