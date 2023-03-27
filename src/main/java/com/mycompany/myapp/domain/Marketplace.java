package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Marketplace.
 */
@Entity
@Table(name = "marketplace")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Marketplace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "marketplace")
    @JsonIgnoreProperties(value = { "marketplace" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Marketplace id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Marketplace name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setMarketplace(null));
        }
        if (products != null) {
            products.forEach(i -> i.setMarketplace(this));
        }
        this.products = products;
    }

    public Marketplace products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Marketplace addProducts(Product product) {
        this.products.add(product);
        product.setMarketplace(this);
        return this;
    }

    public Marketplace removeProducts(Product product) {
        this.products.remove(product);
        product.setMarketplace(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marketplace)) {
            return false;
        }
        return id != null && id.equals(((Marketplace) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marketplace{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
