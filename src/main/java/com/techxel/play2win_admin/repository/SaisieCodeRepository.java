package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.SaisieCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SaisieCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaisieCodeRepository extends JpaRepository<SaisieCode, Long> {}
