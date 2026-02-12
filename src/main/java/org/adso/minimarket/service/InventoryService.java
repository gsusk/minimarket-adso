package org.adso.minimarket.service;

import org.adso.minimarket.models.inventory.InventoryTransaction;
import org.adso.minimarket.models.inventory.TransactionType;

import java.util.List;

public interface InventoryService {
    void adjustStock(Long productId, int quantity, TransactionType type, String reason);
    List<InventoryTransaction> getStockHistory(Long productId);
}
