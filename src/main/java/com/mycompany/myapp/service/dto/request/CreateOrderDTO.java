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

    private Long timeslot;

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

    public Long getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Long timeslot) {
        this.timeslot = timeslot;
    }
    //    @Override
    //    public boolean equals(Object o) {
    //        if (this == o) {
    //            return true;
    //        }
    //        if (!(o instanceof OrderDTO)) {
    //            return false;
    //        }
    //
    //        OrderDTO orderDTO = (OrderDTO) o;
    //        if (this.id == null) {
    //            return false;
    //        }
    //        return Objects.equals(this.id, orderDTO.getId());
    //    }
    //
    //    @Override
    //    public int hashCode() {
    //        return Objects.hash(this.id);
    //    }
    // prettier-ignore
    //    @Override
    //    public String toString() {
    //        return "OrderDTO{" +
    //            "id=" + getId() +
    //            ", address='" + getAddress() + "'" +
    //            ", symptom='" + getSymptom() + "'" +
    //            ", date='" + getDate() + "'" +
    //            ", status='" + getStatus() + "'" +
    //            ", price=" + getPrice() +
    //            ", timeslot=" + getTimeslot() +
    //            ", customer=" + getCustomer() +
    //            ", doctor=" + getDoctor() +
    //            ", pack=" + getPack() +
    //            "}";
    //    }
}
