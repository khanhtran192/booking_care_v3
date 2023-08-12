package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.TimeSlot;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import com.mycompany.myapp.domain.enumeration.TimeSlotValue;
import com.mycompany.myapp.exception.BadRequestException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.repository.TimeSlotRepository;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CheckUtilService {

    private final Logger log = LoggerFactory.getLogger(CheckUtilService.class);

    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    private final PackRepository packRepository;

    private final OrderRepository orderRepository;

    public CheckUtilService(
        TimeSlotRepository timeSlotRepository,
        DoctorRepository doctorRepository,
        PackRepository packRepository,
        OrderRepository orderRepository
    ) {
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
        this.packRepository = packRepository;
        this.orderRepository = orderRepository;
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
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDoctorAndActiveIsTrue(doctor);
        for (TimeSlot timeSlot : timeSlots) {
            return checkTime(createTimeSlotDTO, timeSlot);
        }
        return false;
    }

    public Boolean checkTimeSlotByPack(CreateTimeSlotDTO createTimeSlotDTO) {
        Pack pack = packRepository.findById(createTimeSlotDTO.getPackId()).orElseThrow(() -> new NotFoundException("Pack not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByPackAndActiveIsTrue(pack);
        for (TimeSlot timeSlot : timeSlots) {
            return checkTime(createTimeSlotDTO, timeSlot);
        }
        return false;
    }

    public Boolean checkTime(CreateTimeSlotDTO createTimeSlotDTO, TimeSlot timeSlot) {
        int startTime = convertTimeSlotToInt(createTimeSlotDTO.getStartTime());
        int endTime = convertTimeSlotToInt(createTimeSlotDTO.getEndTime());
        if (!(startTime >= timeSlot.getEndTime().getNumber() || endTime <= timeSlot.getStartTime().getNumber())) {
            return false;
        } else {
            return true;
        }
    }

    public Integer convertTimeSlotToInt(String timeSlot) {
        TimeSlotValue timeSlotValue = TimeSlotValue.valueOf(timeSlot);
        return timeSlotValue.getNumber();
    }

    public Boolean checkActiveTimeSlot(TimeSlot timeSlot) {
        int startTime = timeSlot.getStartTime().getNumber();
        int endTime = timeSlot.getEndTime().getNumber();
        boolean byDoctor = false;
        List<TimeSlot> timeSlots;
        if (timeSlot.getDoctor() != null && timeSlot.getPack() == null) {
            byDoctor = true;
        }
        if (byDoctor) {
            timeSlots = timeSlotRepository.findAllByDoctorAndActiveIsTrue(timeSlot.getDoctor());
            for (TimeSlot slot : timeSlots) {
                if (!(startTime >= slot.getEndTime().getNumber() || endTime <= slot.getStartTime().getNumber())) {
                    return false;
                }
            }
            return true;
        } else {
            timeSlots = timeSlotRepository.findAllByPackAndActiveIsTrue(timeSlot.getPack());
            for (TimeSlot slot : timeSlots) {
                if (!(startTime >= slot.getEndTime().getNumber() || endTime <= slot.getStartTime().getNumber())) {
                    return false;
                }
            }
            return true;
        }
    }

    public Boolean checkTimeSlotFree(TimeSlot timeSlot, Doctor doctor, LocalDate date) {
        List<Order> orders = orderRepository.findAllByDoctorAndDateAndTimeslotAndStatusIn(
            doctor,
            date,
            timeSlot,
            Arrays.asList(OrderStatus.APPROVED)
        );
        if (orders.isEmpty()) {
            return true;
        }
        return false;
    }
}
