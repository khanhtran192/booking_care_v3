package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.service.dto.HospitalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hospital} and its DTO {@link HospitalDTO}.
 */
@Mapper(componentModel = "spring")
public interface HospitalMapper extends EntityMapper<HospitalDTO, Hospital> {}
