package com.ps.foodstore.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.foodstore.Application;
import com.ps.foodstore.Data.CartData;
import com.ps.foodstore.R;
import com.ps.foodstore.databinding.ActivityProductDetailAcivityBinding;

public class ProductDetailAcivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityProductDetailAcivityBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private CartData cartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailAcivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        getandsetDatatoView();
        checkdataincart();


    }

    private void getandsetDatatoView() {

        firebaseDatabase.getReference("foodstore").child(getIntent().getStringExtra("fooddata")).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                cartData=new CartData(task.getResult().child("product_id").getValue(String.class),
                        task.getResult().child("name").getValue(String.class),
                        task.getResult().child("price").getValue(Integer.class),
                        task.getResult().child("size").getValue(String.class),
                        task.getResult().child("image").getValue(String.class),
                       task.getResult().child("description").getValue(String.class),
                       1);

                binding.productName.setText(task.getResult().child("name").getValue(String.class));
                binding.productPrice.setText("$"+task.getResult().child("price").getValue(Integer.class).toString());
                binding.abouttext.setText(task.getResult().child("description").getValue(String.class));


                Glide.with(binding.ivItem.getContext()).load(task.getResult().child("image").getValue(String.class)).into(binding.ivItem);

            }
        });
    }


    private void init() {
        firebaseDatabase = ((Application) getApplicationContext()).getFirebaseDatabase();
        auth = ((Application) getApplicationContext()).getFirebaseAuth();
        binding.addtocart.setOnClickListener(this);
        binding.menucart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
if (v.getId()== R.id.addtocart){

    firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()).child("Cart").child(getIntent().getStringExtra("fooddata")).setValue(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
           startActivity(new Intent(ProductDetailAcivity.this, CartActivity.class));
        }
    });


}
if (v.getId()==R.id.back){
    onBackPressed();
}
if (v.getId()==R.id.menucart){
    startActivity(new Intent(this,CartActivity.class));
}

    }

    private void checkdataincart(){
        firebaseDatabase.getReference("Users")
                .child(auth.getCurrentUser().getUid())
                .child("Cart")
                .child(getIntent().getStringExtra("fooddata"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            binding.addtocart.setText("Already In Cart");
                            binding.addtocart.setBackgroundColor(ContextCompat.getColor(ProductDetailAcivity.this, R.color.black));
                            binding.addtocart.setEnabled(false);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}