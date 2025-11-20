/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel.premier.tp.deso.datos.dao;

import hotel.premier.tp.deso.dominio.Huesped;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface HuespedDAO {
    List<Huesped> buscarPor(Predicate<Huesped> filtro);
    List<Huesped> buscarTodos();
    Optional<Huesped> buscarPorDocumento(String tipoDoc, String numDoc);
    void guardar(Huesped huesped);
    void actualizar(Huesped huesped);
    void eliminar(String tipoDoc, String numDoc);
}
