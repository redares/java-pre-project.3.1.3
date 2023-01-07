package org.example.pp.repository;

import org.example.pp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Optional<User> findUserById(Long id);

    List<User> findAll();

    void deleteAllById(Long id);
}
