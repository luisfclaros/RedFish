package com.RedFish.RedFish.shared.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithAnonymousUser;

import com.RedFish.RedFish.inventory.application.service.CreateProductService;
import com.RedFish.RedFish.inventory.application.service.GetProductService;
import com.RedFish.RedFish.inventory.application.service.ListProductsService;
import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.inventory.interfaces.rest.ProductController;
import com.RedFish.RedFish.shared.interfaces.rest.RestExceptionHandler;

@WebMvcTest(controllers = ProductController.class)
@Import({SecurityConfiguration.class, RestExceptionHandler.class})
class SecurityConfigurationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateProductService createProductService;

    @MockitoBean
    private GetProductService getProductService;

    @MockitoBean
    private ListProductsService listProductsService;

    @Test
    @WithMockUser(username = "operator", roles = "OPERATOR")
    void permitsProductRequestsForAuthenticatedUsers() throws Exception {
        when(listProductsService.list()).thenReturn(List.of());

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithAnonymousUser
    void rejectsUnauthenticatedApiRequests() throws Exception {
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "operator", roles = "OPERATOR")
    void preventsOperatorsFromCreatingUsers() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "operator", roles = "OPERATOR")
    void rejectsProductCreationWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "code": "PROD-SEC-01",
                      "name": "Tilapia Roja",
                      "unitOfMeasure": "kg",
                      "type": "PRODUCT"
                    }
                    """))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "operator", roles = "OPERATOR")
    void permitsProductCreationWithCsrfToken() throws Exception {
        Product created = new Product(1L, "PROD-SEC-01", "Tilapia Roja", "kg", ProductType.PRODUCT, true);
        when(createProductService.create(any(Product.class))).thenReturn(created);

        mockMvc.perform(post("/api/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "code": "PROD-SEC-01",
                      "name": "Tilapia Roja",
                      "unitOfMeasure": "kg",
                      "type": "PRODUCT"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/v1/products/1"))
            .andExpect(jsonPath("$.code").value("PROD-SEC-01"));
    }

    @Test
    void issuesReadableCsrfCookieOnApiRequest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/api/products"))
            .andReturn().getResponse();

        assertThat(response.getCookie("XSRF-TOKEN")).isNotNull();
        assertThat(response.getCookie("XSRF-TOKEN").isHttpOnly()).isFalse();
    }
}
