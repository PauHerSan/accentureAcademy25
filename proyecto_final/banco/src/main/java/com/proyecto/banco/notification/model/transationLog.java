package com.proyecto.banco.notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;


@Document(collection = "transaction_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class transationLog {

    @Id
    private String id;

    private Long transactionId;

    private Long accountId;

    private String accountNumber;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private String status;

    private LocalDateTime timestamp;

    private Map<String, Object> metadata;

    private String description;
}
