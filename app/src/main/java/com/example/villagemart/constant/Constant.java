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

    public static final Product PRODUCT4 = new Product(4," Prakruti crushed Sugar",BigDecimal.valueOf(49),"Pure & Sure Organic Sugar (Chini) 1 Kg \n+" +
            "100% White Sugar Organically Processed from Quality Sugarcanes \n+" +
            " Chemical Free & Sulphur Free | Pack of 1","sugar");

    public static final Product PRODUCT5 = new Product(5,"pure Seedless Tamrind ",BigDecimal.valueOf(209),"Prakruthi Quality bazar All Natural Seedless Tamarind \n+" +
            "Imli 1kg (New Crop)","tamrind");

    public static final Product PRODUCT6 = new Product(6,"Pro Nature 100% Organic Dhaniya",BigDecimal.valueOf(369),"Taste the purity of pro nature organic spices\n" +
            "Hand picked spices where they are best grown from\n" +
            "Country of Origin: India\n" +
            "No gmo used in production\n" +
            "item_form:seeds","dhaniya");

    public static final Product PRODUCT7 = new Product(7,"Red gram pack of 1kg",BigDecimal.valueOf(155),"\n" +
            "Brand\tPrakruti\n" +
            "Item Weight\t1 Kilograms\n" +
            "Speciality\tNo preservatives\n" +
            "Item Form\tWhole\n" +
            "Diet Type\tVegetarian\n" +
            "Package Weight\t1 Kilograms","redgram");

    public static final List<Product> PRODUCT_LIST = new ArrayList<Product>();

    static {
        PRODUCT_LIST.add(PRODUCT1);
        PRODUCT_LIST.add(PRODUCT2);
        PRODUCT_LIST.add(PRODUCT3);
        PRODUCT_LIST.add(PRODUCT4);
        PRODUCT_LIST.add(PRODUCT5);
        PRODUCT_LIST.add(PRODUCT6);
        PRODUCT_LIST.add(PRODUCT7);



    }

    public static final String CURRENCY = "₹";

}



