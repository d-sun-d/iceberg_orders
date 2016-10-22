package IsbergOrders;

import java.util.TreeMap;
import java.util.Collections;

/**
 * Created by d-sun-d on 09.10.2016.
 */
public class OrderQueue {
    public TreeMap<Integer, SamePriceOrderQueue> priceToOrders;
    public int size = 0;
    private char orderType;

    public OrderQueue(char initOrderType){
        orderType = initOrderType;
        if (orderType == 'B'){
            priceToOrders=  new TreeMap<Integer, SamePriceOrderQueue>(Collections.reverseOrder());
        }else{
            priceToOrders=  new TreeMap<Integer, SamePriceOrderQueue>();
        }
    };

    public void addOrder(Order order){
        size += 1;
        order.setToDefaultPublicSize();
        if (priceToOrders.containsKey(order.price)){
            priceToOrders.get(order.price).addOrder(order);
        } else {
            priceToOrders.put(order.price, new SamePriceOrderQueue(order));
        }
    };

    public void matchOrder(Order inOrder){
        if (size != 0){
            int bestPrice = priceToOrders.firstKey();
            while (((inOrder.orderType == 'B' &&(inOrder.price >= bestPrice))||
                    ((inOrder.orderType == 'S')&&(inOrder.price <= bestPrice)))
                    && (size > 0) && (inOrder.publicSize > 0)){
                SamePriceOrderQueue bestDeals = priceToOrders.get(bestPrice);
                int size_before = bestDeals.notEmptyOrders;
                bestDeals.matchOrder(inOrder);
                size -= size_before - bestDeals.notEmptyOrders;
                if (bestDeals.notEmptyOrders == 0){
                    priceToOrders.remove(bestPrice);
                    if (size > 0) {
                        bestPrice = priceToOrders.firstKey();
                    }
                }
            }
        }
    }
}
