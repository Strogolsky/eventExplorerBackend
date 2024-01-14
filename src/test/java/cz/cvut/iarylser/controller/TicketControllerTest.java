package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTicketById() throws Exception {
        Long ticketId = 1L;
        TicketDTO mockTicket = new TicketDTO();
        mockTicket.setId(ticketId);

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        Mockito.when(ticketService.getTicketById(ticketId)).thenReturn(ticket);
        Mockito.when(ticketService.convertToDto(Mockito.any(Ticket.class))).thenReturn(mockTicket);

        mockMvc.perform(get("/ticket/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ticketId));

    }

    @Test
    void getTicketByUser() throws Exception{
        Long userId = 1L;
        TicketDTO mockTicket = new TicketDTO();
        mockTicket.setIdCustomer(userId);

        Ticket ticket = new Ticket();
        ticket.setIdCustomer(userId);

        Mockito.when(ticketService.getTicketByUser(userId)).thenReturn(List.of(ticket));
        Mockito.when(ticketService.convertTicketsToDTOs(Mockito.anyList())).thenReturn(List.of(mockTicket));

        mockMvc.perform(get("/ticket/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idCustomer").value(userId));

    }
}