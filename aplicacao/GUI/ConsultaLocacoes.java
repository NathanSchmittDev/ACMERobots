package aplicacao.GUI;

import dados.Clientes.Cliente;
import dados.Locacao;
import dados.Robos.Robo;
import dados.XMLMaster;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ConsultaLocacoes extends JFrame {
    private JTable locacoesTable;
    private DefaultTableModel tableModel;

    public ConsultaLocacoes() {
        super("Consulta de Locações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());

        String[] colunas = {"Número", "Cliente", "Data Início", "Data Fim", "Status", "Robôs", "Valor Total"};
        tableModel = new DefaultTableModel(colunas, 0);
        locacoesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(locacoesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(panel);

        setLocationRelativeTo(null);
        setVisible(true);

        carregarLocacoes();
    }

    private void carregarLocacoes() {
        tableModel.setRowCount(0);

        try {
            Map<Integer, Cliente> clientesMap = XMLMaster.carregarClientes();
            Map<Integer, Robo> robosMap = XMLMaster.carregarRobos();

            Map<Integer, Locacao> locacoes = XMLMaster.carregarLocacoes(clientesMap, robosMap);

            if (locacoes.isEmpty()) {
                tableModel.addRow(new Object[]{"Nenhuma locação cadastrada", "", "", "", "", "", ""});
            } else {
                for (Locacao locacao : locacoes.values()) {
                    StringBuilder robosInfo = new StringBuilder();
                    double valorTotal = 0.0;


                    String robosStr = robosInfo.toString().isEmpty() ? "Nenhum robô alocado" : robosInfo.toString();
                    String clienteNome = locacao.getClientes().isEmpty() ? "Cliente desconhecido" : locacao.getClientes().iterator().next().getNome();
                    String status = locacao.getSituacao().name();
                    String dataInicio = locacao.getDataInicio().toString();
                    String dataFim = locacao.getDataFim().toString();
                    String valorTotalStr = String.format("%.2f", valorTotal);

                    tableModel.addRow(new Object[]{locacao.getNumero(), clienteNome, dataInicio, dataFim, status, robosStr, valorTotalStr});
                }
            }
        } catch (Exception e) {
            tableModel.addRow(new Object[]{"Erro ao carregar locações", "", "", "", "", "", ""});
            JOptionPane.showMessageDialog(this, "Erro ao carregar locações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}