package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.service.dto.HospitalDTO;
import com.mycompany.myapp.service.dto.PackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pack} and its DTO {@link PackDTO}.
 */
@Mapper(componentModel = "spring")
public interface PackMapper extends EntityMapper<PackDTO, Pack> {
    @Mapping(target = "hospital", source = "hospital", qualifiedByName = "hospitalId")
    PackDTO toDto(Pack s);

    @Named("hospitalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HospitalDTO toDtoHospitalId(Hospital hospital);
}
