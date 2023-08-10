package com.mycompany.myapp.service.dto.response;

public class TimeSlotResponseDTO {

    private Long id;
    private String time;
    private TimeSlotValueResponseDTO startTime;
    private TimeSlotValueResponseDTO endTime;
    private String description;
    private Double price;
    private Boolean status;
    private DoctorResponseDTO doctor;
    private PackResponseDTO pack;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public TimeSlotValueResponseDTO getStartTime() {
        return startTime;
    }

    public void setStartTime(TimeSlotValueResponseDTO startTime) {
        this.startTime = startTime;
    }

    public TimeSlotValueResponseDTO getEndTime() {
        return endTime;
    }

    public void setEndTime(TimeSlotValueResponseDTO endTime) {
        this.endTime = endTime;
    }
}
