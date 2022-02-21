package com.softkit;

import com.softkit.dto.UserDataDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.HttpRetryException;
import java.util.HashMap;

import static com.softkit.utils.UserUtils.getValidUserForSignup;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserIntegrationControllerTests extends AbstractControllerTest {
    private final String signupUrl = "/users/signup";
    private final String signInUrl = "/users/signin";

    @Test
    void simpleSignupSuccessTest() {
        String token = this.restTemplate.postForObject(
                getBaseUrl() + signupUrl,
                getValidUserForSignup(),
                String.class);

        assertThat(token).isNotBlank();
    }

    @Test
    void signupAgainErrorTest() {
        UserDataDTO userForSignup = getValidUserForSignup();
        String token = this.restTemplate.postForObject(
                getBaseUrl() + signupUrl,
                userForSignup,
                String.class);

        assertThat(token).isNotBlank();

        try {
            // to handle 401
            ResponseEntity<HashMap<String, Object>> response = this.restTemplate.exchange(
                    getBaseUrl() + signupUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(userForSignup),
                    new ParameterizedTypeReference<HashMap<String, Object>>() {});

        } catch (Exception e) {
            assertThat(((HttpRetryException)e.getCause()).responseCode()).isEqualTo(401);
        }
    }

    @Test
    void noSuchUserForLogin() {
        try {
            // to handle 401
            this.restTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(getBaseUrl() + signInUrl)
                            .queryParam("username", "fakeusername")
                            .queryParam("password", "fakepass")
                            .build().encode().toUri(),
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<HashMap<String, Object>>() {
                    });
        } catch (Exception e) {
            assertThat(((HttpRetryException)e.getCause()).responseCode()).isEqualTo(401);
        }
    }

    @Test
    void successSignupAndSignIn() {
        UserDataDTO user = getValidUserForSignup();

        String signupToken = this.restTemplate.postForObject(getBaseUrl() + signupUrl, user, String.class);
        assertThat(signupToken).isNotBlank();

        String token = this.restTemplate.postForObject(
                UriComponentsBuilder.fromHttpUrl(getBaseUrl() + signInUrl)
                        .queryParam("username", user.getUsername())
                        .queryParam("password", user.getPassword())
                        .build().encode().toUri(),
                HttpEntity.EMPTY,
                String.class);

        assertThat(token).isNotBlank();

        String tokenFromLogin = this.restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(getBaseUrl() + signInUrl)
                        .queryParam("username", user.getUsername())
                        .queryParam("password", user.getPassword())
                        .build().encode().toUri(),
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertThat(tokenFromLogin).isNotBlank();
    }

}
