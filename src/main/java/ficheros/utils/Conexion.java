package ficheros.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

  private static Connection con = null;

  public static Connection getConexion() {
    try {
      if (con == null) {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/araclon?serverTimezone=America/Bogota", "root", "");
      }
      return con;
    } catch (ClassNotFoundException | SQLException ex) {
      throw new RuntimeException("Error al crear la conexion!", ex);
    }
  }
}
