package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiagnoseMapperTest {

    private DiagnoseMapper diagnoseMapper;

    @BeforeEach
    public void setUp() {
        diagnoseMapper = new DiagnoseMapperImpl();
    }
}
