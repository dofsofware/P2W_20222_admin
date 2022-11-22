package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.InfosAbonne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InfosAbonne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfosAbonneRepository extends JpaRepository<InfosAbonne, Long> {}
