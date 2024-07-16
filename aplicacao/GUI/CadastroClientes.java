package aplicacao.GUI;

import dados.Clientes.*;
import dados.XMLMaster;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

public class CadastroClientes extends JFrame {
    private JTextField campoCodigo, campoNome, campoCPF, campoAno;
    private JTextArea campoAvisos;
    private JButton botaoCadastrar, botaoLimpar, botaoListarClientes, botaoFechar;
    private JComboBox<String> comboBoxTipoCliente;
    private Map<Integer, Cliente> clientes;
    private final String arquivoDados = "dados.xml";

    public CadastroClientes() {
        super("Cadastro de clientes");

        initComponents();

        try {
            clientes = XMLMaster.carregarClientes();
        } catch (Exception e) {
            enviarAviso("Erro ao carregar dados: " + e.getMessage());
            clientes = new TreeMap<>();
        }
    }

    private void initComponents() {
        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel lblCodigo = new JLabel("Código:");
        campoCodigo = new JTextField(10);
        painelCadastro.add(lblCodigo);
        painelCadastro.add(campoCodigo);

        JLabel lblNome = new JLabel("Nome:");
        campoNome = new JTextField(20);
        painelCadastro.add(lblNome);
        painelCadastro.add(campoNome);

        JLabel lblTipoCliente = new JLabel("Tipo de Cliente:");
        comboBoxTipoCliente = new JComboBox<>();
        comboBoxTipoCliente.addItem("Individual");
        comboBoxTipoCliente.addItem("Empresarial");
        comboBoxTipoCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCamposAdicionais();
            }
        });
        painelCadastro.add(lblTipoCliente);
        painelCadastro.add(comboBoxTipoCliente);

        JLabel lblCPF = new JLabel("CPF:");
        campoCPF = new JTextField(15);
        painelCadastro.add(lblCPF);
        painelCadastro.add(campoCPF);
        campoCPF.setVisible(false);

        JLabel lblAno = new JLabel("Ano:");
        campoAno = new JTextField(10);
        painelCadastro.add(lblAno);
        painelCadastro.add(campoAno);
        campoAno.setVisible(false);

        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });
        painelCadastro.add(botaoCadastrar);

        botaoLimpar = new JButton("Limpar");
        botaoLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpar();
            }
        });
        painelCadastro.add(botaoLimpar);

        botaoListarClientes = new JButton("Listar clientes");
        botaoListarClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarClientes();
            }
        });
        painelCadastro.add(botaoListarClientes);

        botaoFechar = new JButton("Fechar");
        botaoFechar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        painelCadastro.add(botaoFechar);

        campoAvisos = new JTextArea(10, 30);
        campoAvisos.setEditable(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painelCadastro, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(campoAvisos), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void atualizarCamposAdicionais() {
        String tipoCliente = comboBoxTipoCliente.getSelectedItem().toString();
        if (tipoCliente.equals("Individual")) {
            campoCPF.setVisible(true);
            campoAno.setVisible(false);
        } else if (tipoCliente.equals("Empresarial")) {
            campoAno.setVisible(true);
            campoCPF.setVisible(false);
        } else {
            campoCPF.setVisible(false);
            campoAno.setVisible(false);
        }
    }

    private void cadastrarCliente() {
        try {
            int codigo = Integer.parseInt(campoCodigo.getText());
            String nome = campoNome.getText();
            String tipoCliente = comboBoxTipoCliente.getSelectedItem().toString();

            if (clientes.containsKey(codigo)) {
                enviarAviso("Já existe um cliente com esse código.");
            } else {
                Cliente cliente;
                if (tipoCliente.equals("Individual")) {
                    String cpf = campoCPF.getText();
                    cliente = new Individual(codigo, nome, cpf);
                } else {
                    int ano = Integer.parseInt(campoAno.getText());
                    cliente = new Empresarial(codigo, nome, ano);
                }

                clientes.put(codigo, cliente);
                XMLMaster.salvarClientes(clientes);
                enviarAviso("Cliente cadastrado com sucesso.");
            }
        } catch (NumberFormatException ex) {
            enviarAviso("O código deve ser um número inteiro.");
        } catch (Exception ex) {
            enviarAviso("Erro ao cadastrar cliente: " + ex.getMessage());
        }
    }

    private void limpar() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoAvisos.setText("");
        campoCodigo.requestFocus();
    }

    private void enviarAviso(String mensagem) {
        campoAvisos.append(mensagem + "\n");
    }

    private void listarClientes() {
        campoAvisos.setText("");

        for (Map.Entry<Integer, Cliente> entry : clientes.entrySet()) {
            int codigo = entry.getKey();
            Cliente cliente = entry.getValue();
            campoAvisos.append(codigo + ": " + cliente.getNome() + "\n");
        }
    }

}