package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountStateRepository extends JpaRepository<AccountState, UUID> {

    Optional<AccountState> findByNameAndStatus(String name, Status status);
}
