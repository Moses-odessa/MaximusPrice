package ua.moses.maximusprice;

import java.util.List;

class Order {
    private List<Good> goods;

    Order(List<Good> goods) {
        this.goods = goods;
    }
    double getOrderSumm(){
        double result = 0;
        for (Good good: goods) {
            result += good.getPrice() * good.getOrder();
        }
        return result;
    }

    int getOrderQuantity(){
        int result = 0;
        for (Good good: goods) {
            result += good.getOrder();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Good good: goods){
            result.append(good.getName()).append(" - ").append(good.getOrder()).append(" x ").append(good.getPrice()).append("\n");
        }
        result.append("%s ").append(getOrderSumm());
        return result.toString();
    }
}
