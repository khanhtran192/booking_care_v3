package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.service.dto.PackDTO;
import com.mycompany.myapp.service.mapper.PackMapper;
import java.util.Optional;
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

    private final PackMapper packMapper;

    public PackService(PackRepository packRepository, PackMapper packMapper) {
        this.packRepository = packRepository;
        this.packMapper = packMapper;
    }

    /**
     * Save a pack.
     *
     * @param packDTO the entity to save.
     * @return the persisted entity.
     */
    public PackDTO save(PackDTO packDTO) {
        log.debug("Request to save Pack : {}", packDTO);
        Pack pack = packMapper.toEntity(packDTO);
        pack = packRepository.save(pack);
        return packMapper.toDto(pack);
    }

    /**
     * Update a pack.
     *
     * @param packDTO the entity to save.
     * @return the persisted entity.
     */
    public PackDTO update(PackDTO packDTO) {
        log.debug("Request to update Pack : {}", packDTO);
        Pack pack = packMapper.toEntity(packDTO);
        pack = packRepository.save(pack);
        return packMapper.toDto(pack);
    }

    /**
     * Partially update a pack.
     *
     * @param packDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PackDTO> partialUpdate(PackDTO packDTO) {
        log.debug("Request to partially update Pack : {}", packDTO);

        return packRepository
            .findById(packDTO.getId())
            .map(existingPack -> {
                packMapper.partialUpdate(existingPack, packDTO);

                return existingPack;
            })
            .map(packRepository::save)
            .map(packMapper::toDto);
    }

    /**
     * Get all the packs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Packs");
        return packRepository.findAll(pageable).map(packMapper::toDto);
    }

    /**
     * Get one pack by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PackDTO> findOne(Long id) {
        log.debug("Request to get Pack : {}", id);
        return packRepository.findById(id).map(packMapper::toDto);
    }

    /**
     * Delete the pack by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pack : {}", id);
        packRepository.deleteById(id);
    }
}
