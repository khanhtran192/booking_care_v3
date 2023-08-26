package com.mycompany.myapp.service.dto.response;

public class PackResponseDTO {

    private Long id;

    private String name;

    private String description;
    private String logo;
    private String backgroundImage;

    private String price;
    private Boolean active;

    private HospitalInfoResponseDTO hospital;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public HospitalInfoResponseDTO getHospital() {
        return hospital;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setHospital(HospitalInfoResponseDTO hospital) {
        this.hospital = hospital;
    }
}
