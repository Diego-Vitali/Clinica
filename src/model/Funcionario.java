package model;

import java.time.LocalDate;

public class Funcionario {
	
	private int id;
    private int usuarioId;
    private String nomeCompleto;
    private String cpf;
    private String cargo;
    private String telefone;
    private LocalDate dataAdmissao;
    private boolean ativo;

    public Funcionario() {
    }

    public Funcionario(int id, int usuarioId, String nomeCompleto, String cpf, String cargo, String telefone, LocalDate dataAdmissao, boolean ativo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.cargo = cargo;
        this.telefone = telefone;
        this.dataAdmissao = dataAdmissao;
        this.ativo = ativo;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
