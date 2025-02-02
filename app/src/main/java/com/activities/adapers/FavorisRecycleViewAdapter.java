package com.activities.adapers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nutrijoy.R;
import com.activities.adapers.interfaces.RecyclerViewInterface;
import com.model.produitScanne.ProduitReponse;

import java.util.ArrayList;

public class FavorisRecycleViewAdapter extends RecyclerView.Adapter<FavorisRecycleViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<ProduitReponse> liste;


    public FavorisRecycleViewAdapter(Context context, ArrayList<ProduitReponse> favoris, RecyclerViewInterface recyclerViewInterface){
        this.context=context;
        this.liste=favoris;
        this.recyclerViewInterface=recyclerViewInterface;
    }

    @NonNull
    @Override
    public FavorisRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_favoris,parent,false);
        return new FavorisRecycleViewAdapter.MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorisRecycleViewAdapter.MyViewHolder holder, int position) {
        holder.nomProduitFavoris.setText( liste
                .get(position)
                .getProduct()
                .getNomProduit()
        );
        Glide.with(context).load(liste.get(position).getProduct().getImageUrl())
                .into(holder.imageProduitFavoris);

        holder.codeProduitFavoris=liste.get(position).getCode();

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recycler_anime_deux));

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //Récupère les views créées dans le recycler_view_historique.xml
        //Agit un peu comme un une méthode onCreate().

        CardView cardView;
        ImageView imageProduitFavoris;
        TextView nomProduitFavoris;

        String codeProduitFavoris;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageProduitFavoris=itemView.findViewById(R.id.imageProduitFavoris);
            nomProduitFavoris=itemView.findViewById(R.id.nomProduitFavoris);
            cardView=itemView.findViewById(R.id.itemCardFavoris);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){

                        int pos=getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onListeClick(pos,codeProduitFavoris);
                        }
                    }
                }
            });

        }
    }


}
