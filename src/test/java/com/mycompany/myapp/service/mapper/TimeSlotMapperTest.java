package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeSlotMapperTest {

    private TimeSlotMapper timeSlotMapper;

    @BeforeEach
    public void setUp() {
        timeSlotMapper = new TimeSlotMapperImpl();
    }
}
