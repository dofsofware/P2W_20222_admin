package com.techxel.play2win_admin.repository;

import com.techxel.play2win_admin.domain.MotDePasseSetting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MotDePasseSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotDePasseSettingRepository extends JpaRepository<MotDePasseSetting, Long> {}
