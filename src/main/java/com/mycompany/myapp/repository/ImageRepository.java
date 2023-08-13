package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Image;
import com.mycompany.myapp.domain.enumeration.ImageType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Image entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByPackId(Long id);
    Image findByHospitalIdAndType(Long id, ImageType type);
    Image findByDepartmentIdAndType(Long id, ImageType type);
    Image findByDoctorIdAndType(Long id, ImageType type);
}
