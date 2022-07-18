package controlclientes;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DB {

    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:clientesdb;create=true";

    public DB() throws ClassNotFoundException {
        Class.forName(DRIVER);

        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            con.createStatement().execute("CREATE TABLE clientes(codigo INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), nombre VARCHAR(40), apellido VARCHAR(40), fechaNacimiento DATE, telefono BIGINT)");
            con.createStatement().execute("CREATE TABLE cortes(codigoCliente BIGINT, fechaCorte DATE, codigoCorte INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1))");
            System.out.println("Se creo la base de datos con exito!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public DefaultTableModel getClientes() {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            Statement sql = con.createStatement();
            ResultSet res = sql.executeQuery("select * from clientes");
            ResultSetMetaData metadata = res.getMetaData();

            DefaultTableModel datos = new DefaultTableModel();

            datos.addColumn(metadata.getColumnName(1));
            datos.addColumn(metadata.getColumnName(2));
            datos.addColumn(metadata.getColumnName(3));
            datos.addColumn("FECHA NAC.");
            datos.addColumn(metadata.getColumnName(5));

            while (res.next()) {
                Object[] fila = new Object[5];
                fila[0] = res.getString(1);
                fila[1] = res.getString(2);
                fila[2] = res.getString(3);
                fila[3] = res.getString(4);
                fila[4] = res.getString(5);
                datos.addRow(fila);
            }

            con.close();

            return datos;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error al obtener los clientes!", 1);
            System.out.println(e);

            return null;
        }
    }

    public DefaultTableModel getClientes(String busqueda) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("select * from clientes");
            busqueda = busqueda.toLowerCase();

            ResultSet res = sql.executeQuery();
            ResultSetMetaData metadata = res.getMetaData();

            DefaultTableModel datos = new DefaultTableModel();

            datos.addColumn(metadata.getColumnName(1));
            datos.addColumn(metadata.getColumnName(2));
            datos.addColumn(metadata.getColumnName(3));
            datos.addColumn("FECHA NAC.");
            datos.addColumn(metadata.getColumnName(5));

            while (res.next()) {
                Object[] fila = new Object[5];
                fila[0] = res.getString(1);
                fila[1] = res.getString(2);
                fila[2] = res.getString(3);
                fila[3] = res.getString(4);
                fila[4] = res.getString(5);
                
                if(res.getString(1).equals(busqueda) || res.getString(2).toLowerCase().contains(busqueda) || res.getString(3).toLowerCase().contains(busqueda) || res.getString(5).equals(busqueda)){
                    System.out.println("Cod: " + res.getString(1));
                    datos.addRow(fila);
                }
            }

            con.close();

            return datos;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error!", 1);
            System.out.println(e);

            return null;
        }
    }

    public void addCliente(String nombre, String apellido, String fechaNac, String telefono) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("insert into clientes values (DEFAULT, ?,?,?,?)");
            sql.setString(1, nombre);
            sql.setString(2, apellido);
            sql.setString(3, fechaNac);
            sql.setString(4, telefono);

            sql.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Revise los campos e intente nuevamente.", "Error!", 1);
            System.out.println(e);
        }
    }

    public void editCliente(String nombre, String apellido, String fechaNac, String telefono, String codigo) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("update clientes set nombre = ?, apellido = ?, fechaNacimiento = ?, telefono = ? where codigo = ?");
            sql.setString(1, nombre);
            sql.setString(2, apellido);
            sql.setString(3, fechaNac);
            sql.setString(4, telefono);
            sql.setString(5, codigo);

            sql.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el cliente.", "Error!", 1);
            System.out.println(e);
        }
    }

    public void deleteCliente(String codigo) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("delete from clientes where codigo = ?");
            sql.setString(1, codigo);
            sql.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el cliente.", "Error!", 1);
            System.out.println(e);
        }
    }

    public DefaultTableModel getCortes(String codigo) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("select * from cortes where codigoCliente = ?");
            sql.setString(1, codigo);
            ResultSet res = sql.executeQuery();
            ResultSetMetaData metadata = res.getMetaData();

            DefaultTableModel datos = new DefaultTableModel();

            datos.addColumn("CODIGO");
            datos.addColumn("FECHA");

            while (res.next()) {
                Object[] fila = new Object[2];
                fila[0] = res.getString(3);
                fila[1] = res.getString(2);
                datos.addRow(fila);
            }

            con.close();

            return datos;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error!", 1);
            System.out.println("Error al obtener los cortes " + e);

            return null;
        }
    }

    public void addCorte(String codigo, String fecha) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("insert into cortes values (?,?, DEFAULT)");
            sql.setString(1, codigo);
            sql.setString(2, fecha);

            sql.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Revise los campos e intente nuevamente.", "Error!", 1);
            System.out.println(e);
        }
    }

    public void editCorte(String codigoCliente, String fecha, String codigoCorte) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("update cortes set fechaCorte = ? where codigoCliente = ? and codigoCorte = ?");
            sql.setString(1, fecha);
            sql.setString(2, codigoCliente);
            sql.setString(3, codigoCorte);

            sql.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el corte.", "Error!", 1);
            System.out.println(e);
        }
    }

    public void deleteCorte(String codigoCliente, String codigoCorte) {
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            PreparedStatement sql = con.prepareStatement("delete from cortes where codigoCliente = ? and codigoCorte = ?");
            sql.setString(1, codigoCliente);
            sql.setString(2, codigoCorte);
            sql.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el corte.", "Error!", 1);
            System.out.println(e);
        }
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(cal.getTime());
    }

    public DefaultTableModel getCumple() {
        String fechaActual = now();
        String[] arrayActual = fechaActual.split("-");
        Integer mesActual = Integer.parseInt(arrayActual[1]);
        try {
            Connection con = DriverManager.getConnection(JDBC_URL);
            Statement sql = con.createStatement();
            ResultSet res = sql.executeQuery("select * from clientes");
            ResultSetMetaData metadata = res.getMetaData();

            DefaultTableModel datos = new DefaultTableModel();

            datos.addColumn(metadata.getColumnName(1));
            datos.addColumn(metadata.getColumnName(2));
            datos.addColumn(metadata.getColumnName(3));
            datos.addColumn("FECHA NAC.");
            datos.addColumn(metadata.getColumnName(5));

            while (res.next()) {
                Object[] fila = new Object[5];
                fila[0] = res.getString(1);
                fila[1] = res.getString(2);
                fila[2] = res.getString(3);
                fila[3] = res.getString(4);
                String[] arrayFecha = res.getString(4).split("-");
                Integer mesFecha = Integer.parseInt(arrayFecha[1]);
                fila[4] = res.getString(5);
                if (mesFecha == mesActual || mesFecha == mesActual + 1) {
                    datos.addRow(fila);
                }
            }

            con.close();

            return datos;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error al obtener los clientes!", 1);
            System.out.println(e);

            return null;
        }
    }

}
