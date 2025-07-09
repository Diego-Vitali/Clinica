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

public class CadastroAgendamentoScreen extends JFrame {

    private JComboBox<Item<Integer>> comboPaciente, comboMedico, comboProcedimento;
    private JTextField campoData, campoHora;
    private Usuario usuarioLogado;

    public CadastroAgendamentoScreen(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Cadastro de Agendamento");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(createFormPanel());
        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Agendamento"));
        int y = 0;

        comboPaciente = new JComboBox<>();
        new PacienteDAO().listarTodos().forEach(p -> comboPaciente.addItem(new Item<>(p.getId(), p.getNomeCompleto())));
        panel.add(new JLabel("Paciente:"), gbc(0, y)); panel.add(comboPaciente, gbc(1, y++));

        comboMedico = new JComboBox<>();
        panel.add(new JLabel("Médico:"), gbc(0, y)); panel.add(comboMedico, gbc(1, y++));
        
        if ("MEDICO".equalsIgnoreCase(usuarioLogado.getFuncao())) {
            MedicoDAO medicoDAO = new MedicoDAO();
            Medico medicoLogado = medicoDAO.buscarPorUsuarioId(usuarioLogado.getId());
            if (medicoLogado != null) {
                comboMedico.addItem(new Item<>(medicoLogado.getId(), medicoLogado.getFuncionario().getNomeCompleto()));
                comboMedico.setEnabled(false);
            }
        } else {
            new MedicoDAO().listarTodosAtivos().forEach(m -> comboMedico.addItem(new Item<>(m.getId(), m.getFuncionario().getNomeCompleto())));
        }
        
        comboProcedimento = new JComboBox<>();
        new ProcedimentoDAO().listarTodosAtivos().forEach(p -> comboProcedimento.addItem(new Item<>(p.getId(), p.getNome() + " (" + p.getDuracaoMinutos() + " min)")));
        panel.add(new JLabel("Procedimento:"), gbc(0, y)); panel.add(comboProcedimento, gbc(1, y++));

        campoData = new JTextField(10);
        panel.add(new JLabel("Data (YYYY-MM-DD):"), gbc(0, y)); panel.add(campoData, gbc(1, y++));
        
        campoHora = new JTextField(5);
        panel.add(new JLabel("Hora (HH:MM):"), gbc(0, y)); panel.add(campoHora, gbc(1, y++));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarAgendamento());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnCancelar);
        GridBagConstraints gbcBotoes = gbc(0, y); gbcBotoes.gridwidth = 2; panel.add(botoesPanel, gbcBotoes);

        return panel;
    }

    private void salvarAgendamento() {
        Item<Integer> pacienteItem = (Item<Integer>) comboPaciente.getSelectedItem();
        Item<Integer> medicoItem = (Item<Integer>) comboMedico.getSelectedItem();
        Item<Integer> procedimentoItem = (Item<Integer>) comboProcedimento.getSelectedItem();

        if (pacienteItem == null || medicoItem == null || procedimentoItem == null) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser selecionados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dataStr = campoData.getText();
        String horaStr = campoHora.getText();
        LocalDateTime dataHoraInicio;
        try {
            dataHoraInicio = LocalDateTime.parse(dataStr + " " + horaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de data ou hora inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Procedimento procSelecionado = new ProcedimentoDAO().listarTodosAtivos().stream()
                .filter(p -> p.getId() == procedimentoItem.getId()).findFirst().orElse(null);
        if (procSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Procedimento não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(procSelecionado.getDuracaoMinutos());
        Timestamp inicioTimestamp = Timestamp.valueOf(dataHoraInicio);
        Timestamp fimTimestamp = Timestamp.valueOf(dataHoraFim);

        AgendamentoDAO dao = new AgendamentoDAO();
        if (!dao.isDentroDoHorarioDeTrabalho(medicoItem.getId(), inicioTimestamp, fimTimestamp)) {
            JOptionPane.showMessageDialog(this, "O horário solicitado está fora do expediente do médico.", "Erro de Agendamento", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dao.isHorarioOcupado(medicoItem.getId(), inicioTimestamp, fimTimestamp, 0)) {
            JOptionPane.showMessageDialog(this, "Este horário já está ocupado por outro agendamento.", "Erro de Agendamento", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Agendamento ag = new Agendamento();
        ag.setPaciente(new Paciente(pacienteItem.getId(), null));
        ag.setMedico(new Medico(medicoItem.getId()));
        ag.setProcedimento(new Procedimento(procedimentoItem.getId()));
        ag.setDataHoraInicio(inicioTimestamp);
        ag.setDataHoraFim(fimTimestamp);
        ag.setCriadoPorUsuarioId(usuarioLogado.getId());

        if (dao.inserir(ag)) {
            JOptionPane.showMessageDialog(this, "Agendamento criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao criar o agendamento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private GridBagConstraints gbc(int x, int y) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x;
        g.gridy = y;
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        return g;
    }
}
