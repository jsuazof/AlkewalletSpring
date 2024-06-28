package com.alkewallet6.connection;

import java.sql.*;

//public class DBConnection {
//
//    //variable para conectar a la DB
//    private static final String URL = "jdbc:mysql://localhost:3306/alkewallet6";
//    private static final String USER = "root";
//    private static final String PASSWORD = "12345678";
//    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//    private static Connection connection = null;
//
//    /**
//     * Método para obtener la conexión a la DB.
//     * @return una conexión a la DB.
//     */
//    public static Connection getConexion() {
//        try {
//            Class.forName(DRIVER);
//            //establace la conexión a la DB
//            connection = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("Conectado");
//        } catch (ClassNotFoundException | SQLException ex) {
//            System.out.println("No conectado");
//            ex.printStackTrace();
//        }
//
//        return connection;
//    }
//
//    /**
//     * Método principal para probar la conexión y realizar una consulta.
//     */
//    public static void main(String[] args) {
//        getConexion();
//        Statement stmt;
//        try {
//            // Crea un Statement para enviar consultas a la base de datos
//            stmt = connection.createStatement();
//            // Consulta SQL para obtener todos los usuarios
//            String consultaSQL = "select * from users";
//            // Ejecuta la consulta
//            ResultSet rs = stmt.executeQuery(consultaSQL);
//
//            // Procesa el ResultSet, fila por fila
//            while(rs.next()) {
//                int id = rs.getInt("id");
//                String nombre = rs.getString("user_name");
//                String apellido = rs.getString("user_surname");
//                String correo = rs.getString("email");
//                String contrasena = rs.getString("pass");
//                System.out.println(id + nombre + apellido + correo + contrasena);
//            }
//
//            //Cierre de conexiones
//            rs.close();
//            stmt.close();
//            connection.close();
//        } catch(SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
//}
