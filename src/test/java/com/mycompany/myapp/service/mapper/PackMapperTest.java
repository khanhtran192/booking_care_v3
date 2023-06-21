package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PackMapperTest {

    private PackMapper packMapper;

    @BeforeEach
    public void setUp() {
        packMapper = new PackMapperImpl();
    }
}
