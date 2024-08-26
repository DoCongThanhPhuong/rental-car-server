package net.codejava.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.MetaConstant;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.dto.meta.SortingDTO;
import net.codejava.domain.dto.transaction.AddSystemTransactionRequestDTO;
import net.codejava.domain.dto.transaction.FilterTransactionByTimeRequestDTO;
import net.codejava.domain.dto.transaction.TransactionResponseDTO;
import net.codejava.domain.entity.Transaction;
import net.codejava.domain.entity.User;
import net.codejava.domain.mapper.TransactionMapper;
import net.codejava.exceptions.AppException;
import net.codejava.repository.TransactionRepository;
import net.codejava.repository.UserRepository;
import net.codejava.responses.MetaResponse;
import net.codejava.service.TransactionService;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final TransactionMapper transactionMapper;

    @Override
    public MetaResponse<MetaResponseDTO, List<TransactionResponseDTO>> getListByUserId(
            Integer userId, MetaRequestDTO metaRequestDTO, FilterTransactionByTimeRequestDTO filterRequestDTO) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not empty");
        String sortField = metaRequestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "transaction_id";
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);

        String endTime = filterRequestDTO.endTime();
        if (endTime != null) endTime += " 23:59";
        Page<Transaction> page =
                transactionRepo.getListByUserId(userId, filterRequestDTO.startTime(), endTime, pageable);
        if (page.isEmpty()) throw new AppException("Transaction list is empty");
        List<TransactionResponseDTO> liRes = page.getContent().stream()
                .map(temp -> transactionMapper.toTransactionResponseDTO(temp))
                .toList();
        return MetaResponse.successfulResponse(
                "Get list transaction success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(metaRequestDTO.currentPage())
                        .pageSize(metaRequestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(metaRequestDTO.sortField())
                                .sortDir(metaRequestDTO.sortDir())
                                .build())
                        .build(),
                liRes);
    }

    @Override
    @Transactional
    public Transaction addTransaction(AddSystemTransactionRequestDTO requestDTO) {
        try {
            Transaction newTransaction = transactionMapper.addSystemTransactionRequestToEntity(requestDTO);
            transactionRepo.save(newTransaction);
            return newTransaction;
        } catch (Exception e) {
            throw new AppException("Add new transaction unsuccessfully");
        }
    }
}
