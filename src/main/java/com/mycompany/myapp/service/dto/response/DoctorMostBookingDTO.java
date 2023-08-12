package com.mycompany.myapp.service.dto.response;

public class DoctorMostBookingDTO {

    private DoctorResponseDTO doctor;
    private Integer quantity;

    public DoctorResponseDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorResponseDTO doctor) {
        this.doctor = doctor;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
