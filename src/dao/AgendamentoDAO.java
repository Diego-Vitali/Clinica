package dao;

import model.Agendamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgendamentoDAO {
    
    public boolean inserir(Agendamento entidade) {
        String sql = "INSERT INTO tb_agendamentos (paciente_id, medico_id, procedimento_id, sala_id, data_hora_inicio, data_hora_fim, status, observacoes, criado_por_usuario_id, data_criacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getPacienteId());
            pstmt.setInt(2, entidade.getMedicoId());
            pstmt.setInt(3, entidade.getProcedimentoId());
            pstmt.setInt(4, entidade.getSalaId());
            pstmt.setTimestamp(5, entidade.getDataHoraInicio());
            pstmt.setTimestamp(6, entidade.getDataHoraFim());
            pstmt.setString(7, entidade.getStatus());
            pstmt.setString(8, entidade.getObservacoes());
            pstmt.setInt(9, entidade.getCriadoPorUsuarioId());
            pstmt.setTimestamp(10, entidade.getDataCriacao());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_agendamentos:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Agendamento entidade) {
        String sql = "UPDATE tb_agendamentos SET paciente_id = ?, medico_id = ?, procedimento_id = ?, sala_id = ?, data_hora_inicio = ?, data_hora_fim = ?, status = ?, observacoes = ?, criado_por_usuario_id = ?, data_criacao = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getPacienteId());
            pstmt.setInt(2, entidade.getMedicoId());
            pstmt.setInt(3, entidade.getProcedimentoId());
            pstmt.setInt(4, entidade.getSalaId());
            pstmt.setTimestamp(5, entidade.getDataHoraInicio());
            pstmt.setTimestamp(6, entidade.getDataHoraFim());
            pstmt.setString(7, entidade.getStatus());
            pstmt.setString(8, entidade.getObservacoes());
            pstmt.setInt(9, entidade.getCriadoPorUsuarioId());
            pstmt.setTimestamp(10, entidade.getDataCriacao());
            pstmt.setInt(11, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_agendamentos:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_agendamentos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_agendamentos:");
            e.printStackTrace();
            return false;
        }
    }
}
