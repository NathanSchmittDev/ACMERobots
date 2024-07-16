package aplicacao.GUI;

import dados.Clientes.Cliente;
import dados.Locacao;
import dados.Robos.Robo;
import dados.Status;
import dados.XMLMaster;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class CadastroLocacoes extends JFrame implements ActionListener {
    private JComboBox<Cliente> comboBoxClientes;
    private JList<Robo> listaRobos;
    private JTextField numLocacao;
    private JTextField dataInicio;
    private JTextField dataFim;
    private JButton cadastraLocacao, fechar, limpar;
    private JTextArea mensagens, areaLocacoes;
    private List<Cliente> clientes;
    private List<Robo> robos;
    private Map<Integer, Locacao> locacoes;

    private final String arquivoDados = "dados.xml";

    public CadastroLocacoes() {
        super();
        this.setTitle("Cadastrar uma nova locação");

        this.clientes = new ArrayList<>();
        this.robos = new ArrayList<>();
        this.locacoes = new TreeMap<>();

        mensagens = new JTextArea(5, 20);
        mensagens.setEditable(false);
        areaLocacoes = new JTextArea(10, 40);
        areaLocacoes.setEditable(false);

        cadastraLocacao = new JButton("Confirmar");
        limpar = new JButton("Limpar");
        fechar = new JButton("Fechar");

        JLabel clienteLabel = new JLabel("Selecione o Cliente");
        JLabel roboLabel = new JLabel("Selecione o(s) Robô(s)");
        JLabel numLocacaoLabel = new JLabel("Número da Locação");
        JLabel dataInicioLabel = new JLabel("Data Início (Ex: 01/01/2024)");
        JLabel dataFimLabel = new JLabel("Data Fim (Ex: 05/01/2024)");

        comboBoxClientes = new JComboBox<>();
        listaRobos = new JList<>();
        listaRobos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaRobos.setLayoutOrientation(JList.VERTICAL);

        numLocacao = new JTextField();
        dataInicio = new JTextField();
        dataFim = new JTextField();

        JPanel container = new JPanel();
        BoxLayout boxLayout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(boxLayout);

        JPanel botoesClientesRobos = new JPanel();
        container.add(botoesClientesRobos);

        container.add(clienteLabel);
        container.add(comboBoxClientes);
        container.add(roboLabel);
        container.add(new JScrollPane(listaRobos));
        container.add(numLocacaoLabel);
        container.add(numLocacao);
        container.add(dataInicioLabel);
        container.add(dataInicio);
        container.add(dataFimLabel);
        container.add(dataFim);
        container.add(cadastraLocacao);
        container.add(limpar);
        container.add(fechar);
        container.add(new JScrollPane(mensagens));
        container.add(new JScrollPane(areaLocacoes));

        this.add(container);
        pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        cadastraLocacao.addActionListener(this);
        limpar.addActionListener(this);
        fechar.addActionListener(this);

        carregarDados();
    }

    private void carregarDados() {
        try {
            Map<Integer, Cliente> clientesMap = XMLMaster.carregarClientes();
            Map<Integer, Robo> robosMap = XMLMaster.carregarRobos();

            clientes.addAll(clientesMap.values());
            for (Cliente cliente : clientes) {
                comboBoxClientes.addItem(cliente);
            }

            robos.addAll(robosMap.values());
            listaRobos.setListData(robos.toArray(new Robo[0]));

            locacoes = XMLMaster.carregarLocacoes(clientesMap, robosMap);

            atualizarAreaLocacoes();

        } catch (Exception e) {
            mensagens.setText("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void atualizarAreaLocacoes() {
        areaLocacoes.setText("");
        for (Locacao locacao : locacoes.values()) {
            areaLocacoes.append(String.format("ID: %d | Cliente: %s | Robôs: %s | Data Início: %s | Data Fim: %s%n",
                    locacao.getNumero(),
                    locacao.getCliente().getNome(),
                    locacao.getRobosIds(),
                    locacao.getDataInicio(),
                    locacao.getDataFim()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cadastraLocacao) {
            cadastrarLocacao();
        } else if (e.getSource() == limpar) {
            limparCampos();
        } else if (e.getSource() == fechar) {
            dispose();
        }
    }

    private void cadastrarLocacao() {
        try {
            int numero = Integer.parseInt(numLocacao.getText());
            Cliente clienteSelecionado = (Cliente) comboBoxClientes.getSelectedItem();
            Date dataInicioLocacao = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicio.getText());
            Date dataFimLocacao = new SimpleDateFormat("dd/MM/yyyy").parse(dataFim.getText());
            List<Robo> robosSelecionados = listaRobos.getSelectedValuesList();

            Locacao novaLocacao = new Locacao(numero, Status.CADASTRADA, dataInicioLocacao, dataFimLocacao);
            novaLocacao.setCliente(clienteSelecionado);

            for (Robo robo : robosSelecionados) {
                novaLocacao.addRobo(robo);
            }

            locacoes.put(novaLocacao.getNumero(), novaLocacao);
            XMLMaster.salvarLocacoes(locacoes);
            atualizarAreaLocacoes();
            limparCampos();
            mensagens.setText("Locação cadastrada com sucesso.");

        } catch (ParseException ex) {
            mensagens.setText("Erro ao converter data: " + ex.getMessage());
        } catch (Exception ex) {
            mensagens.setText("Erro ao cadastrar locação: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        comboBoxClientes.setSelectedIndex(-1);
        listaRobos.clearSelection();
        numLocacao.setText("");
        dataInicio.setText("");
        dataFim.setText("");
        mensagens.setText("");
    }

}