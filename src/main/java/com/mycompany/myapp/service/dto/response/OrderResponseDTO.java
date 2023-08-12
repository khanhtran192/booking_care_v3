package com.mycompany.myapp.service.dto.response;

import com.mycompany.myapp.domain.enumeration.OrderStatus;
import java.time.Instant;

public class OrderResponseDTO {

    private Long id;

    private String address;

    private String symptom;

    private Instant date;

    private OrderStatus status;

    private Double price;

    private TimeSlotResponseDTO timeSlot;

    private CustomerResponseDTO customer;

    private DoctorResponseDTO doctor;

    private PackResponseDTO pack;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TimeSlotResponseDTO getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotResponseDTO timeSlot) {
        this.timeSlot = timeSlot;
    }

    public CustomerResponseDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponseDTO customer) {
        this.customer = customer;
    }

    public DoctorResponseDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorResponseDTO doctor) {
        this.doctor = doctor;
    }

    public PackResponseDTO getPack() {
        return pack;
    }

    public void setPack(PackResponseDTO pack) {
        this.pack = pack;
    }
}
