package com.eventsourcing;

import com.eventsourcing.bankAccount.dto.CreateBankAccountRequestDTO;
import com.eventsourcing.bankAccount.dto.DepositAmountRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BankAccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Order(1)
	UUID testCreateBankAccount() throws Exception {
		final CreateBankAccountRequestDTO request = new CreateBankAccountRequestDTO("test@example.com");
		final MvcResult mvcResult = mockMvc.perform(post("/api/account/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();
		final UUID id = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UUID.class);
		assertNotNull(id);
		return id;
	}

	@Test
	@Order(2)
	void testDepositAmount() throws Exception {
		final UUID aggregateId = testCreateBankAccount();
		final DepositAmountRequestDTO request = new DepositAmountRequestDTO(BigDecimal.valueOf(100));
		mockMvc.perform(post("/api/account/deposit/" + aggregateId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk());
	}
}

