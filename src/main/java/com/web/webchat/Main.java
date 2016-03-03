package com.web.webchat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import static spark.Spark.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        Connection connection = DriverManager.getConnection("jdbc:sqlite:chat1.db");
        Statement stmt = connection.createStatement();

        get("/viestit", (req, res) -> {

            ResultSet rs = stmt.executeQuery("SELECT * FROM Viesti;");
            ArrayList<String> viestilista = new ArrayList<>();
            while (rs.next()) {
                String viesti = "";
                viesti += rs.getString("sisalto");
                viesti += " - ";
                viesti += rs.getString("kayttaja");
                viestilista.add(viesti);
            }

            String viestit = "";
            for (String viesti : viestilista) {
                viestit += viesti + "<br/>";
            }

            return viestit
                    + "<form method=\"POST\" action=\"/viestit\">\n"
                    + "Viesti:<br/>\n"
                    + "<input type=\"text\" name=\"viesti\"/><br/>\n"
                    + "Käyttäjätunnus;<br/>\n"
                    + "<input type=\"text\" name=\"kt\"/><br/>\n"
                    + "<input type=\"submit\" value=\"Lisää viesti\"/>\n"
                    + "</form>";
        });
        post("/viestit", (req, res) -> {
            String viesti = req.queryParams("viesti");
            String kt = req.queryParams("kt");

            stmt.executeUpdate("INSERT INTO Viesti (sisalto, kayttaja) VALUES ('" + viesti + "', '" + kt + "');");
            return "Kerrotaan siitä tiedon lähettäjälle: " + viesti;

        });

    }
}
