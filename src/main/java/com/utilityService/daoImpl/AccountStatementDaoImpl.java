package com.utilityService.daoImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.utilityService.model.Transaction;
import com.utilityService.repo.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountStatementDaoImpl implements AccountStatementDao {

	private final TransactionRepository transactionRepository;
	
	@Override
	public Optional<List<Transaction>> getAccountStatement(String accountNumber, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndDateBetween(accountNumber, startDateTime, endDateTime);
		log.info("AccountStatementDaoImpl: Fetching date customized account statement using getAccountStatement() for A/c no. "+accountNumber);
		return Optional.of(transactions);
	}
	
	@Override
	public Optional<List<Transaction>> getAccountStatementById(Long userId) {
		List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
		log.info("AccountStatementDaoImpl: Fetching User specified account statement using getAccountStatementById() for User Id "+userId);
		return Optional.of(transactions);
	}
	
	@Override
	public Optional<List<Transaction>> getAccountStatementByAccountNumber(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findAllByAccountNumberOrderByDateDesc(accountNumber);
		log.info("AccountStatementDaoImpl: Fetching Account number specific account statement using getAccountStatementByAccountNumber() for A/c no. "+accountNumber);
		return Optional.of(transactions);
	}
	
}
