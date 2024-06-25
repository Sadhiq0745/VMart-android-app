package com.example.villagemart;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class PlaceOrderActivity extends AppCompatActivity implements PaymentResultListener {


    EditText shipName, shipPhone, shipAddress, shipCity;
    AppCompatButton confirmOrder;
    FirebaseAuth auth;
    Intent intent;
    String totalAmount;
    TextView cartpricetotal;

    int amount;
    Toolbar cartToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        shipName=findViewById(R.id.shipName);
        shipPhone = findViewById(R.id.shipPhone);
        shipAddress = findViewById(R.id.shipAddress);
        shipCity = findViewById(R.id.shipCity);
        confirmOrder = findViewById(R.id.confirmOrder);
        cartpricetotal = findViewById(R.id.cartpricetotal);
        cartToolbar = findViewById(R.id.cart_toolbar);

        auth = FirebaseAuth.getInstance();

        cartToolbar.setBackgroundResource(R.drawable.bg_color);
        confirmOrder.setBackgroundResource(R.drawable.bg_color);

        intent = getIntent();
        totalAmount = intent.getStringExtra("TotalAmount");

        cartpricetotal.setText(totalAmount);

        String sAmount = "100";
        amount = Math.round(Float.parseFloat(sAmount) * 100);
        cartpricetotal.setText(totalAmount);


        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check();
            }
        });


}

    private void check(){
        if (TextUtils.isEmpty(shipName.getText().toString())) {
            shipName.setError("Enter Name");
            Toast.makeText(this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shipPhone.getText().toString())) {
            shipPhone.setError("Enter Mobile No.");
            Toast.makeText(this, "Please Enter Your Mobile No.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shipAddress.getText().toString())) {
            shipAddress.setError("Enter Address");
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shipCity.getText().toString())) {
            shipCity.setError("Enter phone no.");
            Toast.makeText(this, "Please Enter Your City", Toast.LENGTH_SHORT).show();
        } else {
            paymentFunc();
        }
    }

    private  void paymentFunc(){                                             //Setting our razor pay for the Payments.....
        Checkout checkout= new Checkout();
        checkout.setKeyID("rzp_test_9Yo4I2UZdFBNcQ");                       //Our Key for the payment process

        checkout.setImage(R.drawable.rzp_logo);

        JSONObject object= new JSONObject();
        try{
            object.put("name","Android User");

            //put description
            object.put("description","Test Payment");

            //put currency unit
            object.put("currency","INR");

            //put amount
            object.put("amount",amount);

            //put mobile number
            object.put("prefill.contact","9876543210");

            //put email
            object.put("prefill.email","androiduser@rzp.com");


            //opening rzr pay checkout activity

            checkout.open(PlaceOrderActivity.this,object);


        }catch(JSONException e){
            e.printStackTrace();

        }
    }

    private  void confirmOrderFunc(){
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()                //for every user there will bew different Orders db
                .child("Orders").child(auth.getCurrentUser().getUid()).child("History")
                .child(saveCurrentDate.replaceAll("/","-")+" "+saveCurrentTime);

        HashMap<String, Object> ordersMap= new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",shipName.getText().toString());
        ordersMap.put("phone",shipPhone.getText().toString());
        ordersMap.put("address",shipAddress.getText().toString());
        ordersMap.put("city",shipCity.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {                                                           //If the user order is succesfull, the we gonna remove everybthing from the DB
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(auth.getCurrentUser().getUid())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(PlaceOrderActivity.this, "Your 0rder Has Been Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intentcart= new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                        intentcart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentcart);
                                        finish();
                                    }


                                }
                            });


                }
            }
        });




    }
    /* @Override
    protected void onStart() {
        super.onStart();
        checkBiometricSupport();
    }

    private void checkBiometricSupport() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(this, "Lock screen security not enabled", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_BIOMETRIC}, REQUEST_CODE);
            } else {
                generateKey();
                if (cipherInit()) {
                    FingerprintHelper helper = new FingerprintHelper(this);
                    helper.startAuth(mAuth);
                }
            }
        }
    }

    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (Exception e) {
            Log.e("Fingerprint", "Cipher initialization failed: " + e);
            return false;
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (Exception e) {
            Log.e("Fingerprint", "Failed to init Cipher: " + e);
            return false;
        }
    }

    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            Log.e("Fingerprint", "Failed to generate key: " + e);
        }
    }
}

     */



    @Override
    public void onPaymentSuccess(String s) {
        confirmOrderFunc();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(s);
        builder.show();


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();


    }
}