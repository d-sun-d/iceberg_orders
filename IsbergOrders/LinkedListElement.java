package IsbergOrders;

/**
 * Created by d-sun-d on 09.10.2016.
 */
public class LinkedListElement {
    public Order order;
    public LinkedListElement nextOrder;
    public LinkedListElement prevOrder;

    LinkedListElement(Order initOrder){
        order = initOrder;
    }
}
