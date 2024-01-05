package cz.cvut.iarylser.dao.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketPurchaseRequest {
    private int quantity;
    private String customer;
}
