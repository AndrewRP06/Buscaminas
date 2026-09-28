/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package a1.buscaminas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
 
public class NewJFrame extends JFrame {
 
    //
    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(NewJFrame.class.getName());
    //PROBLEMA ARREGLADO POR CLAUDE.IA
    
    private static final int FILAS = 10;
    private static final int COLUMNAS = 10;
    private static final int TOTAL_CASILLAS = FILAS * COLUMNAS;
    private static final int NUMERO_MINAS = 10;
 
    private static final Color COLOR_TAPADA = new Color(120, 144, 190);
    private static final Color COLOR_DESCUBIERTA = Color.WHITE;
    private static final Color COLOR_MINA = new Color(220, 70, 70);
    private static final Color COLOR_MINA_GANADA = new Color(110, 190, 110);
 
    JButton[] casillas = new JButton[TOTAL_CASILLAS];
    boolean[] minas = new boolean[TOTAL_CASILLAS];
    boolean[] casillasMarcadas = new boolean[TOTAL_CASILLAS];
    boolean[] casillasDescubiertas = new boolean[TOTAL_CASILLAS];
    JLabel mensaje = new JLabel("Pulsa una casilla");
    JLabel contadorMinas = new JLabel();
    Random aleatorio = new Random();
    int marcasColocadas;
    int totalDescubiertas;
    boolean partidaTerminada;
 
    public NewJFrame() {
        setTitle("Buscaminas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
 
        GridBagConstraints gbc = new GridBagConstraints();
 
        // ---------- Panel superior: título y contador ----------
        JPanel panelArriba = new JPanel(new GridBagLayout());
        GridBagConstraints gbcArriba = new GridBagConstraints();
 
        JLabel titulo = new JLabel("BUSCAMINAS");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbcArriba.gridx = 0;
        gbcArriba.gridy = 0;
        gbcArriba.insets = new Insets(8, 8, 2, 8);
        panelArriba.add(titulo, gbcArriba);
 
        gbcArriba.gridy = 1;
        gbcArriba.insets = new Insets(2, 8, 8, 8);
        panelArriba.add(contadorMinas, gbcArriba);
        actualizarContadorMinas();
 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panelArriba, gbc);
 
        // ---------- Panel central: tablero 10x10 ----------
        JPanel panelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.fill = GridBagConstraints.BOTH;
        gbcCentro.weightx = 1.0;
        gbcCentro.weighty = 1.0;
        gbcCentro.insets = new Insets(1, 1, 1, 1);
 
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            casillas[i] = new JButton();
            casillas[i].setPreferredSize(new Dimension(45, 45));
            casillas[i].setMargin(new Insets(0, 0, 0, 0));
            casillas[i].setFocusPainted(false);
            casillas[i].setOpaque(true);
            casillas[i].setBackground(COLOR_TAPADA);
 
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
 
            gbcCentro.gridx = i % COLUMNAS;
            gbcCentro.gridy = i / COLUMNAS;
            panelCentro.add(casillas[i], gbcCentro);
        }
 
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(panelCentro, gbc);
 
        // ---------- Panel inferior: botón y resultado ----------
        JPanel panelAbajo = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAbajo = new GridBagConstraints();
        gbcAbajo.insets = new Insets(8, 8, 8, 8);
 
        gbcAbajo.gridx = 0;
        gbcAbajo.gridy = 0;
        gbcAbajo.weightx = 1.0;
        gbcAbajo.anchor = GridBagConstraints.WEST;
        panelAbajo.add(mensaje, gbcAbajo);
 
        JButton nuevaPartida = new JButton("Nueva partida");
        nuevaPartida.addActionListener((ActionEvent e) -> {
            iniciarNuevaPartida();
        });
        gbcAbajo.gridx = 1;
        gbcAbajo.weightx = 0;
        gbcAbajo.anchor = GridBagConstraints.EAST;
        panelAbajo.add(nuevaPartida, gbcAbajo);
 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panelAbajo, gbc);
 
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
        if (partidaTerminada || casillasDescubiertas[posicion]) {
            return;
        }
 
        if (casillasMarcadas[posicion]) {
            mensaje.setText("Quita la marca antes de abrir esta casilla");
            return;
        }
 
        if (minas[posicion]) {
            perder(posicion);
            return;
        }
 
        int minasCercanas = contarMinasCercanas(posicion);
        casillasDescubiertas[posicion] = true;
        totalDescubiertas++;
        casillas[posicion].setBackground(COLOR_DESCUBIERTA);
        casillas[posicion].setForeground(Color.BLACK);
        casillas[posicion].setText(minasCercanas == 0 ? "" : String.valueOf(minasCercanas));
        mensaje.setText("Casilla segura: " + minasCercanas + " minas cerca");
 
        if (totalDescubiertas == TOTAL_CASILLAS - NUMERO_MINAS) {
            ganar();
        }
    }
 
    private void perder(int posicionPisada) {
        partidaTerminada = true;
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            if (minas[i]) {
                casillas[i].setText("M");
                casillas[i].setForeground(Color.BLACK);
                casillas[i].setBackground(COLOR_MINA);
            }
        }
        casillas[posicionPisada].setBackground(Color.RED.darker());
        mensaje.setText("¡Has pisado una mina! Has perdido");
    }
 
    private void ganar() {
        partidaTerminada = true;
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            if (minas[i]) {
                casillas[i].setText("M");
                casillas[i].setForeground(Color.BLACK);
                casillas[i].setBackground(COLOR_MINA_GANADA);
            }
        }
        mensaje.setText("¡Has ganado! Has abierto todas las casillas seguras");
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
        if (partidaTerminada || casillasDescubiertas[posicion]) {
            return;
        }
 
        if (casillasMarcadas[posicion]) {
            casillasMarcadas[posicion] = false;
            casillas[posicion].setText("");
            marcasColocadas--;
            mensaje.setText("Marca quitada");
        } else if (marcasColocadas < NUMERO_MINAS) {
            casillasMarcadas[posicion] = true;
            casillas[posicion].setText("!");
            casillas[posicion].setForeground(Color.RED);
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
            casillas[i].setBackground(COLOR_TAPADA);
            casillasMarcadas[i] = false;
            casillasDescubiertas[i] = false;
        }
        marcasColocadas = 0;
        totalDescubiertas = 0;
        partidaTerminada = false;
        generarMinas();
        actualizarContadorMinas();
        mensaje.setText("Nueva partida");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, (UnsupportedLookAndFeelException) ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new NewJFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
