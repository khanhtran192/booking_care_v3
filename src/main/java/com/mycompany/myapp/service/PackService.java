package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.Image;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.enumeration.ImageType;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import com.mycompany.myapp.exception.AlreadyExistedException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.repository.ImageRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.service.dto.FileDTO;
import com.mycompany.myapp.service.dto.request.CreatePackDTO;
import com.mycompany.myapp.service.dto.response.PackMostBookingDTO;
import com.mycompany.myapp.service.dto.response.PackResponseDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pack}.
 */
@Service
@Transactional
public class PackService {

    private final Logger log = LoggerFactory.getLogger(PackService.class);

    private final PackRepository packRepository;

    private final OrderRepository orderRepository;

    private final HospitalRepository hospitalRepository;

    private final MapperService mapperService;
    private final MinioService minioService;
    private final ImageRepository imageRepository;

    public PackService(
        PackRepository packRepository,
        HospitalRepository hospitalRepository,
        MapperService mapperService,
        OrderRepository orderRepository,
        MinioService minioService,
        ImageRepository imageRepository
    ) {
        this.packRepository = packRepository;
        this.hospitalRepository = hospitalRepository;
        this.mapperService = mapperService;
        this.orderRepository = orderRepository;
        this.minioService = minioService;
        this.imageRepository = imageRepository;
    }

    /**
     * Save a pack.
     *
     * @param packDTO the entity to save.
     * @return the persisted entity.
     */
    public PackResponseDTO save(CreatePackDTO packDTO) {
        log.debug("Request to save Pack : {}", packDTO);
        Hospital hospital = hospitalRepository
            .findById(packDTO.getHospitalId())
            .orElseThrow(() -> new NotFoundException("Hospital not found"));
        if (packRepository.existsByNameAndHospital(packDTO.getName(), hospital)) {
            throw new AlreadyExistedException("This pack already exists");
        }
        Pack pack = new Pack();
        pack.setName(packDTO.getName());
        pack.setActive(true);
        pack.setDescription(packDTO.getDescription());
        pack.setHospital(hospital);
        pack = packRepository.save(pack);
        return mapperService.mapToDto(pack);
    }

    /**
     * Update a pack.
     *
     * @param packDTO the entity to save.
     * @return the persisted entity.
     */
    public PackResponseDTO update(CreatePackDTO packDTO, Long id) {
        log.debug("Request to update Pack : {}", packDTO);
        Pack pack = packRepository.findById(id).orElse(new Pack());
        Hospital hospital = hospitalRepository
            .findById(packDTO.getHospitalId())
            .orElseThrow(() -> new NotFoundException("Hospital not found"));
        if (packRepository.existsByNameAndHospital(packDTO.getName(), hospital)) {
            throw new AlreadyExistedException("This pack already exists");
        }
        pack.setName(packDTO.getName());
        pack.setDescription(packDTO.getDescription());
        pack.setActive(packDTO.getActive());
        pack.setHospital(hospital);
        pack = packRepository.save(pack);
        return mapperService.mapToDto(pack);
    }

    /**
     * Get all the packs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PackResponseDTO> findAll(Pageable pageable, Boolean active, Long hospitalId, String keyword) {
        log.debug("Request to get all Packs");
        Hospital hospital = null;
        if (hospitalId != null) {
            hospital = hospitalRepository.findById(hospitalId).orElse(null);
        }
        return packRepository.pagePack(pageable, active, hospital, keyword).map(mapperService::mapToDto);
    }

    /**
     * Get one pack by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PackResponseDTO> findOne(Long id) {
        log.debug("Request to get Pack : {}", id);
        return packRepository.findById(id).map(mapperService::mapToDto);
    }

    /**
     * Delete the pack by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pack : {}", id);
        Pack pack = packRepository.findById(id).orElseThrow(() -> new NotFoundException("pack: " + id + " not found"));
        pack.setActive(false);
        packRepository.save(pack);
    }

    public void activePack(Long id) {
        log.debug("Request to active Pack : {}", id);
        Pack pack = packRepository.findById(id).orElseThrow(() -> new NotFoundException("pack: " + id + " not found"));
        pack.setActive(true);
        packRepository.save(pack);
    }

    public List<PackMostBookingDTO> listMostBooking() {
        List<Pack> packs = packRepository.findAllByActiveIsTrue();
        return packs
            .stream()
            .map(pack -> {
                PackMostBookingDTO p = new PackMostBookingDTO();
                p.setPack(mapperService.mapToDto(pack));
                p.setQuantity(
                    orderRepository
                        .orderByPack(
                            pack,
                            Arrays.asList(OrderStatus.COMPLETE, OrderStatus.PENDING, OrderStatus.APPROVED, OrderStatus.REJECTED)
                        )
                        .size()
                );
                return p;
            })
            .sorted((Comparator.comparing(PackMostBookingDTO::getQuantity)).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }

    public void uploadLogo(Long id, FileDTO fileDTO) {
        Pack pack = packRepository.findById(id).orElseThrow(() -> new NotFoundException("pack: " + id + " not found"));
        String name = minioService.uploadFile(fileDTO);
        String path = minioService.getObject(name);
        Image image = new Image();
        image.setName(name);
        image.setPath(path);
        image.setPackId(pack.getId());
        image.setType(ImageType.LOGO);
        Image old = imageRepository.findByPackId(id);
        if (old != null) {
            imageRepository.delete(old);
        }
        imageRepository.save(image);
    }
}
