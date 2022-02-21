package com.softkit.controller;

import com.softkit.dto.UserDataDTO;
import com.softkit.mapper.UserMapper;
import com.softkit.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/signin")
    @ApiOperation(value = "${userController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String login(
            @ApiParam("username") @RequestParam String username,
            @ApiParam("password") @RequestParam String password) {
        return userService.signIn(username, password);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "${userController.signup}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public String signup(@ApiParam("Signup User") @Valid @RequestBody UserDataDTO user) {
        return userService.signup(userMapper.mapUserDataToUser(user));
    }
}
