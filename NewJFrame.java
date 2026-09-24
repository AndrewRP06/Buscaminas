package a2.buscaminas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NewJFrame extends JFrame {

    JButton[] casillas = new JButton[64];
    JLabel mensaje = new JLabel("Pulsa una casilla");

    public NewJFrame() {
        setTitle("Buscaminas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelArriba = new JPanel();
        panelArriba.add(new JLabel("Minas: 10"));
        add(panelArriba, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 64; i++) {
            casillas[i] = new JButton();
            casillas[i].setPreferredSize(new Dimension(45, 45));
            // Lambda lo recomienda el IDE se hace asi
            casillas[i].addActionListener((ActionEvent e) -> {
                JButton boton = (JButton) e.getSource();
                boton.setText("X");
                boton.setEnabled(false);
                mensaje.setText("Casilla pulsada");
            });
            panelCentro.add(casillas[i]);
        }
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelAbajo = new JPanel();
        JButton nuevaPartida = new JButton("Nueva partida");
        // Lambda lo recomienda el IDE se hace asi
        nuevaPartida.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < 64; i++) {
                casillas[i].setText("");
                casillas[i].setEnabled(true);
            }
            mensaje.setText("Nueva partida");
        });
        panelAbajo.add(mensaje);
        panelAbajo.add(nuevaPartida);
        add(panelAbajo, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new NewJFrame().setVisible(true);
    }
}
