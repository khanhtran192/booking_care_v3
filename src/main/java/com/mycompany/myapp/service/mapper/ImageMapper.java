package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Image;
import com.mycompany.myapp.service.dto.ImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {}
