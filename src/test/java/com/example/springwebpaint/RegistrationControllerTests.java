package com.example.springwebpaint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registrationPageTest() throws Exception {
        this.mockMvc.perform(get("/registration"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Регистрация")));
    }

    @Test
    @Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void registerUserTest() throws Exception {
        MockMultipartHttpServletRequestBuilder action = (MockMultipartHttpServletRequestBuilder) multipart("/registration")
                .param("username", "test_user")
                .param("password", "1")
                .with(csrf());
        this.mockMvc.perform(action)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main"));
    }

    @Test
    @Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void registerDuplicateUserTest() throws Exception {
        MockMultipartHttpServletRequestBuilder action = (MockMultipartHttpServletRequestBuilder) multipart("/registration")
                .param("username", "test_user")
                .param("password", "1")
                .with(csrf());
        MockMultipartHttpServletRequestBuilder action2 = (MockMultipartHttpServletRequestBuilder) multipart("/registration")
                .param("username", "test_user")
                .param("password", "1")
                .with(csrf());
        this.mockMvc.perform(action)
                .andDo(
                        result -> {
                            this.mockMvc.perform(action2)
                                    .andDo(print())
                                    .andExpect(content().string(containsString("User already exists")));
                        }
                )
                .andDo(print());
    }

}
