package dao;

import model.Funcionario;

import java.sql.*;

public class FuncionarioDAO {

    public int inserirRetornandoId(Funcionario funcionario) {
        String sql = "INSERT INTO tb_funcionarios (usuario_id, nome_completo, cpf, cargo, telefone, data_admissao, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, funcionario.getUsuarioId());
            pstmt.setString(2, funcionario.getNomeCompleto());
            pstmt.setString(3, funcionario.getCpf());
            pstmt.setString(4, funcionario.getCargo());
            pstmt.setString(5, funcionario.getTelefone());
            pstmt.setDate(6, Date.valueOf(funcionario.getDataAdmissao()));
            pstmt.setBoolean(7, funcionario.isAtivo());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir funcionário:");
            e.printStackTrace();
        }

        return -1;
    }

    public boolean atualizar(Funcionario funcionario) {
        String sql = "UPDATE tb_funcionarios SET nome_completo = ?, cpf = ?, cargo = ?, telefone = ?, data_admissao = ?, ativo = ? " +
                     "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, funcionario.getNomeCompleto());
            pstmt.setString(2, funcionario.getCpf());
            pstmt.setString(3, funcionario.getCargo());
            pstmt.setString(4, funcionario.getTelefone());
            pstmt.setDate(5, Date.valueOf(funcionario.getDataAdmissao()));
            pstmt.setBoolean(6, funcionario.isAtivo());
            pstmt.setInt(7, funcionario.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar funcionário:");
            e.printStackTrace();
        }

        return false;
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_funcionarios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar funcionário:");
            e.printStackTrace();
        }

        return false;
    }
}
