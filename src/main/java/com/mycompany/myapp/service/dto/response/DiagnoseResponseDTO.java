package com.mycompany.myapp.service.dto.response;

public class DiagnoseResponseDTO {

    private Long id;
    private String description;
    private OrderResponseDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderResponseDTO getOrder() {
        return order;
    }

    public void setOrder(OrderResponseDTO order) {
        this.order = order;
    }
}
