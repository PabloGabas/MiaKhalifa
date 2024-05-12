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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Flujo extends Thread {
    Socket nsfd;
    DataInputStream FlujoLectura;

    public Flujo(Socket sfd) {
        nsfd = sfd;
        try {
            FlujoLectura = new DataInputStream(new BufferedInputStream(sfd.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("IOException(Flujo): " + ioe);
        }
    }

    public void run() {
        try {
            // Recibir nombre del archivo
            String nombreArchivo = FlujoLectura.readUTF();
            System.out.println("Recibiendo archivo: " + nombreArchivo);

            // Recibir tamaño del archivo
            long tamanoArchivo = FlujoLectura.readLong();
            System.out.println("Tamaño del archivo: " + tamanoArchivo);

            // Crear un buffer para almacenar los datos del archivo
            byte[] buffer = new byte[1024]; // Tamaño del buffer, puedes ajustarlo según tus necesidades
            int bytesRead;
            long bytesRecibidos = 0;

            // Crear un FileOutputStream para escribir el archivo recibido
            FileOutputStream fileOutputStream = new FileOutputStream(Servidor.CARPETA_DESTINO + nombreArchivo);

            // Leer los bytes del archivo y escribirlos en el FileOutputStream
            while ((bytesRead = FlujoLectura.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                bytesRecibidos += bytesRead;

                // Opcional: Mostrar progreso de recepción del archivo
                System.out.println("Recibidos: " + bytesRecibidos + "/" + tamanoArchivo);

                // Si se han recibido todos los bytes, salir del bucle
                if (bytesRecibidos == tamanoArchivo) {
                    break;
                }
            }

            // Cerrar el FileOutputStream
            fileOutputStream.close();
            System.out.println("Archivo recibido correctamente");
        } catch (IOException ioe) {
            System.out.println("Error al recibir el archivo: " + ioe.getMessage());
        }
    }
}
