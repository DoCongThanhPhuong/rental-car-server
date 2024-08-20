package net.codejava.service;

import java.util.List;

import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.dto.transaction.AddSystemTransactionRequestDTO;
import net.codejava.domain.dto.transaction.FilterTransactionByTimeRequestDTO;
import net.codejava.domain.dto.transaction.TransactionResponseDTO;
import net.codejava.domain.entity.Transaction;
import net.codejava.responses.MetaResponse;

public interface TransactionService {
    MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>> getListByUserId(
            Integer userId, MetaRequestDTO metaRequestDTO, FilterTransactionByTimeRequestDTO filterRequestDTO);

    Transaction addTransaction(AddSystemTransactionRequestDTO requestDTO);
}
