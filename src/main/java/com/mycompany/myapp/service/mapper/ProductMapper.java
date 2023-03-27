package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import com.mycompany.myapp.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "marketplace", source = "marketplace", qualifiedByName = "marketplaceName")
    ProductDTO toDto(Product s);

    @Named("marketplaceName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MarketplaceDTO toDtoMarketplaceName(Marketplace marketplace);
}
