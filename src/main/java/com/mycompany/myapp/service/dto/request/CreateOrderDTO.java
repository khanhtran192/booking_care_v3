package com.mycompany.myapp.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import com.mycompany.myapp.service.dto.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CreateOrderDTO implements Serializable {

    private String symptom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Long timeSlot;

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Long timeSlot) {
        this.timeSlot = timeSlot;
    }
}
