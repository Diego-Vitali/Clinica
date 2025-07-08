package view;

import dao.AgendamentoDAO;
import model.Agendamento;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuScreen extends JFrame {
    private Usuario usuarioLogado;
    private JTable tabelaAgendamentos;
    private DefaultTableModel modeloTabela;

    public MenuScreen(Usuario usuario) {
        this.usuarioLogado = usuario;
        setTitle("Clínica Médica - Painel Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonsPanel(), BorderLayout.EAST);
        add(mainPanel);
        carregarAgendamentos();
    }
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        String mensagem = String.format("Usuário: %s | Função: %s", usuarioLogado.getEmail(), usuarioLogado.getFuncao().toUpperCase());
        JLabel labelBoasVindas = new JLabel(mensagem);
        labelBoasVindas.setFont(new Font("Arial", Font.BOLD, 16));
        JButton botaoSair = new JButton("Sair");
        botaoSair.addActionListener(e -> fazerLogout());
        headerPanel.add(labelBoasVindas, BorderLayout.WEST);
        headerPanel.add(botaoSair, BorderLayout.EAST);
        return headerPanel;
    }
    private JScrollPane createTablePanel() {
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Data", "Início", "Fim", "Paciente", "Médico", "Procedimento", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaAgendamentos = new JTable(modeloTabela);
        tabelaAgendamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaAgendamentos.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaAgendamentos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabelaAgendamentos.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tabelaAgendamentos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Agenda de Consultas"));
        return scrollPane;
    }
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Ações"));
        String funcao = usuarioLogado.getFuncao();
        if (funcao.equalsIgnoreCase("ADMIN") || funcao.equalsIgnoreCase("RECEPCIONISTA")) buttonsPanel.add(createMenuButton("Cadastrar Agendamento"));
        if (funcao.equalsIgnoreCase("ADMIN")) buttonsPanel.add(createMenuButton("Cadastrar Funcionário"));
        if (funcao.equalsIgnoreCase("ADMIN") || funcao.equalsIgnoreCase("RECEPCIONISTA")) buttonsPanel.add(createMenuButton("Cadastrar Paciente"));
        if (funcao.equalsIgnoreCase("ADMIN") || funcao.equalsIgnoreCase("RECEPCIONISTA") || funcao.equalsIgnoreCase("MEDICO")) buttonsPanel.add(createMenuButton("Cadastrar Prontuário"));
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonsPanel.add(createMenuButton("Visualizar/Editar Agendamentos"));
        if (funcao.equalsIgnoreCase("ADMIN")) buttonsPanel.add(createMenuButton("Visualizar/Editar Funcionários"));
        if (funcao.equalsIgnoreCase("ADMIN") || funcao.equalsIgnoreCase("RECEPCIONISTA") || funcao.equalsIgnoreCase("MEDICO")) {
            buttonsPanel.add(createMenuButton("Visualizar/Editar Pacientes"));
            buttonsPanel.add(createMenuButton("Visualizar/Editar Prontuários"));
        }
        buttonsPanel.add(Box.createVerticalGlue());
        return buttonsPanel;
    }
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        if ("Cadastrar Funcionário".equals(text)) button.addActionListener(e -> new CadastroFuncionarioScreen(usuarioLogado).setVisible(true));
        else if ("Cadastrar Paciente".equals(text)) button.addActionListener(e -> new CadastroPacienteScreen(usuarioLogado).setVisible(true));
        else if ("Cadastrar Prontuário".equals(text)) button.addActionListener(e -> new CadastroProntuarioScreen(usuarioLogado).setVisible(true));
        else if ("Visualizar/Editar Pacientes".equals(text)) button.addActionListener(e -> new TabelaPacientesScreen(usuarioLogado).setVisible(true));
        else if ("Visualizar/Editar Prontuários".equals(text)) button.addActionListener(e -> new TabelaProntuariosScreen(usuarioLogado).setVisible(true));
        else if ("Visualizar/Editar Funcionários".equals(text)) button.addActionListener(e -> new TabelaFuncionariosScreen(usuarioLogado).setVisible(true));
        else if ("Cadastrar Agendamento".equals(text)) button.addActionListener(e -> new CadastroAgendamentoScreen(usuarioLogado).setVisible(true));
        else if ("Visualizar/Editar Agendamentos".equals(text)) button.addActionListener(e -> new TabelaAgendamentosScreen(usuarioLogado).setVisible(true));
        else button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Tela de '" + text + "' a ser implementada.", "Aviso", JOptionPane.INFORMATION_MESSAGE));
        return button;
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
    private void fazerLogout() {
        if (JOptionPane.showConfirmDialog(this, "Você tem certeza que deseja sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginScreen().setVisible(true);
        }
    }
}
