package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketplaceMapperTest {

    private MarketplaceMapper marketplaceMapper;

    @BeforeEach
    public void setUp() {
        marketplaceMapper = new MarketplaceMapperImpl();
    }
}
