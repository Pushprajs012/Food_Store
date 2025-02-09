package com.ps.foodstore.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.foodstore.Application;
import com.ps.foodstore.Data.User;
import com.ps.foodstore.R;
import com.ps.foodstore.databinding.ActivityCheckoutBinding;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityCheckoutBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private Boolean cod=false;
    private int isradiobtnselected=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        fetchDataFromFirebase();

        binding.paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {

            binding.creditCardDetails.setVisibility(View.GONE);
            isradiobtnselected=checkedId;


            // Show the corresponding payment option details based on the selected radio button
            if (checkedId == R.id.credit_card_option) {
                binding.creditCardDetails.setVisibility(View.VISIBLE);
                cod=false;
            } else if (checkedId == R.id.debit_card_option) {
                binding.creditCardDetails.setVisibility(View.VISIBLE);
                cod=false;
            } else if (checkedId == R.id.payment_portal_option) {
                cod=true;
            }
        });
    }

    private void init(){
        db=((Application) getApplicationContext()).getFirebaseDatabase();
        auth=((Application) getApplicationContext()).getFirebaseAuth();
        binding.proceedButton.setOnClickListener(this);
    }

    private void fetchDataFromFirebase(){
        String userId = auth.getCurrentUser().getUid();
        db.getReference("Users").child(userId).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    User user = snapshot.getValue(User.class);
                    binding.name.setText(user.getFullName());
                    binding.emailAddress.setText(user.getEmail());
                    binding.mailingAddress.setText(user.getAddress());
                    binding.phoneNumber.setText(user.getMobile());
                    binding.totalprice.setText("$"+String.valueOf(getIntent().getIntExtra("totalPrice",0)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(CheckoutActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.proceed_button){


            if (velidation()){
                db.getReference("Users").child(auth.getCurrentUser().getUid()).child("Cart").removeValue();
                Toast.makeText(this, "payment success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ThankuActivity.class));
            finish();
        }}
    }

    private boolean velidation() {
        if (isradiobtnselected == -1) {
            Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!cod) {
                if (binding.cardNumber.getText().toString().isEmpty() || binding.cardNumber.getText().toString().length() != 16) {
                    binding.cardNumberLayout.setError("Invalid Card Number");
                    return false;
                } else if (binding.expiryDate.getText().toString().isEmpty() || binding.expiryDate.getText().toString().length() != 4) {
                    binding.expiryDate.setError("Invalid Expiry Date");
                    return false;
                } else if (binding.cvv.getText().toString().isEmpty() || binding.cvv.getText().toString().length() != 3) {
                    binding.cvvInputLayout.setError("Invalid CVV");
                    return false;
                }
                return true;
            }
            return true;
        }
    }
}