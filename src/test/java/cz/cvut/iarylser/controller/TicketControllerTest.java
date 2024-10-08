//package cz.cvut.iarylser.unit.controller;
//
//import cz.cvut.iarylser.controller.TicketController;
//import cz.cvut.iarylser.dao.DTO.TicketDTO;
//import cz.cvut.iarylser.dao.entity.Ticket;
//import cz.cvut.iarylser.facade.TicketFacadeImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// todo fix
//@WebMvcTest(TicketController.class)
//class TicketControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TicketFacadeImpl ticketFacade;
//
//    @BeforeEach
//    void setUp() {
//        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getTicketByIdSucceeded() throws Exception {
//        Long ticketId = 1L;
//        TicketDTO mockTicketDTO = new TicketDTO();
//        mockTicketDTO.setId(ticketId);
//
//        Mockito.when(ticketFacade.getById(ticketId)).thenReturn(mockTicketDTO);
//
//        mockMvc.perform(get("/tickets/" + ticketId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(ticketId));
//
//    }
//    @Test
//    void getTicketByIdFailure() throws Exception {
//        Long ticketId = 1L;
//        when(ticketFacade.getById(ticketId)).thenReturn(null);
//
//        mockMvc.perform(get("/tickets/{ticketId}", ticketId))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void getTicketByUserSucceeded() throws Exception{
//        Long userId = 1L;
//        TicketDTO mockTicket = new TicketDTO();
//        mockTicket.setCustomerId(userId);
//
//        Ticket ticket = new Ticket();
//        ticket.setCustomerId(userId);
//
//        Mockito.when(ticketFacade.getByUserId(userId)).thenReturn(List.of(mockTicket));
//
//        mockMvc.perform(get("/tickets/user/" + userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].customerId").value(userId));
//
//    }
//
//    @Test
//    void getTicketByUserFailure() throws Exception {
//        Long userId = 1L;
//        when(ticketFacade.getByUserId(userId)).thenReturn(new ArrayList<>());
//
//        mockMvc.perform(get("/tickets/user/{userId}", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//    }
//}