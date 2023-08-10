package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.TimeSlot;
import com.mycompany.myapp.exception.BadRequestException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.repository.TimeSlotRepository;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckUtilService {

    private final Logger log = LoggerFactory.getLogger(CheckUtilService.class);

    private final TimeSlotRepository timeSlotRepository;

    private final DoctorRepository doctorRepository;
    private final PackRepository packRepository;

    public CheckUtilService(TimeSlotRepository timeSlotRepository, DoctorRepository doctorRepository, PackRepository packRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
        this.packRepository = packRepository;
    }

    public Boolean checkTimeSlot(CreateTimeSlotDTO createTimeSlotDTO) {
        boolean byDoctor = false;
        if (createTimeSlotDTO.getDoctorId() != null && createTimeSlotDTO.getPackId() != null) {
            throw new BadRequestException("Can not has both doctorId and packId");
        } else if (createTimeSlotDTO.getDoctorId() == null && createTimeSlotDTO.getPackId() == null) {
            throw new BadRequestException("Both doctorId and packId cannot be null");
        } else if (createTimeSlotDTO.getDoctorId() != null && createTimeSlotDTO.getPackId() == null) {
            byDoctor = true;
        }
        if (byDoctor) {
            return checkTimeSlotByDoctor(createTimeSlotDTO);
        } else {
            return checkTimeSlotByPack(createTimeSlotDTO);
        }
    }

    public Boolean checkTimeSlotByDoctor(CreateTimeSlotDTO createTimeSlotDTO) {
        Doctor doctor = doctorRepository
            .findById(createTimeSlotDTO.getDoctorId())
            .orElseThrow(() -> new NotFoundException("Doctor not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDoctor(doctor);

        return true;
    }

    public Boolean checkTimeSlotByPack(CreateTimeSlotDTO createTimeSlotDTO) {
        return true;
    }

    public Boolean exitedTimeSlotAllDay(TimeSlot timeSlot) {
        return true;
    }
}
