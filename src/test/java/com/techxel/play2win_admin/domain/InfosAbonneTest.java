package com.techxel.play2win_admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.techxel.play2win_admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfosAbonneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfosAbonne.class);
        InfosAbonne infosAbonne1 = new InfosAbonne();
        infosAbonne1.setId(1L);
        InfosAbonne infosAbonne2 = new InfosAbonne();
        infosAbonne2.setId(infosAbonne1.getId());
        assertThat(infosAbonne1).isEqualTo(infosAbonne2);
        infosAbonne2.setId(2L);
        assertThat(infosAbonne1).isNotEqualTo(infosAbonne2);
        infosAbonne1.setId(null);
        assertThat(infosAbonne1).isNotEqualTo(infosAbonne2);
    }
}
