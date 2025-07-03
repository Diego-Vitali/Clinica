package model;

import java.time.LocalDateTime;

public class Usuario {
	
	private int id;
    private String email;
    private String senhaHash;
    private String tipoUsuario; // ENUM: PACIENTE, MEDICO, RECEPCIONISTA, ADMIN
    private boolean ativo;
    private LocalDateTime dataCriacao;

    public Usuario() {
    }

    public Usuario(int id, String email, String senhaHash, String tipoUsuario, boolean ativo, LocalDateTime dataCriacao) {
        this.id = id;
        this.email = email;
        this.senhaHash = senhaHash;
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

    public String getSenhaHash() {
        return senhaHash;
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

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
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
