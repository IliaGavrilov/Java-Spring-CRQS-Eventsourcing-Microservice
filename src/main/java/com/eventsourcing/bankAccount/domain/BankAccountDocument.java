package com.eventsourcing.bankAccount.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bankAccounts")
public class BankAccountDocument {

    @BsonProperty(value = "_id")
    private UUID id;

    @BsonProperty(value = "aggregateId")
    private UUID aggregateId;

    @BsonProperty(value = "email")
    private String email;

    @BsonProperty(value = "balance")
    private BigDecimal balance;

    @BsonProperty(value = "debit")
    private BigDecimal debit;

    @BsonProperty(value = "credit")
    private BigDecimal credit;

    @BsonProperty(value = "creditLine")
    private BigDecimal creditLine;

    @BsonProperty(value = "overdraftLimit")
    private BigDecimal overdraftLimit;

}
