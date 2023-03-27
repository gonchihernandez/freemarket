package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import com.mycompany.myapp.service.mapper.MarketplaceMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MarketplaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarketplaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marketplaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarketplaceRepository marketplaceRepository;

    @Autowired
    private MarketplaceMapper marketplaceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarketplaceMockMvc;

    private Marketplace marketplace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marketplace createEntity(EntityManager em) {
        Marketplace marketplace = new Marketplace().name(DEFAULT_NAME);
        return marketplace;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marketplace createUpdatedEntity(EntityManager em) {
        Marketplace marketplace = new Marketplace().name(UPDATED_NAME);
        return marketplace;
    }

    @BeforeEach
    public void initTest() {
        marketplace = createEntity(em);
    }

    @Test
    @Transactional
    void createMarketplace() throws Exception {
        int databaseSizeBeforeCreate = marketplaceRepository.findAll().size();
        // Create the Marketplace
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);
        restMarketplaceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeCreate + 1);
        Marketplace testMarketplace = marketplaceList.get(marketplaceList.size() - 1);
        assertThat(testMarketplace.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createMarketplaceWithExistingId() throws Exception {
        // Create the Marketplace with an existing ID
        marketplace.setId(1L);
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        int databaseSizeBeforeCreate = marketplaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketplaceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketplaceRepository.findAll().size();
        // set the field null
        marketplace.setName(null);

        // Create the Marketplace, which fails.
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        restMarketplaceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarketplaces() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get all the marketplaceList
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketplace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMarketplace() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get the marketplace
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL_ID, marketplace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marketplace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMarketplace() throws Exception {
        // Get the marketplace
        restMarketplaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMarketplace() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();

        // Update the marketplace
        Marketplace updatedMarketplace = marketplaceRepository.findById(marketplace.getId()).get();
        // Disconnect from session so that the updates on updatedMarketplace are not directly saved in db
        em.detach(updatedMarketplace);
        updatedMarketplace.name(UPDATED_NAME);
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(updatedMarketplace);

        restMarketplaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marketplaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
        Marketplace testMarketplace = marketplaceList.get(marketplaceList.size() - 1);
        assertThat(testMarketplace.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingMarketplace() throws Exception {
        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();
        marketplace.setId(count.incrementAndGet());

        // Create the Marketplace
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marketplaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarketplace() throws Exception {
        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();
        marketplace.setId(count.incrementAndGet());

        // Create the Marketplace
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarketplace() throws Exception {
        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();
        marketplace.setId(count.incrementAndGet());

        // Create the Marketplace
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketplaceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarketplaceWithPatch() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();

        // Update the marketplace using partial update
        Marketplace partialUpdatedMarketplace = new Marketplace();
        partialUpdatedMarketplace.setId(marketplace.getId());

        partialUpdatedMarketplace.name(UPDATED_NAME);

        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketplace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketplace))
            )
            .andExpect(status().isOk());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
        Marketplace testMarketplace = marketplaceList.get(marketplaceList.size() - 1);
        assertThat(testMarketplace.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMarketplaceWithPatch() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();

        // Update the marketplace using partial update
        Marketplace partialUpdatedMarketplace = new Marketplace();
        partialUpdatedMarketplace.setId(marketplace.getId());

        partialUpdatedMarketplace.name(UPDATED_NAME);

        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketplace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketplace))
            )
            .andExpect(status().isOk());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
        Marketplace testMarketplace = marketplaceList.get(marketplaceList.size() - 1);
        assertThat(testMarketplace.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMarketplace() throws Exception {
        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();
        marketplace.setId(count.incrementAndGet());

        // Create the Marketplace
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marketplaceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarketplace() throws Exception {
        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();
        marketplace.setId(count.incrementAndGet());

        // Create the Marketplace
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarketplace() throws Exception {
        int databaseSizeBeforeUpdate = marketplaceRepository.findAll().size();
        marketplace.setId(count.incrementAndGet());

        // Create the Marketplace
        MarketplaceDTO marketplaceDTO = marketplaceMapper.toDto(marketplace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(marketplaceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marketplace in the database
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarketplace() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        int databaseSizeBeforeDelete = marketplaceRepository.findAll().size();

        // Delete the marketplace
        restMarketplaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, marketplace.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Marketplace> marketplaceList = marketplaceRepository.findAll();
        assertThat(marketplaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
