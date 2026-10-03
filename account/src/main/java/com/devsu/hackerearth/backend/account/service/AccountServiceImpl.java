package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

    @Override
    public List<AccountDto> getAll() {
        return accountRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        return accountRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        Account account = mapToEntity(accountDto);
        Account saved = accountRepository.save(account);
        return mapToDto(saved);
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        if (accountDto == null || accountDto.getId() == null) {
            return null;
        }
        Account account = accountRepository.findById(accountDto.getId()).orElse(null);
        if (account == null) {
            return null;
        }
        account.setNumber(accountDto.getNumber());
        account.setType(accountDto.getType());
        account.setInitialAmount(accountDto.getInitialAmount());
        account.setActive(accountDto.isActive());
        account.setClientId(accountDto.getClientId());
        Account saved = accountRepository.save(account);
        return mapToDto(saved);
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return null;
        }
        account.setActive(partialAccountDto.isActive());
        Account saved = accountRepository.save(account);
        return mapToDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    private AccountDto mapToDto(Account account) {
        if (account == null) return null;
        return new AccountDto(
                account.getId(),
                account.getNumber(),
                account.getType(),
                account.getInitialAmount(),
                account.isActive(),
                account.getClientId()
        );
    }

    private Account mapToEntity(AccountDto dto) {
        if (dto == null) return null;
        Account account = new Account();
        if (dto.getId() != null) {
            account.setId(dto.getId());
        }
        account.setNumber(dto.getNumber());
        account.setType(dto.getType());
        account.setInitialAmount(dto.getInitialAmount());
        account.setActive(dto.isActive());
        account.setClientId(dto.getClientId());
        return account;
    }
}
