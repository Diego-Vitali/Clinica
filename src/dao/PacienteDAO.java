package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PacienteDAO {
    
    public boolean inserir(Paciente entidade) {
        String sql = "INSERT INTO tb_pacientes (usuario_id, nome_completo, cpf, data_nascimento, telefone, endereco, convenio, numero_carteirinha) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getUsuario_id());
            pstmt.setString(2, entidade.getNome_completo());
            pstmt.setString(3, entidade.getCpf());
            pstmt.setDate(4, entidade.getData_nascimento());
            pstmt.setString(5, entidade.getTelefone());
            pstmt.setString(6, entidade.getEndereco());
            pstmt.setString(7, entidade.getConvenio());
            pstmt.setString(8, entidade.getNumero_carteirinha());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_pacientes:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Paciente entidade) {
        String sql = "UPDATE tb_pacientes SET usuario_id = ?, nome_completo = ?, cpf = ?, data_nascimento = ?, telefone = ?, endereco = ?, convenio = ?, numero_carteirinha = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getUsuario_id());
            pstmt.setString(2, entidade.getNome_completo());
            pstmt.setString(3, entidade.getCpf());
            pstmt.setDate(4, entidade.getData_nascimento());
            pstmt.setString(5, entidade.getTelefone());
            pstmt.setString(6, entidade.getEndereco());
            pstmt.setString(7, entidade.getConvenio());
            pstmt.setString(8, entidade.getNumero_carteirinha());
            pstmt.setInt(9, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_pacientes:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_pacientes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_pacientes:");
            e.printStackTrace();
            return false;
        }
    }
}
