package com.openclassrooms.poseidon.integration;

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
public class RuleIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameService ruleNameService;

    private int id = 0;



    @BeforeEach
    public void before() {

        RuleName ruleName = new RuleName("IT", "IT", "IT", "IT", "IT", "IT");
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
}

