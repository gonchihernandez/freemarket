package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.service.dto.MarketplaceWithProductsDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Marketplace} and its DTO {@link MarketplaceWithProductsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MarketplaceWithProductsMapper extends EntityMapper<MarketplaceWithProductsDTO, Marketplace> {}
