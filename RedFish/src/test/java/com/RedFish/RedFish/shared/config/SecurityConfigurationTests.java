package com.RedFish.RedFish.shared.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RedFish.RedFish.inventory.application.service.CreateProductService;
import com.RedFish.RedFish.inventory.application.service.GetProductService;
import com.RedFish.RedFish.inventory.application.service.ListProductsService;
import com.RedFish.RedFish.orders.application.service.CreateCustomerService;
import com.RedFish.RedFish.orders.application.service.GetCustomerService;
import com.RedFish.RedFish.orders.application.service.ListCustomersService;

@WebMvcTest
@Import({SecurityConfiguration.class, SecurityConfigurationTests.ProbeController.class})
class SecurityConfigurationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateProductService createProductService;

    @MockitoBean
    private GetProductService getProductService;

    @MockitoBean
    private ListProductsService listProductsService;

    @MockitoBean
    private CreateCustomerService createCustomerService;

    @MockitoBean
    private GetCustomerService getCustomerService;

    @MockitoBean
    private ListCustomersService listCustomersService;

    @Test
    void permitsProductAndOtherRequestsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/products/probe"))
            .andExpect(status().isOk())
            .andExpect(content().string("ok"));

        mockMvc.perform(get("/private/probe"))
            .andExpect(status().isOk())
            .andExpect(content().string("ok"));
    }

    @Test
    void csrfIsDisabledForPostRequests() throws Exception {
        mockMvc.perform(post("/api/products/probe")
                .contentType(MediaType.TEXT_PLAIN)
                .content("payload"))
            .andExpect(status().isOk())
            .andExpect(content().string("created"));
    }

    @RestController
    static class ProbeController {
        @GetMapping({"/api/products/probe", "/private/probe"})
        String get() {
            return "ok";
        }

        @PostMapping("/api/products/probe")
        String post() {
            return "created";
        }
    }
}
