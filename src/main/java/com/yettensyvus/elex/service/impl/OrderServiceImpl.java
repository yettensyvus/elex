package com.yettensyvus.elex.service.impl;

import com.yettensyvus.elex.domain.*;
import com.yettensyvus.elex.domain.constants.ORDER_STATUS;
import com.yettensyvus.elex.domain.constants.PAYMENT_STATUS;
import com.yettensyvus.elex.repository.AddressRepository;
import com.yettensyvus.elex.repository.OrderItemRepository;
import com.yettensyvus.elex.repository.OrderRepository;
import com.yettensyvus.elex.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        user.getAddresses().add(shippingAddress);
        Address address = addressRepository.save(shippingAddress);

        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        Set<Order> orders = new HashSet<>();
        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = calculateTotalPrice(items);
            int totalItem = calculateTotalItem(items);

            Order createOrder = buildOrder(user, sellerId, totalOrderPrice, totalItem, address);
            Order savedOrder = orderRepository.save(createOrder);
            orders.add(savedOrder);

            createOrderItems(savedOrder, items);
        }

        return orders;
    }

    private int calculateTotalPrice(List<CartItem> items) {
        return items.stream().mapToInt(CartItem::getSellingPrice).sum();
    }

    private int calculateTotalItem(List<CartItem> items) {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    private Order buildOrder(User user, Long sellerId, int totalOrderPrice, int totalItem, Address address) {
        Order order = new Order();
        order.setUser(user);
        order.setSellerId(sellerId);
        order.setTotalMrpPrice(totalOrderPrice);
        order.setTotalSellingPrice(totalOrderPrice);
        order.setTotalItem(totalItem);
        order.setShippingAddress(address);
        order.setOrderStatus(ORDER_STATUS.PENDING);
        order.getPaymentDetails().setStatus(PAYMENT_STATUS.PENDING);
        return order;
    }

    private void createOrderItems(Order savedOrder, List<CartItem> items) {
        for (CartItem item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMrpPrice(item.getMrpPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUserId(item.getUserId());
            orderItem.setSellingPrice(item.getSellingPrice());

            savedOrder.getOrderItems().add(orderItem);
            orderItemRepository.save(orderItem);
        }
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found"));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, ORDER_STATUS status) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);

        if (!user.getId().equals(order.getUser().getId())) {
            throw new Exception("You don't have access to this order!");
        }
        order.setOrderStatus(ORDER_STATUS.CANCELED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Order item does not exist"));
    }
}
