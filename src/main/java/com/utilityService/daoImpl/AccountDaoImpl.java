package com.utilityService.daoImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.utilityService.model.Account;
import com.utilityService.repo.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDaoImpl implements AccountDao {
	
	private final AccountRepository accountRepository;
	
	@Override
	public Optional<Account> getAccount(String accountNumber) {
		log.info("AccountDaoImpl: Fetching Account Details using getAccount() for A/c no. "+accountNumber);
		return accountRepository.findById(accountNumber);
	}
	
	@Override
	public Optional<Boolean> existsById(String accountNumber) {
		Boolean result = accountRepository.existsById(accountNumber);
		log.info("AccountDaoImpl: Checking presence of account using existsById() for A/c no. "+accountNumber);
		return Optional.of(result);
	}
	
	@Override
	public Optional<Account> saveAccountDetails(Account account) {
		Account savedAccount = accountRepository.save(account);
		log.info("AccountDaoImpl: Saving account details using saveAccountDetails() for A/c no. "+account.getAccountNumber());
		return Optional.of(savedAccount);
	}

	@Override
	public Optional<Account> getAccountByCard(String cardNumber) {
		Optional<Account> linkedAccount = accountRepository.findByCardNumber(cardNumber);
		log.info("AccountDaoImpl: Fetching account detail using getAccountByCard() for Card no. "+cardNumber);
		return linkedAccount;
	}

}
