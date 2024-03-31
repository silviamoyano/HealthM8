package es.studium.healthm8.ui.citas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.studium.healthm8.R;

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
            //Asociamos los objetos a la vista del CardView
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
    public CitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.citas_card, parent, false);
        return new CitasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CitasViewHolder viewHolder, int position)
    {
        Citas cita = items.get(position);
        //Obtenemos los datos de la BD
        Date fechaCitaBD = cita.getFechaCita();
        Time horaCitaBD = cita.getHoraCita();
        int especialidadCitaBD = cita.getIdEspecialidadFK(); // numero

        //Formatear fecha y hora
        SimpleDateFormat formateoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formateoFecha.format(fechaCitaBD);
        SimpleDateFormat formateoHora = new SimpleDateFormat("HH:mm");
        String horaFormateada = formateoHora.format(horaCitaBD);
        String especialidadCita = especialidadCitaBD +"";

        //Asignamos los datos a los elementos del cardview
        viewHolder.fechaCita.setText(fechaFormateada);
        viewHolder.horaCita.setText(horaFormateada);
        viewHolder.especialidadCita.setText(especialidadCita);

        // Agregamos el clic del elemento del RecyclerView
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {}
        });
    }
}
