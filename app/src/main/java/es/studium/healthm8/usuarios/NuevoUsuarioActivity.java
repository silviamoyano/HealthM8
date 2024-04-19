package es.studium.healthm8.usuarios;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import es.studium.healthm8.LoginActivity;
import es.studium.healthm8.R;
import es.studium.healthm8.io.ApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*Esta clase da de alta un nuevo usuario para tener acceso a la app.
* Una vez que se ha dado de alta, nos devuelve al LoginActivity. */
public class NuevoUsuarioActivity  extends AppCompatActivity implements View.OnClickListener
{
    EditText editTextUsuario;
    EditText editTextPassword;
    Button btnAceptar;
    Button btnCancelar;

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
            btnAceptar.setEnabled(camposCumplimentados());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_usuario_activity);

        //Asociamos los objetos a la vista
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnAceptar = findViewById(R.id.button_aceptar);
        btnCancelar = findViewById(R.id.button_cancelar);

        // Mostrar mensaje de error para el campo de usuario
        editTextUsuario.setError("Escribe tu nombre");
        //Deshabilitamos el botón
        btnAceptar.setEnabled(false);

        //Asocio el TextWatcher a los campos de texto
        editTextUsuario.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);

        //Asociamos los listener
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_aceptar)
        {
            String nombreUsuario = editTextUsuario.getText().toString();
            String claveUsuario = editTextPassword.getText().toString();
            //Conexion con la API REST
            //Dar de alta un usuario
            darAltaUsuario(nombreUsuario, claveUsuario);
        }
        //Botón cancelar
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
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

    //Dar de alta un usuario
    public void darAltaUsuario(String nombre, String clave)
    {
        //Creamos un objeto Usuario
        Usuarios usuarios = new Usuarios(nombre, clave);

        // Log para mostrar el objeto JSON que se enviará
        Gson gson = new Gson();
        String json = gson.toJson(usuarios);
        Log.d("Mnsj.JSON enviado", json);

        //Llamamos a la API
        Call<Void> callAltaUsuario = ApiAdapter.getApiService().altaUsuario(usuarios);
        //Manejamos la respuesta de la llamada a la API. Damos de alta al usuario
        callAltaUsuario.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                if (response.isSuccessful())
                {
                    // Éxito en la llamada a la API para dar de alta el pedido
                    Log.d("Mnsj. Alta Usuario ", "Usuario dado de alta correctamente");
                    //Toast.makeText(NuevoUsuarioActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    mostrarToast("Usuario registrado correctamente");
                    //Hacemos visible el otro Activity
                    Intent intent = new Intent(NuevoUsuarioActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Log.d("Mnsj.", "Abriendo LoginActivity");
                }
                else
                {
                    // Manejamos el error alta usuario
                    Log.d("Mnsj. Error Alta Usuario", "Error al dar de alta el usuario");
                    //Toast.makeText(LoginActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                //Mensaje de error en la llamada a la API
                Log.d("Mnsj. darAltaUsuario-onFailure ", "Error en la llamada a la API");
            }
        });
    }
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
