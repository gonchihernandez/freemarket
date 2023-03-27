package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarketplaceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketplaceDTO.class);
        MarketplaceDTO marketplaceDTO1 = new MarketplaceDTO();
        marketplaceDTO1.setId(1L);
        MarketplaceDTO marketplaceDTO2 = new MarketplaceDTO();
        assertThat(marketplaceDTO1).isNotEqualTo(marketplaceDTO2);
        marketplaceDTO2.setId(marketplaceDTO1.getId());
        assertThat(marketplaceDTO1).isEqualTo(marketplaceDTO2);
        marketplaceDTO2.setId(2L);
        assertThat(marketplaceDTO1).isNotEqualTo(marketplaceDTO2);
        marketplaceDTO1.setId(null);
        assertThat(marketplaceDTO1).isNotEqualTo(marketplaceDTO2);
    }
}
