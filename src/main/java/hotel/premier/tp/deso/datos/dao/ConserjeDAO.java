/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel.premier.tp.deso.datos.dao;

import hotel.premier.tp.deso.dominio.Conserje;
import java.util.Optional;

/**
 *
 * @author I-MAG
 */
public interface ConserjeDAO {
    Optional<Conserje> buscarPorNombre(String nombre);
}
