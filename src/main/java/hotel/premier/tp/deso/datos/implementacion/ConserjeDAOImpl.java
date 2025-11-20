/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel.premier.tp.deso.datos.implementacion;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hotel.premier.tp.deso.datos.dao.ConserjeDAO;
import hotel.premier.tp.deso.datos.dto.ConserjeDTO;
import hotel.premier.tp.deso.dominio.Conserje;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConserjeDAOImpl implements ConserjeDAO {
    private final String RUTA_ARCHIVO = "datos/conserjes.json";
    private final Gson gson = new Gson();

    @Override
    public Optional<Conserje> buscarPorNombre(String nombre) {
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Type tipoLista = new TypeToken<ArrayList<ConserjeDTO>>() {}.getType();
            List<ConserjeDTO> conserjesDTO = gson.fromJson(reader, tipoLista);
            if (conserjesDTO == null) return Optional.empty();

            return conserjesDTO.stream()
                    .filter(c -> c.nombre.equalsIgnoreCase(nombre))
                    .map(dto -> new Conserje(dto.nombre, dto.contrasena))
                    .findFirst();

        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
