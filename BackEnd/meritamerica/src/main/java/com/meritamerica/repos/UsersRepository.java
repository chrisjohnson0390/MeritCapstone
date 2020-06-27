package com.meritamerica.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.models.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
	Optional<Users>  findByUsername(String username);
}
