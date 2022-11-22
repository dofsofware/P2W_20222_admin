package com.techxel.play2win_admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.techxel.play2win_admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthentificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authentification.class);
        Authentification authentification1 = new Authentification();
        authentification1.setId(1L);
        Authentification authentification2 = new Authentification();
        authentification2.setId(authentification1.getId());
        assertThat(authentification1).isEqualTo(authentification2);
        authentification2.setId(2L);
        assertThat(authentification1).isNotEqualTo(authentification2);
        authentification1.setId(null);
        assertThat(authentification1).isNotEqualTo(authentification2);
    }
}
