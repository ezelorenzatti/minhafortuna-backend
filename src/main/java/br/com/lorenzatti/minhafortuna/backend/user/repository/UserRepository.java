package br.com.lorenzatti.minhafortuna.backend.user.repository;

import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
