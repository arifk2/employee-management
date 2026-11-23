package com.dxc.rf.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class EmployeeControllerTest {

    private static final String NEW_EMPLOYEE = """
            {"firstName":"John","lastName":"Doe","email":"john.doe@example.com",
             "department":"Sales","salary":70000.00}
            """;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void listReturnsSeededEmployees() throws Exception {
        mvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void createThenFetchThenUpdateThenDelete() throws Exception {
        String location = mvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON).content(NEW_EMPLOYEE))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        mvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NEW_EMPLOYEE.replace("Sales", "Marketing")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department", is("Marketing")));

        mvc.perform(delete(location))
                .andExpect(status().isNoContent());

        mvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidEmployeeIsRejected() throws Exception {
        mvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.email").exists());
    }

    @Test
    void unknownIdReturnsNotFound() throws Exception {
        mvc.perform(get("/api/employees/999999"))
                .andExpect(status().isNotFound());
    }
}
