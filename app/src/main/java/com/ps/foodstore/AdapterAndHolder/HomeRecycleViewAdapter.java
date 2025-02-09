package com.ps.foodstore.AdapterAndHolder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.foodstore.Data.FoodStoreData;
import com.ps.foodstore.Data.CartData;
import com.ps.foodstore.UI.ProductDetailAcivity;
import com.ps.foodstore.databinding.RowLayoutProdectBinding;

import java.util.List;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeHolder> {

    private List<FoodStoreData> foodStoreData;
    private FirebaseDatabase firebaseDatabase;
    private String uid;

    // Corrected Constructor Name
    public HomeRecycleViewAdapter(List<FoodStoreData> foodStoreData, FirebaseDatabase firebaseDatabase, String uid) {
        this.foodStoreData = foodStoreData;
        this.firebaseDatabase = firebaseDatabase;
        this.uid = uid;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLayoutProdectBinding binding = RowLayoutProdectBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HomeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        FoodStoreData storeData = foodStoreData.get(position);

        // Binding data to views
        holder.binding.tvTitle.setText(storeData.getName());
        holder.binding.tvPrice.setText("$"+storeData.getPrice());

        Glide.with(holder.binding.ivShoeImage.getContext())
                .load(storeData.getImage())
                .into(holder.binding.ivShoeImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailAcivity.class);
            intent.putExtra("fooddata", storeData.getProduct_id());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.binding.btnCart.setOnClickListener(v -> {
            //check to data is already in cart
            firebaseDatabase.getReference("Users")
                    .child(uid)
                    .child("Cart")
                    .child(storeData.getProduct_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(holder.itemView.getContext(), "Product already in cart", Toast.LENGTH_SHORT).show();

                    } else {
                        CartData cartData=new CartData(storeData.getProduct_id(),storeData.getName(),storeData.getPrice(),storeData.getSize(),storeData.getImage(),storeData.getDescription(),1);
                        firebaseDatabase.getReference("Users").child(uid).child("Cart").child(storeData.getProduct_id()).setValue(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(holder.itemView.getContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
                         }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        });
    }

    @Override
    public int getItemCount() {

        return foodStoreData.size();
    }
}
