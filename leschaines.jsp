<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Les chaines</title>
</head>
<body bgcolor=white>
<h1>Exercices sur les chaines de caractères</h1>
<form action="#" method="post">
    <p>Saisir une chaine (Du texte avec 6 caractères minimum) : <input type="text" id="inputValeur" name="chaine"></p>
    <p><input type="submit" value="Afficher"></p>
</form>
<%-- Récupération des valeurs --%>
    <% String chaine = request.getParameter("chaine"); %>
    
    <% if (chaine != null) { %>

    <%-- Obtention de la longueur de la chaîne --%>
    <% int longueurChaine = chaine.length(); %>
    <p>La longueur de votre chaîne est de <%= longueurChaine %> caractères</p>

    <%-- Extraction du 3° caractère dans votre chaine --%>
    <% char caractereExtrait = chaine.charAt(2); %>
    <p>Le 3° caractère de votre chaine est la lettre <%= caractereExtrait %></p>

    <%-- Obtention d'une sous-chaîne --%>
    <% String sousChaine = chaine.substring(2, 6); %>
    <p>Une sous chaine de votre texte : <%= sousChaine %></p>

    <%-- Recharche de la lettre "e" --%>
    <% char recherche = 'e'; 
       int position = chaine.indexOf(recherche); %>
    <p>Votre premier "e" est en : <%= position %></p>

    
<h2>Exercice 1 : Combien de 'e' dans notre chaine de caractère ?</h2>
<p>Ecrire un programme pour compter le nombre de lettre e dans votre chaine de caractères</p>
<p>
    Ecrire un programme pour compter le nombre de lettre e dans votre chaine de caractères.
    <% int countE = 0;
       for (int i = 0; i < chaine.length(); i++) {
           if (Character.toLowerCase(chaine.charAt(i)) == 'e') {
               countE++;
           }
       }
    %>
    Le nombre de 'e' dans la chaîne "<%= chaine %>" est : <%= countE %>
</p>


<h2>Exercice 2 : Affichage verticale</h2>
<p>Ecrire le programme pour afficher le texte en vertical</br>
Exemple : Bonjour</br>
B</br>
o</br>
n</br>
j</br>
o</br>
u</br>
r</p>

<%
for (int i = 0; i < chaine.length(); i++) {
    char currentChar = chaine.charAt(i);
%>
    <%= currentChar %></br>
<%
}
%>

<h2>Exercice 3 : Retour à la ligne</h2>
<p>La présence d'un espace provoque un retour à la ligne </br>
Exemple : L'hiver sera pluvieux</br>
L'hiver</br>
sera</br>
pluvieux</p>

<%
for (int i = 0; i < chaine.length(); i++) {
    char currentChar = chaine.charAt(i);
    if (Character.isWhitespace(currentChar)) {
%>
        </br>
<%
    } else {
%>
        <%= currentChar %>
<%
    }
}
%>

<h2>Exercice 4 : Afficher une lettre sur deux</h2>
<p>Ecrire le programme pour afficher seulement une lettre sur deux de votre texte </br>
Exemple : L'hiver sera pluvieux</br>
Lhvrsr lvex</p>

<%
for (int i = 0; i < chaine.length(); i += 2) {
    char currentChar = chaine.charAt(i);
%>
    <%= currentChar %>
<%
}
%>

<h2>Exercice 5 : La phrase en verlant</h2>
<p>Ecrire le programme afin d'afficher le texte en verlant </br>
Exemple : L'hiver sera pluvieux</br>
xueivulp ares revih'l</p>

<%
for (int i = chaine.length() - 1; i >= 0; i--) {
    char currentChar = chaine.charAt(i);
%>
    <%= currentChar %>
<%
}
%>

<h2>Exercice 6 : Consonnes et voyelles</h2>
<p>Ecrire le programme afin de compter les consonnes et les voyelles dans votre texte</p>
<%
int countConsonnes = 0;
int countVoyelles = 0;
String voyelles = "aeiouyAEIOUY";

for (int i = 0; i < chaine.length(); i++) {
    char currentChar = chaine.charAt(i);
    if (Character.isLetter(currentChar)) {
        if (voyelles.indexOf(currentChar) != -1) {
            countVoyelles++;
        } else {
            countConsonnes++;
        }
    }
}
%>
Nombre de consonnes : <%= countConsonnes %><br>
Nombre de voyelles : <%= countVoyelles %>
<p><a href="index.html">Retour au sommaire</a></p>
</body>
</html>
