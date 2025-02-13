package com.diamond.store.repository;

import com.diamond.store.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
   Optional<Account> findByUsername(String username);

   List<Account> findAllByUsername(String username);
}
