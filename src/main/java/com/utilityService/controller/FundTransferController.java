package com.utilityService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilityService.dto.CardValidateRequest;
import com.utilityService.dto.CardValidateResponse;
import com.utilityService.dto.DepositRequest;
import com.utilityService.dto.FundTransferRequest;
import com.utilityService.dto.WithdrawalRequest;
import com.utilityService.service.FundTransferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/utility/transfers")
@RequiredArgsConstructor
public class FundTransferController {
	
	private final FundTransferService fundTransferService;

	@PostMapping("/send")
	public ResponseEntity<String> transferFunds(@RequestBody FundTransferRequest fundTransferRequest) throws Exception {
		String message = fundTransferService.transferFunds(fundTransferRequest);
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/atm/cashWithdrawal")
	public ResponseEntity<String> withdrawMoney(@RequestBody WithdrawalRequest withdrawalRequest) throws Exception {
		String message = fundTransferService.withdrawMoney(withdrawalRequest);
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/atm/cashDeposit")
	public ResponseEntity<String> depositMoney(@RequestBody DepositRequest depositRequest) throws Exception {
		String message = fundTransferService.depositMoney(depositRequest);
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/atm/validate")
	public ResponseEntity<CardValidateResponse> validateCard(@RequestBody CardValidateRequest cardValidateRequest) {
		CardValidateResponse cardValidateResponse = fundTransferService.validateCard(cardValidateRequest);
		return new ResponseEntity<CardValidateResponse>(cardValidateResponse, HttpStatus.ACCEPTED);
	}
	
}
