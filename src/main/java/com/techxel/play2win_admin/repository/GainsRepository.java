package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Gains;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Gains entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GainsRepository extends JpaRepository<Gains, Long> {}
