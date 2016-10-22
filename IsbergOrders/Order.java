package IsbergOrders;

import  java.text.DecimalFormat;
import  java.text.DecimalFormatSymbols;

import static java.lang.Integer.min;

/**
 * Created by d-sun-d on 09.10.2016.
 */
public class Order {
    public char orderType;
    public int orderId;
    public int price;
    public int publicSize;
    private int defaultSize;
    public int leftPeakSize;
    public int dealSize = 0;

    public Order(
            char initOrderType,
            int initOrderId,
            int initPrice,
            int initPublicSize,
            int initPeakSize
            ){
        orderType = initOrderType;
        orderId = initOrderId;
        price = initPrice;
        publicSize = initPublicSize;
        defaultSize = initPublicSize;
        leftPeakSize = initPeakSize - publicSize;
    }

    public void printSlef(){
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,###", otherSymbols);
        if (orderType == 'B'){
            System.out.print(String.format(
                    "%10d|%13s|%7s",
                    orderId,
                    formatter.format(publicSize),
                    formatter.format(price)
            ));
        } else{
            System.out.print(String.format(
                    "%7s|%13s|%10d",
                    formatter.format(price),
                    formatter.format(publicSize),
                    orderId
            ));

        }
    }

    public void Deal(int size){
        dealSize += size;
        publicSize -= size;
        if (publicSize == 0){
            if (leftPeakSize >= defaultSize){
                publicSize = defaultSize;
                leftPeakSize -= defaultSize;
            }else{
                publicSize = leftPeakSize;
                leftPeakSize = 0;
            }
        }
        // for debug you can tern on
        // printSlef();
        // System.out.print("\n");
    }

    public void printDealResult(int inOrderId){
        if (dealSize > 0){
            if (orderType == 'B'){
                System.out.println(orderId +","+inOrderId+","+price+","+dealSize);
            }else{
                System.out.println(inOrderId+","+ orderId +","+price+","+dealSize);
            }
            dealSize = 0;
        }
    }

    public boolean match(Order inOrder){
        // return True if current order deal all it publicSize else return False
        // check if can not deal
        if ((inOrder.orderType == orderType)||(inOrder.orderType == 'B' &&(inOrder.price < price))||
                ((inOrder.orderType == 'S')&&(inOrder.price > price))){
            return Boolean.FALSE;
        }else{
            if (publicSize > inOrder.publicSize){
                int dealSize = inOrder.publicSize;
                Deal(dealSize);
                inOrder.Deal(dealSize);
                return  Boolean.FALSE;
            }else{
                int dealSize = publicSize;
                Deal(dealSize);
                inOrder.Deal(dealSize);
                return  Boolean.TRUE;
            }
        }
    }

    public void setToDefaultPublicSize(){
        if ((defaultSize > publicSize) && (leftPeakSize > 0)){
            int moveValue = min(defaultSize - publicSize, leftPeakSize);
            publicSize += moveValue;
            leftPeakSize -= moveValue;
        }
    }
}
