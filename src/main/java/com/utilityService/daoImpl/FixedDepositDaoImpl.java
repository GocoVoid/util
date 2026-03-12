package com.utilityService.daoImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.utilityService.model.FixedDeposit;
import com.utilityService.repo.FixedDepositRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FixedDepositDaoImpl implements FixedDepositDao {
	
	private final FixedDepositRepository fixedDepositRepository;
	
	@Override
	public Optional<FixedDeposit> saveFdDetails(FixedDeposit fixedDeposit) {
		FixedDeposit fd = fixedDepositRepository.save(fixedDeposit);
		log.info("FixedDepositDaoImpl : Generating new Fixed Deposit account for Funding A/c no. "+fixedDeposit.getFundingAccountNumber());
		return Optional.of(fd);
	}
	
	@Override
	public Optional<List<FixedDeposit>> findFundingAccountNumber(String accountNumber) {
		List<FixedDeposit> fdList = fixedDepositRepository.findAllByFundingAccountNumber(accountNumber);
		log.info("FixedDepositDaoImpl : Finding Fixed Deposit accounts using A/c no. "+accountNumber);
		return Optional.of(fdList);
	}

}
