package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Diagnose;
import com.mycompany.myapp.repository.DiagnoseRepository;
import com.mycompany.myapp.service.dto.DiagnoseDTO;
import com.mycompany.myapp.service.mapper.DiagnoseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final DiagnoseMapper diagnoseMapper;

    public DiagnoseService(DiagnoseRepository diagnoseRepository, DiagnoseMapper diagnoseMapper) {
        this.diagnoseRepository = diagnoseRepository;
        this.diagnoseMapper = diagnoseMapper;
    }

    /**
     * Save a diagnose.
     *
     * @param diagnoseDTO the entity to save.
     * @return the persisted entity.
     */
    public DiagnoseDTO save(DiagnoseDTO diagnoseDTO) {
        log.debug("Request to save Diagnose : {}", diagnoseDTO);
        Diagnose diagnose = diagnoseMapper.toEntity(diagnoseDTO);
        diagnose = diagnoseRepository.save(diagnose);
        return diagnoseMapper.toDto(diagnose);
    }

    /**
     * Update a diagnose.
     *
     * @param diagnoseDTO the entity to save.
     * @return the persisted entity.
     */
    public DiagnoseDTO update(DiagnoseDTO diagnoseDTO) {
        log.debug("Request to update Diagnose : {}", diagnoseDTO);
        Diagnose diagnose = diagnoseMapper.toEntity(diagnoseDTO);
        diagnose = diagnoseRepository.save(diagnose);
        return diagnoseMapper.toDto(diagnose);
    }

    /**
     * Partially update a diagnose.
     *
     * @param diagnoseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DiagnoseDTO> partialUpdate(DiagnoseDTO diagnoseDTO) {
        log.debug("Request to partially update Diagnose : {}", diagnoseDTO);

        return diagnoseRepository
            .findById(diagnoseDTO.getId())
            .map(existingDiagnose -> {
                diagnoseMapper.partialUpdate(existingDiagnose, diagnoseDTO);

                return existingDiagnose;
            })
            .map(diagnoseRepository::save)
            .map(diagnoseMapper::toDto);
    }

    /**
     * Get all the diagnoses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DiagnoseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Diagnoses");
        return diagnoseRepository.findAll(pageable).map(diagnoseMapper::toDto);
    }

    /**
     * Get one diagnose by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DiagnoseDTO> findOne(Long id) {
        log.debug("Request to get Diagnose : {}", id);
        return diagnoseRepository.findById(id).map(diagnoseMapper::toDto);
    }

    /**
     * Delete the diagnose by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Diagnose : {}", id);
        diagnoseRepository.deleteById(id);
    }
}
