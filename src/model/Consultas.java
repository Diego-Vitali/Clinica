package model;

public class Consultas {
	
	    private int id;
	    private Paciente paciente;
	    private Medico medico;
	    private Funcionario funcionarioTriagem;
	    private TipoConsulta tipoConsulta;
	    private java.sql.Timestamp dataHora;
	    private String pressao;
	    private String temperatura;
	    private String peso;
	    private String sintomas;

	    public int getId() { 
	    	return id; 
    	}
	    public void setId(int id) { 
	    	this.id = id; 
    	}
	    
	    public Paciente getPaciente() { 
	    	return paciente; 
    	}
	    public void setPaciente(Paciente paciente) { 
	    	this.paciente = paciente; 
    	}
	    
	    public Medico getMedico() {
	    	return medico; 
    	}
	    public void setMedico(Medico medico) {
	    	this.medico = medico; 
    	}
	    
	    public Funcionario getFuncionarioTriagem() { 
	    	return funcionarioTriagem; 
    	}
	    public void setFuncionarioTriagem(Funcionario funcionarioTriagem) {
	    	this.funcionarioTriagem = funcionarioTriagem; 
    	}
	    
	    public TipoConsulta getTipoConsulta() { 
	    	return tipoConsulta; 
    	}
	    public void setTipoConsulta(TipoConsulta tipoConsulta) { 
	    	this.tipoConsulta = tipoConsulta; 
    	}
	    
	    public java.sql.Timestamp getDataHora() { 
	    	return dataHora; 
    	}
	    public void setDataHora(java.sql.Timestamp dataHora) { 
	    	this.dataHora = dataHora;
    	}
	    
	    public String getPressao() { 
	    	return pressao; 
    	}
	    public void setPressao(String pressao) { 
	    	this.pressao = pressao;
    	}
	    
	    public String getTemperatura() { 
	    	return temperatura;
    	}
	    public void setTemperatura(String temperatura){ 
	    	this.temperatura = temperatura; 
    	}
	    
	    public String getPeso() { 
	    	return peso; 
		}
	    public void setPeso(String peso) { 
	    	this.peso = peso; 
    	}
	    
	    public String getSintomas() { 
	    	return sintomas;
    	}
	    public void setSintomas(String sintomas) { 
	    	this.sintomas = sintomas;
    	}
}
