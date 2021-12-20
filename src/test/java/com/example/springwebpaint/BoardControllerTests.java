package com.example.springwebpaint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("test_user")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/create-boards-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-boards-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BoardControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void boardPageTestNotAvailable() throws Exception {
        this.mockMvc.perform(get("/board").param("id", "100"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isNotFound());
    }

    @Test
    public void boardPageTestAvailable() throws Exception {
        this.mockMvc.perform(get("/board").param("id", "1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("Пробная доска created by test_user")));
    }

}
