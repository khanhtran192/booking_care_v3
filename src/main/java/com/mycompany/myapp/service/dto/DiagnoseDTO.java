package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Diagnose} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiagnoseDTO implements Serializable {

    private Long id;

    private String description;

    private OrderDTO order;

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

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiagnoseDTO)) {
            return false;
        }

        DiagnoseDTO diagnoseDTO = (DiagnoseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, diagnoseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiagnoseDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
