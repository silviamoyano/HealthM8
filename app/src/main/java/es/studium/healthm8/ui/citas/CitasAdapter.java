package es.studium.healthm8.ui.citas;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.ui.especialidad.Especialidades;

/*Representa la vista individual de cada elemento en el RecyclerView*/
public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitasViewHolder>
{
    private List<Citas> items;
    private NavController navController;
    private  Context context;

    public void setItems(List<Citas> listaCitasUsuario) {this.items = listaCitasUsuario;}

    public static class CitasViewHolder extends RecyclerView.ViewHolder
    {
        //Campos respectivos de un cardView
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
    //Contructor del adapter
    public CitasAdapter(List<Citas> items, NavController navController, Context context) {
        this.items = items;
        this.navController = navController;
        this.context = context;
    }

    /*Devolvemos el tamaño de la lista para poder cargarla*/
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
    public void onBindViewHolder(@NonNull CitasViewHolder viewHolder, int position) {
       //Creamos un objeto de tipo Cita y obetenemos su posicion
        Citas cita = items.get(position);

        /*Obtenemos los items y trabajamos con ellos para poder asignarlos a la vista*/
        //Obtenemos los datos de la BD
        String fechaCitaBD = cita.getFechaCita();
        String horaCitaBD = cita.getHoraCita();
        Especialidades especialidadesBD = cita.getEspecialidades();//obtengo: id y nombre
        String nombreEspecialidad = cita.getEspecialidades().getNombreEspecialidad();//nombre

        Log.d("Mnsj. CitasAdapter", "fechaCitaBD: " + fechaCitaBD);
        Log.d("Mnsj. CitasAdapter", "horaCitaBD: " + horaCitaBD);
        Log.d("Mnsj. CitasAdapter", "especialidadCitaBD: " + nombreEspecialidad);

        //Formatear fecha
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleDateFormat formateoDeseado = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada;
        try {
            Date fechaParseada = formatoOriginal.parse(fechaCitaBD);
            fechaFormateada = formateoDeseado.format(fechaParseada);
            Log.d("Mnsj. CitasAdapter", "fechaParseada: " + fechaParseada);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        //String fechaFormateada = formateoFecha.format(fechaCitaBD);

        //Formatear hora
        String horaFormateada = formateoHora(horaCitaBD);
        String especialidadCita = nombreEspecialidad;

        //Asignamos los datos a los elementos del cardview
        viewHolder.fechaCita.setText(fechaFormateada);
        viewHolder.horaCita.setText(horaFormateada);
        viewHolder.especialidadCita.setText(nombreEspecialidad);

        // Agregamos el clic del elemento del RecyclerView
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crear Bundle y agregar datos
                Bundle args = new Bundle();
                args.putString("fechaCita", fechaFormateada);
                args.putString("horaCita", horaCitaBD);
                args.putString("especialidadCita", nombreEspecialidad);
                //Añadir más argumentos de las citas para la siguiente vista
                Log.d("Mnsj. CitasAdapter", "=============================================================================" );
                Log.d("Mnsj. CitasAdapter", "item pulsado:" + cita.getIdCita());//obtenemos id de la cita pulsada
                Log.d("Mnsj. CitasAdapter", "=============================================================================" );

                // Navegar al fragmento de detalles usando NavController
                // Obtener el NavController y navegar al fragmento de detalles con argumentos
//                Navigation.findNavController(v).navigate(R.id.nav_detalles_citas, args);
            }
        });
    }

    // Método para formatear la hora
    public String formateoHora(String hora) {
        String[] partesHora = hora.split(":");
        int horaInt = Integer.parseInt(partesHora[0]);
        int minutosInt = Integer.parseInt(partesHora[1]);

        return String.format("%02d:%02d", horaInt, minutosInt);
    }

    public void actualizarLista(List<Citas> listadoCitas)
    {
        this.items = listadoCitas;
        notifyDataSetChanged();//Cargará toda la lista
    }
}
