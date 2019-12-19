package com.cluster.warehouse.service.dealpool;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.repository.DealRepository;
import com.cluster.warehouse.repository.InvalidDealRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class JoiningComponent {

    private final DealRepository dealRepository;
    private final InvalidDealRepository invalidDealRepository;

    public JoiningComponent(DealRepository dealRepository, InvalidDealRepository invalidDealRepository) {
        this.dealRepository = dealRepository;
        this.invalidDealRepository = invalidDealRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void executeBatch(List<Deal> deals) {

        for (Deal deal : deals) {
            try {
                dealRepository.save(deal);
            } catch (Exception ex) {
                InvalidDeal invalidDeal = new InvalidDeal(deal, ex.getMessage());
                try {
                    invalidDealRepository.save(invalidDeal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
