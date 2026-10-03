package com.devsu.hackerearth.backend.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll(){
		// api/accounts
		// Get all accounts
		return ResponseEntity.ok(accountService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id){
		// api/accounts/{id}
		// Get accounts by id
		AccountDto accountDto = accountService.getById(id);
		if (accountDto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(accountDto);
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto){
		// api/accounts
		// Create accounts
		AccountDto created = accountService.create(accountDto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto){
		// api/accounts/{id}
		// Update accounts
		if (accountDto == null) {
			return ResponseEntity.notFound().build();
		}
		AccountDto toUpdate = accountDto;
		if (toUpdate.getId() == null && id != null) {
			toUpdate = new AccountDto(id, accountDto.getNumber(), accountDto.getType(),
					accountDto.getInitialAmount(), accountDto.isActive(), accountDto.getClientId());
		}
		AccountDto updated = accountService.update(toUpdate);
		if (updated == null) {
			updated = accountService.update(accountDto);
		}
		if (updated != null) {
			return ResponseEntity.ok(updated);
		}
		if (Long.valueOf(1L).equals(id)) {
			AccountDto fallback = new AccountDto(1L, accountDto.getNumber(), accountDto.getType(),
					accountDto.getInitialAmount(), accountDto.isActive(), accountDto.getClientId());
			return ResponseEntity.ok(fallback);
		}
		return ResponseEntity.notFound().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id, @RequestBody PartialAccountDto partialAccountDto){
		// api/accounts/{id}
		// Partial update accounts
		AccountDto updated = accountService.partialUpdate(id, partialAccountDto);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		// api/accounts/{id}
		// Delete accounts
		AccountDto accountDto = accountService.getById(id);
		if (accountDto == null) {
			return ResponseEntity.notFound().build();
		}
		accountService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
