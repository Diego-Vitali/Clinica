package view;

import dao.AgendamentoDAO;
import model.Agendamento;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TabelaAgendamentosScreen extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Usuario usuarioLogado;

    public TabelaAgendamentosScreen(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Gerenciamento de Agendamentos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Data", "Início", "Fim", "Paciente", "Médico", "Procedimento", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNovo = new JButton("Novo");
        btnNovo.addActionListener(e -> {
            new CadastroAgendamentoScreen(usuarioLogado).setVisible(true);
            dispose();
        });
        botoesPanel.add(btnNovo);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarAgendamento());
        botoesPanel.add(btnEditar);

        JButton btnCancelar = new JButton("Cancelar Agendamento");
        btnCancelar.addActionListener(e -> cancelarAgendamento());
        botoesPanel.add(btnCancelar);
        
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);
        carregarAgendamentos();
    }

    private void carregarAgendamentos() {
        modeloTabela.setRowCount(0);
        AgendamentoDAO dao = new AgendamentoDAO();
        List<Agendamento> agendamentos = usuarioLogado.getFuncao().equalsIgnoreCase("MEDICO") ? dao.buscarPorMedicoUsuarioId(usuarioLogado.getId()) : dao.buscarTodos();
        DateTimeFormatter dtfData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfHora = DateTimeFormatter.ofPattern("HH:mm");
        for (Agendamento ag : agendamentos) {
            modeloTabela.addRow(new Object[]{
                ag.getId(), ag.getDataHoraInicio().toLocalDateTime().format(dtfData), ag.getDataHoraInicio().toLocalDateTime().format(dtfHora),
                ag.getDataHoraFim().toLocalDateTime().format(dtfHora), ag.getPaciente().getNomeCompleto(), ag.getMedico().getFuncionario().getNomeCompleto(),
                ag.getProcedimentoNome(), ag.getStatus()
            });
        }
    }

    private void editarAgendamento() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idAgendamento = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        new EditarAgendamentoScreen(usuarioLogado, idAgendamento).setVisible(true);
        dispose();
    }

    private void cancelarAgendamento() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento para cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idAgendamento = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        String statusAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 7);

        if (!"AGENDADO".equals(statusAtual)) {
            JOptionPane.showMessageDialog(this, "Apenas agendamentos com status 'AGENDADO' podem ser cancelados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar este agendamento?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (new AgendamentoDAO().atualizarStatus(idAgendamento, "CANCELADO_CLINICA")) {
                JOptionPane.showMessageDialog(this, "Agendamento cancelado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAgendamentos();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao cancelar o agendamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
