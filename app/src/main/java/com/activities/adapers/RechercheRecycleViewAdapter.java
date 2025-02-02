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
import com.model.produitRecherche.Rproduit;

import java.util.ArrayList;

public class RechercheRecycleViewAdapter extends RecyclerView.Adapter<com.activities.adapers.RechercheRecycleViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<Rproduit> liste;


    public RechercheRecycleViewAdapter(Context context, ArrayList<Rproduit> recherche, RecyclerViewInterface recyclerViewInterface){
        this.context=context;
        this.liste=recherche;
        this.recyclerViewInterface=recyclerViewInterface;
    }

    @NonNull
    @Override
    public com.activities.adapers.RechercheRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_recherche,parent,false);
        return new com.activities.adapers.RechercheRecycleViewAdapter.MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull com.activities.adapers.RechercheRecycleViewAdapter.MyViewHolder holder, int position) {
        holder.codeProduitRecherche=liste.get(position).getCode();

        Glide.with(context).load(liste.get(position).getImageUrl()).into(holder.imageProduitRecherche);
        holder.nomProduitRecherche.setText( liste.get(position).getNomProduit());
        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recycler_anime_deux));

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        //Récupère les views créées dans le recycler_view_historique.xml
        //Agit un peu comme un une méthode onCreate().

        ImageView imageProduitRecherche;
        TextView nomProduitRecherche;

        String codeProduitRecherche;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageProduitRecherche=itemView.findViewById(R.id.imageProduitRecherche);
            nomProduitRecherche=itemView.findViewById(R.id.nomProduitRecherche);
            cardView=itemView.findViewById(R.id.itemCardRechercher);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){

                        int pos=getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onListeClick(pos,codeProduitRecherche);
                        }
                    }
                }
            });

        }
    }


}
