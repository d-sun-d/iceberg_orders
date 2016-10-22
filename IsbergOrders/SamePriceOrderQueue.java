package IsbergOrders;

import java.util.ArrayList;

/**
 * Created by d-sun-d on 09.10.2016.
 */
public class SamePriceOrderQueue {
    public LinkedListElement currentOrder;
    public int notEmptyOrders;
    private ArrayList<Order> exhaustedOrders = new ArrayList<Order>();

    public SamePriceOrderQueue(Order order){
        currentOrder = new LinkedListElement(order);
        currentOrder.nextOrder = currentOrder;
        currentOrder.prevOrder = currentOrder;
        notEmptyOrders = 1;
    }

    public void addOrder(Order order){
        LinkedListElement newOrder = new LinkedListElement(order);
        newOrder.prevOrder = currentOrder.prevOrder;
        newOrder.nextOrder = currentOrder;
        currentOrder.prevOrder.nextOrder = newOrder;
        currentOrder.prevOrder = newOrder;
        notEmptyOrders += 1;
    }

    private void removeCurrentOrder(){
        notEmptyOrders -= 1;
        if (notEmptyOrders > 0){
            currentOrder.nextOrder.prevOrder = currentOrder.prevOrder;
            currentOrder.prevOrder.nextOrder = currentOrder.nextOrder;
            currentOrder = currentOrder.nextOrder;
        }
    }

    public void matchOrder(Order inOrder){
        // check if can not deal
        if ((inOrder.orderType == 'B' &&(inOrder.price < currentOrder.order.price))||
                ((inOrder.orderType == 'S')&&(inOrder.price > currentOrder.order.price))){
            return;
        }
        // do deals
        while ((inOrder.publicSize > 0) && notEmptyOrders != 0){
            boolean shouldGoNext = currentOrder.order.match(inOrder);
            if (currentOrder.order.publicSize == 0){
                exhaustedOrders.add(currentOrder.order);
                removeCurrentOrder();
            }
            if (shouldGoNext){
                currentOrder = currentOrder.nextOrder;
            }
        }
        // all deals done, print them
        for (Order order : exhaustedOrders){
            order.printDealResult(inOrder.orderId);
        }
        exhaustedOrders = new ArrayList<Order>();
        if (notEmptyOrders > 0){
            int startId = currentOrder.order.orderId;
            LinkedListElement current = currentOrder;
            int currentId = currentOrder.order.orderId;
            do{
                current.order.printDealResult(inOrder.orderId);
                current = current.nextOrder;
                currentId = current.order.orderId;
            }
            while (currentId != startId);
        }
        inOrder.dealSize = 0;
    }

}
