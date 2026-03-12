package com.utilityService.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepositRequest {
	
	private String accountNumber;
	private BigDecimal amount;

}
