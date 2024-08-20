package net.codejava.domain.dto.transaction;

import java.util.List;

import lombok.Builder;

@Builder
public record WalletResponseDTO(Double wallet, List<TransactionResponseDTO> transactionList) {}
