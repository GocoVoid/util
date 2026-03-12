package com.utilityService.daoImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.utilityService.model.Transaction;
import com.utilityService.repo.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

	private final TransactionRepository transactionRepository;
	
	@Override
	public Optional<Transaction> saveTransaction(Transaction transaction) {
		Transaction savedTransaction = transactionRepository.save(transaction);
		log.info("TransactionDaoImpl: Saving Transaction details with Transaction Id "+transaction.getSno());
		return Optional.of(savedTransaction);
	}
	
	@Override
	public Optional<List<Transaction>> getAllTransactions() {
		List<Transaction> transactions = transactionRepository.findAll();
		return Optional.of(transactions);
	}
}
