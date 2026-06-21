package fr.tiogars.data.settings.useraccount.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, String>, JpaSpecificationExecutor<UserAccountEntity> {

    Optional<UserAccountEntity> findByUsername(String username);

    List<UserAccountEntity> findAllByOrderByUsernameAsc();
}
