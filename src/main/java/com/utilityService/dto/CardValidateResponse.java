package com.utilityService.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardValidateResponse {

	private Long userId;
	private String cardNumber;
	private String accountNumber;
	private String ifscCode;
	private String branchName;
	private BigDecimal availableBalance;
	private String accountStatus;
	
}
