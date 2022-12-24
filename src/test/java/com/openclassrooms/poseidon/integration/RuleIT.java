package com.openclassrooms.poseidon.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RuleNameService;
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
public class RuleIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameService ruleNameService;

    @Autowired
    private ObjectMapper objectMapper;

    private int id = 0;

    private RuleName ruleName;



    @BeforeEach
    public void before() {

        ruleName = new RuleName("IT", "IT", "IT", "IT", "IT", "IT");
        ruleNameService.addRuleName(ruleName);
        id = ruleName.getId();
    }

    @AfterEach
    public void after() {

        try {
            ruleNameService.deleteById(id);}
        catch (NotExistingException e) {}
    }


    @Test
    public void homeTest() throws Exception {

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk());
    }


    @Test
    public void validateTest() throws Exception {

        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "IT")
                        .param("description", "IT")
                        .param("json", "IT")
                        .param("template", "IT")
                        .param("sqlStr", "IT")
                        .param("sqlPart", "IT"))
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(status().isFound());
    }


    @Test
    public void showUpdateFormTest() throws Exception {

        mockMvc.perform(get("/ruleName/update/{id}", id))
                .andExpect(status().isOk());
    }


    @Test
    public void updateRuleNameTest() throws Exception {

        mockMvc.perform(post("/ruleName/update/{id}", id)
                        .param("name", "IT")
                        .param("description", "IT")
                        .param("json", "IT")
                        .param("template", "IT")
                        .param("sqlStr", "IT")
                        .param("sqlPart", "IT"))
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    @Test
    public void deleteRuleNameTest() throws Exception {

        mockMvc.perform(get("/ruleName/delete/{id}", id))
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    ////////////////////////////////////////REST CONTROLLERS////////////////////////////////////////



    @Test
    public void testGetAllRule() throws Exception {

        RuleName ruleName0 = new RuleName("IT0", "IT0", "IT0", "IT0", "IT0", "IT0");
        ruleNameService.addRuleName(ruleName0);
        int id0 = ruleName0.getId();

        mockMvc.perform(get("/api/rule/all"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$..id", hasItems(ruleName.getId(), ruleName0.getId())))
                .andExpect(jsonPath("$..name", hasItems(ruleName.getName(), ruleName0.getName())))
                .andExpect(jsonPath("$..description", hasItems(ruleName.getDescription(), ruleName0.getDescription())))
                .andExpect(jsonPath("$..json", hasItems(ruleName.getJson(), ruleName0.getJson())))
                .andExpect(jsonPath("$..template", hasItems(ruleName.getTemplate(), ruleName0.getTemplate())))
                .andExpect(jsonPath("$..sqlStr", hasItems(ruleName.getSqlStr(), ruleName0.getSqlStr())))
                .andExpect(jsonPath("$..sqlPart", hasItems(ruleName.getSqlPart(), ruleName0.getSqlPart())));

        ruleNameService.deleteById(id0);
    }


    @Test
    public void getRuleAPITest() throws Exception {

        mockMvc.perform(get("/api/rule")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(ruleName.getName()))
                .andExpect(jsonPath("$.description").value(ruleName.getDescription()))
                .andExpect(jsonPath("$.json").value(ruleName.getJson()))
                .andExpect(jsonPath("$.template").value(ruleName.getTemplate()))
                .andExpect(jsonPath("$.sqlStr").value(ruleName.getSqlStr()))
                .andExpect(jsonPath("$.sqlPart").value(ruleName.getSqlPart()));


    }


    @Test
    public void testAddRule() throws Exception {

        String requestBody = objectMapper.writeValueAsString(ruleName);

        mockMvc.perform(post("/api/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("Rule created !"));

        //check if object is well created
        mockMvc.perform(get("/api/rule")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(ruleName.getName()))
                .andExpect(jsonPath("$.description").value(ruleName.getDescription()))
                .andExpect(jsonPath("$.json").value(ruleName.getJson()))
                .andExpect(jsonPath("$.template").value(ruleName.getTemplate()))
                .andExpect(jsonPath("$.sqlStr").value(ruleName.getSqlStr()))
                .andExpect(jsonPath("$.sqlPart").value(ruleName.getSqlPart()));

        //check answer with invalid object
        ruleName.setName("");
        requestBody = objectMapper.writeValueAsString(ruleName);
        mockMvc.perform(post("/api/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name is mandatory"));
    }


    @Test
    public void updateRuleAPITest() throws Exception {

        RuleName ruleToUpdate = new RuleName("IT0", "IT0", "IT0", "IT0", "IT0", "IT0");

        mockMvc.perform(put("/api/rule")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("Rule with id " + id + " updated !"));

        //check if object is well updated
        mockMvc.perform(get("/api/rule")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.name").value(ruleToUpdate.getName()))
                .andExpect(jsonPath("$.description").value(ruleToUpdate.getDescription()))
                .andExpect(jsonPath("$.json").value(ruleToUpdate.getJson()))
                .andExpect(jsonPath("$.template").value(ruleToUpdate.getTemplate()))
                .andExpect(jsonPath("$.sqlStr").value(ruleToUpdate.getSqlStr()))
                .andExpect(jsonPath("$.sqlPart").value(ruleToUpdate.getSqlPart()));

        //check answer with invalid object
        ruleName.setName("");
        String requestBody = objectMapper.writeValueAsString(ruleName);
        mockMvc.perform(put("/api/rule")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name is mandatory"));
    }



    @Test
    public void deleteRuleAPITest() throws Exception {

        mockMvc.perform(delete("/api/rule")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string("Rule with id " + id + " deleted !"));

        //check if object is well deleted
        mockMvc.perform(get("/api/rule" + id))
                .andExpect(status().isNotFound());
    }
}

