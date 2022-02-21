package com.softkit.dto;

import com.softkit.annotation.ValidPassword;
import com.softkit.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    @NotNull
    private String username;

    @NotNull
    @Email(message = "Email not valid")
    private String email;

    @NotNull
    @ValidPassword
    private String password;
    private List<Role> roles;

}
