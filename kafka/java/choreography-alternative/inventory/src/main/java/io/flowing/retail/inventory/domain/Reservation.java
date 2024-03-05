package io.flowing.retail.inventory.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {

    String refId;
    String reason;
    List<Item> items;
    LocalDateTime deadline;

    public Reservation(String refId, String reason, List<Item> items, LocalDateTime deadline) {
        this.refId = refId;
        this.reason = reason;
        this.items = items;
        this.deadline = deadline;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
