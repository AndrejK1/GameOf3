package com.softkit.utils;

import com.softkit.dto.UserDataDTO;
import com.softkit.model.Role;
import lombok.experimental.UtilityClass;
import org.assertj.core.util.Lists;

import java.util.UUID;

@UtilityClass
public class UserUtils {
    public static UserDataDTO getValidUserForSignup() {
        UUID randomUUID = UUID.randomUUID();
        return new UserDataDTO(
                randomUUID + "test",
                randomUUID + "youremail@test.com",
                randomUUID + "Dtest_1",
                Lists.newArrayList(Role.ROLE_ADMIN, Role.ROLE_CLIENT));
    }

}
