package view;

import dao.UsuarioDAO;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoLogin;

    public LoginScreen() {
        setTitle("Login - Clínica Médica");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(200, 400, 200, 400));

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Credenciais de Acesso"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        Font font = new Font("Arial", Font.PLAIN, 16);

        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setFont(font);
        campoEmail = new JTextField(20);
        campoEmail.setFont(font);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(font);
        campoSenha = new JPasswordField(20);
        campoSenha.setFont(font);

        botaoLogin = new JButton("Entrar");
        botaoLogin.setFont(new Font("Arial", Font.BOLD, 18));
        botaoLogin.setBackground(new Color(70, 130, 180));
        botaoLogin.setForeground(Color.WHITE);
        botaoLogin.setFocusPainted(false);
        botaoLogin.addActionListener(e -> realizarLogin());

        addToPanel(panel, emailLabel, gbc, 0, 0);
        addToPanel(panel, campoEmail, gbc, 1, 0);
        addToPanel(panel, senhaLabel, gbc, 0, 1);
        addToPanel(panel, campoSenha, gbc, 1, 1);

        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(botaoLogin, gbc);
        
        return panel;
    }

    private void realizarLogin() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        if (email.trim().isEmpty() || senha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha os campos de e-mail e senha.", "Campos Vazios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioLogado = usuarioDAO.logar(email, senha);

        if (usuarioLogado != null) {
            new MenuScreen(usuarioLogado).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou senha incorretos, ou usuário inativo.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addToPanel(JPanel panel, Component comp, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = (comp instanceof JLabel) ? GridBagConstraints.WEST : GridBagConstraints.CENTER;
        gbc.fill = (comp instanceof JTextField || comp instanceof JPasswordField) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        gbc.weightx = (comp instanceof JTextField || comp instanceof JPasswordField) ? 1.0 : 0;
        panel.add(comp, gbc);
    }
}
