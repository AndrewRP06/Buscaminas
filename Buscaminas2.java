/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package a1.buscaminas;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author PC GAMING
 */
public class Buscaminas2 extends javax.swing.JFrame {
    int filas = 10;
    int columnas = 10;
    int totalCasillas;
    int numeroMinas;
    int marcasColocadas;
 
    JButton[] casillas;
    boolean[] minas;
    boolean[] marcadas;
    boolean[] abiertas;
 
    JPanel panelCentro = new JPanel();
    JComboBox<String> comboDificultad = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"});
    JLabel mensaje = new JLabel("Pulsa una casilla");
    JLabel contadorMinas = new JLabel();
    Random aleatorio = new Random();
 
    public Buscaminas2() {
        setTitle("Buscaminas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
 
        crearMenus();
 
        // parte de arriba: dificultad y contador
        JPanel panelArriba = new JPanel();
        panelArriba.add(new JLabel("Dificultad:"));
        panelArriba.add(comboDificultad);
        panelArriba.add(contadorMinas);
        add(panelArriba, BorderLayout.NORTH);
 
        comboDificultad.addActionListener((ActionEvent e) -> {
            iniciarNuevaPartida();
        });
 
        add(panelCentro, BorderLayout.CENTER);
 
        // parte de abajo: mensaje y boton
        JPanel panelAbajo = new JPanel();
        JButton nuevaPartida = new JButton("Nueva partida");
        nuevaPartida.addActionListener((ActionEvent e) -> {
            iniciarNuevaPartida();
        });
        panelAbajo.add(mensaje);
        panelAbajo.add(nuevaPartida);
        add(panelAbajo, BorderLayout.SOUTH);
 
        construirTablero();
        pack();
        setLocationRelativeTo(null);
    }
 
    private void crearMenus() {
        JMenuBar barra = new JMenuBar();
 
       
        JMenu menuPartida = new JMenu("Partida");
 
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.addActionListener((ActionEvent e) -> {
           
            new AcercaDe().setVisible(true);
        });
 
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
 
        menuPartida.add(itemAcerca);
        menuPartida.addSeparator();
        menuPartida.add(itemSalir);
 
        JMenu menuTamano = new JMenu("Tamaño");
        JRadioButtonMenuItem tam8 = new JRadioButtonMenuItem("8 x 8");
        JRadioButtonMenuItem tam10 = new JRadioButtonMenuItem("10 x 10", true);
        JRadioButtonMenuItem tam15 = new JRadioButtonMenuItem("15 x 15");
 
        tam8.addActionListener((ActionEvent e) -> {
            cambiarTamano(8);
        });
        tam10.addActionListener((ActionEvent e) -> {
            cambiarTamano(10);
        });
        tam15.addActionListener((ActionEvent e) -> {
            cambiarTamano(15);
        });
 
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(tam8);
        grupo.add(tam10);
        grupo.add(tam15);
 
        menuTamano.add(tam8);
        menuTamano.add(tam10);
        menuTamano.add(tam15);
 
        barra.add(menuPartida);
        barra.add(menuTamano);
        setJMenuBar(barra);
    }
 
    private void cambiarTamano(int tamano) {
        filas = tamano;
        columnas = tamano;
        construirTablero();
        pack();
        setLocationRelativeTo(null);
    }
 
    private void construirTablero() {
        totalCasillas = filas * columnas;
        casillas = new JButton[totalCasillas];
        minas = new boolean[totalCasillas];
        marcadas = new boolean[totalCasillas];
        abiertas = new boolean[totalCasillas];
 
        panelCentro.removeAll();
        panelCentro.setLayout(new GridLayout(filas, columnas));
 
        for (int i = 0; i < totalCasillas; i++) {
            casillas[i] = new JButton();
            casillas[i].setPreferredSize(new Dimension(45, 45));
            final int posicion = i;
 
            // clic izquierdo
            casillas[i].addActionListener((ActionEvent e) -> {
                pulsarCasilla(posicion);
            });
 
            // clic derecho, alabada sea la IA por optimizar esto y que no parezca que esta pegado con cinta adhesiva el codigo
            casillas[i].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                    marcarCasilla(posicion);
                    }
                }
            });
            panelCentro.add(casillas[i]);
        }
 
        panelCentro.revalidate();
        panelCentro.repaint();
        iniciarNuevaPartida();
    }
 
    private void calcularNumeroMinas() {
        int dificultad = comboDificultad.getSelectedIndex();
        numeroMinas = switch (dificultad) {
            case 0 -> totalCasillas * 10 / 100;
            case 1 -> totalCasillas * 15 / 100;
            default -> totalCasillas * 20 / 100;
        };
    }
 
    private void generarMinas() {
        Arrays.fill(minas, false);
        int colocadas = 0;
        while (colocadas < numeroMinas) {
            int posicion = aleatorio.nextInt(totalCasillas);
            if (!minas[posicion]) {
                minas[posicion] = true;
                colocadas++;
            }
        }
    }
 
    private void pulsarCasilla(int posicion) {
        if (marcadas[posicion]) {
            mensaje.setText("Quita la marca antes de abrir esta casilla");
            return;
        }
 
        if (minas[posicion]) {
            casillas[posicion].setText("M");
            pintar(posicion);
            JOptionPane.showMessageDialog(this,
                    "Has pisado una mina. Se inicia una nueva partida. -1000 de aura",
                    "Nueva partida",
                    JOptionPane.INFORMATION_MESSAGE);
            iniciarNuevaPartida();
        } else {
            abrirCasilla(posicion);
            mensaje.setText("Casilla segura: " + contarMinas(posicion) + " minas cerca");
        }
    }
 
    private void abrirCasilla(int posicion) {
        if (abiertas[posicion] || marcadas[posicion] || minas[posicion]) {
            return;
        }
 
        abiertas[posicion] = true;
        pintar(posicion);
         int cerca = contarMinas(posicion);
        if (cerca > 0) {
            casillas[posicion].setText(String.valueOf(cerca));
            return;
        }
         int fila = posicion / columnas;
        int columna = posicion % columnas;
        for (int f = fila - 1; f <= fila + 1; f++) {
            for (int c = columna - 1; c <= columna + 1; c++) {
                if (estaDentro(f, c)) {
                    abrirCasilla(f * columnas + c);
                }
            }
        }
    }
 
