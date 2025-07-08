package dao;

import helpers.DBConnection;
import model.*;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    private final String BASE_QUERY = "SELECT a.id, a.data_hora_inicio, a.data_hora_fim, a.status, a.observacoes, a.criado_por_usuario_id, p.id as paciente_id, p.nome_completo as paciente_nome, m.id as medico_id, f.nome_completo as medico_nome, f.usuario_id as medico_usuario_id, pr.id as procedimento_id, pr.nome as procedimento_nome, pr.duracao_minutos FROM tb_agendamentos a JOIN tb_pacientes p ON a.paciente_id = p.id JOIN tb_medicos m ON a.medico_id = m.id JOIN tb_funcionarios f ON m.funcionario_id = f.id JOIN tb_procedimentos pr ON a.procedimento_id = pr.id ";

    public List<Agendamento> buscarTodos() {
        return buscar(BASE_QUERY + "ORDER BY a.data_hora_inicio", null);
    }

    public List<Agendamento> buscarPorMedicoUsuarioId(int usuarioId) {
        return buscar(BASE_QUERY + "WHERE f.usuario_id = ? ORDER BY a.data_hora_inicio", usuarioId);
    }

    public Agendamento buscarPorId(int id) {
        List<Agendamento> resultados = buscar(BASE_QUERY + "WHERE a.id = ?", id);
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public boolean inserir(Agendamento agendamento) {
        String sql = "INSERT INTO tb_agendamentos (paciente_id, medico_id, procedimento_id, data_hora_inicio, data_hora_fim, status, criado_por_usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getPaciente().getId());
            stmt.setInt(2, agendamento.getMedico().getId());
            stmt.setInt(3, agendamento.getProcedimento().getId());
            stmt.setTimestamp(4, agendamento.getDataHoraInicio());
            stmt.setTimestamp(5, agendamento.getDataHoraFim());
            stmt.setString(6, "AGENDADO");
            stmt.setInt(7, agendamento.getCriadoPorUsuarioId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Agendamento agendamento) {
        String sql = "UPDATE tb_agendamentos SET paciente_id = ?, medico_id = ?, procedimento_id = ?, data_hora_inicio = ?, data_hora_fim = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getPaciente().getId());
            stmt.setInt(2, agendamento.getMedico().getId());
            stmt.setInt(3, agendamento.getProcedimento().getId());
            stmt.setTimestamp(4, agendamento.getDataHoraInicio());
            stmt.setTimestamp(5, agendamento.getDataHoraFim());
            stmt.setString(6, agendamento.getStatus());
            stmt.setInt(7, agendamento.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizarStatus(int agendamentoId, String novoStatus) {
        String sql = "UPDATE tb_agendamentos SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, agendamentoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isHorarioOcupado(int medicoId, Timestamp inicio, Timestamp fim, int agendamentoIdExcluido) {
        String sql = "SELECT COUNT(*) FROM tb_agendamentos WHERE medico_id = ? AND id != ? AND status IN ('AGENDADO', 'CONCLUIDO') AND (? < data_hora_fim AND ? > data_hora_inicio)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicoId);
            stmt.setInt(2, agendamentoIdExcluido);
            stmt.setTimestamp(3, inicio);
            stmt.setTimestamp(4, fim);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; 
    }

    public boolean isDentroDoHorarioDeTrabalho(int medicoId, Timestamp inicio, Timestamp fim) {
        DayOfWeek diaDaSemana = inicio.toLocalDateTime().getDayOfWeek();
        String nomeDiaSemana = getNomeDiaSemanaParaEnum(diaDaSemana);
        
        LocalTime horaInicio = inicio.toLocalDateTime().toLocalTime();
        LocalTime horaFim = fim.toLocalDateTime().toLocalTime();

        String sql = "SELECT COUNT(*) FROM tb_horarios_trabalho WHERE medico_id = ? AND dia_semana = ? AND hora_inicio <= ? AND hora_fim >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicoId);
            stmt.setString(2, nomeDiaSemana);
            stmt.setTime(3, Time.valueOf(horaInicio));
            stmt.setTime(4, Time.valueOf(horaFim));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getNomeDiaSemanaParaEnum(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "SEGUNDA";
            case TUESDAY: return "TERCA";
            case WEDNESDAY: return "QUARTA";
            case THURSDAY: return "QUINTA";
            case FRIDAY: return "SEXTA";
            case SATURDAY: return "SABADO";
            case SUNDAY: return "DOMINGO";
            default: return "";
        }
    }

    private List<Agendamento> buscar(String sql, Integer id) {
        List<Agendamento> agendamentos = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (id != null) stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) agendamentos.add(mapResultSetToAgendamento(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return agendamentos;
    }

    private Agendamento mapResultSetToAgendamento(ResultSet rs) throws SQLException {
        Paciente p = new Paciente(); p.setId(rs.getInt("paciente_id")); p.setNomeCompleto(rs.getString("paciente_nome"));
        Funcionario fm = new Funcionario(); fm.setNomeCompleto(rs.getString("medico_nome")); fm.setUsuarioId(rs.getInt("medico_usuario_id"));
        Medico m = new Medico(); m.setId(rs.getInt("medico_id")); m.setFuncionario(fm);
        Procedimento proc = new Procedimento(); proc.setId(rs.getInt("procedimento_id")); proc.setNome(rs.getString("procedimento_nome")); proc.setDuracaoMinutos(rs.getInt("duracao_minutos"));
        Agendamento a = new Agendamento(); a.setId(rs.getInt("id")); a.setDataHoraInicio(rs.getTimestamp("data_hora_inicio")); a.setDataHoraFim(rs.getTimestamp("data_hora_fim")); a.setStatus(rs.getString("status")); a.setPaciente(p); a.setMedico(m); a.setProcedimento(proc); a.setProcedimentoNome(proc.getNome()); a.setCriadoPorUsuarioId(rs.getInt("criado_por_usuario_id"));
        return a;
    }
}
