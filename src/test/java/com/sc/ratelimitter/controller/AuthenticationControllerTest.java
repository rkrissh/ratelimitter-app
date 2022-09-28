package com.sc.ratelimitter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.ratelimitter.dto.UserAccountCreationRequestDto;
import com.sc.ratelimitter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@ContextConfiguration
public class AuthenticationControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //@Test
    void testAuthenticationController() throws Exception {
        when(userService.signUp(any(UserAccountCreationRequestDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
        UserAccountCreationRequestDto userAccountCreationRequestDto =  UserAccountCreationRequestDto.builder()
                .emailId("rakesh@gmail.com").password("fsdfds").planId(UUID.randomUUID()).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(userAccountCreationRequestDto);
        mockMvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
