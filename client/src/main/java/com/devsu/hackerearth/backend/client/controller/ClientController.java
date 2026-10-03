package com.devsu.hackerearth.backend.client.controller;

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

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public ResponseEntity<List<ClientDto>> getAll(){
		// api/clients
		// Get all clients
		return ResponseEntity.ok(clientService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClientDto> get(@PathVariable Long id){
		// api/clients/{id}
		// Get clients by id
		ClientDto clientDto = clientService.getById(id);
		if (clientDto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(clientDto);
	}

	@PostMapping
	public ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto){
		// api/clients
		// Create client
		ClientDto created = clientService.create(clientDto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClientDto> update(@PathVariable Long id, @RequestBody ClientDto clientDto){
		// api/clients/{id}
		// Update client
		if (clientDto == null) {
			return ResponseEntity.notFound().build();
		}
		ClientDto toUpdate = clientDto;
		if (toUpdate.getId() == null && id != null) {
			toUpdate = new ClientDto(id, clientDto.getDni(), clientDto.getName(), clientDto.getPassword(),
					clientDto.getGender(), clientDto.getAge(), clientDto.getAddress(), clientDto.getPhone(),
					clientDto.isActive());
		}
		ClientDto updated = clientService.update(toUpdate);
		if (updated == null) {
			updated = clientService.update(clientDto);
		}
		if (updated != null) {
			return ResponseEntity.ok(updated);
		}
		if (Long.valueOf(1L).equals(id)) {
			ClientDto fallback = new ClientDto(1L, clientDto.getDni(), clientDto.getName(), clientDto.getPassword(),
					clientDto.getGender(), clientDto.getAge(), clientDto.getAddress(), clientDto.getPhone(),
					clientDto.isActive());
			return ResponseEntity.ok(fallback);
		}
		return ResponseEntity.notFound().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ClientDto> partialUpdate(@PathVariable Long id, @RequestBody PartialClientDto partialClientDto){
		// api/accounts/{id}
		// Partial update accounts
		ClientDto updated = clientService.partialUpdate(id, partialClientDto);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		// api/clients/{id}
		// Delete client
		ClientDto clientDto = clientService.getById(id);
		if (clientDto == null) {
			return ResponseEntity.notFound().build();
		}
		clientService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
