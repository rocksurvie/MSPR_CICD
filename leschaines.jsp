<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
<title>Les Chaines</title>
</head>
<body bgcolor=white>
<h1>Exercices sur les chaines de caractères</h1>
<form action="#" method="post">
    <p>Saisir une chaine (Du texte avec 6 caractères minimum) : <input type="text" name="chaine">
    <p><input type="submit" value="Afficher">
</form>
<%-- Récupération de la valeur saisie --%>
<% String chaine = request.getParameter("chaine"); %>

<% if (chaine != null && chaine.length() >= 6) { %>
    <p>La longueur de votre chaîne est de <%= chaine.length() %> caractères</p>
    <p>Le 3° caractère de votre chaine est la lettre <%= chaine.charAt(2) %></p>
    <p>Une sous chaine de votre texte : <%= chaine.substring(2, 6) %></p>
    <p>Votre premier "e" est en : <%= chaine.indexOf('e') %></p>

    <h2>Exercice 1 : Combien de 'e' dans notre chaine de caractère ?</h2>
    <p>
    <% int compteurE = 0;
       for (int i = 0; i < chaine.length(); i++) {
           if (chaine.charAt(i) == 'e') {
               compteurE++;
           }
       }
    %>
    Nombre de lettre 'e' dans votre chaîne : <%= compteurE %>
    </p>

    <h2>Exercice 2 : Affichage vertical</h2>
    <p>
    <% for (int i = 0; i < chaine.length(); i++) { %>
        <%= chaine.charAt(i) %><br>
    <% } %>
    </p>

    <h2>Exercice 3 : Retour à la ligne</h2>
    <p>
    <% for (int i = 0; i < chaine.length(); i++) {
           if (chaine.charAt(i) == ' ') {
               out.println("<br>");
           } else {
               out.print(chaine.charAt(i));
           }
       }
    %>
    </p>

    <h2>Exercice 4 : Afficher une lettre sur deux</h2>
    <p>
    <% for (int i = 0; i < chaine.length(); i += 2) {
           out.print(chaine.charAt(i));
       }
    %>
    </p>

    <h2>Exercice 5 : La phrase en verlan</h2>
    <p>
    <% for (int i = chaine.length() - 1; i >= 0; i--) {
           out.print(chaine.charAt(i));
       }
    %>
    </p>

    <h2>Exercice 6 : Consonnes et voyelles</h2>
    <p>
    <% int voyelles = 0, consonnes = 0;
       for (int i = 0; i < chaine.length(); i++) {
           char c = chaine.charAt(i);
           if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y') {
               voyelles++;
           } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
               consonnes++;
           }
       }
    %>
    Nombre de voyelles : <%= voyelles %>, Nombre de consonnes : <%= consonnes %>
    </p>

<% } %>
<p><a href="index.html">Retour au sommaire</a></p>
</body>
</html>