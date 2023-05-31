/*
 Archivo: ControladorVentana.java
 Proyecto III - Biblioteca univalle
 28 de mayo de 2022

 Autores:
  @author Manuel Felipe Cardoso Forero (2027288)
  @author Juan David Rodriguez Rubio (2025435) 

  Esta es la clase que se encargara de controlar todo el programa, los botones de la vista, desde esta clase
  se conectan todas las capas de la logica a la vista.

  -Relacion de vista Ventana Main.
  -Relacion modelo (logica) Biblioteca(conoce y administra elementos del sistema)
 
 */

package controlador;

import DAO.*;
import modelo.*;
import vista.*;
import java.awt.event.*;
import java.math.*;

import javax.management.ValueExp;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.xml.transform.Source;

import java.util.*;
import java.awt.*;

public class ControladorVentana {
    private String apartadoTipo;
    private String apartadoFormulario;
    private JButton agregar, eliminar, editar, limpiar;
    private VentanaMain ventanaMain;
    private JTable datosTable = new JTable();
    private JScrollPane paneltabla = new JScrollPane();
    String  rutaArchivoBin = "./src/Archivos/biblioteca.bin";
    private Biblioteca biblioteca;
    private Integer serialGenero;
    

    public ControladorVentana(VentanaMain ventanaMain){
        this.ventanaMain = ventanaMain;

        this.biblioteca = Managerecords.leerArchivoBin(rutaArchivoBin);
        if(biblioteca == null)
        biblioteca = new Biblioteca("Biblioteca Universidad del Valle");

        serialGenero = biblioteca.getCodSerialgrlt();

        ventanaMain.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent){
                biblioteca.setCodSerialgrlt(serialGenero);
                Managerecords.guardarArchivoBin(biblioteca, rutaArchivoBin);
                System.exit(0);
            }
        });

        ventanaMain.setVisible(true);
        this.ventanaMain.addListener(new AddListener());
        apartadoTipo = (String)ventanaMain.getApartado().getSelectedItem();
        pintartabla(apartadoFormulario);
        System.out.println(apartadoTipo);
        System.out.println(ventanaMain.getFormulario());

    }

    class AddListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Genero")){
                ControladorGenero.pintar(ventanaMain, serialGenero);
                apartadoFormulario = "generoform";
            } else if (e.getActionCommand().equals("Autor")){
                apartadoFormulario = "autoresform";
            } else if (e.getActionCommand().equals("Usuario")){
                apartadoFormulario = "usuarioform";
            }else if (e.getActionCommand().equals("Recurso")){
                apartadoFormulario = "recursoform";
            }

            if(e.getActionCommand().equalsIgnoreCase("Agregar")){
                if(apartadoFormulario == "generoform"){
                    ControladorGenero.pintar(ventanaMain, serialGenero);
                    if(ControladorGenero.revisarGeneroCampos(ventanaMain)){
                        Generoliterario nuevogeneroliterario = ControladorGenero.crearGeneroliterario(ventanaMain);
                        Integer codigoGeneroLiterario = nuevogeneroliterario.getcodigogenero();
                        String nombreGeneroLiterario = nuevogeneroliterario.getgeneroliterario();
                        if(biblioteca.getGeneroLiterario().añadir(nuevogeneroliterario)){
                                JOptionPane.showMessageDialog(null, "" + nombreGeneroLiterario + " Ha sido agregado como nuevo genero literario", "Agregado correctamente", JOptionPane.INFORMATION_MESSAGE);
                                serialGenero++;
                        } else {
                                JOptionPane.showMessageDialog(null,"El producto " + codigoGeneroLiterario + " " + nombreGeneroLiterario + " ya se encuentra registrado en el sistema, no lo puede volver a registrar.", "Advertencia", JOptionPane.ERROR_MESSAGE);
                        }
                    } 
                } // añadir para cada formulario else if 
                //terminar con metodo
            } else if (e.getActionCommand().equalsIgnoreCase("limpiar")){
                if(apartadoFormulario == "generoform"){
                    ControladorGenero.limpiar(ventanaMain, serialGenero);
                }
                
            } else if(e.getActionCommand().equalsIgnoreCase("Editar")){
                if(apartadoFormulario == "generoform"){
                    if(ControladorGenero.revisarGeneroCampos(ventanaMain)){
                        Generoliterario nuevGeneroliterario = ControladorGenero.crearGeneroliterario(ventanaMain);
                        Integer idGelt = nuevGeneroliterario.getcodigogenero();
                        String nombreGenerolt = nuevGeneroliterario.getgeneroliterario();
                        if(biblioteca.getGeneroLiterario().elementoPresente(idGelt) && biblioteca.getGeneroLiterario().actualizar(idGelt, nuevGeneroliterario)){
                            JOptionPane.showMessageDialog(null, " !Genero Literario Actualizado! " + "\n Codigo: " + idGelt + "\n Genero Literario: " + nombreGenerolt+ "", "Actualizacion", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "El genero Literario " + "\n Codigo: " + idGelt + "\n Genero Literario: " + nombreGenerolt+ "\n No existe, debe registrarlo en el sistema", "Advertencia",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            } else if (e.getActionCommand().equalsIgnoreCase("Eliminar")){
                if(apartadoFormulario == "generoform"){
                    String stringcodgenero = ventanaMain.getFiledcodg().getText();
                    Integer idGelt = Integer.valueOf(stringcodgenero);
                    if(biblioteca.getGeneroLiterario().elementoPresente(idGelt)){
                        String nombreGeneroLiterario = biblioteca.getGeneroLiterario().getElemento(idGelt).getgeneroliterario();
                        int seguir = JOptionPane.showConfirmDialog(null , "¿Relamente desea eliminar el genero literario" + idGelt + "" + nombreGeneroLiterario + " ?\n Esta acción es Irreversible", "Eliminar Genero Literario" ,JOptionPane.YES_NO_OPTION);
                        if(seguir == JOptionPane.YES_OPTION && biblioteca.getGeneroLiterario().eliminar(idGelt)){
                            JOptionPane.showMessageDialog(null, "Genero Literario " + "\n Codigo: "+ idGelt + "\n Genero Literario:  " + nombreGeneroLiterario + "\n Fue eliminado con exito", "Eliminacipon",JOptionPane.INFORMATION_MESSAGE);
                            ControladorGenero.limpiar(ventanaMain, serialGenero);
                        } else {
                            JOptionPane.showMessageDialog(null, "El genero literario: " + "\n Codigo: " + idGelt + "\n Genero Literario: " + nombreGeneroLiterario + " No existe en el sistema", "Advertencia", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }   
            }
        }
          
    }

    public void pintartabla(String apartadoTipo){
        datosTable = ventanaMain.getDatosEnTabla();
        paneltabla = ventanaMain.getPaneltable();
        paneltabla.removeAll();
        datosTable.removeAll();

        if(apartadoTipo == "Genero literario"){
            String[][] generoLiterarioData = biblioteca.getGeneroLiterario().getListables();
            datosTable = new JTable(ControladorVentana.asignarModelTabla(generoLiterarioData, ControladorGenero.getTitleGenero()));

            ventanaMain.setDatosEnTabla(datosTable);
            paneltabla = new JScrollPane(ventanaMain.getDatosEnTabla());
            ventanaMain.setPaneltable(paneltabla); 

        }

        datosTable.addMouseListener(new test());
    } 
    // CAMBIAR POR EL METODO DE IDENTIFICAR (PARA VER SI FUNCIONA POR LO MENOS LISTAR LA TABLA)
    class test implements MouseInputListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            DefaultTableModel tableModel= (DefaultTableModel)ventanaMain.getDatosEnTabla().getModel();

            if(apartadoTipo == "Genero literario"){
                ControladorGenero.crearTabla(tableModel, ventanaMain);
            }
        
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

    }

    public static TableModel asignarModelTabla (String[][] datos, String[] encabezado){
        TableModel model = new DefaultTableModel(datos, encabezado){
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        }; 
        
        return model;

    }
}
