package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.TimeSlot;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.PackDTO;
import com.mycompany.myapp.service.dto.TimeSlotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeSlot} and its DTO {@link TimeSlotDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeSlotMapper extends EntityMapper<TimeSlotDTO, TimeSlot> {
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "doctorId")
    @Mapping(target = "pack", source = "pack", qualifiedByName = "packId")
    TimeSlotDTO toDto(TimeSlot s);

    @Named("doctorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctorDTO toDtoDoctorId(Doctor doctor);

    @Named("packId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PackDTO toDtoPackId(Pack pack);
}
