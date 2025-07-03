package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicoDAO {
    
    public boolean inserir(Medico entidade) {
        String sql = "INSERT INTO tb_medicos (funcionario_id, crm) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getFuncionario_id());
            pstmt.setString(2, entidade.getCrm());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_medicos:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Medico entidade) {
        String sql = "UPDATE tb_medicos SET funcionario_id = ?, crm = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getFuncionario_id());
            pstmt.setString(2, entidade.getCrm());
            pstmt.setInt(3, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_medicos:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_medicos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_medicos:");
            e.printStackTrace();
            return false;
        }
    }
}
