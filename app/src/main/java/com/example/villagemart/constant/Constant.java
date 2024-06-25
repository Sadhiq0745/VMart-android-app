package com.example.villagemart.constant;

import com.example.villagemart.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final List<Integer> QUANTITY_LIST = new ArrayList<>();
    static {
        for (int i=1;i<11;i++){
            QUANTITY_LIST.add(i);
        }
    }
    public static final Product PRODUCT1 = new Product(1,"Boost Chocolate Nutrition Drink",BigDecimal.valueOf(299),
            "Nutrition Drink 500 g Pet Jar\n" +
                    " Builds Bone & muscle strength\n" +
                    "For 3x3 stamina","boost");
    public static final Product PRODUCT2 = new Product(2, "Natural Guava is made with orange pulp",BigDecimal.valueOf(99),
            "Relish the goodness of fruit and fiber with B Natural\n" +
                    "made with orange pulp; it has 100% Indian fruit\n" +
                    "Goodness of Fiber, 1 Litre","juice");
    public static final Product PRODUCT3 = new Product(3,"Sunfeast Dark Fantasy Yumfills",BigDecimal.valueOf(149),"A rich chocolate pie cake experience with Sunfeast Dark Fantasy Yumfills\n" +
            "Soft, spongy cake is enrobed in rich chocolate and filled with luscious crème\n" +
            "Give into those sweet cravings with Dark Fantasy","biscuit");

    public static final List<Product> PRODUCT_LIST = new ArrayList<Product>();

    static {
        PRODUCT_LIST.add(PRODUCT1);
        PRODUCT_LIST.add(PRODUCT2);
        PRODUCT_LIST.add(PRODUCT3);
    }

    public static final String CURRENCY = "₹";

}



