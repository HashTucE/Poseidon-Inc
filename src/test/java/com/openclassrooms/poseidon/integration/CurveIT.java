package com.openclassrooms.poseidon.integration;

import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.CurveService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles("test")
//@TestPropertySource(properties = "spring.profiles.active=test")
//@Sql({"/doc/schema.sql", "/doc/data.sql"})
public class CurveIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurveService curveService;

    private int id = 0;



    @BeforeEach
    public void before() {

        CurvePoint curvePoint = new CurvePoint(1, 1.0, 1.0);
        curveService.addCurvePoint(curvePoint);
        id = curvePoint.getId();
    }

    @AfterEach
    public void after() {

        try {
            curveService.deleteById(id);}
        catch (NotExistingException e) {}
    }


    @Test
    public void homeTest() throws Exception {

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk());
    }


    @Test
    public void validateTest() throws Exception {

        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", String.valueOf(1))
                        .param("term", String.valueOf(1.0))
                        .param("value", String.valueOf(1.0)))
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(status().isFound());
    }


    @Test
    public void showUpdateFormTest() throws Exception {

        mockMvc.perform(get("/curvePoint/update/{id}", id))
                .andExpect(status().isOk());
    }


    @Test
    public void updateCurveTest() throws Exception {

        mockMvc.perform(post("/curvePoint/update/{id}", id)
                        .param("curveId", String.valueOf(1))
                        .param("term", String.valueOf(1.0))
                        .param("value", String.valueOf(2.0)))
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    @Test
    public void deleteCurveTest() throws Exception {

        mockMvc.perform(get("/curvePoint/delete/{id}", id))
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(status().isFound())
                .andReturn();
    }
}