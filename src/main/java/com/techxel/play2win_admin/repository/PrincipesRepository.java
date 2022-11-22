package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Principes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Principes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrincipesRepository extends JpaRepository<Principes, Long> {}
