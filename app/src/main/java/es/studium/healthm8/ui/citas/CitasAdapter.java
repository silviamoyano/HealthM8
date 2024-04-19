package es.studium.healthm8.ui.citas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.citas.Citas;

/*Representa la vista individual de cada elemento en el RecyclerView*/
public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitasViewHolder>
{
    private List<Citas> items;
    private NavController navController;
    private  Context context;

    public static class CitasViewHolder extends RecyclerView.ViewHolder
    {
        //Campos respectivos de un item
        public TextView fechaCita;
        public TextView especialidadCita;
        public TextView horaCita;

        public CitasViewHolder(@NonNull View v) {
            super(v);
            //Asociamos los elementos del cardview a la vista
            fechaCita = v.findViewById(R.id.TextView_fechaCita);
            especialidadCita = v.findViewById(R.id.TextView_especialistaCita);
            horaCita = v.findViewById(R.id.TextView_horaCita);
        }
    }

    public CitasAdapter(List<Citas> items, NavController navController, Context context) {
        this.items = items;
        this.navController = navController;
        this.context = context;
    }

    /*Devolvemos el tamaño de la lista*/
    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public CitasAdapter.CitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.citas_card, parent, false);
        return new CitasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CitasViewHolder viewHolder, int position) {
        //Llamamos a la API
        Citas cita = items.get(position);
        viewHolder.fechaCita.setText("26/03/2024");
        viewHolder.horaCita.setText("08:15");
        viewHolder.especialidadCita.setText("Médico de Familia");

        // Agregamos el clic del elemento del RecyclerView
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {}
        });
    }
}
