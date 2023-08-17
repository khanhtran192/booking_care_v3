package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TimeSlotValue;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "time_slot")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "time", nullable = false)
    private String time;

    @Column(name = "start_time")
    private TimeSlotValue startTime;

    @Column(name = "end_time")
    private TimeSlotValue endTime;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties(value = { "timeSlots", "orders", "department" }, allowSetters = true)
    private Doctor doctor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "timeSlots", "orders", "hospital" }, allowSetters = true)
    private Pack pack;

    @OneToMany(mappedBy = "timeslot")
    @JsonIgnoreProperties(value = { "customer", "customer", "doctor", "pack" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public TimeSlot() {}

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimeSlot(TimeSlotValue startTime, TimeSlotValue endTime, Double price) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.time = startTime.getValue() + " - " + endTime.getValue();
        this.price = price;
    }

    public TimeSlot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public TimeSlot time(String time) {
        this.setTime(time);
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return this.description;
    }

    public TimeSlot description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return this.price;
    }

    public TimeSlot price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getActive() {
        return this.active;
    }

    public TimeSlot active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public TimeSlotValue getStartTime() {
        return startTime;
    }

    public void setStartTime(TimeSlotValue startTime) {
        this.startTime = startTime;
    }

    public TimeSlotValue getEndTime() {
        return endTime;
    }

    public void setEndTime(TimeSlotValue endTime) {
        this.endTime = endTime;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public TimeSlot doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public Pack getPack() {
        return this.pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public TimeSlot pack(Pack pack) {
        this.setPack(pack);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSlot)) {
            return false;
        }
        return id != null && id.equals(((TimeSlot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSlot{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", status='" + getActive() + "'" +
            "}";
    }
}
