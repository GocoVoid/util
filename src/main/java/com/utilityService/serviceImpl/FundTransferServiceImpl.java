package com.utilityService.serviceImpl;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utilityService.daoImpl.AccountDao;
import com.utilityService.daoImpl.TransactionDao;
import com.utilityService.dto.CardValidateRequest;
import com.utilityService.dto.CardValidateResponse;
import com.utilityService.dto.DepositRequest;
import com.utilityService.dto.FundTransferRequest;
import com.utilityService.dto.WithdrawalRequest;
import com.utilityService.exception.CardNotFoundException;
import com.utilityService.exception.InvalidBeneficiaryException;
import com.utilityService.model.Account;
import com.utilityService.model.Transaction;
import com.utilityService.service.FundTransferService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FundTransferServiceImpl implements FundTransferService {
    
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;
    private final ModelMapper mapper;

    @Override
    @Transactional 
    public String transferFunds(FundTransferRequest request) throws Exception {

        String fromAccountNo = request.getFromAccountNumber();
        String toAccountNo = request.getToAccountNumber();
        BigDecimal amount = request.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }
        if (fromAccountNo.equals(toAccountNo)) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account.");
        }
        
        Account senderAccount = accountDao.getAccount(fromAccountNo).get();
        Account receiverAccount = accountDao.getAccount(toAccountNo).get();
        
        if (!Objects.equals(senderAccount.getUserId(), receiverAccount.getUserId()) 
                || (!("ACTIVE".equals(senderAccount.getAccountStatus())) 
                || !("ACTIVE".equals(receiverAccount.getAccountStatus())))) {
            
            throw new InvalidBeneficiaryException("Unable to send money to A/c no. " + toAccountNo);
        }

        Account sourceAccount = accountDao.getAccount(fromAccountNo)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found."));

//        if (!accountValidationService.verifyTransactionPassword(sourceAccount.getUserId(), request.getTransactionPassword())) {
//            throw new IllegalArgumentException("Invalid transaction password.");
//        }

        if (sourceAccount.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in the source account.");
        }

        Account destAccount = accountDao.getAccount(toAccountNo)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found."));

        sourceAccount.setAvailableBalance(sourceAccount.getAvailableBalance().subtract(amount));
        destAccount.setAvailableBalance(destAccount.getAvailableBalance().add(amount));
        
        accountDao.saveAccountDetails(sourceAccount).orElseThrow(() -> new Exception("Unable to update Source Account Details"));
        accountDao.saveAccountDetails(destAccount).orElseThrow(() -> new Exception("Unable to update Destination Account Details"));
        

        String txnRef = "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Transaction debitTxn = new Transaction();
        debitTxn.setAccountNumber(fromAccountNo);
        debitTxn.setTransactionAmount(amount);
        debitTxn.setType("DR");
        debitTxn.setDate(LocalDateTime.now());
        debitTxn.setDescription("Fund Transfer to " + toAccountNo + " (Ref: " + txnRef + ") - " + request.getRemarks());
        debitTxn.setUserId(accountDao.getAccount(fromAccountNo).get().getUserId());
        transactionDao.saveTransaction(debitTxn).orElseThrow(() -> new Exception("Unable to update Source Account Transactions"));

        Transaction creditTxn = new Transaction();
        creditTxn.setAccountNumber(toAccountNo);
        creditTxn.setTransactionAmount(amount);
        creditTxn.setType("CR");
        creditTxn.setDate(LocalDateTime.now());
        creditTxn.setDescription("Fund Transfer from " + fromAccountNo + " (Ref: " + txnRef + ") - " + request.getRemarks());
        creditTxn.setUserId(accountDao.getAccount(toAccountNo).get().getUserId());
        transactionDao.saveTransaction(creditTxn).orElseThrow(() -> new Exception("Unable to update Destination Account Transactions"));

        return txnRef;
    }
    
    @Override
    @Transactional
    public String withdrawMoney(WithdrawalRequest withdrawalRequest) throws Exception {

    	String accountNumber = withdrawalRequest.getAccountNumber();
    	BigDecimal amount = withdrawalRequest.getAmount();
    
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        
        Account account = accountDao.getAccount(accountNumber).get();
    
        if (!account.getAccountStatus().equals("ACTIVE") || !account.getAccountType().equals("SELF")) {
            throw new InvalidBeneficiaryException("A/c no. "+accountNumber+" is not eligible for withdrawal. Contact Main Branch");
        }

        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in account.");
        }

        account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
        
        accountDao.saveAccountDetails(account).orElseThrow(() -> new Exception("Unable to update Account Details"));
        
        String txnRef = "CW" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Transaction debitTxn = new Transaction();
        debitTxn.setAccountNumber(accountNumber);
        debitTxn.setTransactionAmount(amount);
        debitTxn.setType("DR");
        debitTxn.setDate(LocalDateTime.now());
        debitTxn.setDescription("ATM Withdrawal of Rs. "+amount+" from " + accountNumber + " (Ref: " + txnRef + ")");
        debitTxn.setUserId(accountDao.getAccount(accountNumber).get().getUserId());
        transactionDao.saveTransaction(debitTxn).orElseThrow(() -> new Exception("Unable to update Source Account Transactions"));

        return txnRef;
    }
    
    @Override
    @Transactional
    public String depositMoney(DepositRequest depositRequest) throws Exception {

    	String accountNumber = depositRequest.getAccountNumber();
    	BigDecimal amount = depositRequest.getAmount();
    	
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        
        Account account = accountDao.getAccount(accountNumber).get();
        
        if (!account.getAccountStatus().equals("ACTIVE") || !account.getAccountType().equals("SELF")) {
            throw new InvalidBeneficiaryException("A/c no. "+accountNumber+" is not verified. Contact Main Branch");
        }

        account.setAvailableBalance(account.getAvailableBalance().add(amount));
        
        accountDao.saveAccountDetails(account).orElseThrow(() -> new Exception("Unable to update Account Details"));
        
        String txnRef = "CD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Transaction creditTxn = new Transaction();
        creditTxn.setAccountNumber(accountNumber);
        creditTxn.setTransactionAmount(amount);
        creditTxn.setType("CR");
        creditTxn.setDate(LocalDateTime.now());
        creditTxn.setDescription("ATM Cash Deposit of Rs. "+amount+" into " + accountNumber + " (Ref: " + txnRef + ")");
        creditTxn.setUserId(accountDao.getAccount(accountNumber).get().getUserId());
        transactionDao.saveTransaction(creditTxn).orElseThrow(() -> new Exception("Unable to update Account Transactions"));

        return txnRef;
    }

	@Override
	public CardValidateResponse validateCard(CardValidateRequest cardValidateRequest) {
		// TODO Auto-generated method stub
		String cardNumber = cardValidateRequest.getCardNumber();
		
		Optional<Account> linkedAccount = accountDao.getAccountByCard(cardNumber);
		if (linkedAccount.isPresent()) {
			CardValidateResponse cardValidateResponse = mapper.map(linkedAccount, CardValidateResponse.class);
			return cardValidateResponse;
		} else {
			throw new CardNotFoundException("Card no. "+cardNumber+" is not linked with your Account");
		}	
		
	}

}
