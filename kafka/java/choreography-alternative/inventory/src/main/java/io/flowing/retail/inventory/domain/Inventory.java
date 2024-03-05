package io.flowing.retail.inventory.domain;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<Item> inventory;

    private static final Inventory instance = new Inventory();

    private Inventory() {
        this.inventory = new ArrayList<>();
    }

    public static Inventory getInstance() {
        return instance;
    }

    public void topUpInventory(Item item) {
        for (Item i: inventory) {
            if (i.getArticleId().equals(item.getArticleId())) {
                i.setAmount(i.getAmount() + item.getAmount());
                return;
            }
        }
        inventory.add(item);
    }

    public void decreaseArticleCount(Item item) throws Exception {
        for (Item i: inventory) {
            if (i.getArticleId().equals(item.getArticleId())) {
                if (i.getAmount() < item.getAmount()){
                    throw new Exception("Not enough items");
                }
                i.setAmount(i.getAmount() - item.getAmount());
                return;
            }
        }
        throw new Exception("Article not available");
    }

    public Item getArticleFromInventory(String articleId) {
        for (Item i: inventory) {
            if (i.getArticleId().equals(articleId)){
                return i;
            }
        }
        return null;
    }
}
