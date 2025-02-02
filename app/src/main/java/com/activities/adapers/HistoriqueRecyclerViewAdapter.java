package com.activities.adapers;


import android.content.Context;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.activities.adapers.interfaces.RecyclerViewInterface;
import com.model.produitScanne.ProduitReponse;

public class HistoriqueRecyclerViewAdapter extends RecyclerView.Adapter<HistoriqueRecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ProduitReponse> liste;
    public HistoriqueRecyclerViewAdapter(Context context, ArrayList<ProduitReponse> historique, RecyclerViewInterface recyclerViewInterface){
        this.context=context;
        this.liste=historique;
        this.recyclerViewInterface=recyclerViewInterface;
    }

    @NonNull
    @Override
    public HistoriqueRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //On inflate le layout (On affiche les rangées)
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_historique,parent,false);
        return new HistoriqueRecyclerViewAdapter.MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriqueRecyclerViewAdapter.MyViewHolder holder, int position) {
        //On assigne des valeurs aux views créées dans le recycler_view_historique.xml
        //selon la position du recyclerview
        holder.nomProduit.setText( liste
                .get(position)
                .getProduct()
                .getNomProduit()
        );
        Glide.with(context).load(liste.get(position).getProduct().getImageUrl())
                .into(holder.imageProduit);

        holder.codeProduit=liste.get(position).getCode();
        holder.date.setText(dateToString(liste.get(position).getTimestamp()));

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recycler_anime_deux));
    }

    @Override
    public int getItemCount() {
        //Combien d'éléments de l'historique on veut afficher à la fois (Pas sûr...).
        //+ logique : Combien d'élément dans l'historique.
        return liste.size();
    }

    private String dateToString(Date tmpDate){

        // Créez un formatteur pour transformer la date en chaîne de caractères
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.FRANCE);

        // Formattez la date
        if (tmpDate==null){
            return "";
        }else{
            return dateFormatter.format(tmpDate);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //Récupère les views créées dans le recycler_view_historique.xml
        //Agit un peu comme un une méthode onCreate().

        ImageView imageProduit;
        TextView nomProduit,date;

        CardView cardView;

        String codeProduit;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageProduit=itemView.findViewById(R.id.imageProduitHistorique);
            nomProduit=itemView.findViewById(R.id.nomProduitHistorique);
            cardView=itemView.findViewById(R.id.itemCardHistorique);
            date=itemView.findViewById(R.id.itemDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){

                        int pos=getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onListeClick(pos,codeProduit);
                        }
                    }
                }
            });

        }
    }

}
