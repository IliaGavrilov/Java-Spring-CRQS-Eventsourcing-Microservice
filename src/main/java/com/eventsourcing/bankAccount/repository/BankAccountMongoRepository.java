package com.eventsourcing.bankAccount.repository;

import com.eventsourcing.bankAccount.domain.BankAccountDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountMongoRepository extends MongoRepository<BankAccountDocument, String> {

    Optional<BankAccountDocument> findByAggregateId(UUID aggregateId);

    void deleteByAggregateId(UUID aggregateId);

    Page<BankAccountDocument> findByBalanceLessThan(BigDecimal balance, Pageable pageable);
}
