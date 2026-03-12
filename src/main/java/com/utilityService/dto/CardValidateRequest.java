package com.utilityService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardValidateRequest {

	private String accountNumber;
	private String cardNumber;
	
}
