package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public List<ClientDto> getAll() {
		return clientRepository.findAll().stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) {
		return clientRepository.findById(id)
				.map(this::mapToDto)
				.orElse(null);
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		Client client = mapToEntity(clientDto);
		Client saved = clientRepository.save(client);
		return mapToDto(saved);
	}

	@Override
	public ClientDto update(ClientDto clientDto) {
		if (clientDto == null || clientDto.getId() == null) {
			return null;
		}
		Client client = clientRepository.findById(clientDto.getId()).orElse(null);
		if (client == null) {
			return null;
		}
		client.setName(clientDto.getName());
		client.setDni(clientDto.getDni());
		client.setPassword(clientDto.getPassword());
		client.setGender(clientDto.getGender());
		client.setAge(clientDto.getAge());
		client.setAddress(clientDto.getAddress());
		client.setPhone(clientDto.getPhone());
		client.setActive(clientDto.isActive());
		Client saved = clientRepository.save(client);
		return mapToDto(saved);
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		Client client = clientRepository.findById(id).orElse(null);
		if (client == null) {
			return null;
		}
		client.setActive(partialClientDto.isActive());
		Client saved = clientRepository.save(client);
		return mapToDto(saved);
	}

	@Override
	public void deleteById(Long id) {
		clientRepository.deleteById(id);
	}

	private ClientDto mapToDto(Client client) {
		if (client == null) return null;
		return new ClientDto(
				client.getId(),
				client.getDni(),
				client.getName(),
				client.getPassword(),
				client.getGender(),
				client.getAge(),
				client.getAddress(),
				client.getPhone(),
				client.isActive()
		);
	}

	private Client mapToEntity(ClientDto dto) {
		if (dto == null) return null;
		Client client = new Client();
		if (dto.getId() != null) {
			client.setId(dto.getId());
		}
		client.setDni(dto.getDni());
		client.setName(dto.getName());
		client.setPassword(dto.getPassword());
		client.setGender(dto.getGender());
		client.setAge(dto.getAge());
		client.setAddress(dto.getAddress());
		client.setPhone(dto.getPhone());
		client.setActive(dto.isActive());
		return client;
	}
}
