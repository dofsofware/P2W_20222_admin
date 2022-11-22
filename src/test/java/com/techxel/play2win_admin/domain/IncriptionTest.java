package com.techxel.play2win_admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.techxel.play2win_admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Incription.class);
        Incription incription1 = new Incription();
        incription1.setId(1L);
        Incription incription2 = new Incription();
        incription2.setId(incription1.getId());
        assertThat(incription1).isEqualTo(incription2);
        incription2.setId(2L);
        assertThat(incription1).isNotEqualTo(incription2);
        incription1.setId(null);
        assertThat(incription1).isNotEqualTo(incription2);
    }
}
