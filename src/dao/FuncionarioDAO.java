package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Funcionario;

public class FuncionarioDAO {
    
    public boolean inserir(Funcionario entidade) {
        String sql = "INSERT INTO tb_funcionarios (usuario_id, nome_completo, cpf, cargo, telefone, data_admissao, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getUsuarioId());
            pstmt.setString(2, entidade.getNomeCompleto());
            pstmt.setString(3, entidade.getCpf());
            pstmt.setString(4, entidade.getCargo());
            pstmt.setString(5, entidade.getTelefone());
            pstmt.setDate(6, entidade.getDataAdmissao());
            pstmt.setBoolean(7, entidade.isAtivo());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_funcionarios:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Funcionario entidade) {
        String sql = "UPDATE tb_funcionarios SET usuario_id = ?, nome_completo = ?, cpf = ?, cargo = ?, telefone = ?, data_admissao = ?, ativo = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getUsuarioId());
            pstmt.setString(2, entidade.getNomeCompleto());
            pstmt.setString(3, entidade.getCpf());
            pstmt.setString(4, entidade.getCargo());
            pstmt.setString(5, entidade.getTelefone());
            pstmt.setDate(6, entidade.getDataAdmissao());
            pstmt.setBoolean(7, entidade.isAtivo());
            pstmt.setInt(8, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_funcionarios:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_funcionarios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_funcionarios:");
            e.printStackTrace();
            return false;
        }
    }
}
