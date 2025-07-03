package view;
import main.MainClass;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginScreen() {
        setTitle("Login - Clínica Médica");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Tela cheia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criar um painel principal com GridBagLayout para centralizar o formulário
        JPanel mainPanel = new JPanel(new GridBagLayout());
        // Ajuste o espaçamento externo para centralizar o formPanel visualmente
        mainPanel.setBorder(BorderFactory.createEmptyBorder(300, 500, 300, 500));

        GridBagConstraints gbcMainPanel = new GridBagConstraints();
        gbcMainPanel.fill = GridBagConstraints.BOTH; // Permitir que o formPanel preencha o espaço
        gbcMainPanel.weightx = 1.0; // Permitir que o formPanel se expanda horizontalmente
        gbcMainPanel.weighty = 1.0; // Permitir que o formPanel se expanda verticalmente


        // Painel para o formulário de login, agora usando GridBagLayout para flexibilidade
        JPanel formPanel = new JPanel(new GridBagLayout()); // Mudado para GridBagLayout
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), // Borda com efeito
                "Credenciais de Acesso",
                TitledBorder.CENTER, // Título centralizado
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16) // Fonte maior para o título da borda
        ));
        // --- Reduzir o EmptyBorder do formPanel para um tamanho mediano ---
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            formPanel.getBorder(), // Mantém a borda original
            BorderFactory.createEmptyBorder(15, 25, 15, 25) // Valores ajustados para um tamanho mediano
        ));

        // Fontes para os rótulos e campos
        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        // GridBagConstraints para os componentes dentro do formPanel
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(8, 8, 8, 8); // Espaçamento entre os elementos do formulário

        // --- Adicionar componentes ao formPanel usando GridBagLayout ---

        // Rótulo Usuário
        JLabel usernameLabel = new JLabel("Usuário:");
        usernameLabel.setFont(labelFont);
        gbcForm.gridx = 0;
        gbcForm.gridy = 0;
        gbcForm.anchor = GridBagConstraints.WEST; // Alinhar à esquerda
        gbcForm.fill = GridBagConstraints.NONE; // Não preencher o espaço horizontal
        gbcForm.weightx = 0; // Não consumir espaço extra
        formPanel.add(usernameLabel, gbcForm);

        // Campo Usuário
        usernameField = new JTextField(20); // Tamanho preferencial inicial
        usernameField.setFont(fieldFont);
        gbcForm.gridx = 1;
        gbcForm.gridy = 0;
        gbcForm.anchor = GridBagConstraints.WEST; // Alinhar à esquerda
        gbcForm.fill = GridBagConstraints.HORIZONTAL; // Preencher horizontalmente
        gbcForm.weightx = 1.0; // Consumir espaço extra horizontal
        formPanel.add(usernameField, gbcForm);

        // Rótulo Senha
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(labelFont);
        gbcForm.gridx = 0;
        gbcForm.gridy = 1;
        gbcForm.anchor = GridBagConstraints.WEST; // Alinhar à esquerda
        gbcForm.fill = GridBagConstraints.NONE;
        gbcForm.weightx = 0;
        formPanel.add(passwordLabel, gbcForm);

        passwordField = new JPasswordField(20); 
        passwordField.setFont(fieldFont);
        gbcForm.gridx = 1;
        gbcForm.gridy = 1;
        gbcForm.anchor = GridBagConstraints.WEST; 
        gbcForm.fill = GridBagConstraints.HORIZONTAL; 
        gbcForm.weightx = 1.0; 
        formPanel.add(passwordField, gbcForm);

        loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbcForm.gridx = 0;
        gbcForm.gridy = 2;
        gbcForm.gridwidth = 2; // Ocupar 2 colunas
        gbcForm.anchor = GridBagConstraints.CENTER; // Centralizar o botão
        gbcForm.fill = GridBagConstraints.NONE; // Não preencher o espaço (o botão mantém o tamanho)
        gbcForm.weightx = 0; // Não consumir espaço extra para o botão
        formPanel.add(loginButton, gbcForm);


        // Adiciona o painel do formulário ao centro do painel principal usando GridBagLayout
        gbcMainPanel.gridx = 0;
        gbcMainPanel.gridy = 0;
        gbcMainPanel.anchor = GridBagConstraints.CENTER; // Centraliza o formPanel no espaço total
        mainPanel.add(formPanel, gbcMainPanel); // Usa gbcMainPanel para o formPanel

        // Adiciona um listener para o botão de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        // Adiciona o painel principal ao frame
        add(mainPanel);
    }
    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); // Converte a senha para String

        // Exemplo de validação simples (apenas para demonstração)
        if (username.equals("admin") && password.equals("123")) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            // Se o login for bem-sucedido, você fecharia a tela de login
            // e abriria a tela principal do sistema.
            dispose(); // Fecha a tela de login
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    MainClass.createMainScreen(); // Chama a tela principal da MainClass
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // Limpa o campo de senha
        }
    }

    // Método main para testar a tela de login individualmente (opcional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }
}