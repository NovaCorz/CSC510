package FoodSeer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FoodSeer.dto.FoodDto;
import FoodSeer.dto.InventoryDto;
import FoodSeer.dto.OrderDto;
import FoodSeer.entity.Food;
import FoodSeer.entity.Order;
import FoodSeer.exception.ResourceNotFoundException;
import FoodSeer.mapper.FoodMapper;
import FoodSeer.mapper.OrderMapper;
import FoodSeer.repository.FoodRepository;
import FoodSeer.repository.OrderRepository;
import FoodSeer.service.InventoryService;
import FoodSeer.service.OrderService;

/**
 * Implementation of the OrderService interface for managing food orders.
 */
@Service
public class OrderServiceImpl implements OrderService {

    /** Repository for food items. */
    @Autowired
    private FoodRepository foodRepository;

    /** Repository for orders. */
    @Autowired
    private OrderRepository orderRepository;

    /** Inventory service for stock management. */
    @Autowired
    private InventoryService inventoryService;

    /**
     * Creates an order with the given information.
     *
     * @param orderDto order to create
     * @return created order
     */
    @Override
    public OrderDto createOrder(final OrderDto orderDto) {
        final List<Food> foods = new ArrayList<>();

        for (final Food food : orderDto.getFoods()) {
            final Food f = foodRepository.findById(food.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("A Food item does not exist within the order."));
            foods.add(f);
        }

        orderDto.setFoods(foods);

        final Order order = OrderMapper.mapToOrder(orderDto);
        final Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToOrderDto(savedOrder);
    }

    /**
     * Returns the order with the given id.
     *
     * @param orderId order's id
     * @return the order with the given id
     * @throws ResourceNotFoundException if the order doesn't exist
     */
    @Override
    public OrderDto getOrderById(final Long orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order does not exist with id " + orderId));
        return OrderMapper.mapToOrderDto(order);
    }

    /**
     * Returns a list of all the orders.
     *
     * @return all the orders
     */
    @Override
    public List<OrderDto> getAllOrders() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
    }

    /**
     * Fulfills the order by checking food availability and updating inventory.
     *
     * @param orderId The id of the order to fulfill
     * @return the updated OrderDto
     */
    @Override
    public OrderDto fulfillOrder(final long orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order does not exist with id " + orderId));

        final InventoryDto inventoryDto = inventoryService.getInventory();

        for (final Food food : order.getFoods()) {
            final boolean foodAvailable = checkFoodAvailability(inventoryDto, food);
            if (!foodAvailable) {
                throw new IllegalArgumentException("Not enough stock to fulfill the order for " + food.getFoodName());
            }

            updateInventory(inventoryDto, food);
        }

        order.setIsFulfilled(true);
        final Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToOrderDto(savedOrder);
    }

    /**
     * Checks if all ingredients/foods for an order item are available.
     *
     * @param inventoryDto The current inventory
     * @param food         The food item to check
     * @return true if available, false otherwise
     */
    private boolean checkFoodAvailability(final InventoryDto inventoryDto, final Food food) {
        final Map<String, Food> inventoryMap = inventoryDto.getFoods().stream()
                .collect(Collectors.toMap(Food::getFoodName, f -> f));

        final Food inventoryFood = inventoryMap.get(food.getFoodName());
        return inventoryFood != null && food.getAmount() <= inventoryFood.getAmount();
    }

    /**
     * Deducts the ordered food amount from the inventory.
     *
     * @param inventoryDto The inventory data
     * @param food         The food item to deduct
     */
    private void updateInventory(final InventoryDto inventoryDto, final Food food) {
        final Map<String, Food> inventoryMap = inventoryDto.getFoods().stream()
                .collect(Collectors.toMap(Food::getFoodName, f -> f));

        final Food inventoryFood = inventoryMap.get(food.getFoodName());
        if (inventoryFood != null) {
            inventoryFood.setAmount(inventoryFood.getAmount() - food.getAmount());
        }

        inventoryService.updateInventory(inventoryDto);
    }

    /**
     * Returns all fulfilled orders.
     *
     * @return list of fulfilled orders
     */
    @Override
    public List<OrderDto> getAllFulfilledOrders() {
        return getAllOrders().stream()
                .filter(OrderDto::getIsFulfilled)
                .collect(Collectors.toList());
    }

    /**
     * Returns all unfulfilled orders.
     *
     * @return list of unfulfilled orders
     */
    @Override
    public List<OrderDto> getAllUnfulfilledOrders() {
        return getAllOrders().stream()
                .filter(o -> !o.getIsFulfilled())
                .collect(Collectors.toList());
    }
}
