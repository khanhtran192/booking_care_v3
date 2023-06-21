package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Task entity.\n@author The JHipster team.
 */
@Entity
@Table(name = "pack")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nane")
    private String nane;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "pack")
    @JsonIgnoreProperties(value = { "doctor", "pack" }, allowSetters = true)
    private Set<TimeSlot> timeSlots = new HashSet<>();

    @OneToMany(mappedBy = "pack")
    @JsonIgnoreProperties(value = { "timeslot", "customer", "doctor", "pack" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "departments", "packs" }, allowSetters = true)
    private Hospital hospital;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pack id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNane() {
        return this.nane;
    }

    public Pack nane(String nane) {
        this.setNane(nane);
        return this;
    }

    public void setNane(String nane) {
        this.nane = nane;
    }

    public String getDescription() {
        return this.description;
    }

    public Pack description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TimeSlot> getTimeSlots() {
        return this.timeSlots;
    }

    public void setTimeSlots(Set<TimeSlot> timeSlots) {
        if (this.timeSlots != null) {
            this.timeSlots.forEach(i -> i.setPack(null));
        }
        if (timeSlots != null) {
            timeSlots.forEach(i -> i.setPack(this));
        }
        this.timeSlots = timeSlots;
    }

    public Pack timeSlots(Set<TimeSlot> timeSlots) {
        this.setTimeSlots(timeSlots);
        return this;
    }

    public Pack addTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.add(timeSlot);
        timeSlot.setPack(this);
        return this;
    }

    public Pack removeTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.remove(timeSlot);
        timeSlot.setPack(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setPack(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setPack(this));
        }
        this.orders = orders;
    }

    public Pack orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Pack addOrder(Order order) {
        this.orders.add(order);
        order.setPack(this);
        return this;
    }

    public Pack removeOrder(Order order) {
        this.orders.remove(order);
        order.setPack(null);
        return this;
    }

    public Hospital getHospital() {
        return this.hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Pack hospital(Hospital hospital) {
        this.setHospital(hospital);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pack)) {
            return false;
        }
        return id != null && id.equals(((Pack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pack{" +
            "id=" + getId() +
            ", nane='" + getNane() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
