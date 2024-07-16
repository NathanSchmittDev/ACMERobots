package aplicacao.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ACMERobots extends JFrame {
    private JTextArea campoAvisos;
    private JButton btnCadastroClientes, 
    btnCadastroRobos, btnCadastroLocacao, btnProcessarLocacoes, btnRelatorioGeral, btnConsultarLocacoes, btnAlterarSituacao, btnFinalizarSistema;

    public ACMERobots() {
        super("ACME Robots - Principal");

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(5, 2, 10, 10));

        btnCadastroClientes = new JButton("Cadastro de Clientes");
        btnCadastroClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCadastroClientes();
            }
        });
        painel.add(btnCadastroClientes);

        btnCadastroRobos = new JButton("Cadastro de Robôs");
        btnCadastroRobos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCadastroRobos();
            }
        });
        painel.add(btnCadastroRobos);

        btnCadastroLocacao = new JButton("Cadastro de Locação");
        btnCadastroLocacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarLocacao();
            }
        });
        painel.add(btnCadastroLocacao);

        btnProcessarLocacoes = new JButton("Processar Locações");
        btnProcessarLocacoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processarLocacoes();
            }
        });
        painel.add(btnProcessarLocacoes);

        btnRelatorioGeral = new JButton("Relatório Geral");
        btnRelatorioGeral.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRelatorioGeral();
            }
        });
        painel.add(btnRelatorioGeral);

        btnConsultarLocacoes = new JButton("Consultar Locações");
        btnConsultarLocacoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarLocacoes();
            }
        });
        painel.add(btnConsultarLocacoes);

        btnAlterarSituacao = new JButton("Alterar Situação de Locação");
        btnAlterarSituacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarSituacaoLocacao();
            }
        });
        painel.add(btnAlterarSituacao);

        btnFinalizarSistema = new JButton("Finalizar Sistema");
        btnFinalizarSistema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarSistema();
            }
        });
        painel.add(btnFinalizarSistema);

        campoAvisos = new JTextArea(10, 30);
        campoAvisos.setEditable(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(campoAvisos), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void enviarAviso(String mensagem) {
        campoAvisos.append(mensagem + "\n");
    }

    private void abrirCadastroClientes() {
        enviarAviso("Abrindo cadastro de clientes...");
        CadastroClientes cadastroClientes = new CadastroClientes();
        cadastroClientes.setVisible(true);
    }

    private void abrirCadastroRobos() {
        enviarAviso("Abrindo cadastro de robôs...");
        CadastroRobos cadastroRobos = new CadastroRobos();
        cadastroRobos.setVisible(true);
    }

    private void cadastrarLocacao() {
        enviarAviso("Abrindo cadastro de locação...");
        CadastroLocacoes cadastroLocacao = new CadastroLocacoes();
        cadastroLocacao.setVisible(true);
    }

    private void processarLocacoes() {
        // Implemente a lógica para processar locações
        enviarAviso("Faltando codigo...");
    }

    public void mostrarRelatorioGeral() {
        RelatorioGeral relatorioGeral = new RelatorioGeral();
        relatorioGeral.setVisible(true);
        enviarAviso("Abrindo Relatórios");
}

    private void consultarLocacoes() {
        ConsultaLocacoes consultaLocacoes = new ConsultaLocacoes();
        consultaLocacoes.setVisible(true);
        enviarAviso("Abrindo Consulta de Locacoes");
    }

    private void alterarSituacaoLocacao() {
        enviarAviso("Faltando codigo...");
    }

    private void finalizarSistema() {
        System.exit(0);
    }

}