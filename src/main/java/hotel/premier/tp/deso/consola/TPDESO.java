/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
//Cambio desde la compu de Bruno aaaabbbcc
package hotel.premier.tp.deso.consola;
import hotel.premier.tp.deso.dominio.*;
import hotel.premier.tp.deso.excepciones.EntidadNoEncontradaException;
import hotel.premier.tp.deso.excepciones.ValidacionException;
import hotel.premier.tp.deso.servicios.GestorConserjes;
import hotel.premier.tp.deso.servicios.GestorHuespedes;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TPDESO {
private static final Scanner scanner = new Scanner(System.in);
    private static final GestorConserjes gestorConserjes = GestorConserjes.getInstancia();
    private static final GestorHuespedes gestorHuespedes = GestorHuespedes.getInstancia();

    public static void main(String[] args) {
        if (!autenticarUsuario()) {
            return;
        }
        mostrarMenuPrincipal();
    }

    // --- CU01: Autenticar Usuario ---
    private static boolean autenticarUsuario() {
        System.out.println("--- BIENVENIDO AL SISTEMA DEL HOTEL PREMIER ---");
        for (int i = 0; i < 3; i++) {
            System.out.print("Usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();
            try {
                if (gestorConserjes.autenticar(usuario, contrasena)) {
                    System.out.println("Autenticación exitosa. ¡Bienvenido!");
                    return true;
                } else {
                    // Flujo Alternativo 3.A.1
                    System.out.println("Error: El usuario o la contraseña no son válidos.");
                }
            } catch (EntidadNoEncontradaException e) {
                // Flujo Alternativo 3.A.1
                System.out.println("Error: El usuario o la contraseña no son válidos.");
            }
        }
        System.out.println("Ha superado el número de intentos. El programa finalizará.");
        return false;
    }

    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestionar Huéspedes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                if (opcion == 1) {
                    buscarHuesped();
                }
            } catch (NumberFormatException e) {
                opcion = -1;
            }
        } while (opcion != 0);
        System.out.println("Gracias por utilizar el sistema.");
    }

    // --- CU02: Buscar Huésped ---
    private static void buscarHuesped() {
        System.out.println("\n--- BÚSQUEDA DE HUÉSPED (CU02) ---");
        System.out.print("Apellido (deje en blanco para omitir): ");
        String apellido = scanner.nextLine().toUpperCase();
        System.out.print("Nombres (deje en blanco para omitir): ");
        String nombre = scanner.nextLine().toUpperCase();
        System.out.print("Tipo de Documento (deje en blanco para omitir): ");
        String tipoDocumento = scanner.nextLine().toUpperCase();
        System.out.print("Número de Documento (deje en blanco para omitir): ");
        String numDoc = scanner.nextLine();
        
        System.out.println("\nPresione Enter para 'Buscar'");
        scanner.nextLine();

        List<Huesped> resultados = gestorHuespedes.buscarHuespedes(apellido, nombre, numDoc);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron huéspedes con los criterios de búsqueda.");
            System.out.print("¿Desea dar de alta un nuevo huésped? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                darDeAltaHuesped();
            }
            /*
            System.out.println("Pasando a 'Dar alta de Huésped' (CU09)...");
            darDeAltaHuesped();
            */
           
        } else {
            System.out.println("Huéspedes encontrados:");
            for (int i = 0; i < resultados.size(); i++) {
                System.out.printf("%d: %s\n", i + 1, resultados.get(i).toString());
            }
            
            System.out.print("Seleccione un huésped para gestionar y presione Enter, o solo presione Enter para dar de alta uno nuevo: ");
            String input = scanner.nextLine();
            
            try {
                if (input.isEmpty()) {
                    // Flujo Alternativo 5.A.1: El actor presiona SIGUIENTE (Enter) sin seleccionar.
                    System.out.println("No se ha seleccionado ningún huésped. Pasando a 'Dar alta de huésped' (CU09)...");
                    darDeAltaHuesped();
                } else {
                    int seleccion = Integer.parseInt(input);
                    if (seleccion > 0 && seleccion <= resultados.size()) {
                        // Flujo Principal 6: El sistema pasa a ejecutar CU10 Modificar Huésped
                        modificarHuesped(resultados.get(seleccion - 1));
                    } else {
                        System.out.println("Selección inválida. Volviendo al menú principal.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Volviendo al menú principal.");
            }
        }
    }

    // --- CU09: Dar de Alta Huésped ---
    private static void darDeAltaHuesped() {
        boolean cargarOtro;
        do {
            cargarOtro = false;
            System.out.println("\n--- ALTA DE NUEVO HUÉSPED (CU09) ---");
            Huesped nuevoHuesped = null;
            boolean correccionNecesaria = true;

            while (correccionNecesaria) {
                try {
                    nuevoHuesped = cargarDatosHuesped(null);
                    boolean decisionFinalizada = false;
                while (!decisionFinalizada) {
                    System.out.print("\nOpciones: Escriba 'SIGUIENTE' para confirmar o 'CANCELAR' para anular: ");
                    String accion = scanner.nextLine().toUpperCase();
                    
                    switch (accion) {
                        case "SIGUIENTE":
                            decisionFinalizada = true;
                            break;
                        case "CANCELAR":
                            // 2.C.1: El sistema presenta el mensaje de confirmación
                            System.out.print("¿Desea cancelar el alta del huésped? (SI/NO): ");
                            String confirmacion = scanner.nextLine().toUpperCase();
                            
                            if ("SI".equals(confirmacion)) {
                                // 2.C.2.1: El sistema continúa en el paso 6 (finaliza el CU)
                                System.out.println("Alta de huésped cancelada.");
                                return;
                            } else {
                                // 2.C.3.1: El sistema regresa al paso 1 mostrando los datos ya ingresados
                                System.out.println("Continuando con el alta...");
                                // Volvemos a cargar los datos manteniendo los ya ingresados
                                nuevoHuesped = cargarDatosHuesped(nuevoHuesped);
                            }
                            break;
                        default:
                            System.out.println("Opción no reconocida. Por favor, escriba 'SIGUIENTE' o 'CANCELAR'.");
                    }
                }
                    
                    // Verificación de existencia para Flujo Alternativo 2.B
                    Optional<Huesped> existente = gestorHuespedes.buscarPorDocumento(
                        nuevoHuesped.getTipoDocumento().toString(), nuevoHuesped.getNumeroDocumento()
                    );

                    if (existente.isPresent()) {
                        System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                        System.out.print("Escriba 'ACEPTAR' para guardar igualmente, o 'CORREGIR' para modificar los datos: ");
                        String decision = scanner.nextLine().toUpperCase();
                        if ("ACEPTAR".equals(decision)) {
                            gestorHuespedes.altaHuesped(nuevoHuesped, true);
                            System.out.println("¡Huésped " + nuevoHuesped.getApellido() + ", " + nuevoHuesped.getNombre() + " ha sido cargado satisfactoriamente!");
                            correccionNecesaria = false;
                        } else { // CORREGIR o cualquier otra cosa
                            continue; // Vuelve al inicio del while para cargar datos de nuevo
                        }
                    } else {
                        gestorHuespedes.altaHuesped(nuevoHuesped, false);
                        System.out.println("¡Huésped " + nuevoHuesped.getApellido() + ", " + nuevoHuesped.getNombre() + " ha sido cargado satisfactoriamente!");
                        correccionNecesaria = false;
                    }

                } catch (ValidacionException e) {
                    // Flujo Alternativo 2.A.1
                    System.out.println("\nError en el alta: \n" + e.getMessage());
                    System.out.println("Por favor, corrija los datos.");
                    // El bucle while asegura que se vuelva a pedir la carga
                } catch (DateTimeParseException | IllegalArgumentException e) {
                    System.out.println("Error en el formato de un dato: " + e.getMessage());
                }
            }

            // Flujo Principal 3 y 4
            System.out.print("¿Desea cargar otro huésped? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                cargarOtro = true; // Flujo Alternativo 5.A.1
            }

        } while (cargarOtro);
        // Flujo Principal 6: El CU termina
    }

    // --- CU10: Modificar Huésped ---
   private static void modificarHuesped(Huesped huesped) {
    System.out.println("\n--- MODIFICAR HUÉSPED (CU10) ---");
    System.out.printf("Gestionando a: %s\n", huesped.toString());
    
    Huesped huespedModificado = huesped; // Empezamos con el original

    while (true) {
        huespedModificado = cargarDatosHuesped(huespedModificado); // Carga o recarga datos

         // VERIFICACIÓN DE DOCUMENTO DUPLICADO - SOLO SI CAMBIÓ EL DOCUMENTO
        boolean documentoCambio = !huespedModificado.getTipoDocumento().equals(huesped.getTipoDocumento()) || 
                                 !huespedModificado.getNumeroDocumento().equals(huesped.getNumeroDocumento());
        
        if (documentoCambio) {
            Optional<Huesped> existente = gestorHuespedes.buscarPorDocumento(
                huespedModificado.getTipoDocumento().toString(), 
                huespedModificado.getNumeroDocumento()
            );

            // Verificar si existe OTRO huésped con ese documento (no el mismo)
            if (existente.isPresent() && !existente.get().getNumeroDocumento().equals(huesped.getNumeroDocumento())) {
                System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                System.out.print("Escriba 'ACEPTAR' para guardar igualmente, o 'CORREGIR' para modificar los datos: ");
                String decision = scanner.nextLine().toUpperCase();
                if (!"ACEPTAR".equals(decision)) {
                    continue; // Vuelve a cargar datos para corregir
                }
                // Si elige "ACEPTAR", continúa con el flujo normal
            }
        }

                System.out.print("\nAcciones: Escriba 'SIGUIENTE' para guardar, 'BORRAR' o 'CANCELAR' y presione Enter: ");
        String accion = scanner.nextLine().toUpperCase();
        
        switch (accion) {
            case "SIGUIENTE":
                try {
                    gestorHuespedes.modificarHuesped(huespedModificado);
                    System.out.println("La operación ha culminado con éxito."); // Flujo Principal 3
                    return; // Flujo Principal 4: El CU termina
                } catch (ValidacionException e) {
                    // Flujo Alternativo 2.A.1
                    System.out.println("\nError al guardar: \n" + e.getMessage());
                    System.out.println("Por favor, corrija los datos e intente guardar de nuevo.");
                    // El bucle continuará, pidiendo los datos de nuevo
                } catch (EntidadNoEncontradaException e) {
                    System.out.println("Error fatal: " + e.getMessage());
                    return;
                }
                break;
            case "BORRAR":
                // Flujo Alternativo 2.D.1: El sistema ejecuta CU11
                darDeBajaHuesped(huespedModificado);
                return; // Flujo Alternativo 2.D.2: El CU termina
            case "CANCELAR":
                // Flujo Alternativo 2.C
                System.out.print("¿Desea cancelar la modificación del huésped? (S/N): ");
                if (scanner.nextLine().equalsIgnoreCase("S")) {
                    System.out.println("Modificación cancelada.");
                    return; // El CU termina
                }
                break; // Si dice 'N', vuelve a mostrar las acciones
            default:
                System.out.println("Acción no reconocida. Por favor, intente de nuevo.");
        }
    }
}

    // --- CU11: Dar de Baja Huésped ---
    private static void darDeBajaHuesped(Huesped huesped) {
        System.out.println("\n--- ELIMINAR HUÉSPED (CU11) ---");

        // Flujo Alternativo 2.A: Constatar que el huésped jamás se ha alojado
        if (gestorHuespedes.huespedSeHaAlojado(huesped.getTipoDocumento().toString(), huesped.getNumeroDocumento())) {
            System.out.println("El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad.");
            System.out.print("PRESIONE CUALQUIER TECLA PARA CONTINUAR...");
            scanner.nextLine();
            return; // 2.A.2 El CU termina
        }

        // Flujo Principal 2
        System.out.printf("Los datos del huésped %s, %s (%s %s) serán eliminados del sistema\n", 
            huesped.getApellido(), huesped.getNombre(), huesped.getTipoDocumento(), huesped.getNumeroDocumento());
        System.out.print("Presione 'ELIMINAR' para confirmar o 'CANCELAR' para anular: ");

        String confirmacion = scanner.nextLine().toUpperCase();

        if ("ELIMINAR".equals(confirmacion)) {
            try {
                // Flujo Principal 3
                gestorHuespedes.bajaHuesped(huesped.getTipoDocumento().toString(), huesped.getNumeroDocumento());
                System.out.printf("Los datos del huésped %s, %s han sido eliminados del sistema.\n",
                    huesped.getApellido(), huesped.getNombre());
                System.out.print("PRESIONE CUALQUIER TECLA PARA CONTINUAR...");
                scanner.nextLine(); // Flujo Principal 4
            } catch (EntidadNoEncontradaException e) {
                System.out.println("Error al eliminar: " + e.getMessage());
            }
        } else {
            // Flujo Alternativo 3.A.1
            System.out.println("Operación de eliminación cancelada.");
        }
        // Flujo Principal 5: El CU termina
    }
    
    // --- Lógica Auxiliar de Carga de Datos ---
   private static Huesped cargarDatosHuesped(Huesped huespedExistente) {
    System.out.println("\n--- Ingrese los datos. Presione 'Enter' para conservar el valor actual sin modificar. ---");
    boolean esModificacion = huespedExistente != null;
    Huesped.Builder builder;

    // Permitir modificar tipo y número de documento incluso en modificación
    if (!esModificacion) {
        System.out.print("Tipo de Documento (DNI, LE, LC, PASAPORTE, OTRO): ");
        TipoDocumento tipoDoc = TipoDocumento.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Número de Documento: ");
        String numDoc = scanner.nextLine();
        builder = new Huesped.Builder(tipoDoc, numDoc);
    } else {
        // En modificación, permitir cambiar tipo y número de documento
        System.out.printf("Tipo de Documento [%s]: ", huespedExistente.getTipoDocumento());
        String tipoDocInput = scanner.nextLine().toUpperCase();
        TipoDocumento tipoDoc = tipoDocInput.isEmpty() ? huespedExistente.getTipoDocumento() : TipoDocumento.valueOf(tipoDocInput);
        
        System.out.printf("Número de Documento [%s]: ", huespedExistente.getNumeroDocumento());
        String numDocInput = scanner.nextLine();
        String numDoc = numDocInput.isEmpty() ? huespedExistente.getNumeroDocumento() : numDocInput;
        
        builder = new Huesped.Builder(tipoDoc, numDoc);
        
        // Cargar el resto de los datos existentes para no perderlos
        builder.setApellido(huespedExistente.getApellido())
               .setNombre(huespedExistente.getNombre())
               .setFechaNacimiento(huespedExistente.getFechaNacimiento())
               .setPosicionIVA(huespedExistente.getPosicionIVA())
               .setEdad(huespedExistente.getEdad())
               .setCuit(huespedExistente.getCuit())
               .setTelefono(huespedExistente.getTelefono())
               .setEmail(huespedExistente.getEmail())
               .setNacionalidad(huespedExistente.getNacionalidad())
               .setOcupacion(huespedExistente.getOcupacion())
               .setDireccion(huespedExistente.getDireccion());
    }

    // Ahora permitir modificar todos los campos, incluyendo los que ya estaban cargados
    System.out.printf("Apellido [%s]: ", esModificacion ? huespedExistente.getApellido() : "");
        String apellidoInput = scanner.nextLine().toUpperCase();
        if (!apellidoInput.isEmpty()) builder.setApellido(apellidoInput);
        
        System.out.printf("Nombres [%s]: ", esModificacion ? huespedExistente.getNombre() : "");
        String nombreInput = scanner.nextLine().toUpperCase();
        if (!nombreInput.isEmpty()) builder.setNombre(nombreInput);

        System.out.printf("Fecha de Nacimiento (AAAA-MM-DD) [%s]: ", esModificacion && huespedExistente.getFechaNacimiento() != null ? huespedExistente.getFechaNacimiento() : "");
        String fechaInput = scanner.nextLine();
        if (!fechaInput.isEmpty()) builder.setFechaNacimiento(LocalDate.parse(fechaInput));
        
        System.out.printf("Posición frente al IVA [%s]: ", esModificacion ? huespedExistente.getPosicionIVA() : "");
        String ivaInput = scanner.nextLine().toUpperCase();
        if (!ivaInput.isEmpty()) builder.setPosicionIVA(PosicionIVA.valueOf(ivaInput));
        
        System.out.printf("Edad [%s]: ", esModificacion ? huespedExistente.getEdad() : "");
        String edadInput = scanner.nextLine();
        if (!edadInput.isEmpty()) {  builder.setEdad(Integer.parseInt(edadInput));
        } else if (esModificacion) {  builder.setEdad(huespedExistente.getEdad());
        }
        
        System.out.printf("CUIT [%s]: ", esModificacion && huespedExistente.getCuit() != null ? huespedExistente.getCuit() : "");
        String cuitInput = scanner.nextLine();
        if (!cuitInput.isEmpty()) builder.setCuit(cuitInput);
        
        System.out.printf("Teléfono [%s]: ", esModificacion && huespedExistente.getTelefono() != null ? huespedExistente.getTelefono() : "");
        String telefonoInput = scanner.nextLine();
        if (!telefonoInput.isEmpty()) builder.setTelefono(telefonoInput);
        
        System.out.printf("Email [%s]: ", esModificacion && huespedExistente.getEmail() != null ? huespedExistente.getEmail() : "");
        String emailInput = scanner.nextLine();
        if (!emailInput.isEmpty()) builder.setEmail(emailInput);
        
        System.out.printf("Nacionalidad [%s]: ", esModificacion && huespedExistente.getNacionalidad() != null ? huespedExistente.getNacionalidad() : "");
        String nacInput = scanner.nextLine().toUpperCase();
        if (!nacInput.isEmpty()) builder.setNacionalidad(nacInput);
        
        System.out.printf("Ocupación [%s]: ", esModificacion && huespedExistente.getOcupacion() != null ? huespedExistente.getOcupacion() : "");
        String ocupacionInput = scanner.nextLine().toUpperCase();
        if (!ocupacionInput.isEmpty()) builder.setOcupacion(ocupacionInput);
        
        System.out.println("--- Dirección ---");
        Direccion dirExistente = esModificacion ? huespedExistente.getDireccion() : null;
        Direccion.Builder dirBuilder = new Direccion.Builder();

        System.out.printf("Calle [%s]: ", dirExistente != null ? dirExistente.getCalle() : "");
        String calleInput = scanner.nextLine().toUpperCase();
        dirBuilder.calle(calleInput.isEmpty() && dirExistente != null ? dirExistente.getCalle() : calleInput);
        
        System.out.printf("Número [%s]: ", dirExistente != null ? dirExistente.getNumero() : 0);
        String numeroInput = scanner.nextLine();

        int numero;
        if (numeroInput.isEmpty()) {
            numero = dirExistente != null ? dirExistente.getNumero() : 0; // Valor por defecto
        } else {
            numero = Integer.parseInt(numeroInput);
        }
        dirBuilder.numero(numero);

        System.out.printf("Piso [%s]: ", dirExistente != null ? dirExistente.getPiso() : 0);
        String pisoInput = scanner.nextLine();

        int piso;
        if (pisoInput.isEmpty()) {
            piso = dirExistente != null ? dirExistente.getPiso() : 0; // Valor por defecto
        } else {
            piso = Integer.parseInt(pisoInput);
        }
        dirBuilder.piso(piso);

        System.out.printf("Departamento [%s]: ", dirExistente != null && dirExistente.getDepartamento() != null ? dirExistente.getDepartamento() : "");
        String deptoInput = scanner.nextLine().toUpperCase();
        dirBuilder.departamento(deptoInput.isEmpty() && dirExistente != null ? dirExistente.getDepartamento() : deptoInput);

        System.out.printf("Código Postal [%s]: ", dirExistente != null && dirExistente.getCodPostal() != null ? dirExistente.getCodPostal() : "");
        String cpInput = scanner.nextLine().toUpperCase();
        dirBuilder.codPostal(cpInput.isEmpty() && dirExistente != null ? dirExistente.getCodPostal() : cpInput);

        System.out.printf("Localidad [%s]: ", dirExistente != null ? dirExistente.getLocalidad() : "");
        String localidadInput = scanner.nextLine().toUpperCase();
        dirBuilder.localidad(localidadInput.isEmpty() && dirExistente != null ? dirExistente.getLocalidad() : localidadInput);

        System.out.printf("Provincia [%s]: ", dirExistente != null && dirExistente.getProvincia() != null ? dirExistente.getProvincia() : "");
        String provinciaInput = scanner.nextLine().toUpperCase();
        dirBuilder.provincia(provinciaInput.isEmpty() && dirExistente != null ? dirExistente.getProvincia() : provinciaInput);

        System.out.printf("País [%s]: ", dirExistente != null && dirExistente.getPais() != null ? dirExistente.getPais() : "");
        String paisInput = scanner.nextLine().toUpperCase();
        dirBuilder.pais(paisInput.isEmpty() && dirExistente != null ? dirExistente.getPais() : paisInput);
        
        builder.setDireccion(dirBuilder.build());
        return builder.build();
    }

}
