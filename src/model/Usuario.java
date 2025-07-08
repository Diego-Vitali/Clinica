package model;

import java.sql.Timestamp;

public class Usuario {
    
    private int id;
    private String email;
    private String senha;
    private String funcao;
    private boolean ativo;
    private Timestamp dataCriacao;

    public Usuario() {}

    public Usuario(int id, String email, String senha, String funcao, boolean ativo) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.funcao = funcao;
        this.ativo = ativo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Timestamp getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Timestamp dataCriacao) { this.dataCriacao = dataCriacao; }
}