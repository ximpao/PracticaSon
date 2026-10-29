package uvm;

import javax.swing.*;
import java.awt.*;

public class HiloCazador extends Thread {

    private JLabel[][] matriz;
    private volatile boolean ejecutando = true;
    private volatile boolean pausado = false;
    private int cazados = 0;

    public HiloCazador(JLabel[][] matriz) {
        this.matriz = matriz;
    }

    @Override
    public void run() {
        while (ejecutando) {
            try {
                // Control de pausa del hilo
                synchronized (this) {
                    while (pausado) {
                        wait();
                    }
                }

                // Recorrido de la matriz 15x15 en busca de '@'
                for (int f = 0; f < 15 && ejecutando; f++) {
                    for (int c = 0; c < 15 && ejecutando; c++) {
                        
                        synchronized (this) {
                            while (pausado) {
                                wait();
                            }
                        }

                        JLabel celda = matriz[f][c];
                        String texto = celda.getText();

                        if (texto != null && texto.contains("@")) {
                            cazarObjetivo(celda);
                        }
                    }
                }

                Thread.sleep(100); // Pequeña pausa para no sobrecargar el procesador

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void cazarObjetivo(JLabel celda) throws InterruptedException {
        Color colorOriginal = celda.getForeground();

        // 1. Cambiar texto a rojo y parpadear (1 segundo en total)
        SwingUtilities.invokeLater(() -> celda.setForeground(Color.RED));
        Thread.sleep(300);

        SwingUtilities.invokeLater(() -> celda.setVisible(false));
        Thread.sleep(300);

        SwingUtilities.invokeLater(() -> {
            celda.setVisible(true);
            celda.setForeground(Color.RED);
        });
        Thread.sleep(400);

        // 2. Reemplazar '@' por 'X'
        SwingUtilities.invokeLater(() -> {
            celda.setText(celda.getText().replace("@", "X"));
            celda.setForeground(colorOriginal);
        });

        // 3. Contar e imprimir avance
        cazados++;
        System.out.println("Cazador -> Total de '@' capturados: " + cazados);
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        pausado = false;
        notify();
    }

    public void terminar() {
        ejecutando = false;
        pausado = false;
        this.interrupt();
    }
}