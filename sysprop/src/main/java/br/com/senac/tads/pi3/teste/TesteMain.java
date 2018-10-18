/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.tads.pi3.teste;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fernando.tsuda
 */
public class TesteMain {

  public static void main(String... args) {

        String dbname = System.getenv().get("DBNAME");
        String dbuser= System.getenv().get("DBUSER");
        String dbpw = System.getenv().get("DBPW");

        if (dbpw == null) {
            dbpw = "";
        }
        System.out.println("Parametros: dbname=" + dbname +",dbuser=" + dbuser + "dbpw=" + dbpw);
    }
}
