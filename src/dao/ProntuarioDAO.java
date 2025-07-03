package dao;

import model.Prontuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProntuarioDAO {
    
    public boolean inserir(Prontuario entidade) {
        String sql = "INSERT INTO tb_prontuarios (agendamento_id, paciente_id, medico_id, dados_triagem, sintomas, diagnostico, prescricao, solicitacao_exames, atestado_medico, data_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getAgendamento_id());
            pstmt.setInt(2, entidade.getPaciente_id());
            pstmt.setInt(3, entidade.getMedico_id());
            pstmt.setString(4, entidade.getDados_triagem());
            pstmt.setString(5, entidade.getSintomas());
            pstmt.setString(6, entidade.getDiagnostico());
            pstmt.setString(7, entidade.getPrescricao());
            pstmt.setString(8, entidade.getSolicitacao_exames());
            pstmt.setString(9, entidade.getAtestado_medico());
            pstmt.setTimestamp(10, entidade.getData_registro());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_prontuarios:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Prontuario entidade) {
        String sql = "UPDATE tb_prontuarios SET agendamento_id = ?, paciente_id = ?, medico_id = ?, dados_triagem = ?, sintomas = ?, diagnostico = ?, prescricao = ?, solicitacao_exames = ?, atestado_medico = ?, data_registro = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getAgendamento_id());
            pstmt.setInt(2, entidade.getPaciente_id());
            pstmt.setInt(3, entidade.getMedico_id());
            pstmt.setString(4, entidade.getDados_triagem());
            pstmt.setString(5, entidade.getSintomas());
            pstmt.setString(6, entidade.getDiagnostico());
            pstmt.setString(7, entidade.getPrescricao());
            pstmt.setString(8, entidade.getSolicitacao_exames());
            pstmt.setString(9, entidade.getAtestado_medico());
            pstmt.setTimestamp(10, entidade.getData_registro());
            pstmt.setInt(11, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_prontuarios:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_prontuarios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_prontuarios:");
            e.printStackTrace();
            return false;
        }
    }
}
