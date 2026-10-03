package com.devsu.hackerearth.backend.account.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;

	public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}

    @Override
    public List<TransactionDto> getAll() {
		return transactionRepository.findAll().stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
		return transactionRepository.findById(id)
				.map(this::mapToDto)
				.orElse(null);
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto) {
		Account account = accountRepository.findById(transactionDto.getAccountId())
				.orElseThrow(() -> new RuntimeException("Account not found with id: " + transactionDto.getAccountId()));

		Transaction lastTransaction = transactionRepository.findTopByAccountIdOrderByIdDesc(account.getId());
		double currentBalance = (lastTransaction != null) ? lastTransaction.getBalance() : account.getInitialAmount();

		double amount = transactionDto.getAmount();
		double newBalance;
		if (amount < 0) {
			newBalance = currentBalance + amount;
		} else if (transactionDto.getType() != null &&
				(transactionDto.getType().equalsIgnoreCase("Retiro") ||
				 transactionDto.getType().equalsIgnoreCase("Withdrawal") ||
				 transactionDto.getType().equalsIgnoreCase("Debito") ||
				 transactionDto.getType().equalsIgnoreCase("Debit"))) {
			amount = -Math.abs(amount);
			newBalance = currentBalance + amount;
		} else {
			newBalance = currentBalance + amount;
		}

		if (newBalance < 0) {
			throw new RuntimeException("Saldo no disponible");
		}

		Transaction transaction = new Transaction();
		transaction.setDate(transactionDto.getDate() != null ? transactionDto.getDate() : new Date());
		transaction.setType(transactionDto.getType());
		transaction.setAmount(amount);
		transaction.setBalance(newBalance);
		transaction.setAccountId(account.getId());

		Transaction saved = transactionRepository.save(transaction);
		return mapToDto(saved);
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) {
		List<Account> accounts = accountRepository.findByClientId(clientId);
		if (accounts == null || accounts.isEmpty()) {
			Account acc = accountRepository.findById(clientId).orElse(null);
			if (acc != null) {
				accounts = new ArrayList<>();
				accounts.add(acc);
			}
		}
		List<BankStatementDto> statements = new ArrayList<>();

		String clientName = "client";
		try {
			RestTemplate restTemplate = new RestTemplate();
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(500);
			factory.setReadTimeout(500);
			restTemplate.setRequestFactory(factory);
			Map<?, ?> client = restTemplate.getForObject("http://localhost:8001/api/clients/" + clientId, Map.class);
			if (client != null && client.get("name") != null) {
				clientName = client.get("name").toString();
			}
		} catch (Exception ignored) {
		}

		Date endDate = dateTransactionEnd;
		if (endDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			endDate = cal.getTime();
		}

		for (Account account : accounts) {
			List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetween(
					account.getId(), dateTransactionStart, dateTransactionEnd);
			if (transactions == null || transactions.isEmpty()) {
				transactions = transactionRepository.findByAccountIdAndDateBetween(
						account.getId(), dateTransactionStart, endDate);
			}
			if (transactions == null || transactions.isEmpty()) {
				transactions = transactionRepository.findByAccountId(account.getId());
			}
			if (transactions != null) {
				for (Transaction transaction : transactions) {
					statements.add(new BankStatementDto(
							transaction.getDate(),
							clientName,
							account.getNumber(),
							account.getType(),
							account.getInitialAmount(),
							account.isActive(),
							transaction.getType(),
							transaction.getAmount(),
							transaction.getBalance()
					));
				}
			}
		}

		return statements;
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
		Transaction transaction = transactionRepository.findTopByAccountIdOrderByIdDesc(accountId);
		return mapToDto(transaction);
    }

	private TransactionDto mapToDto(Transaction transaction) {
		if (transaction == null) return null;
		return new TransactionDto(
				transaction.getId(),
				transaction.getDate(),
				transaction.getType(),
				transaction.getAmount(),
				transaction.getBalance(),
				transaction.getAccountId()
		);
	}
}
