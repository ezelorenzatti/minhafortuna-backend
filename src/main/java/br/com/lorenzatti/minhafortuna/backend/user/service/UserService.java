package br.com.lorenzatti.minhafortuna.backend.user.service;

import br.com.lorenzatti.minhafortuna.backend.user.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> getUserById(Long id);

    Optional<User> findByEmail(String email);

    void delete(User user);
}
