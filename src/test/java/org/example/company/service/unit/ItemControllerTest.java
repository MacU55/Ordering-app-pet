package org.example.company.service.unit;

import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.example.company.controller.ItemController;
import org.example.company.dto.request.RequestItem;
import org.example.company.dto.response.ResponseItem;
import org.example.company.model.ItemType;
import org.example.company.repository.CustomerRepository;
import org.example.company.repository.EmployeeRepository;
import org.example.company.repository.ItemRepository;
import org.example.company.repository.RoleRepository;
import org.example.company.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private RoleRepository itemRepository;

    @MockitoBean
    private EmployeeRepository employeeRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @Test
    void shouldReturnItem() throws Exception {
        long id = 1L;
        String name = "shirt";
        BigDecimal price = new BigDecimal("50");
        String description = "test";
        ItemType itemType = ItemType.CLOTHES;

        when(itemService.getItem(1L))
            .thenReturn(new ResponseItem(id, name, price, description, itemType));

        mockMvc.perform(get("/items/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("shirt"))
            .andExpect(jsonPath("$.price").value(50));
    }

    @Test
    void shouldCreateItem() throws Exception {
        long id = 1L;
        String name = "shirt";
        BigDecimal price = new BigDecimal("50");
        String description = "cotton";
        ItemType itemType = ItemType.CLOTHES;

        RequestItem request = new RequestItem(name, description, price, itemType);
        ResponseItem response = new ResponseItem(id, name, price, description, itemType);
        when(itemService.saveItem(request)).thenReturn(response);

        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"shirt","description":"cotton","price":50,"itemType":"CLOTHES"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));
    }


}
