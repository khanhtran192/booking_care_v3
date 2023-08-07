package com.mycompany.myapp.service.dto.request;

public class CreatePackDTO {

    private Long id;

    private String name;

    private String description;

    private Boolean active;

    //    private Double price;

    private Long hospitalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nane) {
        this.name = nane;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //    public Double getPrice() {
    //        return price;
    //    }
    //
    //    public void setPrice(Double price) {
    //        this.price = price;
    //    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
