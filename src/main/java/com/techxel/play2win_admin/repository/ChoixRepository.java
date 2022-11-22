package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Choix;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Choix entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChoixRepository extends JpaRepository<Choix, Long> {}
