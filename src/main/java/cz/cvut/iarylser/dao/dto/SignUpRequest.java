package cz.cvut.iarylser.dao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotNull
    @Length(min = 3, max = 30)
    private String username;
    @NotNull
    @Length(min = 8, max = 30)
    private String password;
    @Email
    private String email;
    @Min(1)
    @Max(100)
    private int age;
}
