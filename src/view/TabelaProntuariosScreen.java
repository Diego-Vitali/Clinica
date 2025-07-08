package view;

import dao.ProntuarioDAO;
import model.Prontuario;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TabelaProntuariosScreen extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Usuario usuarioLogado;

    public TabelaProntuariosScreen(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Gerenciamento de Prontuários");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "ID Agend.", "ID Pac.", "ID Méd.", "Diagnóstico"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNovo = new JButton("Novo");
        btnNovo.addActionListener(e -> {
            new CadastroProntuarioScreen(usuarioLogado).setVisible(true);
            dispose();
        });
        botoesPanel.add(btnNovo);

        JButton btnEditar = new JButton("Visualizar/Editar");
        btnEditar.addActionListener(e -> editarProntuario());
        botoesPanel.add(btnEditar);
        
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);
        carregarProntuarios();
    }

    private void carregarProntuarios() {
        modeloTabela.setRowCount(0);
        ProntuarioDAO dao = new ProntuarioDAO();
        List<Prontuario> prontuarios = dao.listarTodos();
        for (Prontuario p : prontuarios) {
            modeloTabela.addRow(new Object[]{p.getId(), p.getAgendamentoId(), p.getPacienteId(), p.getMedicoId(), p.getDiagnostico()});
        }
    }

    private void editarProntuario() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um prontuário para visualizar/editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idProntuario = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        new EditarProntuarioScreen(usuarioLogado, idProntuario).setVisible(true);
        dispose();
    }
}
