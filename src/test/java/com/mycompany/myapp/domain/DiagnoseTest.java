package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiagnoseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diagnose.class);
        Diagnose diagnose1 = new Diagnose();
        diagnose1.setId(1L);
        Diagnose diagnose2 = new Diagnose();
        diagnose2.setId(diagnose1.getId());
        assertThat(diagnose1).isEqualTo(diagnose2);
        diagnose2.setId(2L);
        assertThat(diagnose1).isNotEqualTo(diagnose2);
        diagnose1.setId(null);
        assertThat(diagnose1).isNotEqualTo(diagnose2);
    }
}
