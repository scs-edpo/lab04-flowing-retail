package io.flowing.retail.inventory.application;

import java.time.LocalDateTime;
import java.util.List;

import io.flowing.retail.inventory.domain.*;
import io.flowing.retail.inventory.messages.Message;
import io.flowing.retail.inventory.messages.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryService {

  Inventory inventory = Inventory.getInstance();
  ReservationRegistry reservationRegistry = ReservationRegistry.getInstance();
  @Autowired
  private MessageSender messageSender;
  
  /**
   * reserve goods on stock for a defined period of time
   * 
   * @param reason A reason why the goods are reserved (e.g. "customer order")
   * @param refId A reference id fitting to the reason of reservation (e.g. the order id), needed to find reservation again later
   * @param expirationDate Date until when the goods are reserved, afterwards the reservation is removed
   * @return if reservation could be done successfully
   */
  public boolean reserveGoods(List<Item> items, String reason, String refId, LocalDateTime expirationDate) {
    Reservation reservation = new Reservation(refId, reason, items, expirationDate);
    reservationRegistry.addReservation(reservation);
    try {
      for (Item item: items) {
        inventory.decreaseArticleCount(item);
      }
      publishInventoryEvent(items, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Reservation created");
    return true;
  }

  /**
   * Order to pick the given items in the warehouse. The inventory is decreased. 
   * Reservation fitting the reason/refId might be used to fulfill the order.
   * 
   * If no enough items are on stock - an exception is thrown.
   * Otherwise a unique pick id is returned, which can be used to 
   * reference the bunch of goods in the shipping area.
   * 
   * @param items to be picked
   * @param reason for which items are picked (e.g. "customer order")
   * @param refId Reference id fitting to the reason of the pick (e.g. "order id"). Used to determine which reservations can be used.
   * @return a unique pick ID 
   */
  public String pickItems(List<Item> items, String reason, String refId) {
    PickOrder pickOrder = new PickOrder().setItems(items);    
    System.out.println("# Items picked: " + pickOrder);      
    return pickOrder.getPickId();
  }

  /**
   * New goods are arrived and inventory is increased
   */
  public void topUpInventory(String articleId, int amount) {
    Item item = new Item()
            .setAmount(amount)
            .setArticleId(articleId);
    inventory.topUpInventory(item);

    Message<Item> m = new Message<>("InventoryToppedUp", item);
    messageSender.send(m);
  }

  private void publishInventoryEvent (List<Item> items, boolean increase) {
    String type = increase ? "InventoryIncreaseEvent" : "InventoryDecreaseEvent";
    Message<List<Item>> m = new Message<>(type, items);
    messageSender.send(m);
  }

}
