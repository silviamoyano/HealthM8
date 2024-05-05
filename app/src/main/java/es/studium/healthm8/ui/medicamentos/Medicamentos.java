package es.studium.healthm8.ui.medicamentos;

public class Medicamentos
{
    //Atributos
    private int idMedicamento;
    private String nombreMedicamento;
    private int numeroPastillas;
    private int tomaMedicamento;
    private String fechaInicio;
    private String fechaFin;
    private String fechaRenovacionReceta;
    private int idUsuarioFK;

    //Constructor por defecto o vacío
    public Medicamentos()
    {
        idMedicamento = 0;
        nombreMedicamento = "";
        numeroPastillas = 0;
        tomaMedicamento = 0;
        fechaInicio = "";
        fechaFin = "";
        fechaRenovacionReceta = "";
        idUsuarioFK = 0;
    }

    //Constructor por parámetros
    public Medicamentos(int idMedicamento, String nombreMedicamento, int numeroPastillas, int tomaMedicamento) {
        this.idMedicamento = idMedicamento;
        this.nombreMedicamento = nombreMedicamento;
        this.numeroPastillas = numeroPastillas;
        this.tomaMedicamento = tomaMedicamento;
    }

    public Medicamentos(int idMedicamento, String nombreMedicamento, int numeroPastillas, int tomaMedicamento, String fechaInicio, String fechaFin, String fechaRenovacionReceta, int idUsuarioFK) {
        this.idMedicamento = idMedicamento;
        this.nombreMedicamento = nombreMedicamento;
        this.numeroPastillas = numeroPastillas;
        this.tomaMedicamento = tomaMedicamento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaRenovacionReceta = fechaRenovacionReceta;
        this.idUsuarioFK = idUsuarioFK;
    }

    //Métodos inspectores (getter y setter)

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public int getNumeroPastillas() {
        return numeroPastillas;
    }

    public void setNumeroPastillas(int numeroPastillas) {
        this.numeroPastillas = numeroPastillas;
    }

    public int getTomaMedicamento() {
        return tomaMedicamento;
    }

    public void setTomaMedicamento(int tomaMedicamento) {
        this.tomaMedicamento = tomaMedicamento;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaRenovacionReceta() {
        return fechaRenovacionReceta;
    }

    public void setFechaRenovacionReceta(String fechaRenovacionReceta) {
        this.fechaRenovacionReceta = fechaRenovacionReceta;
    }

    public int getIdUsuarioFK() {
        return idUsuarioFK;
    }

    public void setIdUsuarioFK(int idUsuarioFK) {
        this.idUsuarioFK = idUsuarioFK;
    }
}