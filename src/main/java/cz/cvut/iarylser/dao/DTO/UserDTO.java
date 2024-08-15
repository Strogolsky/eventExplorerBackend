package cz.cvut.iarylser.dao.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private int age;
    private String email;
    private String firstName;
    private String lastName;
    private String description;
}

