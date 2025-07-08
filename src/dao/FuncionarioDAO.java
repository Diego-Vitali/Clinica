package dao;

import helpers.DBConnection;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public boolean inserirFuncionarioCompleto(Usuario usuario, Funcionario funcionario, Medico medico, List<Integer> especialidadeIds, List<HorarioTrabalho> horarios) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int usuarioId = usuarioDAO.inserirComRetornoDeID(usuario, conn);
            if (usuarioId == -1) throw new SQLException("Falha ao criar usuário.");
            funcionario.setUsuarioId(usuarioId);

            int funcionarioId = inserirFuncionario(funcionario, conn);
            if (medico != null) {
                medico.setFuncionarioId(funcionarioId);
                int medicoId = inserirMedico(medico, conn);

                if (especialidadeIds != null && !especialidadeIds.isEmpty()) {
                    new MedicoEspecialidadeDAO().inserir(medicoId, especialidadeIds, conn);
                }
                if (horarios != null && !horarios.isEmpty()) {
                    inserirHorariosTrabalho(medicoId, horarios, conn);
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean atualizarFuncionarioCompleto(Usuario usuario, Funcionario funcionario, Medico medico, List<Integer> especialidadeIds, List<HorarioTrabalho> horarios) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            new UsuarioDAO().atualizar(usuario, conn);
            atualizarFuncionario(funcionario, conn);

            if (medico != null) {
                atualizarMedico(medico, conn);
                MedicoEspecialidadeDAO meDAO = new MedicoEspecialidadeDAO();
                meDAO.excluirPorMedico(medico.getId(), conn);
                if (especialidadeIds != null && !especialidadeIds.isEmpty()) {
                    meDAO.inserir(medico.getId(), especialidadeIds, conn);
                }
                
                excluirHorariosTrabalho(medico.getId(), conn);
                if (horarios != null && !horarios.isEmpty()) {
                    inserirHorariosTrabalho(medico.getId(), horarios, conn);
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT f.id, f.nome_completo, f.cpf, f.telefone, f.ativo, u.email, u.funcao, " +
                     "GROUP_CONCAT(CONCAT(ht.dia_semana, ': ', TIME_FORMAT(ht.hora_inicio, '%H:%i'), '-', TIME_FORMAT(ht.hora_fim, '%H:%i')) ORDER BY ht.id SEPARATOR '; ') AS horarios " +
                     "FROM tb_funcionarios f " +
                     "JOIN tb_usuarios u ON f.usuario_id = u.id " +
                     "LEFT JOIN tb_medicos m ON f.id = m.funcionario_id " +
                     "LEFT JOIN tb_horarios_trabalho ht ON m.id = ht.medico_id " +
                     "GROUP BY f.id, f.nome_completo, f.cpf, f.telefone, f.ativo, u.email, u.funcao " +
                     "ORDER BY f.nome_completo";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNomeCompleto(rs.getString("nome_completo"));
                f.setCpf(rs.getString("cpf"));
                f.setTelefone(rs.getString("telefone"));
                f.setAtivo(rs.getBoolean("ativo"));
                f.setHorariosFormatados(rs.getString("horarios"));
                
                Usuario u = new Usuario();
                u.setEmail(rs.getString("email"));
                u.setFuncao(rs.getString("funcao"));
                f.setUsuario(u);
                
                funcionarios.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }
    
    public Funcionario buscarCompletoPorId(int id) {
        String sql = "SELECT * FROM tb_funcionarios f JOIN tb_usuarios u ON f.usuario_id = u.id WHERE f.id = ?";
        Funcionario funcionario = null;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                funcionario = mapResultSetToFuncionario(rs);
                Usuario usuario = new UsuarioDAO().mapResultSetToUsuario(rs);
                funcionario.setUsuario(usuario);

                if("MEDICO".equalsIgnoreCase(usuario.getFuncao())) {
                    Medico medico = buscarMedicoPorFuncionarioId(id, conn);
                    if(medico != null) {
                        medico.setEspecialidades(new MedicoEspecialidadeDAO().buscarEspecialidadesPorMedico(medico.getId()));
                        medico.setHorarios(buscarHorariosPorMedicoId(medico.getId(), conn));
                    }
                    funcionario.setMedico(medico);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    }

    public boolean alterarStatusFuncionario(int funcionarioId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String selectSql = "SELECT f.ativo, f.usuario_id FROM tb_funcionarios f WHERE f.id = ?";
            boolean novoStatus;
            int usuarioId;

            try (PreparedStatement stmtSelect = conn.prepareStatement(selectSql)) {
                stmtSelect.setInt(1, funcionarioId);
                ResultSet rs = stmtSelect.executeQuery();
                if (rs.next()) {
                    novoStatus = !rs.getBoolean("ativo");
                    usuarioId = rs.getInt("usuario_id");
                } else {
                    throw new SQLException("Funcionário não encontrado, ID: " + funcionarioId);
                }
            }

            String updateFuncSql = "UPDATE tb_funcionarios SET ativo = ? WHERE id = ?";
            try (PreparedStatement stmtFunc = conn.prepareStatement(updateFuncSql)) {
                stmtFunc.setBoolean(1, novoStatus);
                stmtFunc.setInt(2, funcionarioId);
                stmtFunc.executeUpdate();
            }

            String updateUserSql = "UPDATE tb_usuarios SET ativo = ? WHERE id = ?";
            try (PreparedStatement stmtUser = conn.prepareStatement(updateUserSql)) {
                stmtUser.setBoolean(1, novoStatus);
                stmtUser.setInt(2, usuarioId);
                stmtUser.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    private int inserirFuncionario(Funcionario f, Connection conn) throws SQLException {
        String sql = "INSERT INTO tb_funcionarios (usuario_id, nome_completo, cpf, telefone, data_admissao, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, f.getUsuarioId());
            stmt.setString(2, f.getNomeCompleto());
            stmt.setString(3, f.getCpf());
            stmt.setString(4, f.getTelefone());
            stmt.setDate(5, f.getDataAdmissao());
            stmt.setBoolean(6, true);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("Falha ao criar funcionário.");
        }
    }

    private int inserirMedico(Medico m, Connection conn) throws SQLException {
        String sql = "INSERT INTO tb_medicos (funcionario_id, crm) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, m.getFuncionarioId());
            stmt.setString(2, m.getCrm());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("Falha ao criar médico.");
        }
    }

    private void inserirHorariosTrabalho(int medicoId, List<HorarioTrabalho> horarios, Connection conn) throws SQLException {
        String sql = "INSERT INTO tb_horarios_trabalho (medico_id, dia_semana, hora_inicio, hora_fim) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (HorarioTrabalho horario : horarios) {
                stmt.setInt(1, medicoId);
                stmt.setString(2, horario.getDiaSemana());
                stmt.setTime(3, horario.getHoraInicio());
                stmt.setTime(4, horario.getHoraFim());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    
    private void atualizarFuncionario(Funcionario f, Connection conn) throws SQLException {
        String sql = "UPDATE tb_funcionarios SET nome_completo = ?, cpf = ?, telefone = ?, data_admissao = ?, ativo = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getNomeCompleto());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getTelefone());
            if (f.getDataAdmissao() != null) {
                stmt.setDate(4, f.getDataAdmissao());
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setBoolean(5, f.isAtivo());
            stmt.setInt(6, f.getId());
            stmt.executeUpdate();
        }
    }

    private void atualizarMedico(Medico m, Connection conn) throws SQLException {
        String sql = "UPDATE tb_medicos SET crm = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getCrm());
            stmt.setInt(2, m.getId());
            stmt.executeUpdate();
        }
    }

    private void excluirHorariosTrabalho(int medicoId, Connection conn) throws SQLException {
        String sql = "DELETE FROM tb_horarios_trabalho WHERE medico_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicoId);
            stmt.executeUpdate();
        }
    }

    private Medico buscarMedicoPorFuncionarioId(int funcionarioId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM tb_medicos WHERE funcionario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, funcionarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapResultSetToMedico(rs);
        }
        return null;
    }

    private List<HorarioTrabalho> buscarHorariosPorMedicoId(int medicoId, Connection conn) throws SQLException {
        List<HorarioTrabalho> horarios = new ArrayList<>();
        String sql = "SELECT dia_semana, hora_inicio, hora_fim FROM tb_horarios_trabalho WHERE medico_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                horarios.add(new HorarioTrabalho(rs.getString("dia_semana"), rs.getTime("hora_inicio"), rs.getTime("hora_fim")));
            }
        }
        return horarios;
    }

    private Funcionario mapResultSetToFuncionario(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id"));
        f.setUsuarioId(rs.getInt("usuario_id"));
        f.setNomeCompleto(rs.getString("nome_completo"));
        f.setCpf(rs.getString("cpf"));
        f.setTelefone(rs.getString("telefone"));
        f.setDataAdmissao(rs.getDate("data_admissao"));
        f.setAtivo(rs.getBoolean("ativo"));
        return f;
    }

    private Medico mapResultSetToMedico(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getInt("id"));
        m.setFuncionarioId(rs.getInt("funcionario_id"));
        m.setCrm(rs.getString("crm"));
        return m;
    }
}
