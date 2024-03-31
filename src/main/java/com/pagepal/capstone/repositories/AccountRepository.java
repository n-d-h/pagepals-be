package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

    List<Account> findByAccountStateAndRole(AccountState accountState, Role role);

    @Query("""
            SELECT a
            FROM Account a
            WHERE a.role.name IN :roles
            AND a.accountState.name IN :accountStates
            """)
    List<Account> findByRoleStringAndAccountStateString(List<String> roles, List<String> accountStates);

    List<Account> findByRole(Role role);

    @Query("""
            SELECT a
            FROM Account a
            WHERE a.role.name IN :roles
            """)
    List<Account> findByRoles(List<String> roles);

    @Query("""
            SELECT a
            FROM Account a
            WHERE a.role.id = :roleId
            """)
    List<Account> findAccountsByRole(String roleId);
}
