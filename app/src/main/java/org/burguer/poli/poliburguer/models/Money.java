package org.burguer.poli.poliburguer.models;

/**
 * Created by Eric on 11/12/16.
 */

public class Money {
    public static String format(int price) {
        return "R$ " + String.valueOf(price / 100) + "," + String.format("%02d", price % 100);
    }
}
