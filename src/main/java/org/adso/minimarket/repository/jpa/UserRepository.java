package org.adso.minimarket.repository.jpa;

import org.adso.minimarket.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    <S extends User> S save(S entity);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    void removeUserById(Long id);
}
