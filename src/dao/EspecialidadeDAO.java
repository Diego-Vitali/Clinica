package dao;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Especialidade;

public class EspecialidadeDAO {
    
    public boolean inserir(Especialidade entidade) {
        String sql = "INSERT INTO tb_especialidades (nome, descricao) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidade.getNome());
            pstmt.setString(2, entidade.getDescricao());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_especialidades:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Especialidade entidade) {
        String sql = "UPDATE tb_especialidades SET nome = ?, descricao = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidade.getNome());
            pstmt.setString(2, entidade.getDescricao());
            pstmt.setInt(3, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_especialidades:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_especialidades WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_especialidades:");
            e.printStackTrace();
            return false;
        }
    }

}
