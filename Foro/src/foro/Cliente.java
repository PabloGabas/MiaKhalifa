/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package foro;

/**
 *
 * @author dng
 */

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Cliente extends JFrame implements ActionListener {
    static DataOutputStream SalidaSocket;
    static JTextArea entrada;
    static JScrollPane scrollPane;
    static String usuario;
    static String[] usuarios = {"Marco"};
    static String[] contraseñas = {"1234"};
    static JTextArea areaEnviar;
    static JTextArea areaTexto;

    public Cliente() {
        setTitle("Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        entrada = new JTextArea();
        entrada.setEditable(false);
        entrada.setBackground(new Color(240, 240, 240));
        scrollPane = new JScrollPane(entrada);
        add(scrollPane, BorderLayout.CENTER);

        mostrarVentanaLogin();
    }

    private void mostrarVentanaLogin() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField tfUsuario = new JTextField(10);
        JPasswordField pfContraseña = new JPasswordField(10);
        panel.add(new JLabel("Usuario:"));
        panel.add(tfUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(pfContraseña);

        int opcion = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Login", "Registrarse"}, "Login");
        if (opcion == JOptionPane.OK_OPTION) {
            usuario = tfUsuario.getText();
            String contraseña = new String(pfContraseña.getPassword());
            if (verificarCredenciales(usuario, contraseña)) {
                entrada.append("Bienvenido, " + usuario + "!\n");
                mostrarVentanaEnviarArchivo();
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } else if (opcion == JOptionPane.CANCEL_OPTION) {
            mostrarVentanaRegistro();
        } else {
            System.exit(0);
        }
    }

    // arreglar esto porque pipipi
    private void mostrarVentanaRegistro() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField tfUsuario = new JTextField(10);
        JPasswordField pfContraseña = new JPasswordField(10);
        panel.add(new JLabel("Usuario:"));
        panel.add(tfUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(pfContraseña);

        int opcion = JOptionPane.showConfirmDialog(null, panel, "Registrarse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoUsuario = tfUsuario.getText();
            String nuevaContraseña = new String(pfContraseña.getPassword());
            if (nuevoUsuario.isEmpty() || nuevaContraseña.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre de usuario y una contraseña", "Error", JOptionPane.ERROR_MESSAGE);
                mostrarVentanaRegistro();
            } else {
                registrarCredenciales(nuevoUsuario, nuevaContraseña);
                usuario = nuevoUsuario;
                entrada.append("Bienvenido, " + usuario + "!\n");
                mostrarVentanaEnviarArchivo();
            }
        }
    }

    private boolean verificarCredenciales(String usuario, String contraseña) {
        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].equalsIgnoreCase(usuario) && contraseñas[i].equals(contraseña)) {
                return true;
            }
        }
        return false;
    }

    private void registrarCredenciales(String usuario, String contraseña) {
        String[] tempUsuarios = new String[usuarios.length + 1];
        String[] tempContraseñas = new String[contraseñas.length + 1];
        for (int i = 0; i < usuarios.length; i++) {
            tempUsuarios[i] = usuarios[i];
            tempContraseñas[i] = contraseñas[i];
        }
        tempUsuarios[tempUsuarios.length - 1] = usuario;
        tempContraseñas[tempContraseñas.length - 1] = contraseña;
        usuarios = tempUsuarios;
        contraseñas = tempContraseñas;
    }

    private void mostrarVentanaEnviarArchivo() {
        JFrame ventanaEnviarArchivo = new JFrame("Enviar Archivo");
        ventanaEnviarArchivo.setSize(400, 300);
        ventanaEnviarArchivo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaEnviarArchivo.setLayout(new BorderLayout());

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        ventanaEnviarArchivo.add(scrollPane, BorderLayout.CENTER);

        areaEnviar = new JTextArea();
        JScrollPane scrollPaneEnviar = new JScrollPane(areaEnviar);
        ventanaEnviarArchivo.add(scrollPaneEnviar, BorderLayout.SOUTH);

        JButton btnCargarArchivo = new JButton("Cargar Archivo");
        btnCargarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarArchivo();
            }
        });
        btnCargarArchivo.setBackground(new Color(255, 102, 102));
        btnCargarArchivo.setForeground(Color.WHITE);
        btnCargarArchivo.setFocusPainted(false);
        btnCargarArchivo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ventanaEnviarArchivo.add(btnCargarArchivo, BorderLayout.WEST);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje(areaEnviar.getText());
                areaEnviar.setText("");
            }
        });
        btnEnviar.setBackground(new Color(102, 204, 255));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ventanaEnviarArchivo.add(btnEnviar, BorderLayout.EAST);

        ventanaEnviarArchivo.setVisible(true);
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setBackground(new Color(240, 240, 240));
        fileChooser.setDialogTitle("Seleccionar Archivo");
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            enviarArchivo(file);
        }
    }

    private void enviarArchivo(File file) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        long fileSize = file.length();
        try (Socket sfd = new Socket("localhost", 8000);
             DataOutputStream SalidaSocket = new DataOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
             FileInputStream fileInputStream = new FileInputStream(file)) {

            SalidaSocket.writeUTF(file.getName());
            SalidaSocket.flush();
            SalidaSocket.writeLong(fileSize);
            SalidaSocket.flush();
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                SalidaSocket.write(buffer, 0, bytesRead);
            }
            SalidaSocket.flush();
            entrada.append("Archivo enviado: " + file.getName() + "\n");
        } catch (IOException e) {
            System.out.println("Error al enviar el archivo: " + e.getMessage());
        }
    }

    private void enviarMensaje(String mensaje) {
        areaTexto.append(usuario + ": " + mensaje + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Cliente();
            }
        });
        try (Socket sfd = new Socket("localhost", 8000)) {
            SalidaSocket = new DataOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
        } catch (UnknownHostException uhe) {
            System.out.println("No se puede acceder al servidor.");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("Comunicación rechazada.");
            System.exit(1);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Cargar Archivo")) {
            cargarArchivo();
        }
    }
}
