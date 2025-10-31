package Modelo;

import java.util.HashSet;

/**
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-10-31
 */
public class Bloque {
    
    private int x;
    private int y;
    private int ancho;
    private int altura;
    private String imagen;
    
    private int inicioX;
    private int inicioY;
    
    private HashSet<Bloque> paredes;
    private HashSet<Bloque> comida;
    private Bloque Pacman;
    
    //X = wall, O = skip, P = pac man, ' ' = food
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XX XX X XXXX",
        "O                 O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    public Bloque(int x, int y, int ancho, int altura, String imagen) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.altura = altura;
        this.imagen = imagen;
        this.inicioX = x;
        this.inicioY = y;
    }

}
