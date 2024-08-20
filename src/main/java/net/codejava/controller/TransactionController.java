package net.codejava.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.codejava.constant.Endpoint;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.dto.transaction.FilterTransactionByTimeRequestDTO;
import net.codejava.domain.dto.transaction.TransactionResponseDTO;
import net.codejava.responses.MetaResponse;
import net.codejava.service.TransactionService;
import net.codejava.utility.AuthUtil;

@Tag(name = "Transaction Controller", description = "APIs related to Transaction operations")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping(Endpoint.V1.Transaction.GET_LIST)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>>> getListByUserId(
            @ParameterObject MetaRequestDTO metaRequestDTO,
            @ParameterObject FilterTransactionByTimeRequestDTO filterRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getListByUserId(
                        AuthUtil.getRequestedUser().getId(), metaRequestDTO, filterRequestDTO));
    }
}
