package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.FacilityType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Hospital.
 */
@Entity
@Table(name = "hospital")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "work_day")
    private String workDay;

    @Column(name = "work_time")
    private String workTime;

    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FacilityType type;

    @Column(name = "jhi_procedure")
    private String procedure;

    @Column(name = "user_Id")
    private Long userId;

    @OneToMany(mappedBy = "hospital")
    @JsonIgnoreProperties(value = { "doctors", "hospital" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "hospital")
    @JsonIgnoreProperties(value = { "timeSlots", "orders", "hospital" }, allowSetters = true)
    private Set<Pack> packs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Hospital id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Hospital name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Hospital address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return this.email;
    }

    public Hospital email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Hospital phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public Hospital description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkDay() {
        return this.workDay;
    }

    public Hospital workDay(String workDay) {
        this.setWorkDay(workDay);
        return this;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public String getWorkTime() {
        return this.workTime;
    }

    public Hospital workTime(String workTime) {
        this.setWorkTime(workTime);
        return this;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public FacilityType getType() {
        return this.type;
    }

    public Hospital type(FacilityType type) {
        this.setType(type);
        return this;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }

    public String getProcedure() {
        return this.procedure;
    }

    public Hospital procedure(String procedure) {
        this.setProcedure(procedure);
        return this;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        if (this.departments != null) {
            this.departments.forEach(i -> i.setHospital(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setHospital(this));
        }
        this.departments = departments;
    }

    public Hospital departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Hospital addDepartment(Department department) {
        this.departments.add(department);
        department.setHospital(this);
        return this;
    }

    public Hospital removeDepartment(Department department) {
        this.departments.remove(department);
        department.setHospital(null);
        return this;
    }

    public Set<Pack> getPacks() {
        return this.packs;
    }

    public void setPacks(Set<Pack> packs) {
        if (this.packs != null) {
            this.packs.forEach(i -> i.setHospital(null));
        }
        if (packs != null) {
            packs.forEach(i -> i.setHospital(this));
        }
        this.packs = packs;
    }

    public Hospital packs(Set<Pack> packs) {
        this.setPacks(packs);
        return this;
    }

    public Hospital addPack(Pack pack) {
        this.packs.add(pack);
        pack.setHospital(this);
        return this;
    }

    public Hospital removePack(Pack pack) {
        this.packs.remove(pack);
        pack.setHospital(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hospital)) {
            return false;
        }
        return id != null && id.equals(((Hospital) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hospital{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", workDay='" + getWorkDay() + "'" +
            ", workTime='" + getWorkTime() + "'" +
            ", type='" + getType() + "'" +
            ", procedure='" + getProcedure() + "'" +
            "}";
    }
}
