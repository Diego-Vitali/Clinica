package dao;

import helpers.DBConnection;
import model.Prontuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioDAO {

    public boolean inserir(Prontuario prontuario) {
        String sql = "INSERT INTO tb_prontuarios (agendamento_id, paciente_id, medico_id, dados_triagem, sintomas, diagnostico, prescricao, solicitacao_exames, atestado_medico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prontuario.getAgendamentoId());
            stmt.setInt(2, prontuario.getPacienteId());
            stmt.setInt(3, prontuario.getMedicoId());
            stmt.setString(4, prontuario.getDadosTriagem());
            stmt.setString(5, prontuario.getSintomas());
            stmt.setString(6, prontuario.getDiagnostico());
            stmt.setString(7, prontuario.getPrescricao());
            stmt.setString(8, prontuario.getSolicitacaoExames());
            stmt.setString(9, prontuario.getAtestadoMedico());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Prontuario prontuario) {
        String sql = "UPDATE tb_prontuarios SET dados_triagem = ?, sintomas = ?, diagnostico = ?, prescricao = ?, solicitacao_exames = ?, atestado_medico = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prontuario.getDadosTriagem());
            stmt.setString(2, prontuario.getSintomas());
            stmt.setString(3, prontuario.getDiagnostico());
            stmt.setString(4, prontuario.getPrescricao());
            stmt.setString(5, prontuario.getSolicitacaoExames());
            stmt.setString(6, prontuario.getAtestadoMedico());
            stmt.setInt(7, prontuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prontuario> listarTodos() {
        List<Prontuario> prontuarios = new ArrayList<>();
        String sql = "SELECT * FROM tb_prontuarios ORDER BY data_registro DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                prontuarios.add(mapResultSetToProntuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prontuarios;
    }
    
    public Prontuario buscarPorId(int id) {
        String sql = "SELECT * FROM tb_prontuarios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProntuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean prontuarioExisteParaAgendamento(int agendamentoId) {
        String sql = "SELECT 1 FROM tb_prontuarios WHERE agendamento_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agendamentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
    
    private Prontuario mapResultSetToProntuario(ResultSet rs) throws SQLException {
        Prontuario p = new Prontuario();
        p.setId(rs.getInt("id"));
        p.setAgendamentoId(rs.getInt("agendamento_id"));
        p.setPacienteId(rs.getInt("paciente_id"));
        p.setMedicoId(rs.getInt("medico_id"));
        p.setDadosTriagem(rs.getString("dados_triagem"));
        p.setSintomas(rs.getString("sintomas"));
        p.setDiagnostico(rs.getString("diagnostico"));
        p.setPrescricao(rs.getString("prescricao"));
        p.setSolicitacaoExames(rs.getString("solicitacao_exames"));
        p.setAtestadoMedico(rs.getString("atestado_medico"));
        return p;
    }
}
