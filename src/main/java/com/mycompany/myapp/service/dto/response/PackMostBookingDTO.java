package com.mycompany.myapp.service.dto.response;

public class PackMostBookingDTO {

    private PackResponseDTO pack;
    private Integer quantity;

    public PackResponseDTO getPack() {
        return pack;
    }

    public void setPack(PackResponseDTO pack) {
        this.pack = pack;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
