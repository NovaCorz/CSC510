package FoodSeer.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import FoodSeer.dto.FoodDto;
import FoodSeer.dto.InventoryDto;
import FoodSeer.dto.OrderDto;
import FoodSeer.entity.Food;
import FoodSeer.mapper.FoodMapper;
import FoodSeer.repository.FoodRepository;
import FoodSeer.repository.InventoryRepository;
import FoodSeer.repository.OrderRepository;

/**
 * Tests OrderService and OrderServiceImpl classes for the FoodSeer project.
 */
@SpringBootTest
class OrderServiceImplTest {

    /** Reference to Food repository */
    @Autowired
    private FoodRepository foodRepository;

    /** Reference to Inventory repository */
    @Autowired
    private InventoryRepository inventoryRepository;

    /** Reference to Order repository */
    @Autowired
    private OrderRepository orderRepository;

    /** Reference to Order service */
    @Autowired
    private OrderService orderService;

    /** Reference to Inventory service */
    @Autowired
    private InventoryService inventoryService;

    /** Reference to Food service */
    @Autowired
    private FoodService foodService;

    /**
     * Clears all repositories before each test.
     */
    @BeforeEach
    public void setUp() throws Exception {
        foodRepository.deleteAll();
        orderRepository.deleteAll();
        inventoryRepository.deleteAll();
    }

    /**
     * Tests creating an order with valid data.
     */
    @Test
    @Transactional
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void testCreateOrder() {
        // Create and save a food item
        final FoodDto food1 = new FoodDto();
        food1.setFoodName("COFFEE");
        food1.setAmount(10);
        food1.setPrice(5);
        food1.setAllergies(List.of("CAFFEINE"));

        final FoodDto savedFood = foodService.createFood(food1);

        // Create order containing that food
        final OrderDto orderDto = new OrderDto(0L, "Order1");
        orderDto.setFoods(List.of(FoodMapper.mapToFood(savedFood))); // ✅ FIXED mapper reference

        final OrderDto savedOrder = orderService.createOrder(orderDto);

        assertAll("Order contents",
                () -> assertEquals("Order1", savedOrder.getName()),
                () -> assertFalse(savedOrder.getIsFulfilled()));
    }

    /**
     * Tests fulfilling an order and verifying inventory updates accordingly.
     */
    @Test
    @Transactional
    @WithMockUser(username = "staff", roles = "STAFF")
    void testFulfillOrder() {
        // Initialize inventory
        final List<Food> foods = new ArrayList<>();
        final Food f1 = new Food(null, "COFFEE", 5, 10, List.of("CAFFEINE"));
        final Food f2 = new Food(null, "MILK", 5, 6, List.of("LACTOSE"));
        final Food f3 = new Food(null, "SUGAR", 5, 4, List.of("GLUCOSE"));

        foods.add(f1);
        foods.add(f2);
        foods.add(f3);

        final InventoryDto inventoryDto = new InventoryDto(1L, foods);
        inventoryService.createInventory(inventoryDto);

        // Save foods in DB
        foodRepository.saveAll(foods);

        // Create an order with one item
        final OrderDto orderDto = new OrderDto(0L, "Order1");
        orderDto.setFoods(List.of(f1));

        assertEquals(0, orderService.getAllOrders().size());

        final OrderDto savedOrder = orderService.createOrder(orderDto);

        assertAll("Order contents before fulfillment",
                () -> assertEquals("Order1", savedOrder.getName()),
                () -> assertFalse(savedOrder.getIsFulfilled()));

        assertEquals(1, orderService.getAllOrders().size());
        assertEquals(0, orderService.getAllFulfilledOrders().size());

        // Fulfill the order
        final OrderDto fulfilledOrder = orderService.fulfillOrder(savedOrder.getId());

        assertAll("Order contents after fulfillment",
                () -> assertEquals("Order1", fulfilledOrder.getName()),
                () -> assertTrue(fulfilledOrder.getIsFulfilled()));

        assertEquals(1, orderService.getAllFulfilledOrders().size());
        assertEquals(savedOrder.getId(), orderService.getOrderById(savedOrder.getId()).getId());
    }
}
