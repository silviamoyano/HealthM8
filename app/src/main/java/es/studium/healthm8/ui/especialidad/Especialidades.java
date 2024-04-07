package es.studium.healthm8.ui.especialidad;

public class Especialidades
{
    private int idEspecialidad;
    private String nombreEspecialidad;

    public Especialidades()
    {
        idEspecialidad = 0;
        nombreEspecialidad = "";
    }

    public Especialidades(int idEspecialidad, String nombreEspecialidad) {
        this.idEspecialidad = idEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }
}
