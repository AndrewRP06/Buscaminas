package a2.buscaminas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NewJFrame extends JFrame {

    private static final int FILAS = 10;
    private static final int COLUMNAS = 10;
    private static final int TOTAL_CASILLAS = FILAS * COLUMNAS;
    private static final int NUMERO_MINAS = 10;

    JButton[] casillas = new JButton[TOTAL_CASILLAS];
    boolean[] minas = new boolean[TOTAL_CASILLAS];
    JLabel mensaje = new JLabel("Pulsa una casilla");
    Random aleatorio = new Random();

    public NewJFrame() {
        setTitle("Buscaminas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelArriba = new JPanel();
        panelArriba.add(new JLabel("Minas: " + NUMERO_MINAS));
        add(panelArriba, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(FILAS, COLUMNAS));
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            casillas[i] = new JButton();
            casillas[i].setPreferredSize(new Dimension(45, 45));
            final int posicion = i;
            casillas[i].addActionListener((ActionEvent e) -> {
                pulsarCasilla(posicion);
            });
            panelCentro.add(casillas[i]);
        }
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelAbajo = new JPanel();
        JButton nuevaPartida = new JButton("Nueva partida");
        nuevaPartida.addActionListener((ActionEvent e) -> {
            iniciarNuevaPartida();
        });
        panelAbajo.add(mensaje);
        panelAbajo.add(nuevaPartida);
        add(panelAbajo, BorderLayout.SOUTH);
        generarMinas();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Coloca minas en posiciones distintas del tablero usando un bucle while.
     */
    private void generarMinas() {
        Arrays.fill(minas, false);
        int minasColocadas = 0;

        while (minasColocadas < NUMERO_MINAS) {
            int posicion = aleatorio.nextInt(TOTAL_CASILLAS);
            if (!minas[posicion]) {
                minas[posicion] = true;
                minasColocadas++;
            }
        }
    }

    private void pulsarCasilla(int posicion) {
        casillas[posicion].setEnabled(false);

        if (minas[posicion]) {
            casillas[posicion].setText("M");
            JOptionPane.showMessageDialog(this,
                    "Has pisado una mina. Se inicia una nueva partida.",
                    "Nueva partida",
                    JOptionPane.INFORMATION_MESSAGE);
            iniciarNuevaPartida();
        } else {
            casillas[posicion].setText("X");
            mensaje.setText("Casilla segura");
        }
    }

    private void iniciarNuevaPartida() {
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            casillas[i].setText("");
            casillas[i].setEnabled(true);
        }
        generarMinas();
        mensaje.setText("Nueva partida");
    }

    public static void main(String[] args) {
        new NewJFrame().setVisible(true);
    }
}
