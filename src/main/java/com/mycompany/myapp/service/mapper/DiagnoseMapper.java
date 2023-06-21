package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Diagnose;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.service.dto.DiagnoseDTO;
import com.mycompany.myapp.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Diagnose} and its DTO {@link DiagnoseDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiagnoseMapper extends EntityMapper<DiagnoseDTO, Diagnose> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    DiagnoseDTO toDto(Diagnose s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
