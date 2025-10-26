package FoodSeer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import FoodSeer.TestUtils;
import FoodSeer.dto.FoodDto;
import FoodSeer.dto.InventoryDto;
import FoodSeer.dto.OrderDto;
import FoodSeer.entity.Food;
import FoodSeer.repository.FoodRepository;
import FoodSeer.repository.OrderRepository;
import FoodSeer.service.FoodService;
import FoodSeer.service.InventoryService;
import FoodSeer.service.OrderService;

/**
 * Tests Controller for API endpoints for an Order.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc mvc;

    /** Repository for orders */
    @Autowired
    private OrderRepository orderRepository;

    /** Repository for food items */
    @Autowired
    private FoodRepository foodRepository;

    /** Service for food */
    @Autowired
    private FoodService foodService;

    /** Service for orders */
    @Autowired
    private OrderService orderService;

    /** Service for inventory */
    @Autowired
    private InventoryService inventoryService;

    /**
     * Sets up test case by clearing repositories and creating sample data.
     */
    @BeforeEach
    public void setUp() throws Exception {
        orderRepository.deleteAll();
        foodRepository.deleteAll();

        // Create initial inventory
        final List<Food> foods = new ArrayList<>();
        final Food coffee = new Food(null, "Coffee", 20, 10, List.of("CAFFEINE"));
        final Food matcha = new Food(null, "Matcha", 15, 8, List.of("CAFFEINE"));
        final Food sugar = new Food(null, "Sugar", 25, 3, List.of("GLUCOSE"));
        foods.add(coffee);
        foods.add(matcha);
        foods.add(sugar);

        final InventoryDto inventoryDto = new InventoryDto(1L, foods);
        inventoryService.createInventory(inventoryDto);

        // Add food items to repo
        foodRepository.saveAll(foods);
    }

    @Test
    @Transactional
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void testGetAndCreateOrder() throws Exception {
        final List<Food> foods = foodRepository.findAll();

        final OrderDto orderDto = new OrderDto(0L, "Order1");
        orderDto.setFoods(foods.subList(0, 2)); // use some foods

        final OrderDto savedOrder = orderService.createOrder(orderDto);

        mvc.perform(get("/api/orders"))
                .andExpect(status().isOk());

        mvc.perform(get("/api/orders/fulfilledOrders"))
                .andExpect(status().isOk());

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(savedOrder))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(get("/api/orders/" + savedOrder.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(username = "staff", roles = "STAFF")
    void testFulfillOrder() throws Exception {
        final List<Food> foods = foodRepository.findAll();
        final OrderDto orderDto = new OrderDto(0L, "Order2");
        orderDto.setFoods(foods.subList(0, 1));

        final OrderDto savedOrder = orderService.createOrder(orderDto);

        mvc.perform(get("/api/orders"))
                .andExpect(status().isOk());

        mvc.perform(get("/api/orders/fulfilledOrders"))
                .andExpect(status().isOk());

        mvc.perform(post("/api/orders/fulfillOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(savedOrder))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(username = "staff", roles = "STAFF")
    void testGetUnfulfilledOrders() throws Exception {
        final List<Food> foods = foodRepository.findAll();
        final OrderDto orderDto = new OrderDto(0L, "Order3");
        orderDto.setFoods(foods.subList(1, 3));

        orderService.createOrder(orderDto);

        mvc.perform(get("/api/orders/unfulfilledOrders"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(username = "staff", roles = "STAFF")
    void testGetSalesTaxRate() throws Exception {
        mvc.perform(get("/api/orders/tax"))
                .andExpect(status().isOk());
    }
}
