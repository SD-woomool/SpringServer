package app.joycourse.www.prod.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExceptionTests {
    /*
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
*/

}
