package aplicacao.GUI;

import dados.Clientes.Cliente;
import dados.Clientes.Empresarial;
import dados.Clientes.Individual;
import dados.Locacao;
import dados.Robos.Agricola;
import dados.Robos.Domestico;
import dados.Robos.Industrial;
import dados.Robos.Robo;
import dados.XMLMaster;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Map;

public class RelatorioGeral extends JFrame {
    private JTextArea relatorioArea;
    private JTable relatorioTable;
    private DefaultTableModel tableModel;
    private final String arquivoDados = "dados.xml";

    public RelatorioGeral() {
        super("Relatório Geral");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());

        String[] colunas = {"Tipo", "Detalhes"};
        tableModel = new DefaultTableModel(colunas, 0);
        relatorioTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(relatorioTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botão para recarregar relatório
        JButton btnRecarregar = new JButton("Recarregar Relatório");
        btnRecarregar.addActionListener(e -> gerarRelatorio());
        panel.add(btnRecarregar, BorderLayout.SOUTH);

        getContentPane().add(panel);

        setLocationRelativeTo(null);
        setVisible(true);

        gerarRelatorio();
    }

    private void gerarRelatorio() {
        tableModel.setRowCount(0);

        try {
            Map<Integer, Cliente> clientes = XMLMaster.carregarClientes();
            Map<Integer, Robo> robos = XMLMaster.carregarRobos();
            Map<Integer, Locacao> locacoes = XMLMaster.carregarLocacoes(clientes, robos);

            // Listar Clientes
            for (Cliente cliente : clientes.values()) {
                String tipoCliente = cliente instanceof Individual ? "Individual" : "Empresarial";
                String detalhes = cliente instanceof Individual ?
                        String.format("Nome: %s, CPF: %s", cliente.getNome(), ((Individual) cliente).getCpf()) :
                        String.format("Nome: %s, Ano: %d", cliente.getNome(), ((Empresarial) cliente).getAno());

                tableModel.addRow(new Object[]{"Cliente", detalhes});
            }
            if (clientes.isEmpty()) {
                tableModel.addRow(new Object[]{"Clientes Cadastrados", "Nenhum cliente cadastrado."});
            }

            for (Robo robo : robos.values()) {
                String tipoRobo;
                String detalhes;
                if (robo instanceof Domestico) {
                    tipoRobo = "Doméstico";
                    detalhes = String.format("Modelo: %s, Tipo: Doméstico", robo.getModelo());
                } else if (robo instanceof Industrial) {
                    tipoRobo = "Industrial";
                    detalhes = String.format("Modelo: %s, Tipo: Industrial, Setor: %s", robo.getModelo(), ((Industrial) robo).getSetor());
                } else if (robo instanceof Agricola) {
                    tipoRobo = "Agrícola";
                    detalhes = String.format("Modelo: %s, Tipo: Agrícola, Área: %.2f, Uso: %s", robo.getModelo(), ((Agricola) robo).getArea(), ((Agricola) robo).getUso());
                } else {
                    tipoRobo = "Desconhecido";
                    detalhes = String.format("Tipo de robô desconhecido: %s", robo.getClass().getSimpleName());
                }
                tableModel.addRow(new Object[]{"Robô", detalhes});
            }
            if (robos.isEmpty()) {
                tableModel.addRow(new Object[]{"Robôs Cadastrados", "Nenhum robô cadastrado."});
            }

            for (Locacao locacao : locacoes.values()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String detalhes = String.format("Número: %d, Situação: %s, Data Início: %s, Data Fim: %s",
                        locacao.getNumero(), locacao.getSituacao().name(),
                        dateFormat.format(locacao.getDataInicio()), dateFormat.format(locacao.getDataFim()));

                tableModel.addRow(new Object[]{"Locação", detalhes});
            }
            if (locacoes.isEmpty()) {
                tableModel.addRow(new Object[]{"Locações Cadastradas", "Nenhuma locação cadastrada."});
            }

        } catch (Exception e) {
            tableModel.addRow(new Object[]{"Erro ao gerar relatório", e.getMessage()});
        }
    }

}