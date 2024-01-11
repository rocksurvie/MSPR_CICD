<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion à MariaDB via JSP</title>
</head>
<body>
    <h1>Exemple de connexion à MariaDB avec JSP</h1>


<h2>Exercice 1 : Les films entre 2000 et 2015</h2>
<p>Extraire les films dont l'année est supérieur à l'année 2000 et inférieur à 2015.</p>
<% 
String url = "jdbc:mariadb://localhost:3306/films";
String user = "mysql";
String password = "mysql";

// Charger le pilote JDBC (pilote disponible dans WEB-INF/lib)
Class.forName("org.mariadb.jdbc.Driver");

// Établir la connexion
Connection conn = DriverManager.getConnection(url, user, password);

// Exemple de requête SQL
String sql = "SELECT idFilm, titre, année FROM Film WHERE année >= 2000 AND année <= 2015";
PreparedStatement pstmt = conn.prepareStatement(sql);
ResultSet rs = pstmt.executeQuery();

// Afficher les résultats (à adapter selon vos besoins)
while (rs.next()) {
    String colonne1 = rs.getString("idFilm");
    String colonne2 = rs.getString("titre");
    String colonne3 = rs.getString("année");
    out.println("id : " + colonne1 + ", titre : " + colonne2 + ", année : " + colonne3 + "<br>");
}

// Fermer les ressources 
rs.close();
pstmt.close();
conn.close();
%>

<h2>Exercice 2 : Année de recherche</h2>
<p>Créer un champ de saisie permettant à l'utilisateur de choisir l'année de sa recherche.</p>
<form action="#" method="post">
    <p>Saisir une année : <input type="text" name="annee"></p>
    <p><input type="submit" value="Rechercher"></p>
</form>

<%
String anneeParam = request.getParameter("annee");

if (anneeParam != null && !anneeParam.isEmpty()) {
    int anneeRecherche = Integer.parseInt(anneeParam);
    // Réalisez ici la recherche avec l'année
}
%>

<h2>Exercice 3 : Modification du titre du film</h2>
<p>Créer un fichier permettant de modifier le titre d'un film sur la base de son ID (ID choisi par l'utilisateur)</p>
<form action="#" method="post">
    <p>ID du film : <input type="text" name="idFilm"></p>
    <p>Nouveau titre : <input type="text" name="nouveauTitre"></p>
    <p><input type="submit" value="Modifier"></p>
</form>

<%
String idFilmParam = request.getParameter("idFilm");
String nouveauTitreParam = request.getParameter("nouveauTitre");

if (idFilmParam != null && nouveauTitreParam != null && !idFilmParam.isEmpty() && !nouveauTitreParam.isEmpty()) {
    int idFilm = Integer.parseInt(idFilmParam);
    // Réalisez ici la modification du titre avec l'ID du film
}
%>

<h2>Exercice 4 : La valeur maximum</h2>
<p>Créer un formulaire pour saisir un nouveau film dans la base de données</p>
<form action="#" method="post">
    <p>Titre du film : <input type="text" name="titre"></p>
    <p>Année du film : <input type="text" name="annee"></p>
    <p><input type="submit" value="Ajouter"></p>
</form>

<%
String titreParam = request.getParameter("titre");
 anneeParam = request.getParameter("annee");

if (titreParam != null && anneeParam != null && !titreParam.isEmpty() && !anneeParam.isEmpty()) {
    try {
         url = "jdbc:mariadb://localhost:3306/films";
         user = "mysql";
         password = "mysql";

        // Établir la connexion
        // Exemple de requête SQL pour l'ajout du nouveau film
        String sql = "INSERT INTO Film (titre, année) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titreParam);
            pstmt.setInt(2, Integer.parseInt(anneeParam));
            pstmt.executeUpdate();
        }
    } catch (Exception e) {
        // Handle exceptions, e.g., SQLException or ClassNotFound
    }
}
%>
</body>
</html>
