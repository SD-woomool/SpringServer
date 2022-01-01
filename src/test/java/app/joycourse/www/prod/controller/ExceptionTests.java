package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExceptionTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Access wrong route")
    public void accessWrongRoute() throws Exception {
        app.joycourse.www.prod.exception.CustomExceptionH.CustomError error = app.joycourse.www.prod.exception.CustomExceptionH.CustomError.PAGE_NOT_FOUND;
        final String expectedResponseContent = objectMapper.writeValueAsString(new Response(error.getMessage()));

        // given - access wrong route
        // when
        mockMvc.perform(get("/INVALID_ROUTE"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedResponseContent));
    }


}
