package model;

import java.time.LocalDate;

public class Paciente {
	
	private int id;
    private Integer usuarioId; // Pode ser nulo
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String endereco;
    private String convenio;
    private String numeroCarteirinha;

    public Paciente() {
    }

    public Paciente(int id, Integer usuarioId, String nomeCompleto, String cpf, LocalDate dataNascimento, String telefone, String endereco, String convenio, String numeroCarteirinha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.endereco = endereco;
        this.convenio = convenio;
        this.numeroCarteirinha = numeroCarteirinha;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getConvenio() {
        return convenio;
    }

    public String getNumeroCarteirinha() {
        return numeroCarteirinha;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public void setNumeroCarteirinha(String numeroCarteirinha) {
        this.numeroCarteirinha = numeroCarteirinha;
    }
}
