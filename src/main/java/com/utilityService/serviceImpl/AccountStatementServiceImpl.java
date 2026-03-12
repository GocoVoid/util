package com.utilityService.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.utilityService.daoImpl.AccountStatementDao;
import com.utilityService.daoImpl.TransactionDao;
import com.utilityService.dto.TransactionResponse;
import com.utilityService.exception.InvalidDateRangeException;
import com.utilityService.exception.UnableToFetchTransactionsException;
import com.utilityService.model.Transaction;
import com.utilityService.service.AccountStatementService;
import com.utilityService.service.AccountValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountStatementServiceImpl implements AccountStatementService {

	private final AccountStatementDao accountStatementDao;
	private final AccountValidationService accountValidationService;
	private final TransactionDao transactionDao;
	private final ModelMapper mapper;
	
	@Override
	public List<TransactionResponse> getAccountStatement(String accountNumber, LocalDate fromDate, LocalDate toDate) throws AccountNotFoundException {
		
		log.info("AccountStatementServiceImpl: Checking Existense of A/c no. "+accountNumber);
		if (!accountValidationService.doesAccountExist(accountNumber)) {
			log.error("AccountStatementServiceImpl: A/c no. "+accountNumber+" does not exist");
            throw new AccountNotFoundException("Account Number " + accountNumber+" Not Found");
        }

		log.info("AccountStatementServiceImpl: Checking the date range");
        if (fromDate.isAfter(toDate)) {
        	log.error("AccountStatementServiceImpl: Date range is invalid");
            throw new InvalidDateRangeException("Invalid date range provided.");
        }

        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(LocalTime.MAX);

        Optional<List<Transaction>> transactions = accountStatementDao.getAccountStatement(accountNumber, startDateTime, endDateTime);

        if (transactions.isPresent()) {
	        return transactions.get().stream()
	                .map((transaction) -> mapper.map(transaction, TransactionResponse.class))
	                .collect(Collectors.toList());
        } else {
        	log.error("AccountStatementServiceImpl: Unable to fetch transactions for A/c no. "+accountNumber);
        	throw new UnableToFetchTransactionsException("Unable to fetch transactions for A/c no. "+accountNumber);
        }
	}
	
	@Override
	public List<TransactionResponse> getAccountStatementByUserId(Long userId) {
		Optional<List<Transaction>> transactions = accountStatementDao.getAccountStatementById(userId);
		if (transactions.isPresent()) {
	        return transactions.get().stream()
	                .map((transaction) -> mapper.map(transaction, TransactionResponse.class))
	                .collect(Collectors.toList());
        } else {
        	log.error("AccountStatementServiceImpl: Unable to fetch transactions");
        	throw new UnableToFetchTransactionsException("Unable to fetch transactions");
        }
	}
	
	@Override
	public List<TransactionResponse> getAccountStatementByAccountNumber(String accountNumber) {
		Optional<List<Transaction>> transactions = accountStatementDao.getAccountStatementByAccountNumber(accountNumber);
		if (transactions.isPresent()) {
	        return transactions.get().stream()
	                .map((transaction) -> mapper.map(transaction, TransactionResponse.class))
	                .collect(Collectors.toList());
        } else {
        	log.error("AccountStatementServiceImpl: Unable to fetch transactions for A/c no. "+accountNumber);
        	throw new UnableToFetchTransactionsException("Unable to fetch transactions for A/c no. "+accountNumber);
        }
	}
	
	@Override
	public List<TransactionResponse> getAllTransactions() {
		Optional<List<Transaction>> transactions = transactionDao.getAllTransactions();
		if (transactions.isPresent()) {
			return transactions.get().stream()
	                .map((transaction) -> mapper.map(transaction, TransactionResponse.class))
	                .collect(Collectors.toList());
		} else {
			log.error("AccountStatementServiceImpl: Unable to fetch transactions");
        	throw new UnableToFetchTransactionsException("Unable to fetch transactions");
		}
	}
	
}