private void pintar(int posicion) {
        casillas[posicion].setEnabled(false);
        casillas[posicion].setBackground(Color.WHITE);
    }
 
private boolean estaDentro(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }
private int contarMinas(int posicion) {
        int fila = posicion / columnas;
        int columna = posicion % columnas;
        int total = 0;
 
        for (int f = fila - 1; f <= fila + 1; f++) {
            for (int c = columna - 1; c <= columna + 1; c++) {
                if (estaDentro(f, c) && minas[f * columnas + c]) {
                    total++;
                }
            }
        }
        return total;
    }
 
private void marcarCasilla(int posicion) {
        if (!casillas[posicion].isEnabled()) {
            return;
        }
 
        if (marcadas[posicion]) {
            marcadas[posicion] = false;
            casillas[posicion].setText("");
            marcasColocadas--;
            mensaje.setText("Marca quitada");
        } else if (marcasColocadas < numeroMinas) {
            marcadas[posicion] = true;
            casillas[posicion].setText("B");
            marcasColocadas++;
            mensaje.setText("Casilla marcada");
        } else {
            mensaje.setText("Solo puedes marcar " + numeroMinas + " casillas");
            return;
        }
 
        actualizarContador();
    }
 
private void actualizarContador() {
        contadorMinas.setText("Minas por marcar: " + (numeroMinas - marcasColocadas));
    }
 
private void iniciarNuevaPartida() {
        for (int i = 0; i < totalCasillas; i++) {
            casillas[i].setText("");
            casillas[i].setEnabled(true);
            casillas[i].setBackground(null);
            marcadas[i] = false;
            abiertas[i] = false;
        }
        marcasColocadas = 0;
        calcularNumeroMinas();
        generarMinas();
        actualizarContador();
        mensaje.setText("Nueva partida");
    }
     
private class AcercaDe extends JFrame {
 
        AcercaDe() {
            setTitle("Acerca de");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
 
            JLabel texto = new JLabel("<html>"
                    + "<b>Buscaminas</b><br><br>"
                    + "Clic izquierdo: abre la casilla.<br>"
                    + "Clic derecho: marca o desmarca una casilla.<br>"
                    + "El número indica las minas en las 8 casillas vecinas."
                    + "</html>");
            texto.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            add(texto, BorderLayout.CENTER);
 
            JButton cerrar = new JButton("Cerrar");
            cerrar.addActionListener((ActionEvent e) -> {
                dispose();
            });
            JPanel panelAbajo = new JPanel();
            panelAbajo.add(cerrar);
            add(panelAbajo, BorderLayout.SOUTH);
 
            pack();
            setLocationRelativeTo(Buscaminas2.this);
        }
    }
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
