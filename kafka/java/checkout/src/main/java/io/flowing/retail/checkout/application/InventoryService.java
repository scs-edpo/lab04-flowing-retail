package io.flowing.retail.checkout.application;

import io.flowing.retail.checkout.domain.Inventory;
import io.flowing.retail.checkout.domain.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryService {

    Inventory inventory = Inventory.getInstance();

    public void increaseInventory(List<Item> items) {
        for (Item item: items) {
            inventory.topUpInventory(item);
        }
        System.out.println("Inventory updated");
    }

    public void decreaseInventory(List<Item> items) {
        for (Item item: items) {
            inventory.decreaseArticleCount(item);
        }
        System.out.println("Inventory updated");
    }
}
