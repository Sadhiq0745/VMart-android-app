package com.example.villagemart.MenuFiles;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villagemart.PlaceOrderActivity;
import com.example.villagemart.R;
import com.example.villagemart.model.Cart;
import com.example.villagemart.viewholder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CartActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private AppCompatButton nextBtn;
    RecyclerView.LayoutManager layoutManager;
    TextView totalprice;
    private int overallPrice = 0;

    Toolbar cartToolbar;
    private EditText editTextCoupon;
    private TextView textViewDiscount;
    private Button buttonApplyCoupon;
    private Button buttonGenerateCoupon;

    FirebaseAuth auth;

    LinearLayout dynamicContent, bottomNavBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_cart);

        dynamicContent = findViewById(R.id.dynamicContent);
        bottomNavBar = findViewById(R.id.bottomNavBar);

        View wizard = getLayoutInflater().inflate(R.layout.activity_cart, null);
        dynamicContent.addView(wizard);

        RadioGroup rg = findViewById(R.id.radioGroup1);
        RadioButton rb = findViewById(R.id.bottom_cart);

        rb.setBackgroundColor(R.color.item_selected);
        rb.setTextColor(Color.parseColor("#3F5185"));

        cartToolbar = findViewById(R.id.cart_toolbar);
        cartToolbar.setBackgroundResource(R.drawable.bg_color);

        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextBtn = findViewById(R.id.next_button);
        totalprice = findViewById(R.id.totalprice);

        editTextCoupon = findViewById(R.id.editTextCoupon);
        textViewDiscount = findViewById(R.id.textViewDiscount);
        buttonApplyCoupon = findViewById(R.id.buttonApplyCoupon);
        buttonGenerateCoupon = findViewById(R.id.buttonGenerateCoupon);

        nextBtn.setBackgroundResource(R.drawable.bg_color);

        buttonApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCoupon();
            }
        });

        buttonGenerateCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCoupon();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalprice.getText().toString().equals("₹0") || totalprice.getText().toString().length() == 0) {
                    Toast.makeText(CartActivity.this, "Cannot place order with 0 items", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CartActivity.this, PlaceOrderActivity.class);
                    intent.putExtra("totalAmount", totalprice.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(auth.getCurrentUser().getUid())
                        .child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                        String name = model.getName().replaceAll("\n", " ");
                        holder.cartProductName.setText(name);
                        holder.cartProductPrice.setText(model.getPrice());
                        String intPrice = model.getPrice().replace("₹", "");
                        overallPrice += Integer.valueOf(intPrice);
                        totalprice.setText("₹" + String.valueOf(overallPrice));

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Delete item");
                                builder.setMessage("Do you want to remove this product from cart?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                cartListRef.child("User View")
                                                        .child(auth.getCurrentUser().getUid())
                                                        .child("Products")
                                                        .child(model.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    String intPrice = model.getPrice().replace("₹", "");
                                                                    overallPrice -= Integer.valueOf(intPrice);
                                                                    totalprice.setText("₹" + String.valueOf(overallPrice));
                                                                    Toast.makeText(CartActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    // Method to apply the coupon discount
    private void applyCoupon() {
        String coupon = editTextCoupon.getText().toString();
        if (!TextUtils.isEmpty(coupon)) {
            int discountPercentage = calculateDiscount(coupon);
            textViewDiscount.setText("Discount Applied: " + discountPercentage + "%");
            textViewDiscount.setVisibility(View.VISIBLE);
            applyDiscount(discountPercentage);
        } else {
            textViewDiscount.setText("Please enter a valid coupon code.");
            textViewDiscount.setVisibility(View.VISIBLE);
        }
    }

    // Method to calculate discount based on the coupon code
    private int calculateDiscount(String coupon) {
        int sum = 0;
        for (char c : coupon.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            }
        }
        return sum % 2 == 0 ? 15 : 10;
    }

    // Method to apply discount to total price
    private void applyDiscount(int discountPercentage) {
        double totalPrice = Double.parseDouble(totalprice.getText().toString().replace("₹", ""));
        double discountedPrice = totalPrice - (totalPrice * discountPercentage / 100);
        totalprice.setText("₹" + String.format("%.2f", discountedPrice));
    }

    // Method to generate a random coupon
    private void generateCoupon() {
        String coupon = generateRandomCoupon();
        editTextCoupon.setText(coupon);
    }

    // Generate a random coupon number
    public static String generateRandomCoupon() {
        Random random = new Random();
        int coupon = random.nextInt(99999); // Up to 5 digits
        return String.format("%05d", coupon); // Zero-padded to 5 digits
    }
}
