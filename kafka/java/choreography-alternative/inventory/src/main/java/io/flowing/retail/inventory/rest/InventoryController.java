package io.flowing.retail.inventory.rest;

import io.flowing.retail.inventory.application.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(path = "/api/inventory/", method = RequestMethod.POST)
    public String changeInventory() {

        inventoryService.topUpInventory("article1", 20);
        inventoryService.topUpInventory("article2", 20);

        return "Inventory filled up";
    }
}
