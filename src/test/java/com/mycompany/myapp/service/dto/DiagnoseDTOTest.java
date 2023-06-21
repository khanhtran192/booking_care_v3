package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiagnoseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiagnoseDTO.class);
        DiagnoseDTO diagnoseDTO1 = new DiagnoseDTO();
        diagnoseDTO1.setId(1L);
        DiagnoseDTO diagnoseDTO2 = new DiagnoseDTO();
        assertThat(diagnoseDTO1).isNotEqualTo(diagnoseDTO2);
        diagnoseDTO2.setId(diagnoseDTO1.getId());
        assertThat(diagnoseDTO1).isEqualTo(diagnoseDTO2);
        diagnoseDTO2.setId(2L);
        assertThat(diagnoseDTO1).isNotEqualTo(diagnoseDTO2);
        diagnoseDTO1.setId(null);
        assertThat(diagnoseDTO1).isNotEqualTo(diagnoseDTO2);
    }
}
