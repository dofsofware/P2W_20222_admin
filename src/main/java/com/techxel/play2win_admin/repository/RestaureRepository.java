package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.Restaure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaureRepository extends JpaRepository<Restaure, Long> {}
