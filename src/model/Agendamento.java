package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Agendamento {

	private int id;
    private int pacienteId;
    private int medicoId;
    private int procedimentoId;
    private int salaId;
    private Timestamp dataHoraInicio;
    private Timestamp dataHoraFim;
    private String status; // Mapeia ENUM
    private String observacoes;
    private int criadoPorUsuarioId;
    private Timestamp dataCriacao;

    public Agendamento() {
    }

    public Agendamento(int id, int pacienteId, int medicoId, int procedimentoId, int salaId, Timestamp dataHoraInicio, Timestamp dataHoraFim, String status, String observacoes, int criadoPorUsuarioId, Timestamp dataCriacao) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.procedimentoId = procedimentoId;
        this.salaId = salaId;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.status = status;
        this.observacoes = observacoes;
        this.criadoPorUsuarioId = criadoPorUsuarioId;
        this.dataCriacao = dataCriacao;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public int getMedicoId() {
        return medicoId;
    }

    public int getProcedimentoId() {
        return procedimentoId;
    }

    public int getSalaId() {
        return salaId;
    }

    public Timestamp getDataHoraInicio() {
        return dataHoraInicio;
    }

    public Timestamp getDataHoraFim() {
        return dataHoraFim;
    }

    public String getStatus() {
        return status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public int getCriadoPorUsuarioId() {
        return criadoPorUsuarioId;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public void setMedicoId(int medicoId) {
        this.medicoId = medicoId;
    }

    public void setProcedimentoId(int procedimentoId) {
        this.procedimentoId = procedimentoId;
    }

    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public void setDataHoraInicio(Timestamp dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public void setDataHoraFim(Timestamp dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setCriadoPorUsuarioId(int criadoPorUsuarioId) {
        this.criadoPorUsuarioId = criadoPorUsuarioId;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
