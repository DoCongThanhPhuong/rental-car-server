package net.codejava.domain.mapper.impl;

import java.util.Date;

import org.springframework.stereotype.Component;

import net.codejava.domain.dto.transaction.AddSystemTransactionRequestDTO;
import net.codejava.domain.dto.transaction.TransactionResponseDTO;
import net.codejava.domain.entity.Transaction;
import net.codejava.domain.mapper.TransactionMapper;

@Component
public class TransactionMapperImpl implements TransactionMapper {
    @Override
    public Transaction addSystemTransactionRequestToEntity(AddSystemTransactionRequestDTO requestDTO) {
        return Transaction.builder()
                .amount(requestDTO.amount())
                .transactionType(requestDTO.transactionType())
                .createdAt(new Date())
                .bookingId(requestDTO.bookingId())
                .carName(requestDTO.carName())
                .user(requestDTO.user())
                .build();
    }

    @Override
    public TransactionResponseDTO toTransactionResponseDTO(Transaction entity) {
        return TransactionResponseDTO.builder()
                .amount(entity.getAmount())
                .transactionType(entity.getTransactionType())
                .transactionTypeTitle(entity.getTransactionType().getTitle())
                .createdAt(entity.getCreatedAt())
                .bookingId(entity.getBookingId())
                .carName(entity.getCarName())
                .build();
    }
}
