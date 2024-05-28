package es.studium.healthm8.ui.citas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentCitasBinding;
import es.studium.healthm8.io.ApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitasDetallesFragment extends Fragment
{
    TextView especialidadCita;
    TextView fechaCita;
    TextView horaCita;
    TextView lugarCita;
    TextView esOnline;
    TextView esTelefonica;
    TextView nombreMedico;
    Button btnRecordatorio;
    Button btnEditarCitas;

    //Argumentos
    String nombreEspecialidad;
    String fechaFormateadaAmostrarTxt;
    String horaFormateadaAmostrarTxt;
    Citas cita;
    int idUsuarioLogueado;
    int idCita;
    DialogoModificarCita dialogoModificarCita;
    DialogoRecordatorioCita dialogoRecordatorioCita;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_detalles_citas, container, false);

        //Asociar los elementos a la vista
        especialidadCita = root.findViewById(R.id.TextView_especialidad);
        fechaCita = root.findViewById(R.id.TextView_fechaCita);
        horaCita = root.findViewById(R.id.TextView_horaCita);
        lugarCita = root.findViewById(R.id.TextView_lugarCita);
        esOnline = root.findViewById(R.id.TextView_esOnline);
        esTelefonica = root.findViewById(R.id.TextView_esTelefonica);
        nombreMedico = root.findViewById(R.id.TextView_medico);

        btnRecordatorio = root.findViewById(R.id.button_Recordatorio);
        btnEditarCitas = root.findViewById(R.id.button_Editar);


        //Obtener el idUsuarioLogueado
        //Recuperamos los argumentos del MainActivity
        Bundle args = getArguments();
        if(args != null)
        {
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            idCita =args.getInt("idCita", 0);
            Log.d("Mnsj. CDetallesFragment", "========================================================================");
            Log.d("Mnsj. CDetallesFragment", "idUsuarioLogueado: " + idUsuarioLogueado);
            Log.d("Mnsj. CDetallesFragment", "idCita con método obtenerIdCita: " + obtenerIdCita());
        }
        else
        {
            Log.d("Mnsj. CDetallesFragment", "No hemos recibido datos del bundle");
            Log.d("Mnsj. CDetallesFragment", "========================================================================");
        }

        //Obtenemos la cita por su ID para mostrar la ventana rellena
        obtenerCitaPorId(idCita);
        //Agregamos el Listener al botón de recordatorio
        btnRecordatorio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Abrimos el diálogo para dar modificar una cita
                abrirDialogoRecordatorio();
                Log.d("Mnsj. CDetallesFragment", "Ha pulsado botón recordatorio ");
            }
        });

        //Agregamos el Listener al botón de editar
        btnEditarCitas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Abrimos el diálogo para dar modificar una cita
                abrirDialogoModificarCita();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    // Método para obtener los datos de la cita por su ID
    public void obtenerCitaPorId(int idCita)
    {
        Call<Citas> callObtenerCita = ApiAdapter.getApiService().obtenerCitaPorId(idCita);
        callObtenerCita.enqueue(new Callback<Citas>()
        {
            @Override
            public void onResponse(Call<Citas> call, Response<Citas> response)
            {
                if (response.isSuccessful())
                {
                    cita = response.body();
                    rellenarCampos(cita);//Rellenamos los campos de la vista
                }
            }

            @Override
            public void onFailure(Call<Citas> call, Throwable t)
            {
                Log.e("Mnsj. CDetallesFragment", "Error al obtener la cita: " + t.getMessage(), t);
                mostrarToast("Error al obtener la cita");
            }
        });
    }

    // Método para rellenar los campos del diálogo con los datos de la cita
    public void rellenarCampos(Citas cita) {
        nombreEspecialidad = cita.getEspecialidades().getNombreEspecialidad();
        //Formateamos la fecha y la hora
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
        String fechaBD = cita.getFechaCita(); // yyyy-MM-dd'T'HH:mm:ss.SSSXXX
        String horaBD = cita.getHoraCita(); // HH:mm:ss
        String partes_hora[] = horaBD.split(":");
        horaFormateadaAmostrarTxt = partes_hora[0] + ":" + partes_hora[1];// HH:mm

        try {
            Date fechaDate = formatoEntrada.parse(fechaBD);
            fechaFormateadaAmostrarTxt = formatoSalida.format(fechaDate);// dd/MM/yyyy

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Rellenar los campos con los datos de la cita
        especialidadCita.setText("Especialidad: " + nombreEspecialidad);
        fechaCita.setText("Fecha: " + fechaFormateadaAmostrarTxt);
        Log.d("Mnsj. CDetallesFragment", "Fecha cita: " + fechaFormateadaAmostrarTxt);
        horaCita.setText("Hora: " + horaFormateadaAmostrarTxt);
        lugarCita.setText("Lugar: " + cita.getLugarCita());
        nombreMedico.setText("Médico: " + cita.getNombreMedico());
        if(cita.getEsOnline()== 0) {
            esOnline.setText("¿Es online? - No");
        } else {
            esOnline.setText("¿Es online? - Sí");
        }

        if(cita.getEsTelefonica()== 0) {
            esTelefonica.setText("¿Es telefónica? - No");
        } else {
            esTelefonica.setText("¿Es telefónica? - Sí");
        }
    }
    //Método para abrir el diálogo para modificar una cita
    public void abrirDialogoModificarCita()
    {
        //Creamos DialogoNuevaCita
        dialogoModificarCita = new DialogoModificarCita();
        //Convertimos el dialogo en modal
        dialogoModificarCita.setCancelable(false);
        //Pasamos los argumentos
        Bundle args = new Bundle();
        args.putString("nombreEspecialidad",nombreEspecialidad);
        args.putInt("idUsuarioLogueado", idUsuarioLogueado);
        args.putInt("idCita", idCita);
        args.putString("fechaCita", fechaFormateadaAmostrarTxt);
        args.putString("horaCita", horaFormateadaAmostrarTxt);
        args.putString("lugarCita", cita.getLugarCita());
        args.putInt("esOnline", cita.getEsOnline());
        args.putInt("esTelefonica", cita.getEsTelefonica());
        args.putString("nombreMedico", cita.getNombreMedico());

        dialogoModificarCita.setArguments(args);
        //Mostramos el dialogo
        /* Como el dialogo lo abrimos en un fragment tenemos que escribir:
         * requireActivity() para obtener una referencia a la actividad asociada (MainActivity)*/
        dialogoModificarCita.show(requireActivity().getSupportFragmentManager(),"Modificar Cita");
        Log.d("Mnsj. CDetallesFragment", "========================================================================");
        Log.d("Mnsj. CDetallesFragment", "Abrimos dialogo modificar cita");
    }

    //Método para abrir el diálogo para establecer un recodatorio de la cita
    public void abrirDialogoRecordatorio()
    {
        //Creamos DialogoNuevaCita
        dialogoRecordatorioCita = new DialogoRecordatorioCita();

        //Argumentos
        Bundle args = new Bundle();
        args.putString("nombreEspecialidad",nombreEspecialidad);
        Log.d("Mnsj. CDetallesFragment", "nombreEspecialidad: " + nombreEspecialidad);

        //Pasamos los argumentos al diálogo
        dialogoRecordatorioCita.setArguments(args);
        //Convertimos el dialogo en modal
        dialogoRecordatorioCita.setCancelable(false);
        dialogoRecordatorioCita.show(requireActivity().getSupportFragmentManager(),"Recordatorio Cita");
    }

    //Método para mostrar un Toast
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public int obtenerIdCita()
    {
      return idCita;
    }
}
