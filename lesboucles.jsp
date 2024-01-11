<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Boucles</title>
</head>
<body bgcolor="white">
    <h1>Exercices sur les boucles</h1>
    <form action="#" method="post">
        <label for="inputValeur">Saisir le nombre d'étoiles : </label>
        <input type="text" id="inputValeur" name="valeur">
        <input type="submit" value="Afficher">
    </form>

    <%-- Récupération de la valeur saisie par l'utilisateur --%>
    <% String valeur = request.getParameter("valeur"); %>
    
    <%-- Vérification de l'existence de la valeur --%>
    <% if (valeur != null && !valeur.isEmpty()) { %>

        <h2>Exercice 1 : Le carré d'étoiles</h2>
        <p>Exemple si l'utilisateur saisie le valeur 5</p>
        <p>retour Reel</p>
        <p>
            <% int row = 1; %>
            <% while (row <= cpt) { %>
                <% int col = 1; %>
                <% while (col <= cpt) { %>
                    <%= "*" %>
                    <% col++; %>
                <% } %><br>
                <% row++; %>
            <% } %>
        </p>

        <h2>Exercice 2 : Triangle rectangle gauche</h2>
        <p>Exemple si l'utilisateur saisie le valeur 5</p>
        <p>retour Reel</p>
        <p>
            <% row = 1; %>
            <% while (row <= cpt) { %>
                <% int col = 1; %>
                <% while (col <= row) { %>
                    <%= "*" %>
                    <% col++; %>
                <% } %><br>
                <% row++; %>
            <% } %>
        </p>

        <h2>Exercice 3 : Triangle rectangle inversé</h2>
        <p>Exemple si l'utilisateur saisie le valeur 5</p>
        <p>retour Reel</p>
        <p>
            <% row = cpt; %>
            <% while (row >= 1) { %>
                <% int col = 1; %>
                <% while (col <= row) { %>
                    <%= "*" %>
                    <% col++; %>
                <% } %><br>
                <% row--; %>
            <% } %>
        </p>

        <h2>Exercice 4 : Triangle rectangle 2</h2>
        <p>Exemple si l'utilisateur saisie le valeur 5</p>
        <p>retour Reel</p>
        <p>    
            <% row = 1; %>
            <% while (row <= cpt) { %>
                <% int space = cpt - row; %>
                <% col = 1; %>
            
                <% while (space > 0) { %>
                    <%= "&nbsp;" %>
                    <% space--; %>
                <% } %>
            
                <% while (col <= row) { %>
                    <%= "*" %>
                    <% col++; %>
                <% } %><br>
            
                <% row++; %>
            <% } %>
        </p>

        <h2>Exercice 5 : Triangle isocele</h2>
        <p>Exemple si l'utilisateur saisie le valeur 5</p>
        <p>retour Reel</p>
        <p>
            <% row = 1; %>
            <% while (row <= cpt) { %>
                <% int space = cpt - row; %>
                <% col = 1; %>
            
                <% while (space > 0) { %>
                    <%= "&nbsp;" %>
                    <% space--; %>
                <% } %>
            
                <% while (col <= row) { %>
                    <%= "*" %>
                    <% col++; %>
                <% } %><br>
            
                <% row++; %>
            <% } %>
        </p>

        <!-- Ajoutez ici les autres exercices -->

        <h2>Exercice 6 : Le demi losange</h2>
        <p>Exemple si l'utilisateur saisie le valeur 5</p>
        <p>retour Reel</p>
        <p>
            <% row = 1; %>
            <% while (row <= cpt) { %>
                <% int space = cpt - row; %>
                <% col = 1; %>
            
                <% while (space > 0) { %>
                    <%= "&nbsp;" %>
                    <% space--; %>
                <% } %>
            
                <% while (col <= row) { %>
                    <%= "*" %>
                    <% col++; %>
                <% } %><br>
            
                <% row++; %>
            <% } %>

            <% row = 1; %>
            <% while (row <= cpt - 1) { %>
                <% int space = row; %>
                <% col = 1; %>
            
                <% while (space > 0) { %>
                    <%= "&nbsp;" %>
                    <% space--; %>
                <% } %>
            
                <% while (col <= cpt - row) { %>
                    <%= "*" %>
                    <% col++; %>
                <% } %><br>
            
                <% row++; %>
            <% } %>
        </p>

        <h2>Exercice 7 : La table de multiplication</h2>
        <p>Exemple si l'utilisateur saisie le valeur 5</p>
        <p>retour Reel</p>
        <p>
            <% int multiplicateur = 5; %>
            <% int i = 1; %>
            <% while (i <= cpt) { %>
                <%= multiplicateur %> x <%= i %> = <%= multiplicateur * i %><br>
                <% i++; %>
            <% } %>
        </p>

        <p><a href="index.html">Retour au sommaire</a></p>
    <% } %>
</body>
</html>
