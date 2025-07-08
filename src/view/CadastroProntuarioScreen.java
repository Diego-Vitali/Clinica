package view;

import dao.AgendamentoDAO;
import dao.ProntuarioDAO;
import model.Agendamento;
import model.Prontuario;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class CadastroProntuarioScreen extends JFrame {
    private JTextField campoAgendamentoId, campoDadosTriagem;
    private JTextArea areaSintomas, areaDiagnostico, areaPrescricao, areaExames, areaAtestado;
    private JLabel labelInfoAgendamento;
    private JButton btnSalvar; 
    private Agendamento agendamentoEncontrado;

    public CadastroProntuarioScreen(Usuario usuarioLogado) {
        setTitle("Cadastro de Prontuário");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(createFormPanel());
        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Novo Prontuário"));
        int y = 0;

        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.add(new JLabel("ID do Agendamento Concluído:"));
        campoAgendamentoId = new JTextField(10);
        buscaPanel.add(campoAgendamentoId);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarAgendamento());
        buscaPanel.add(btnBuscar);
        
        GridBagConstraints gbcBusca = gbc(0, y++);
        gbcBusca.gridwidth = 2;
        panel.add(buscaPanel, gbcBusca);
        
        labelInfoAgendamento = new JLabel(" ");
        labelInfoAgendamento.setFont(new Font("Arial", Font.ITALIC, 12));
        GridBagConstraints gbcInfo = gbc(0, y++);
        gbcInfo.gridwidth = 2;
        panel.add(labelInfoAgendamento, gbcInfo);

        campoDadosTriagem = new JTextField(40);
        panel.add(new JLabel("Dados Triagem (JSON):"), gbc(0, y));
        panel.add(campoDadosTriagem, gbc(1, y++));
        
        areaSintomas = new JTextArea(3, 30);
        panel.add(new JLabel("Sintomas:"), gbc(0, y));
        panel.add(new JScrollPane(areaSintomas), gbc(1, y++));
        
        areaDiagnostico = new JTextArea(3, 30);
        panel.add(new JLabel("Diagnóstico:"), gbc(0, y));
        panel.add(new JScrollPane(areaDiagnostico), gbc(1, y++));
        
        areaPrescricao = new JTextArea(3, 30);
        panel.add(new JLabel("Prescrição:"), gbc(0, y));
        panel.add(new JScrollPane(areaPrescricao), gbc(1, y++));
        
        areaExames = new JTextArea(3, 30);
        panel.add(new JLabel("Solicitação de Exames:"), gbc(0, y));
        panel.add(new JScrollPane(areaExames), gbc(1, y++));
        
        areaAtestado = new JTextArea(3, 30);
        panel.add(new JLabel("Atestado Médico:"), gbc(0, y));
        panel.add(new JScrollPane(areaAtestado), gbc(1, y++));

        btnSalvar = new JButton("Salvar");
        btnSalvar.setEnabled(false);
        btnSalvar.addActionListener(e -> salvarProntuario());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnCancelar);
        
        GridBagConstraints gbcBotoes = gbc(0, y);
        gbcBotoes.gridwidth = 2;
        panel.add(botoesPanel, gbcBotoes);
        
        return panel;
    }

    private void buscarAgendamento() {
        agendamentoEncontrado = null;
        btnSalvar.setEnabled(false);
        labelInfoAgendamento.setText(" ");

        try {
            int id = Integer.parseInt(campoAgendamentoId.getText());
            Agendamento ag = new AgendamentoDAO().buscarPorId(id);
            
            if (ag == null) {
                labelInfoAgendamento.setText("Agendamento com ID " + id + " não encontrado.");
                return;
            }

            if (!"CONCLUIDO".equalsIgnoreCase(ag.getStatus())) {
                labelInfoAgendamento.setText("Agendamento encontrado, mas o status é '" + ag.getStatus() + "', não 'CONCLUIDO'.");
                return;
            }

            if (new ProntuarioDAO().prontuarioExisteParaAgendamento(id)) {
                labelInfoAgendamento.setText("Erro: Já existe um prontuário para este agendamento (ID: " + id + ").");
                return;
            }

            agendamentoEncontrado = ag;
            String info = String.format("Pronto para registrar. Paciente: %s | Médico: %s", ag.getPaciente().getNomeCompleto(), ag.getMedico().getFuncionario().getNomeCompleto());
            labelInfoAgendamento.setText(info);
            btnSalvar.setEnabled(true);

        } catch (NumberFormatException e) {
            labelInfoAgendamento.setText("ID inválido. Por favor, insira um número.");
        }
    }

    private void salvarProntuario() {
        if (agendamentoEncontrado == null) {
            JOptionPane.showMessageDialog(this, "Busque e selecione um agendamento válido primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Prontuario prontuario = new Prontuario();
        prontuario.setAgendamentoId(agendamentoEncontrado.getId());
        prontuario.setPacienteId(agendamentoEncontrado.getPaciente().getId());
        prontuario.setMedicoId(agendamentoEncontrado.getMedico().getId());
        prontuario.setDadosTriagem(campoDadosTriagem.getText());
        prontuario.setSintomas(areaSintomas.getText());
        prontuario.setDiagnostico(areaDiagnostico.getText());
        prontuario.setPrescricao(areaPrescricao.getText());
        prontuario.setSolicitacaoExames(areaExames.getText());
        prontuario.setAtestadoMedico(areaAtestado.getText());
        
        if (new ProntuarioDAO().inserir(prontuario)) {
            JOptionPane.showMessageDialog(this, "Prontuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao cadastrar o prontuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
}
