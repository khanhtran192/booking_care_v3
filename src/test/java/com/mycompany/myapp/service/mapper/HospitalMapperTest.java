package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HospitalMapperTest {

    private HospitalMapper hospitalMapper;

    @BeforeEach
    public void setUp() {
        hospitalMapper = new HospitalMapperImpl();
    }
}
