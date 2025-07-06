package dao;

import model.Medico;

import java.sql.*;

public class MedicoDAO {

    public int inserirRetornandoId(Medico medico) {
        String sql = "INSERT INTO tb_medicos (funcionario_id, crm) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, medico.getFuncionarioId());
            pstmt.setString(2, medico.getCrm());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir médico:");
            e.printStackTrace();
        }

        return -1;
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_medicos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar médico:");
            e.printStackTrace();
        }

        return false;
    }
}
