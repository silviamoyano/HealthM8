package es.studium.healthm8.io;

import java.util.List;

import es.studium.healthm8.ui.citas.Citas;
import es.studium.healthm8.ui.especialidad.Especialidades;
import es.studium.healthm8.ui.medicamentos.Medicamentos;
import es.studium.healthm8.usuarios.Usuarios;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService
{
    //USUARIOS
    //Alta
    @POST("usuarios")
    Call<Void> altaUsuario(@Body Usuarios usuarios);

    //Lectura
    @GET("usuarios/getall")
    Call<List<Usuarios>> getAllUsuarios();

    //CITAS
    //Alta
    @POST("citas")
    Call<Void> altaCita(@Body Citas cita);

    //Lectura
    /*Todas las citas segun el idUsuarioFK*/
    @GET("citas/usuarios/{idUsuarioFK}")
    Call<List<Citas>> obtenerCitasPorUsuario(@Path("idUsuarioFK") int idUsuarioFK);

    /*Todos los datos segun el idCita*/
    @GET("citas/id/{idCita}")
    Call<Citas> obtenerCitaPorId(@Path("idCita") int idCita);

    //Eliminar
    @DELETE("citas/id/{idCita}")
    Call<Void> eliminarCitaPorId(@Path("idCita") int idCita);

    //Modificar
    @PUT("citas/id/{idCita}")
    Call<Void> actualizarCita(@Path("idCita") int idCita, @Body Citas citaActualizada);

    //ESPECIALIDADES
    //Lectura
    @GET("especialidades/getall")
    Call<List<Especialidades>> getAllEspecialidades();
    @GET("especialidades/{idEspecialidad}")
    Call<List<Especialidades>> obtenerEspecialidadesPorId();

    //MEDICAMENTOS
    //Lectura
    @GET("medicamentos/usuarios/{idUsuarioFK}")
    Call<List<Medicamentos>> obtenerMedicamentosPorUsuario(@Path("idUsuarioFK") int idUsuarioFK);

    @GET("medicamentos/id/{idMedicamento}")
    Call<List<Medicamentos>> obtenerMedicamentosPorId(@Path("idMedicamento") int idMedicamento);


}
