package net.codejava.domain.mapper;

import net.codejava.domain.dto.transaction.AddSystemTransactionRequestDTO;
import net.codejava.domain.dto.transaction.TransactionResponseDTO;
import net.codejava.domain.entity.Transaction;

public interface TransactionMapper {
    Transaction addSystemTransactionRequestToEntity(AddSystemTransactionRequestDTO requestDTO);

    TransactionResponseDTO toTransactionResponseDTO(Transaction entity);
}
