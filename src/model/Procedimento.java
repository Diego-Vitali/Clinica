package model;

import java.math.BigDecimal;

public class Procedimento {
    private int id;
    private int especialidadeId;
    private String nome;
    private int duracaoMinutos;
    private BigDecimal valor;

    public Procedimento() {}

    public Procedimento(int id) {
        this.id = id;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEspecialidadeId() { return especialidadeId; }
    public void setEspecialidadeId(int especialidadeId) { this.especialidadeId = especialidadeId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
