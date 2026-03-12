package com.utilityService.service;

import com.utilityService.dto.CardValidateRequest;
import com.utilityService.dto.CardValidateResponse;
import com.utilityService.dto.DepositRequest;
import com.utilityService.dto.FundTransferRequest;
import com.utilityService.dto.WithdrawalRequest;

public interface FundTransferService {

    String transferFunds(FundTransferRequest request) throws Exception;

	String withdrawMoney(WithdrawalRequest withdrawalRequest) throws Exception;

	String depositMoney(DepositRequest depositRequest) throws Exception;

	CardValidateResponse validateCard(CardValidateRequest cardValidateRequest);

    //boolean isBeneficiaryActiveAndValid(Long customerId, String beneficiaryAccountNo);
}
