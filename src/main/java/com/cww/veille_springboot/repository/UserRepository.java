package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<User, Integer> {
         User findByEmail(String email);
}
