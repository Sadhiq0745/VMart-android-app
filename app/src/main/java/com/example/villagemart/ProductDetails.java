package com.example.villagemart;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villagemart.model.AddProdModel;
import com.example.villagemart.viewholder.RelatedProductsHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.example.villagemart.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

//import androidx.appcompat.widget.Toolbar;

public class ProductDetails extends AppCompatActivity {

    Intent intent;
    ImageView productImg;
    TextView productName, productCategory, productDesc, productPrice;
    AppCompatButton order;
    Toolbar detailsToolbar;

    FirebaseAuth auth;
    String uniqueId;
    ImageView back;
    RecyclerView related_prod_list;
    String relCategory;
    String name;
    String checkName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        auth = FirebaseAuth.getInstance();

        productImg = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productCategory = findViewById(R.id.product_category);
        productDesc = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        detailsToolbar = findViewById(R.id.detailsToolbar);

        detailsToolbar.setBackgroundResource(R.drawable.bg_color);

        back = findViewById(R.id.product_back);

        order = findViewById(R.id.order);
        related_prod_list = findViewById(R.id.related_prod_list);

        related_prod_list.setLayoutManager(new LinearLayoutManager(ProductDetails.this, LinearLayoutManager.HORIZONTAL, true));
        intent = getIntent();


        productCategory.setText(intent.getStringExtra("category"));

        int id = intent.getIntExtra("id", 1);
        uniqueId = intent.getStringExtra("uniqueId");
        relCategory = productCategory.getText().toString();

        if (id == 1) {
            uniqueId = uniqueId.replaceAll("\n", " ");
            productName.setText(intent.getStringExtra("name").replaceAll("\n", " "));
            productPrice.setText("₹300");
            productDesc.setText("Brand\tLay'sFlavour\tCream and Onion, Masala, TomatoNet Quantity\t208.0 gramNumber of Items\t1Type\tPotato ChipDiet Type\tVegetarianItem Form\tChipsItem Weight\t208 GramsSize\t208 g (Pack of 1)Package Weight\t0.24 Kilograms");
            productImg.setImageResource(R.drawable.shoe1);
        } else if (id == 2) {
            uniqueId = uniqueId.replaceAll("\n", " ");
            productName.setText(intent.getStringExtra("name").replaceAll("\n", " "));
            productPrice.setText("₹2500");
            productDesc.setText("Brand\tIndia GateDiet Type\tVegetarianNumber of Items\t1Net Quantity\t25000.0 gramItem Weight\t25 Kilogram");
            productImg.setImageResource(R.drawable.shoe2);
        } else if (id == 3) {
            productName.setText(intent.getStringExtra("name").replaceAll("\n", " "));
            productPrice.setText(intent.getStringExtra("pprice"));
            String img = intent.getStringExtra("imageName");
            productDesc.setText(intent.getStringExtra("description"));
            productImg.setImageResource(this.getResources().getIdentifier(img, "drawable", this.getPackageName()));
        } else {
            productName.setText(intent.getStringExtra("addProdName"));
            productPrice.setText(intent.getStringExtra("addProdPrice"));
            productCategory.setText(intent.getStringExtra("addProdCategory"));
            productDesc.setText(intent.getStringExtra("addProdDesc"));
            String img = intent.getStringExtra("img");
            Picasso.get().load(img).into(productImg);
        }
        back.bringToFront();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetails.this, HomeActivity.class));
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });
        onStart();

    }

    private void addingToCartList() {
        String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference cartListRef =FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap= new HashMap<>();
        cartMap.put("pid",uniqueId);
        cartMap.put("name",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);

        cartListRef.child("User View").child(auth.getCurrentUser().getUid()).child("Products")
                .child(uniqueId).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            Intent intentcart = new Intent(ProductDetails.this, HomeActivity.class);
                            intentcart.putExtra("cartadd","yes");
                            startActivity(intentcart);
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference prodListRef = FirebaseDatabase.getInstance().getReference().child("View All")
                .child("User View").child("Products");


        FirebaseRecyclerOptions<AddProdModel> options = new FirebaseRecyclerOptions.Builder<AddProdModel>()
                .setQuery(prodListRef.orderByChild("category").startAt(relCategory), AddProdModel.class).build();

        FirebaseRecyclerAdapter<AddProdModel,RelatedProductsHolder> adapter=
                new FirebaseRecyclerAdapter<AddProdModel, RelatedProductsHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RelatedProductsHolder holder, int position, @NonNull AddProdModel model) {
                        name = model.getName();
                        String price = model.getPrice();
                        String imgUri = model.getImg();
                        holder.relatedProdName.setText(name);
                        holder.relatedProdPrice.setText(price);
                        Picasso.get().load(imgUri).into(holder.relatedProdImg);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(ProductDetails.this,ProductDetails.class);
                                intent.putExtra("id", 4);                                                  // id4 is for the products that are shown onn the Search Activity
                                intent.putExtra("uniqueId", name);
                                intent.putExtra("addProdName", name);
                                intent.putExtra("addProdPrice", price);
                                intent.putExtra("addProdDesc", model.getDescription());
                                intent.putExtra("addProdCategory", model.getCategory());
                                intent.putExtra("img", imgUri);
                                startActivity(intent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public RelatedProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_products_adapter, parent, false);
                        RelatedProductsHolder holder = new RelatedProductsHolder(view);
                        return holder;
                    }
                };

        related_prod_list.setAdapter(adapter);
        adapter.startListening();

    }
}