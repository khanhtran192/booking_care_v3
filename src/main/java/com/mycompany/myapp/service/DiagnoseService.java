package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Diagnose;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DiagnoseRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.service.dto.request.CreateDiagnoseDTO;
import com.mycompany.myapp.service.dto.response.DiagnoseResponseDTO;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Diagnose}.
 */
@Service
@Transactional
public class DiagnoseService {

    private final Logger log = LoggerFactory.getLogger(DiagnoseService.class);

    private final DiagnoseRepository diagnoseRepository;
    private final OrderRepository orderRepository;
    private final MapperService mapperService;

    public DiagnoseService(DiagnoseRepository diagnoseRepository, OrderRepository orderRepository, MapperService mapperService) {
        this.diagnoseRepository = diagnoseRepository;
        this.orderRepository = orderRepository;
        this.mapperService = mapperService;
    }

    public DiagnoseResponseDTO createDiagnose(Long id, CreateDiagnoseDTO dto) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        Diagnose diagnose = new Diagnose();
        diagnose.setDescription(dto.getDescription());
        diagnose.setOrder(order);
        diagnose = diagnoseRepository.save(diagnose);
        return mapperService.mapToDto(diagnose);
    }

    public Optional<DiagnoseResponseDTO> getDiagnose(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        return diagnoseRepository.findByOrder(order).map(mapperService::mapToDto);
    }
}
