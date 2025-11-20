/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel.premier.tp.deso.servicios;

import hotel.premier.tp.deso.datos.dao.ConserjeDAO;
import hotel.premier.tp.deso.datos.dao.EstadiaDAO;
import hotel.premier.tp.deso.datos.dao.HuespedDAO;
import hotel.premier.tp.deso.datos.implementacion.ConserjeDAOImpl;
import hotel.premier.tp.deso.datos.implementacion.EstadiaDAOImpl;
import hotel.premier.tp.deso.datos.implementacion.HuespedDAOImpl;

public class DAOFactory {
    public static HuespedDAO getHuespedDAO() {
        return new HuespedDAOImpl();
    }

    public static ConserjeDAO getConserjeDAO() {
        return new ConserjeDAOImpl();
    }
    
    public static EstadiaDAO getEstadiaDAO() {
        return new EstadiaDAOImpl();
    }
}
