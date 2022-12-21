package com.openclassrooms.poseidon.integration;

import com.openclassrooms.poseidon.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserIT {


//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//
//    @Test
//    @WithMockUser(username = "user")
//    void viewTransferIT() throws Exception {
//        mockMvc.perform(get("/transfer"))
//                .andExpect(status().isFound())
//                .andExpect(authenticated())
//                .andExpect(redirectedUrl("/transfer/page/1"))
//                .andExpect(view().name("redirect:/transfer/page/1"))
//                .andReturn();
//    }
//
//
//    @Test
//    @WithMockUser(username = "test@paymybuddy.com")
//    void receiveFromBankIT() throws Exception {
//
//        mockMvc.perform(post("/transfer/fromBank")
//                        .sessionAttr("bankDTO", new BankDto())
//                        .param("amount", String.valueOf(BigDecimal.valueOf(100))))
//                .andExpect(status().isFound())
//                .andExpect(model().size(1))
//                .andExpect(authenticated())
//                .andExpect(model().attributeExists("bankDto"))
//                .andExpect(redirectedUrl("/transfer"))
//                .andExpect(view().name("redirect:/transfer"))
//                .andReturn();
//    }

}