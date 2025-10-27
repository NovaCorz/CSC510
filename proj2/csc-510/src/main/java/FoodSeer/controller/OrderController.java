package FoodSeer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FoodSeer.dto.OrderDto;
import FoodSeer.exception.ResourceNotFoundException;
import FoodSeer.service.OrderService;

/**
 * Controller for Orders in the FoodSeer system.
 * Provides endpoints for managing and fulfilling food orders.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /** Connection to OrderService */
    @Autowired
    private OrderService orderService;

    /**
     * Retrieves all orders in the system.
     *
     * @return JSON list of all orders
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @GetMapping
    public List<OrderDto> getOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Retrieves all fulfilled orders.
     *
     * @return JSON list of fulfilled orders
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @GetMapping("/fulfilledOrders")
    public List<OrderDto> getFulfilledOrders() {
        return orderService.getAllFulfilledOrders();
    }

    /**
     * Retrieves all unfulfilled orders.
     *
     * @return JSON list of unfulfilled orders
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @GetMapping("/unfulfilledOrders")
    public List<OrderDto> getUnfulfilledOrders() {
        return orderService.getAllUnfulfilledOrders();
    }

    /**
     * Creates a new order.
     *
     * @param orderDto the order to create
     * @return ResponseEntity containing the created order
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody final OrderDto orderDto) {
        final OrderDto savedOrderDto = orderService.createOrder(orderDto);
        return ResponseEntity.ok(savedOrderDto);
    }

    /**
     * Marks an order as fulfilled.
     *
     * @param orderDto the order to fulfill
     * @return ResponseEntity with status depending on fulfillment result
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @PostMapping("/fulfillOrder")
    public ResponseEntity<OrderDto> fulfillOrder(@RequestBody final OrderDto orderDto) {
        try {
            orderService.getOrderById(orderDto.getId());
        } catch (final ResourceNotFoundException e) {
            return new ResponseEntity<>(orderDto, HttpStatus.PRECONDITION_FAILED);
        }

        final OrderDto existingOrder = orderService.getOrderById(orderDto.getId());

        if (existingOrder.getIsFulfilled()) {
            return new ResponseEntity<>(orderDto, HttpStatus.GONE);
        }

        try {
            final OrderDto updatedOrder = orderService.fulfillOrder(existingOrder.getId());
            return ResponseEntity.ok(updatedOrder);
        } catch (final Exception e) {
            return new ResponseEntity<>(orderDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a specific order by ID.
     *
     * @param id the ID of the order
     * @return ResponseEntity containing the order or 404 if not found
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") final Long id) {
        try {
            final OrderDto orderDto = orderService.getOrderById(id);
            return ResponseEntity.ok(orderDto);
        } catch (final ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
