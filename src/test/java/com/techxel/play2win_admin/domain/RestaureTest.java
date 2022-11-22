package com.techxel.play2win_admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.techxel.play2win_admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestaureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaure.class);
        Restaure restaure1 = new Restaure();
        restaure1.setId(1L);
        Restaure restaure2 = new Restaure();
        restaure2.setId(restaure1.getId());
        assertThat(restaure1).isEqualTo(restaure2);
        restaure2.setId(2L);
        assertThat(restaure1).isNotEqualTo(restaure2);
        restaure1.setId(null);
        assertThat(restaure1).isNotEqualTo(restaure2);
    }
}
