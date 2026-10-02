package a2.buscaminas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    boolean[] casillasMarcadas = new boolean[TOTAL_CASILLAS];
    JLabel mensaje = new JLabel("Pulsa una casilla");
    JLabel contadorMinas = new JLabel();
    Random aleatorio = new Random();
    int marcasColocadas;

    public NewJFrame() {
        setTitle("Buscaminas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelArriba = new JPanel();
        actualizarContadorMinas();
        panelArriba.add(contadorMinas);
        add(panelArriba, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(FILAS, COLUMNAS));
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            casillas[i] = new JButton();
            casillas[i].setPreferredSize(new Dimension(45, 45));
            final int posicion = i;
            casillas[i].addActionListener((ActionEvent e) -> {
                pulsarCasilla(posicion);
            });
            casillas[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        marcarCasilla(posicion);
                    }
                }
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
        if (casillasMarcadas[posicion]) {
            mensaje.setText("Quita la marca antes de abrir esta casilla");
            return;
        }

        casillas[posicion].setEnabled(false);

        if (minas[posicion]) {
            casillas[posicion].setText("M");
            JOptionPane.showMessageDialog(this,
                    "Has pisado una mina. Se inicia una nueva partida. -1000 de aura",
                    "Nueva partida",
                    JOptionPane.INFORMATION_MESSAGE);
            iniciarNuevaPartida();
        } else {
            int minasCercanas = contarMinasCercanas(posicion);
            casillas[posicion].setText(String.valueOf(minasCercanas));
            mensaje.setText("Casilla segura: " + minasCercanas + " minas cerca");
        }
    }

    /**
     * Cuenta las minas situadas alrededor de una casilla, incluidas diagonales.
     */
    private int contarMinasCercanas(int posicion) {
        int fila = posicion / COLUMNAS;
        int columna = posicion % COLUMNAS;
        int minasCercanas = 0;

        for (int desplazamientoFila = -1; desplazamientoFila <= 1;
                desplazamientoFila++) {
            for (int desplazamientoColumna = -1;
                    desplazamientoColumna <= 1; desplazamientoColumna++) {
                if (desplazamientoFila == 0 && desplazamientoColumna == 0) {
                    continue;
                }

                int filaVecina = fila + desplazamientoFila;
                int columnaVecina = columna + desplazamientoColumna;
                boolean estaDentroTablero = filaVecina >= 0 && filaVecina < FILAS
                        && columnaVecina >= 0 && columnaVecina < COLUMNAS;

                if (estaDentroTablero
                        && minas[filaVecina * COLUMNAS + columnaVecina]) {
                    minasCercanas++;
                }
            }
        }

        return minasCercanas;
    }

    private void marcarCasilla(int posicion) {
        if (!casillas[posicion].isEnabled()) {
            return;
        }

        if (casillasMarcadas[posicion]) {
            casillasMarcadas[posicion] = false;
            casillas[posicion].setText("");
            marcasColocadas--;
            mensaje.setText("Marca quitada");
        } else if (marcasColocadas < NUMERO_MINAS) {
            casillasMarcadas[posicion] = true;
            casillas[posicion].setText("MARCADA");
            marcasColocadas++;
            mensaje.setText("Casilla marcada");
        } else {
            mensaje.setText("Solo puedes marcar " + NUMERO_MINAS + " casillas");
            return;
        }

        actualizarContadorMinas();
    }

    private void actualizarContadorMinas() {
        contadorMinas.setText("Minas por marcar: "
                + (NUMERO_MINAS - marcasColocadas));
    }

    private void iniciarNuevaPartida() {
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            casillas[i].setText("");
            casillas[i].setEnabled(true);
            casillasMarcadas[i] = false;
        }
        marcasColocadas = 0;
        generarMinas();
        actualizarContadorMinas();
        mensaje.setText("Nueva partida");
    }

    public static void main(String[] args) {
        new NewJFrame().setVisible(true);
    }
}
