package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Marketplace} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarketplaceWithProductsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private List<ProductDTO> products;

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

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarketplaceWithProductsDTO)) {
            return false;
        }

        MarketplaceWithProductsDTO marketplaceDTO = (MarketplaceWithProductsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marketplaceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "MarketplaceWithProductsDTO{" + "id=" + id + ", name='" + name + '\'' + ", products=" + products + '}';
    }
}
