package shop.domain;

public class Item {
    private long id;
    private  String name;
    private int price;
    private  int stockQuantity;

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}

