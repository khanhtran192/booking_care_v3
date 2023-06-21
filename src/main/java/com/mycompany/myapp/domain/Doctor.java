package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "hospital_id")
    private Integer hospitalId;

    @Column(name = "degree")
    private String degree;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "specialize")
    private String specialize;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnoreProperties(value = { "doctor", "pack" }, allowSetters = true)
    private Set<TimeSlot> timeSlots = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    @JsonIgnoreProperties(value = { "timeslot", "customer", "doctor", "pack" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "doctors", "hospital" }, allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Doctor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Doctor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Doctor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Doctor phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Doctor dateOfBirth(Instant dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getHospitalId() {
        return this.hospitalId;
    }

    public Doctor hospitalId(Integer hospitalId) {
        this.setHospitalId(hospitalId);
        return this;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDegree() {
        return this.degree;
    }

    public Doctor degree(String degree) {
        this.setDegree(degree);
        return this;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Double getRate() {
        return this.rate;
    }

    public Doctor rate(Double rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getSpecialize() {
        return this.specialize;
    }

    public Doctor specialize(String specialize) {
        this.setSpecialize(specialize);
        return this;
    }

    public void setSpecialize(String specialize) {
        this.specialize = specialize;
    }

    public Set<TimeSlot> getTimeSlots() {
        return this.timeSlots;
    }

    public void setTimeSlots(Set<TimeSlot> timeSlots) {
        if (this.timeSlots != null) {
            this.timeSlots.forEach(i -> i.setDoctor(null));
        }
        if (timeSlots != null) {
            timeSlots.forEach(i -> i.setDoctor(this));
        }
        this.timeSlots = timeSlots;
    }

    public Doctor timeSlots(Set<TimeSlot> timeSlots) {
        this.setTimeSlots(timeSlots);
        return this;
    }

    public Doctor addTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.add(timeSlot);
        timeSlot.setDoctor(this);
        return this;
    }

    public Doctor removeTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.remove(timeSlot);
        timeSlot.setDoctor(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setDoctor(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setDoctor(this));
        }
        this.orders = orders;
    }

    public Doctor orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Doctor addOrder(Order order) {
        this.orders.add(order);
        order.setDoctor(this);
        return this;
    }

    public Doctor removeOrder(Order order) {
        this.orders.remove(order);
        order.setDoctor(null);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Doctor department(Department department) {
        this.setDepartment(department);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doctor)) {
            return false;
        }
        return id != null && id.equals(((Doctor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doctor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", hospitalId=" + getHospitalId() +
            ", degree='" + getDegree() + "'" +
            ", rate=" + getRate() +
            ", specialize='" + getSpecialize() + "'" +
            "}";
    }
}
