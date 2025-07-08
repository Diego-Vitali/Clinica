package view;

import dao.ProntuarioDAO;
import model.Prontuario;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class EditarProntuarioScreen extends JFrame {
    private JTextArea areaSintomas, areaDiagnostico, areaPrescricao, areaExames, areaAtestado;
    private JTextField campoDadosTriagem;
    private Prontuario prontuario;

    public EditarProntuarioScreen(Usuario usuarioLogado, int idProntuario) {
        this.prontuario = new ProntuarioDAO().buscarPorId(idProntuario);
        if (prontuario == null) {
            JOptionPane.showMessageDialog(null, "Prontuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Editar Prontuário - Agendamento ID: " + prontuario.getAgendamentoId());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(createFormPanel());
        add(mainPanel);
        
        preencherCampos();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Prontuário"));
        int y = 0;
        
        JLabel infoLabel = new JLabel(String.format("Editando Prontuário ID: %d | Paciente ID: %d | Médico ID: %d", prontuario.getId(), prontuario.getPacienteId(), prontuario.getMedicoId()));
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbcInfo = gbc(0, y++); gbcInfo.gridwidth = 2; panel.add(infoLabel, gbcInfo);

        campoDadosTriagem = new JTextField(40); panel.add(new JLabel("Dados Triagem (JSON):"), gbc(0, y)); panel.add(campoDadosTriagem, gbc(1, y++));
        areaSintomas = new JTextArea(3, 30); panel.add(new JLabel("Sintomas:"), gbc(0, y)); panel.add(new JScrollPane(areaSintomas), gbc(1, y++));
        areaDiagnostico = new JTextArea(3, 30); panel.add(new JLabel("Diagnóstico:"), gbc(0, y)); panel.add(new JScrollPane(areaDiagnostico), gbc(1, y++));
        areaPrescricao = new JTextArea(3, 30); panel.add(new JLabel("Prescrição:"), gbc(0, y)); panel.add(new JScrollPane(areaPrescricao), gbc(1, y++));
        areaExames = new JTextArea(3, 30); panel.add(new JLabel("Solicitação de Exames:"), gbc(0, y)); panel.add(new JScrollPane(areaExames), gbc(1, y++));
        areaAtestado = new JTextArea(3, 30); panel.add(new JLabel("Atestado Médico:"), gbc(0, y)); panel.add(new JScrollPane(areaAtestado), gbc(1, y++));
        
        JButton btnSalvar = new JButton("Salvar Alterações"); btnSalvar.addActionListener(e -> salvarAlteracoes());
        JButton btnCancelar = new JButton("Cancelar"); btnCancelar.addActionListener(e -> dispose());
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); botoesPanel.add(btnSalvar); botoesPanel.add(btnCancelar);
        GridBagConstraints gbcBotoes = gbc(0, y); gbcBotoes.gridwidth = 2; panel.add(botoesPanel, gbcBotoes);
        
        return panel;
    }

    private void preencherCampos() {
        campoDadosTriagem.setText(prontuario.getDadosTriagem());
        areaSintomas.setText(prontuario.getSintomas());
        areaDiagnostico.setText(prontuario.getDiagnostico());
        areaPrescricao.setText(prontuario.getPrescricao());
        areaExames.setText(prontuario.getSolicitacaoExames());
        areaAtestado.setText(prontuario.getAtestadoMedico());
    }

    private void salvarAlteracoes() {
        prontuario.setDadosTriagem(campoDadosTriagem.getText());
        prontuario.setSintomas(areaSintomas.getText());
        prontuario.setDiagnostico(areaDiagnostico.getText());
        prontuario.setPrescricao(areaPrescricao.getText());
        prontuario.setSolicitacaoExames(areaExames.getText());
        prontuario.setAtestadoMedico(areaAtestado.getText());
        
        if (new ProntuarioDAO().atualizar(prontuario)) {
            JOptionPane.showMessageDialog(this, "Prontuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao atualizar o prontuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private GridBagConstraints gbc(int x, int y) { GridBagConstraints g = new GridBagConstraints(); g.gridx = x; g.gridy = y; g.insets = new Insets(5, 5, 5, 5); g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL; return g; }
}
