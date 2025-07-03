package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProcedimentoDAO {
    
    public boolean inserir(Procedimento entidade) {
        String sql = "INSERT INTO tb_procedimentos (especialidade_id, tipo_sala_id, nome, duracao_minutos, valor, instrucoes_preparo, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getEspecialidade_id());
            pstmt.setInt(2, entidade.getTipo_sala_id());
            pstmt.setString(3, entidade.getNome());
            pstmt.setInt(4, entidade.getDuracao_minutos());
            pstmt.setBigDecimal(5, entidade.getValor());
            pstmt.setString(6, entidade.getInstrucoes_preparo());
            pstmt.setBoolean(7, entidade.getAtivo());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_procedimentos:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Procedimento entidade) {
        String sql = "UPDATE tb_procedimentos SET especialidade_id = ?, tipo_sala_id = ?, nome = ?, duracao_minutos = ?, valor = ?, instrucoes_preparo = ?, ativo = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getEspecialidade_id());
            pstmt.setInt(2, entidade.getTipo_sala_id());
            pstmt.setString(3, entidade.getNome());
            pstmt.setInt(4, entidade.getDuracao_minutos());
            pstmt.setBigDecimal(5, entidade.getValor());
            pstmt.setString(6, entidade.getInstrucoes_preparo());
            pstmt.setBoolean(7, entidade.getAtivo());
            pstmt.setInt(8, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_procedimentos:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_procedimentos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_procedimentos:");
            e.printStackTrace();
            return false;
        }
    }
}
