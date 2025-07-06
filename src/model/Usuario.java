package model;

import java.time.LocalDateTime;

public class Usuario {
	
	private int id;
    private String email;
    private String senha;
    private String tipoUsuario;
    private boolean ativo;
    private LocalDateTime dataCriacao;

    public Usuario() {}

    public Usuario(int id, String email, String senha, String tipoUsuario, boolean ativo, LocalDateTime dataCriacao) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
