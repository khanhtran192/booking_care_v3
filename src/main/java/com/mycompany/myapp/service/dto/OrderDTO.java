package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Order} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDTO implements Serializable {

    private Long id;

    private String address;

    private String symptom;

    private LocalDate date;

    private OrderStatus status;

    private Double price;

    private TimeSlotDTO timeslot;

    private CustomerDTO customer;

    private DoctorDTO doctor;

    private PackDTO pack;

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

    public TimeSlotDTO getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(TimeSlotDTO timeslot) {
        this.timeslot = timeslot;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public PackDTO getPack() {
        return pack;
    }

    public void setPack(PackDTO pack) {
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
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", symptom='" + getSymptom() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", price=" + getPrice() +
            ", timeslot=" + getTimeslot() +
            ", customer=" + getCustomer() +
            ", doctor=" + getDoctor() +
            ", pack=" + getPack() +
            "}";
    }
}
