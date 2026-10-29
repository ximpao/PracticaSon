package uvm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMatrizHilos extends JFrame {
    HiloCazador hcaz;
    HiloClima hcli;
    HiloLimpiador hlim;
    HiloRebotador hreb;
    HiloSerpiente hser;

    private JLabel[][] matriz = new JLabel[15][15];

    private JButton btIniciarS, btPausaS, btTerminarS;
    private JButton btIniciarC, btPausaC, btTerminarC;
    private JButton btIniciarR, btPausaR, btTerminarR;
    private JButton btIniciarCl, btPausaCl, btTerminarCl;
    private JButton btIniciarL, btPausaL, btTerminarL;

    public VentanaMatrizHilos() {
        super("Práctica de Concurrencia - Matriz Compartida");
        inicializarGUI();
        asociarEventos();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void inicializarGUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelMatriz = new JPanel(new GridLayout(15, 15, 2, 2));
        panelMatriz.setBorder(BorderFactory.createTitledBorder("Matriz Compartida (15x15)"));
        
        for (int fila = 0; fila < 15; fila++) {
            for (int col = 0; col < 15; col++) {
                JLabel lbl = new JLabel("-", SwingConstants.CENTER);
                lbl.setOpaque(true);
                lbl.setBackground(Color.WHITE);
                lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                lbl.setFont(new Font("Monospaced", Font.BOLD, 18));
                
                matriz[fila][col] = lbl;
                panelMatriz.add(lbl);
            }
        }

        add(panelMatriz, BorderLayout.CENTER);

        // Paneles Norte, Sur y Oeste
        JPanel panelNorte = new JPanel(new GridLayout(1, 2, 5, 5));

        JPanel panelSerpiente = new JPanel(new FlowLayout());
        panelSerpiente.setBorder(BorderFactory.createTitledBorder("Hilo Serpiente"));
        btIniciarS = new JButton("Iniciar"); btPausaS = new JButton("Pausa"); btTerminarS = new JButton("Terminar");
        panelSerpiente.add(btIniciarS); panelSerpiente.add(btPausaS); panelSerpiente.add(btTerminarS);

        JPanel panelCazador = new JPanel(new FlowLayout());
        panelCazador.setBorder(BorderFactory.createTitledBorder("Hilo Cazador"));
        btIniciarC = new JButton("Iniciar"); btPausaC = new JButton("Pausa"); btTerminarC = new JButton("Terminar");
        panelCazador.add(btIniciarC); panelCazador.add(btPausaC); panelCazador.add(btTerminarC);

        panelNorte.add(panelSerpiente); panelNorte.add(panelCazador);
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelSur = new JPanel(new GridLayout(1, 2, 5, 5));

        JPanel panelRebotador = new JPanel(new FlowLayout());
        panelRebotador.setBorder(BorderFactory.createTitledBorder("Hilo Rebotador"));
        btIniciarR = new JButton("Iniciar"); btPausaR = new JButton("Pausa"); btTerminarR = new JButton("Terminar");
        panelRebotador.add(btIniciarR); panelRebotador.add(btPausaR); panelRebotador.add(btTerminarR);

        JPanel panelClima = new JPanel(new FlowLayout());
        panelClima.setBorder(BorderFactory.createTitledBorder("Hilo Clima"));
        btIniciarCl = new JButton("Iniciar"); btPausaCl = new JButton("Pausa"); btTerminarCl = new JButton("Terminar");
        panelClima.add(btIniciarCl); panelClima.add(btPausaCl); panelClima.add(btTerminarCl);

        panelSur.add(panelRebotador); panelSur.add(panelClima);
        add(panelSur, BorderLayout.SOUTH);

        JPanel panelLimpiador = new JPanel(new GridLayout(3, 1, 5, 5));
        panelLimpiador.setBorder(BorderFactory.createTitledBorder("Limpiador"));
        btIniciarL = new JButton("Iniciar"); btPausaL = new JButton("Pausa"); btTerminarL = new JButton("Terminar");
        panelLimpiador.add(btIniciarL); panelLimpiador.add(btPausaL); panelLimpiador.add(btTerminarL);

        JPanel panelWestContenedor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelWestContenedor.add(panelLimpiador);
        add(panelWestContenedor, BorderLayout.WEST);
    }

    private void asociarEventos() {
        btIniciarS.addActionListener(e -> iniciarSerpiente());
        btPausaS.addActionListener(e -> pausarSerpiente());
        btTerminarS.addActionListener(e -> terminarSerpiente());

        btIniciarC.addActionListener(e -> iniciarCazador());
        btPausaC.addActionListener(e -> pausarCazador());
        btTerminarC.addActionListener(e -> terminarCazador());

        btIniciarR.addActionListener(e -> iniciarRebotador());
        btPausaR.addActionListener(e -> pausarRebotador());
        btTerminarR.addActionListener(e -> terminarRebotador());

        btIniciarCl.addActionListener(e -> iniciarClima());
        btPausaCl.addActionListener(e -> pausarClima());
        btTerminarCl.addActionListener(e -> terminarClima());

        btIniciarL.addActionListener(e -> iniciarLimpiador());
        btPausaL.addActionListener(e -> pausarLimpiador());
        btTerminarL.addActionListener(e -> terminarLimpiador());
    }

    private void iniciarSerpiente() { System.out.println("Evento: Iniciar Serpiente"); }
    private void pausarSerpiente() { System.out.println("Evento: Pausar Serpiente"); }
    private void terminarSerpiente() { System.out.println("Evento: Terminar Serpiente"); }

    private void iniciarCazador() {
        if (hcaz == null || !hcaz.isAlive()) {
            // Colocamos los '@' justo en el momento de dar clic a Iniciar
            matriz[1][2].setText("@");
            matriz[4][7].setText("@");
            matriz[8][12].setText("@");
            matriz[12][3].setText("@");

            hcaz = new HiloCazador(getMatrizLabels());
            hcaz.start();
            System.out.println("Cazador iniciado.");
        } else {
            hcaz.reanudar();
            System.out.println("Cazador reanudado.");
        }
    }

    private void pausarCazador() {
        if (hcaz != null && hcaz.isAlive()) {
            hcaz.pausar();
            System.out.println("Cazador pausado.");
        }
    }

    private void terminarCazador() {
        if (hcaz != null) {
            hcaz.terminar();
            hcaz = null;
            System.out.println("Cazador terminado.");
        }
    }

    private void iniciarRebotador() { System.out.println("Evento: Iniciar Rebotador"); }
    private void pausarRebotador() { System.out.println("Evento: Pausar Rebotador"); }
    private void terminarRebotador() { System.out.println("Evento: Terminar Rebotador"); }

    private void iniciarClima() { System.out.println("Evento: Iniciar Clima"); }
    private void pausarClima() { System.out.println("Evento: Pausar Clima"); }
    private void terminarClima() { System.out.println("Evento: Terminar Clima"); }

    private void iniciarLimpiador() { System.out.println("Evento: Iniciar Limpiador"); }
    private void pausarLimpiador() { System.out.println("Evento: Pausar Limpiador"); }
    private void terminarLimpiador() { System.out.println("Evento: Terminar Limpiador"); }

    public JLabel[][] getMatrizLabels() {
        return matriz;
    }
}