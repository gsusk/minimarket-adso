package org.adso.minimarket.repository;

import jakarta.websocket.OnError;
import org.adso.minimarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    <S extends User> S save(S entity);

    boolean existsByEmail(String email);
}
