package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

@SpringBootTest
public class sampleTest {

	private ClientService clientService = mock(ClientService.class);
	private ClientController clientController = new ClientController(clientService);

	@Autowired
	private ClientService integrationClientService;

    @Test
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ClientDto createdClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        when(clientService.create(newClient)).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClient);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClient, response.getBody());
    }

    // F5: Prueba unitaria para la entidad de dominio Client
    @Test
    void clientEntityUnitTest() {
        Client client = new Client();
        client.setId(1L);
        client.setDni("1712345678");
        client.setName("Jose Lema");
        client.setPassword("1234");
        client.setGender("Masculino");
        client.setAge(32);
        client.setAddress("Otavalo");
        client.setPhone("098254785");
        client.setActive(true);

        assertEquals(1L, client.getId());
        assertEquals("1712345678", client.getDni());
        assertEquals("Jose Lema", client.getName());
        assertEquals("1234", client.getPassword());
        assertEquals("Masculino", client.getGender());
        assertEquals(32, client.getAge());
        assertEquals("Otavalo", client.getAddress());
        assertEquals("098254785", client.getPhone());
        assertTrue(client.isActive());
    }

    // F6: Prueba de integracion
    @Test
    void clientIntegrationTest() {
        ClientDto newClient = new ClientDto(null, "1798765432", "Maria Lopez", "secret", "Femenino", 28, "Quito", "099123456", true);
        ClientDto created = integrationClientService.create(newClient);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Maria Lopez", created.getName());

        ClientDto found = integrationClientService.getById(created.getId());
        assertNotNull(found);
        assertEquals("1798765432", found.getDni());
    }
}
