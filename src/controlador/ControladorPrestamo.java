/*
 Archivo: ControladorPrestamo.java
 Proyecto III - Biblioteca univalle
 03 de junio de 2023

 Autores:
  @author Manuel Felipe Cardoso Forero (2027288)
  @author Juan David Rodriguez Rubio (2025435) 
 
 */

package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;


import modelo.*;
import vista.*;

public class ControladorPrestamo {
    private String apartadoPrestamo;
    private static String prestamoEncabezado[] = {"Id prestamo", "Id cliente", "Cliente","Recursos","Estado"};
    
    class AddListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento) {
            if(evento.getActionCommand().equals("Pre")){
                apartadoPrestamo = "PrestamoForm";
                //revisarRecursoCampos(null);
            }
        }
    }

    public static void pintar(VentanaMain ventanaMain, Integer codSerialPrestamo){
        ventanaMain.getFildPresCod().setText(String.valueOf(codSerialPrestamo));

    }

    public static void limpiar(VentanaMain ventanaMain, Integer codSerialPrestamo){
        ventanaMain.getFildPresCod().setText(String.valueOf(codSerialPrestamo));
        ventanaMain.getFildPresIdUs().setText("");
        ventanaMain.getFildPresNomUs().setText("");
        ventanaMain.getFildPresCantidad().setText("");
        ventanaMain.getDropEstado().setSelectedItem("Seleccionar");
    }

    public static String[] getTitlePrestamo(){
        return prestamoEncabezado;
    }

    public static boolean revisarPrestamoCampos(VentanaMain ventanaMain){
        String PrestamoIdUsuario = ventanaMain.getFildPresIdUs().getText();
        try {
            Integer.valueOf(PrestamoIdUsuario);
        } catch (NumberFormatException exception){
            JOptionPane.showMessageDialog(null, "Porfavor Ingrese un ID valido de Usuario, sin puntos ni comas" + "\n Ejemplo: 1004250131" , "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String nombreUsuarioP = ventanaMain.getFildPresNomUs().getText();
        if(nombreUsuarioP.isEmpty() || nombreUsuarioP.isBlank()){
            JOptionPane.showMessageDialog(null, "El Cliente que ingreso no corresponde a ningun Usuario dentro del sistema\n Verifique el Id que ingreso o registe el Usuario en | Usuarios | " , "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String numeroPrestamo = ventanaMain.getFildPresCantidad().getText();
        try {
            Integer cantidad = Integer.valueOf(numeroPrestamo);
            if(cantidad == 0)
                throw new NumberFormatException();
        } catch (NumberFormatException exception){
            JOptionPane.showMessageDialog(null,"La cantidad de Recursos debe ser un número diferente de cero. Debe agregar al menos un recurso a la lista de recursos de la venta.\nAgregue una lista de recursos, haciendo clic sobre el botón", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String estado = ventanaMain.getDropEstado().getSelectedItem().toString();
        if(estado.equals("Seleccionar")){
            JOptionPane.showMessageDialog(null, "Agregue un estado para el prestamo, \n |Recuerde| \n " , "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
        
    }

    public static Prestamo crearPrestamo(VentanaMain ventanaMain, HashMap<Integer, String[]> listaRecursos){
        String stIdPrestamo = ventanaMain.getFildPresCod().getText();
        Integer idPrestamo = Integer.valueOf(stIdPrestamo);
        String stIdUserPres = ventanaMain.getFildPresIdUs().getText();
        Integer idUserPrestamo = Integer.valueOf(stIdUserPres);
        String userNombrePr = ventanaMain.getFildPresNomUs().getText();
        String stCantidad = ventanaMain.getFildPresCantidad().getText();
        Integer cantidad = Integer.valueOf(stCantidad);
        String estado = ventanaMain.getDropEstado().getSelectedItem().toString();

        Prestamo prestamo = new Prestamo(idPrestamo, idUserPrestamo, userNombrePr, listaRecursos, cantidad, estado);
        return prestamo;
    }

    public static void crearTabla(DefaultTableModel modelTlabe, VentanaMain ventanaMain){
        String tablaidPrestamo = modelTlabe.getValueAt(ventanaMain.getDatosEnTabla().getSelectedRow(),0).toString();
        String tablausuarioIDPrestamo = modelTlabe.getValueAt(ventanaMain.getDatosEnTabla().getSelectedRow(),1).toString();
        String tablausuarioPrestamo = modelTlabe.getValueAt(ventanaMain.getDatosEnTabla().getSelectedRow(),2).toString();
        String tablacantidad = modelTlabe.getValueAt(ventanaMain.getDatosEnTabla().getSelectedRow(),3).toString();
        String tablaestado = modelTlabe.getValueAt(ventanaMain.getDatosEnTabla().getSelectedRow(),4).toString();

        ventanaMain.getFildPresCod().setText(tablaidPrestamo);
        ventanaMain.getFildPresIdUs().setText(tablausuarioIDPrestamo);
        ventanaMain.getFildPresNomUs().setText(tablausuarioPrestamo);
        ventanaMain.getFildPresCantidad().setText(tablacantidad);
        ventanaMain.getDropEstado().setSelectedItem(tablaestado);
    }

        
}
 