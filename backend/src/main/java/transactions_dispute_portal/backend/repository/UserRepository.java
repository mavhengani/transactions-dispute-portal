package transactions_dispute_portal.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import transactions_dispute_portal.backend.entity.User;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCellphone(String cellphone);
}