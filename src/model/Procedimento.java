package model;

import java.math.BigDecimal;

public class Procedimento {
	private int id;
    private int especialidadeId;
    private int tipoSalaId;
    private String nome;
    private int duracaoMinutos;
    private BigDecimal valor;
    private String instrucoesPreparo;
    private boolean ativo;

    public Procedimento() {
    }

    public Procedimento(int id, int especialidadeId, int tipoSalaId, String nome, int duracaoMinutos, BigDecimal valor, String instrucoesPreparo, boolean ativo) {
        this.id = id;
        this.especialidadeId = especialidadeId;
        this.tipoSalaId = tipoSalaId;
        this.nome = nome;
        this.duracaoMinutos = duracaoMinutos;
        this.valor = valor;
        this.instrucoesPreparo = instrucoesPreparo;
        this.ativo = ativo;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getEspecialidadeId() {
        return especialidadeId;
    }

    public int getTipoSalaId() {
        return tipoSalaId;
    }

    public String getNome() {
        return nome;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getInstrucoesPreparo() {
        return instrucoesPreparo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setEspecialidadeId(int especialidadeId) {
        this.especialidadeId = especialidadeId;
    }

    public void setTipoSalaId(int tipoSalaId) {
        this.tipoSalaId = tipoSalaId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setInstrucoesPreparo(String instrucoesPreparo) {
        this.instrucoesPreparo = instrucoesPreparo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
