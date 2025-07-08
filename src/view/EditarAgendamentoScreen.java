package view;

import dao.AgendamentoDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import dao.ProcedimentoDAO;
import helpers.Item;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditarAgendamentoScreen extends JFrame {

    private JComboBox<Item<Integer>> comboPaciente, comboMedico, comboProcedimento;
    private JComboBox<String> comboStatus;
    private JTextField campoData, campoHora;
    private Usuario usuarioLogado;
    private Agendamento agendamento;

    public EditarAgendamentoScreen(Usuario usuarioLogado, int idAgendamento) {
        this.usuarioLogado = usuarioLogado;
        this.agendamento = new AgendamentoDAO().buscarPorId(idAgendamento);
        if (agendamento == null) {
            JOptionPane.showMessageDialog(null, "Agendamento não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Editar Agendamento - ID: " + agendamento.getId());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(createFormPanel());
        add(mainPanel);
        
        preencherCampos();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Agendamento"));
        int y = 0;

        comboPaciente = new JComboBox<>(); new PacienteDAO().listarTodos().forEach(p -> comboPaciente.addItem(new Item<>(p.getId(), p.getNomeCompleto())));
        panel.add(new JLabel("Paciente:"), gbc(0, y)); panel.add(comboPaciente, gbc(1, y++));

        comboMedico = new JComboBox<>(); new MedicoDAO().listarTodosAtivos().forEach(m -> comboMedico.addItem(new Item<>(m.getId(), m.getFuncionario().getNomeCompleto())));
        panel.add(new JLabel("Médico:"), gbc(0, y)); panel.add(comboMedico, gbc(1, y++));
        
        comboProcedimento = new JComboBox<>(); new ProcedimentoDAO().listarTodosAtivos().forEach(p -> comboProcedimento.addItem(new Item<>(p.getId(), p.getNome() + " (" + p.getDuracaoMinutos() + " min)")));
        panel.add(new JLabel("Procedimento:"), gbc(0, y)); panel.add(comboProcedimento, gbc(1, y++));

        campoData = new JTextField(10); panel.add(new JLabel("Data (YYYY-MM-DD):"), gbc(0, y)); panel.add(campoData, gbc(1, y++));
        campoHora = new JTextField(5); panel.add(new JLabel("Hora (HH:MM):"), gbc(0, y)); panel.add(campoHora, gbc(1, y++));

        comboStatus = new JComboBox<>(new String[]{"AGENDADO", "CONCLUIDO", "CANCELADO_PACIENTE", "CANCELADO_CLINICA", "NAO_COMPARECEU"});
        panel.add(new JLabel("Status:"), gbc(0, y)); panel.add(comboStatus, gbc(1, y++));

        JButton btnSalvar = new JButton("Salvar Alterações"); btnSalvar.addActionListener(e -> salvarAlteracoes());
        JButton btnCancelar = new JButton("Cancelar"); btnCancelar.addActionListener(e -> dispose());
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); botoesPanel.add(btnSalvar); botoesPanel.add(btnCancelar);
        GridBagConstraints gbcBotoes = gbc(0, y); gbcBotoes.gridwidth = 2; panel.add(botoesPanel, gbcBotoes);

        return panel;
    }

    private void preencherCampos() {
        selecionarItemNoCombo(comboPaciente, agendamento.getPaciente().getId());
        selecionarItemNoCombo(comboMedico, agendamento.getMedico().getId());
        selecionarItemNoCombo(comboProcedimento, agendamento.getProcedimento().getId());

        LocalDateTime dataHora = agendamento.getDataHoraInicio().toLocalDateTime();
        campoData.setText(dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        campoHora.setText(dataHora.format(DateTimeFormatter.ofPattern("HH:mm")));
        comboStatus.setSelectedItem(agendamento.getStatus());
    }

    private void salvarAlteracoes() {
        Item<Integer> pacienteItem = (Item<Integer>) comboPaciente.getSelectedItem();
        Item<Integer> medicoItem = (Item<Integer>) comboMedico.getSelectedItem();
        Item<Integer> procedimentoItem = (Item<Integer>) comboProcedimento.getSelectedItem();

        LocalDateTime dataHoraInicio;
        try {
            dataHoraInicio = LocalDateTime.parse(campoData.getText() + " " + campoHora.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de data ou hora inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Procedimento procSelecionado = new ProcedimentoDAO().listarTodosAtivos().stream().filter(p -> p.getId() == procedimentoItem.getId()).findFirst().orElse(null);
        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(procSelecionado.getDuracaoMinutos());
        Timestamp inicioTimestamp = Timestamp.valueOf(dataHoraInicio);
        Timestamp fimTimestamp = Timestamp.valueOf(dataHoraFim);

        AgendamentoDAO dao = new AgendamentoDAO();
        if (!dao.isDentroDoHorarioDeTrabalho(medicoItem.getId(), inicioTimestamp, fimTimestamp)) {
            JOptionPane.showMessageDialog(this, "O horário solicitado está fora do expediente do médico.", "Erro de Agendamento", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dao.isHorarioOcupado(medicoItem.getId(), inicioTimestamp, fimTimestamp, agendamento.getId())) {
            JOptionPane.showMessageDialog(this, "Este horário já está ocupado por outro agendamento.", "Erro de Agendamento", JOptionPane.ERROR_MESSAGE);
            return;
        }

        agendamento.setPaciente(new Paciente(pacienteItem.getId(), null));
        agendamento.setMedico(new Medico(medicoItem.getId()));
        agendamento.setProcedimento(new Procedimento(procedimentoItem.getId()));
        agendamento.setDataHoraInicio(inicioTimestamp);
        agendamento.setDataHoraFim(fimTimestamp);
        agendamento.setStatus((String) comboStatus.getSelectedItem());

        if (dao.atualizar(agendamento)) {
            JOptionPane.showMessageDialog(this, "Agendamento atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao atualizar o agendamento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void selecionarItemNoCombo(JComboBox<Item<Integer>> combo, int idParaSelecionar) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == idParaSelecionar) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private GridBagConstraints gbc(int x, int y) { GridBagConstraints g = new GridBagConstraints(); g.gridx = x; g.gridy = y; g.insets = new Insets(5, 5, 5, 5); g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL; return g; }
}
