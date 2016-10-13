package IsbergOrders;
import java.util.Scanner;

/**
 * Created by d-sun-d on 09.10.2016.
 */
public class Stock {
    public OrderQueue sellsQueue = new OrderQueue('S');
    public OrderQueue buyQueue = new OrderQueue('B');

    public Stock(){};

    public void processNewOrder(Order inOrder){
        if (inOrder.orderType == 'S'){
            buyQueue.matchOrder(inOrder);
            if (inOrder.publicSize != 0) {
                sellsQueue.addOrder(inOrder);
            }
        }else{
            sellsQueue.matchOrder(inOrder);
            if (inOrder.publicSize != 0){
                buyQueue.addOrder(inOrder);
            }
        }
        printSelf();
    };

    public void printSelf(){
        System.out.println("+-----------------------------------------------------------------+");
        System.out.println("| BUY                            | SELL                           |");
        System.out.println("| Id       | Volume      | Price | Price | Volume      | Id       |");
        System.out.println("+----------+-------------+-------+-------+-------------+----------+");

        OrderQueueIterator sellIterator = new OrderQueueIterator(sellsQueue);
        OrderQueueIterator buyIterator = new OrderQueueIterator(buyQueue);
        while (sellIterator.hasNext() && buyIterator.hasNext()){
            System.out.print("|");
            buyIterator.next().printSlef();
            System.out.print("|");
            sellIterator.next().printSlef();
            System.out.print("|\n");
        }
        while (sellIterator.hasNext()){
            System.out.print("|                                |");
            sellIterator.next().printSlef();
            System.out.print("|\n");
        }
        while (buyIterator.hasNext()){
            System.out.print("|");
            buyIterator.next().printSlef();
            System.out.print("|                                |\n");
        }

        System.out.println("+-----------------------------------------------------------------+");
    }

    public void runFromStdin(){
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String[] tokens = sc.nextLine().split(",");
            char type = tokens[0].charAt(0);
            int orderId = Integer.parseInt(tokens[1]);
            int price = Integer.parseInt(tokens[2]);
            int size = Integer.parseInt(tokens[3]);
            if (tokens.length == 4){
                processNewOrder(new Order(type, orderId, price, size, size));
            } else {
                int publicSize = Integer.parseInt(tokens[4]);
                processNewOrder(new Order(type, orderId, price, publicSize, size));
            }
        }
    }
}
