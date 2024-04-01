package es.studium.healthm8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import es.studium.healthm8.io.ApiAdapter;
import es.studium.healthm8.ui.citas.CitasFragment;
import es.studium.healthm8.usuarios.NuevoUsuarioActivity;
import es.studium.healthm8.usuarios.Usuarios;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    //Creamos lo objetos
    ImageView imagen;
    EditText editTextUsuario;
    EditText editTextPassword;
    Switch switchGuardarCredenciales;
    Button btnAcceder;
    Button btnRegistrarse;

    /*SharedPreferences*/
    //Nombre del xml
    public static final String MyPREFERENCES = "LoginCredentials";
    /*Los campos que vamos a guardar*/
    public static final String Nombre = "nombreKey";
    public static final String Password = "passwordKey";
    //Creamos la sharedPreferences
    SharedPreferences sharedpreferences;

    int idUsuarioLogueado;

    Usuarios usuarios;

    //Habilitamos el botón acceder según los campos de texto
    /* Para ello, usamos un rasterador de texto. Cada vez que el EditText cambie,
     * esto lo analizará.
     * El botón Acceder se habilitará si los campos han sido cumplimentados y cumplen con los
     * requisitos. */
    TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable)
        {
            // Obtenemos el dato del campo password
            String password = editTextPassword.getText().toString();

            // Comprobamos el formato de la contraseña
            boolean passwordValida = comprobarPassword(password);

            if (editable.toString().isEmpty())
            {
                editTextUsuario.setError("Escribe tu nombre");
            }
            else if (!passwordValida)
            {
                editTextPassword.setError("Debe contener números, un caracter especial y una letra");
            }
            Log.d("MnsjAfterTextChanged.", "El usuario y la contraseña son correctos");
            btnAcceder.setEnabled(camposCumplimentados());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Asociamos los objetos a la vista
        imagen = findViewById(R.id.imageView);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        switchGuardarCredenciales = findViewById(R.id.switch_guardar);
        btnAcceder = findViewById(R.id.button_acceder);
        btnRegistrarse = findViewById(R.id.button_registrarse);

        // Mostrar mensaje de error para el campo de usuario
        editTextUsuario.setError("Escribe tu nombre");
        //Deshabilitamos el botón
        btnAcceder.setEnabled(false);

        //Asocio el TextWatcher a los campos de texto
        editTextUsuario.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);

        /*Creamos el sharedPreferences, indicando los dos parametros, el nombre que recibirá
         * el fichero .xml y el modo en el que lo creamos, que será privado.*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //Comprabación de si hay datos en LoginCredentials.xml
        String isShared = sharedpreferences.getString(Nombre, "");
        if(!isShared.isEmpty())
        {
            //Abrimos el MainActivity si tenemos datos dentor del fichero
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            // Mostrar mensaje de error para el campo de usuario
            editTextUsuario.setError("Escribe tu nombre");
        }

        //Asociamos los listener
        btnAcceder.setOnClickListener(this);
        btnRegistrarse.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_acceder)
        {
            if (camposCumplimentados())
            {
                //Leer todos los usuarios de la API
                obtenerUsuarios();
                if (switchGuardarCredenciales.isChecked())
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Nombre, editTextUsuario.getText().toString());
                    editor.putString(Password, editTextPassword.getText().toString());
                    editor.putInt("idUsuario", idUsuarioLogueado); // Guardar el ID de usuario
                    editor.apply();
                }
            }
        }
        //Botón Resgistrarse
        else
        {
            //Abrimos NuevoUsuarioActivity
            Intent intent = new Intent(this, NuevoUsuarioActivity.class);
            startActivity(intent);
        }
    }

    //Método para comprobar que los campos están cumplimentados
    @SuppressLint("WrongConstant")
    public boolean camposCumplimentados()
    {
        //Obtenemos los datos de los campos
        String usuario = editTextUsuario.getText().toString();
        Log.d("Mnsj.", "editTextUsuario: " + usuario);
        String password = editTextPassword.getText().toString();
        Log.d("Mnsj.", "editTextPassword: " + password);

        // Para comprobar el formato de la contraseña
        boolean passwordValida = comprobarPassword(password);

        if (!passwordValida)
        {
            return false;  // Retorna false ya que la contraseña no es válida
        }

        Log.d("Mnsj.", "camposCumplimentados - Campos Completos: " + (!usuario.isEmpty() && !password.isEmpty()));
        Log.d("Mnsj.", "camposCumplimentados - Password Válida: " + passwordValida);

        return true;  // Retorna true solo si los campos están completos y la contraseña es válida
    }
    // Método para validar la contraseña
    public boolean comprobarPassword(String password)
    {
        //La contraseña tiene que tener números, un caracter especial y una letra

        boolean passwordValida = password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!.;])(?=\\S+$).*$");

        Log.d("Mnsj.", "comprobarPassword: " + passwordValida);

        return passwordValida;
    }
    public void obtenerUsuarios()
    {
        //Llamamos a la API
        Call<List<Usuarios>> callAllUsuarios = ApiAdapter.getApiService().getAllUsuarios();
        callAllUsuarios.enqueue(new Callback<List<Usuarios>>()
        {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response)
            {
                if (response.isSuccessful())
                {
                    List<Usuarios> usuariosList = response.body();
                    if (usuariosList != null && !usuariosList.isEmpty())
                    {
                        // Verificar si algún usuario coincide con los datos ingresados
                        String nombreUsuario = editTextUsuario.getText().toString();
                        String claveUsuario = editTextPassword.getText().toString();
                        for (Usuarios usuario : usuariosList)
                        {
                            if (usuario.getNombreUsuario().equals(nombreUsuario) && usuario.getClaveUsuario().equals(claveUsuario))
                            {
                                //Guardamos el id de usuario logueado
                                idUsuarioLogueado = usuario.getIdUsuario();
                                Log.d("Mnsj. LoginActivityAPI", "idUsuarioLogueado: " + idUsuarioLogueado);

                                // Acceder al MainActivity
                                abrirMainActivity();
                                return; // Salir del bucle si se encuentra una coincidencia
                            }
                        }
                        // Si no se encontró ninguna coincidencia, mostrar un Toast
                        mostrarToast("Tienes que darte de alta primero");
                    }
                    else
                    {
                        // Si la lista de usuarios está vacía, mostrar un Toast
                        mostrarToast("No hay usuarios registrados");
                    }
                }
                else
                {
                    // Manejar el error de la llamada a la API
                    Log.d("Error", "Error al leer usuarios: " + response.message());
                    mostrarToast("Error al leer usuarios");
                }
            }
            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t)
            {
                Log.d("Error", "Error al leer usuarios: " + t.getMessage());
                mostrarToast("Error al leer usuarios");
            }
        });
    }

    public void abrirMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        //Pasamos el id al nuevo activity
        intent.putExtra("idUsuarioLogueado", idUsuarioLogueado);
        Log.d("Mnsj. LoginActivityIntent", "idUsuarioLogueado: " + idUsuarioLogueado);
        startActivity(intent);
        finish();
    }

    public void mostrarToast(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
