package com.mycompany.myapp.service.dto.request;

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

    private Long id;

    private String address;

    private String symptom;

    private LocalDate date;

    private OrderStatus status;

    private Double price;

    private Long timeslot;

    private Long customer;

    private Long doctor;

    private Long pack;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public Long getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Long timeslot) {
        this.timeslot = timeslot;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getDoctor() {
        return doctor;
    }

    public void setDoctor(Long doctor) {
        this.doctor = doctor;
    }

    public Long getPack() {
        return pack;
    }

    public void setPack(Long pack) {
        this.pack = pack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
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
