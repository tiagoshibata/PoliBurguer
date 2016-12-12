package org.burguer.poli.poliburguer.activity;

import org.burguer.poli.poliburguer.models.Order;
import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    public int compare(Order o, Order p) {
        if (o.getTimestamp() > p.getTimestamp()) {
            return 1;
        }
        if (o.getTimestamp() < p.getTimestamp()) {
            return -1;
        }
        return 0;
    }
}