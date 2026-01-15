package org.adso.minimarket.service;

import org.adso.minimarket.dto.OrderDetails;
import org.adso.minimarket.models.User;

public interface OrderService {

    OrderDetails placeOrder(User user);
}
