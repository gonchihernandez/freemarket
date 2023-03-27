package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarketplaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marketplace.class);
        Marketplace marketplace1 = new Marketplace();
        marketplace1.setId(1L);
        Marketplace marketplace2 = new Marketplace();
        marketplace2.setId(marketplace1.getId());
        assertThat(marketplace1).isEqualTo(marketplace2);
        marketplace2.setId(2L);
        assertThat(marketplace1).isNotEqualTo(marketplace2);
        marketplace1.setId(null);
        assertThat(marketplace1).isNotEqualTo(marketplace2);
    }
}
