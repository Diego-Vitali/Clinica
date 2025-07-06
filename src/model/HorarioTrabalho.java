package model;

import java.time.LocalTime;

public class HorarioTrabalho {
	
	private int id;
    private int medicoId;
    private String diaSemana; // Mapeia ENUM('DOMINGO', 'SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO')
    private LocalTime horaInicio;
    private LocalTime horaFim;

    public HorarioTrabalho() {
    }

    public HorarioTrabalho(int id, int medicoId, String diaSemana, LocalTime horaInicio, LocalTime horaFim) {
        this.id = id;
        this.medicoId = medicoId;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getMedicoId() {
        return medicoId;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMedicoId(int medicoId) {
        this.medicoId = medicoId;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
}
