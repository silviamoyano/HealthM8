package es.studium.healthm8.ui.citas;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import java.util.Date;

public class DialogoModificarCita extends DialogFragment
{
    //Atributos
    Spinner spinnerEspecialidades;
    EditText editTextFecha;
    EditText editTextHora;
    EditText editTextLugar;
    EditText editTextNombreMedico;
    RadioButton radioBtnEsOnline;
    RadioButton radioBtnEsTelefonica;
    Button btnRecordatorioCita;

    int idUsuarioLogueado;

    OnDialogoCitaListener mListener;

    int idEspecialidad;
    String nombreEspecialidad;
    String fecha;
    Date fechaDate;
    String fechaBD;
    Date fechaBDdate;
    String hora;
    String horaBD;
    String lugar;
    String nombreMedico;
    int esOnlineChecked;
    int esTelefonicaChecked;
    int idUsuarioFK;
}
