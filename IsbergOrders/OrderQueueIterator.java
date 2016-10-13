package IsbergOrders;

import java.util.Set;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by d-sun-d on 13.10.2016.
 */
public class OrderQueueIterator {
    Iterator priceIterator;
    int priceSize = 0;
    int pricePosition = 0;
    LinkedListElement currentPriceElement;

    OrderQueueIterator(OrderQueue inQueue){
        Set set = inQueue.priceToOrders.entrySet();
        priceIterator = set.iterator();
    }

    boolean hasNext(){
        if ((priceSize != pricePosition) || priceIterator.hasNext()){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    Order next(){
        if (priceSize == pricePosition){
            Map.Entry me = (Map.Entry)priceIterator.next();
            SamePriceOrderQueue value = (SamePriceOrderQueue)me.getValue();
            priceSize = value.notEmptyOrders;
            pricePosition = 0;
            currentPriceElement = value.currentOrder;
        }
        Order result = currentPriceElement.order;
        currentPriceElement = currentPriceElement.nextOrder;
        pricePosition += 1;
        return result;
    }
}
