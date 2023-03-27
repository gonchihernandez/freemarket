package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.service.MarketplaceService;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import com.mycompany.myapp.service.mapper.MarketplaceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Marketplace}.
 */
@Service
@Transactional
public class MarketplaceServiceImpl implements MarketplaceService {

    private final Logger log = LoggerFactory.getLogger(MarketplaceServiceImpl.class);

    private final MarketplaceRepository marketplaceRepository;

    private final MarketplaceMapper marketplaceMapper;

    public MarketplaceServiceImpl(MarketplaceRepository marketplaceRepository, MarketplaceMapper marketplaceMapper) {
        this.marketplaceRepository = marketplaceRepository;
        this.marketplaceMapper = marketplaceMapper;
    }

    @Override
    public MarketplaceDTO save(MarketplaceDTO marketplaceDTO) {
        log.debug("Request to save Marketplace : {}", marketplaceDTO);
        Marketplace marketplace = marketplaceMapper.toEntity(marketplaceDTO);
        marketplace = marketplaceRepository.save(marketplace);
        return marketplaceMapper.toDto(marketplace);
    }

    @Override
    public MarketplaceDTO update(MarketplaceDTO marketplaceDTO) {
        log.debug("Request to update Marketplace : {}", marketplaceDTO);
        Marketplace marketplace = marketplaceMapper.toEntity(marketplaceDTO);
        marketplace = marketplaceRepository.save(marketplace);
        return marketplaceMapper.toDto(marketplace);
    }

    @Override
    public Optional<MarketplaceDTO> partialUpdate(MarketplaceDTO marketplaceDTO) {
        log.debug("Request to partially update Marketplace : {}", marketplaceDTO);

        return marketplaceRepository
            .findById(marketplaceDTO.getId())
            .map(existingMarketplace -> {
                marketplaceMapper.partialUpdate(existingMarketplace, marketplaceDTO);

                return existingMarketplace;
            })
            .map(marketplaceRepository::save)
            .map(marketplaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceDTO> findAll() {
        log.debug("Request to get all Marketplaces");
        return marketplaceRepository.findAll().stream().map(marketplaceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarketplaceDTO> findOne(Long id) {
        log.debug("Request to get Marketplace : {}", id);
        return marketplaceRepository.findById(id).map(marketplaceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Marketplace : {}", id);
        marketplaceRepository.deleteById(id);
    }
}
