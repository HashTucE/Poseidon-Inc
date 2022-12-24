package com.openclassrooms.poseidon.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CurveIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurveService curveService;

    @Autowired
    private ObjectMapper objectMapper;

    private int id = 0;

    private CurvePoint curvePoint;



    @BeforeEach
    public void before() {

        curvePoint = new CurvePoint(1, 1.0, 1.0);
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


    ////////////////////////////////////////REST CONTROLLERS////////////////////////////////////////



    @Test
    public void testGetAllCurve() throws Exception {

        CurvePoint curvePoint0 = new CurvePoint(1, 1.0, 1.0);
        curveService.addCurvePoint(curvePoint0);
        int id0 = curvePoint0.getId();

        mockMvc.perform(get("/api/curve/all"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$..id", hasItems(curvePoint.getId(), curvePoint0.getId())))
                .andExpect(jsonPath("$..curveId", hasItems(curvePoint.getCurveId(), curvePoint0.getCurveId())))
                .andExpect(jsonPath("$..term", hasItems(curvePoint.getTerm(), curvePoint0.getTerm())))
                .andExpect(jsonPath("$..value", hasItems(curvePoint.getValue(), curvePoint0.getValue())));

        curveService.deleteById(id0);
    }


    @Test
    public void getCurveAPITest() throws Exception {

        mockMvc.perform(get("/api/curve")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.curveId").value(1))
                .andExpect(jsonPath("$.term").value(1.0))
                .andExpect(jsonPath("$.value").value(1.0));


    }


    @Test
    public void testAddCurve() throws Exception {

        String requestBody = objectMapper.writeValueAsString(curvePoint);

        mockMvc.perform(post("/api/curve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("Curve created !"));

        //check if object is well created
        mockMvc.perform(get("/api/curve")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.curveId").value(curvePoint.getCurveId()))
                .andExpect(jsonPath("$.term").value(curvePoint.getTerm()))
                .andExpect(jsonPath("$.value").value(curvePoint.getValue()));

        //check answer with invalid object
        curvePoint.setCurveId(null);
        requestBody = objectMapper.writeValueAsString(curvePoint);
        mockMvc.perform(post("/api/curve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CurvePointId is mandatory"));
    }


    @Test
    public void updateCurveAPITest() throws Exception {

        CurvePoint curveToUpdate = new CurvePoint(2, 1.0, 1.0);

        mockMvc.perform(put("/api/curve")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curveToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("Curve with id " + id + " updated !"));

        //check if object is well updated
        mockMvc.perform(get("/api/curve")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.curveId").value(curveToUpdate.getCurveId()))
                .andExpect(jsonPath("$.term").value(curveToUpdate.getTerm()))
                .andExpect(jsonPath("$.value").value(curveToUpdate.getValue()));

        //check answer with invalid object
        curvePoint.setCurveId(null);
        String requestBody = objectMapper.writeValueAsString(curvePoint);
        mockMvc.perform(put("/api/curve")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CurvePointId is mandatory"));
    }



    @Test
    public void deleteCurveAPITest() throws Exception {

        mockMvc.perform(delete("/api/curve")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string("Curve with id " + id + " deleted !"));

        //check if object is well deleted
        mockMvc.perform(get("/api/curve" + id))
                .andExpect(status().isNotFound());
    }
}