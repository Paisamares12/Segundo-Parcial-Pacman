/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.parcial.cliente.vista;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author paisa
 */
public final class Iconos {

    private Iconos() {
    }

    public static Image cargar(String nombre) {
        return Toolkit.getDefaultToolkit()
                .createImage(Iconos.class.getResource("/Imagenes/" + nombre));
    }
}
