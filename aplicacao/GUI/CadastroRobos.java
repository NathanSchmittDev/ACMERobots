package aplicacao.GUI;

import dados.Robos.*;
import dados.XMLMaster;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

public class CadastroRobos extends JFrame {
    private JTextField campoId, campoValorDiario, campoModelo, campoNivel, campoSetor, campoArea, campoUso;
    private JComboBox<String> tipoComboBox;
    private JTextArea campoAvisos;
    private JButton botaoCadastrar, botaoLimpar, botaoListarRobos, botaoFechar;
    private Map<Integer, Robo> robos;
    private final String arquivoDados = "dados.xml";

    public CadastroRobos() {
        super("Cadastro de Robôs");

        initComponents();

        carregarDados();
    }

    private void initComponents() {
        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new GridLayout(11, 5, 20, 20));

        JLabel lblId = new JLabel("ID:");
        campoId = new JTextField(10);
        painelCadastro.add(lblId);
        painelCadastro.add(campoId);

        JLabel lblValorDiario = new JLabel("Valor Diário:");
        campoValorDiario = new JTextField(20);
        painelCadastro.add(lblValorDiario);
        painelCadastro.add(campoValorDiario);

        JLabel lblModelo = new JLabel("Modelo:");
        campoModelo = new JTextField(20);
        painelCadastro.add(lblModelo);
        painelCadastro.add(campoModelo);

        JLabel lblTipo = new JLabel("Tipo de Robô:");
        tipoComboBox = new JComboBox<>(new String[]{"Doméstico", "Industrial", "Agrícola"});
        tipoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCamposAdicionais();
            }
        });
        painelCadastro.add(lblTipo);
        painelCadastro.add(tipoComboBox);

        JLabel lblNivel = new JLabel("Nível (para Doméstico):");
        campoNivel = new JTextField(20);
        painelCadastro.add(lblNivel);
        painelCadastro.add(campoNivel);

        JLabel lblSetor = new JLabel("Setor (para Industrial):");
        campoSetor = new JTextField(20);
        painelCadastro.add(lblSetor);
        painelCadastro.add(campoSetor);

        JLabel lblArea = new JLabel("Área (para Agrícola):");
        campoArea = new JTextField(20);
        painelCadastro.add(lblArea);
        painelCadastro.add(campoArea);

        JLabel lblUso = new JLabel("Uso (para Agrícola):");
        campoUso = new JTextField(20);
        painelCadastro.add(lblUso);
        painelCadastro.add(campoUso);

        atualizarCamposAdicionais();

        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarRobo();
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

        botaoListarRobos = new JButton("Listar Robôs");
        botaoListarRobos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarRobos();
            }
        });
        painelCadastro.add(botaoListarRobos);

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
        String tipo = (String) tipoComboBox.getSelectedItem();
        campoNivel.setEnabled("Doméstico".equals(tipo));
        campoSetor.setEnabled("Industrial".equals(tipo));
        campoArea.setEnabled("Agrícola".equals(tipo));
        campoUso.setEnabled("Agrícola".equals(tipo));
    }

    private void cadastrarRobo() {
        try {
            int id = Integer.parseInt(campoId.getText());
            double valorDiario = Double.parseDouble(campoValorDiario.getText());
            String modelo = campoModelo.getText();

            if (robos.containsKey(id)) {
                enviarAviso("Já existe um robô com esse ID.");
            } else {
                String tipo = (String) tipoComboBox.getSelectedItem();
                Robo robo;
                switch (tipo) {
                    case "Doméstico":
                        int nivel = Integer.parseInt(campoNivel.getText());
                        robo = new Domestico(id, modelo, valorDiario, nivel);
                        break;
                    case "Industrial":
                        String setor = campoSetor.getText();
                        robo = new Industrial(id, modelo, valorDiario, setor);
                        break;
                    case "Agrícola":
                        double area = Double.parseDouble(campoArea.getText());
                        String uso = campoUso.getText();
                        robo = new Agricola(id, modelo, valorDiario, area, uso);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo de robô desconhecido: " + tipo);
                }

                robos.put(id, robo);
                salvarDados();
                enviarAviso("Robô cadastrado.");
            }
        } catch (NumberFormatException ex) {
            enviarAviso("ID deve ser um número inteiro e Valor Diário deve ser um número.");
        } catch (IllegalArgumentException ex) {
            enviarAviso(ex.getMessage());
        }
    }

    private void limpar() {
        campoId.setText("");
        campoValorDiario.setText("");
        campoModelo.setText("");
        campoNivel.setText("");
        campoSetor.setText("");
        campoArea.setText("");
        campoUso.setText("");
        campoAvisos.setText("");
        campoId.requestFocus();
    }

    private void enviarAviso(String mensagem) {
        campoAvisos.append(mensagem + "\n");
    }

    private void listarRobos() {
        campoAvisos.setText("");

        for (Robo robo : robos.values()) {
            campoAvisos.append(robo.toString() + "\n");
        }
    }

    private void carregarDados() {
        try {
            robos = XMLMaster.carregarRobos();
        } catch (Exception e) {
            enviarAviso("Erro ao carregar dados: " + e.getMessage());
            robos = new TreeMap<>();
        }
    }

    private void salvarDados() {
        try {
            XMLMaster.salvarRobos(robos);
        } catch (Exception e) {
            enviarAviso("Erro ao salvar dados: " + e.getMessage());
        }
    }

}