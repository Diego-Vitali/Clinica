package dao;

import model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PacienteDAO {

    public boolean inserir(Paciente paciente) {
        String sql = "INSERT INTO tb_pacientes (usuario_id, nome_completo, cpf, data_nascimento, telefone, endereco, convenio, numero_carteirinha) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paciente.getUsuarioId());
            pstmt.setString(2, paciente.getNomeCompleto());
            pstmt.setString(3, paciente.getCpf());
            pstmt.setDate(4, java.sql.Date.valueOf(paciente.getDataNascimento()));
            pstmt.setString(5, paciente.getTelefone());
            pstmt.setString(6, paciente.getEndereco());
            pstmt.setString(7, paciente.getConvenio());
            pstmt.setString(8, paciente.getNumeroCarteirinha());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir paciente:");
            e.printStackTrace();
            return false;
        }
    }

}
