package services;


import domain.entity.Delivery;
import domain.entity.DeliveryStatus;

import domain.entity.Order;
import domain.entity.User;
import domain.manager.DeliveryManager;
import domain.manager.UserManager;
import domain.repositories.Loghmeh;
import org.json.simple.JSONArray;

import java.time.LocalTime;
import java.util.ArrayList;

public class GetDeliveryList implements Runnable {

    @Override
    public void run() {
        Loghmeh instance = Loghmeh.getInstance();
        System.out.println("\n\n\nget delivery triggered by scheduler");
        DeliveryManager.getInstance().getDeliveryFromUrl();
        System.out.println("loop for undelivered orders...");
        for (Integer orderId:DeliveryManager.getInstance().getAllUndeliveredOrders()){
            DeliveryManager.getInstance().deliveryUserOrder(orderId);
        }

    }
}