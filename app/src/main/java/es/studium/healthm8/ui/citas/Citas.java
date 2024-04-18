package es.studium.healthm8.ui.citas;

import es.studium.healthm8.ui.especialidad.Especialidades;

public class Citas
{
    //Atributos
    private int idCita;
    private String fechaCita;
    private String horaCita;
    private String lugarCita;
    private int esOnline;
    private int esTelefonica;
    private String nombreMedico;
    private Especialidades especialidades;
    private int idUsuarioFK;

    //Constructor vacío o por defecto
    public Citas()
    {
        idCita = 0;
        fechaCita = null;
        horaCita = null;
        lugarCita = "";
        esOnline = 0;
        esTelefonica = 0;
        nombreMedico = "";
        idUsuarioFK = 0;
    }
    //Constructor por parámetros


    public Citas(int idCita, String fechaCita, String horaCita, String lugarCita, int esOnline, int esTelefonica, String nombreMedico, Especialidades especialidades, int idUsuarioFK) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.lugarCita = lugarCita;
        this.esOnline = esOnline;
        this.esTelefonica = esTelefonica;
        this.nombreMedico = nombreMedico;
        this.especialidades = especialidades;
        this.idUsuarioFK = idUsuarioFK;
    }

    public Citas(int idCita, String fechaCita, String horaCita, Especialidades especialidades) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.especialidades = especialidades;

    }
    //Métodos inspectores (getter and setter)
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getLugarCita() {
        return lugarCita;
    }

    public void setLugarCita(String lugarCita) {
        this.lugarCita = lugarCita;
    }

    public int getEsOnline() {
        return esOnline;
    }

    public void setEsOnline(int esOnline) {
        this.esOnline = esOnline;
    }

    public int getEsTelefonica() {
        return esTelefonica;
    }

    public void setEsTelefonica(int esTelefonica) {
        this.esTelefonica = esTelefonica;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public Especialidades getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Especialidades especialidades) {
        this.especialidades = especialidades;
    }

    public int getIdUsuarioFK() {
        return idUsuarioFK;
    }

    public void setIdUsuarioFK(int idUsuarioFK) {
        this.idUsuarioFK = idUsuarioFK;
    }
}
