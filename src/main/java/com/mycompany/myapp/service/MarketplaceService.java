package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MarketplaceDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Marketplace}.
 */
public interface MarketplaceService {
    /**
     * Save a marketplace.
     *
     * @param marketplaceDTO the entity to save.
     * @return the persisted entity.
     */
    MarketplaceDTO save(MarketplaceDTO marketplaceDTO);

    /**
     * Updates a marketplace.
     *
     * @param marketplaceDTO the entity to update.
     * @return the persisted entity.
     */
    MarketplaceDTO update(MarketplaceDTO marketplaceDTO);

    /**
     * Partially updates a marketplace.
     *
     * @param marketplaceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MarketplaceDTO> partialUpdate(MarketplaceDTO marketplaceDTO);

    /**
     * Get all the marketplaces.
     *
     * @return the list of entities.
     */
    List<MarketplaceDTO> findAll();

    /**
     * Get the "id" marketplace.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MarketplaceDTO> findOne(Long id);

    /**
     * Delete the "id" marketplace.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
