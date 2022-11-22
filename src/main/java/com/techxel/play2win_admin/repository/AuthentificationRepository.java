package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Authentification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Authentification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthentificationRepository extends JpaRepository<Authentification, Long> {}
