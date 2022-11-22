package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Incription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Incription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncriptionRepository extends JpaRepository<Incription, Long> {}
