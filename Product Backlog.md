📋 Product Backlog : FIX Web Simulator



**Sprint 1 : Architecture Backend \& FIX (Le moteur)**



**US 1.1 :** Créer le projet Spring Boot et intégrer les dépendances QuickFIX/J et H2.



**US 1.2 :** Configurer l'Acceptor (Broker) pour qu'il soit "écouteur" dès le lancement de Spring.



**US 1.3 :** Configurer l'Initiator (Client) en tant que Service Spring pour qu'il puisse être piloté par des APIs.



**US 1.4 :** Développer la logique de réception : l'Acceptor reçoit un ordre, génère un ID et renvoie immédiatement un Execution Report (ER).



**Sprint 2 : API REST \& Persistance (Le pont)**



**US 2.1 :** Créer l'entité Order et son Repository (JPA/Hibernate) pour stocker les ordres en base MySQL.



**US 2.2 :** Créer un OrderController avec un endpoint POST /api/orders/send pour déclencher l'envoi d'un message FIX.



**US 2.3 :** Créer un endpoint GET /api/orders pour récupérer l'historique des ordres et leurs statuts (NEW, FILLED, REJECTED).



**Sprint 3 : Temps Réel avec WebSockets (La réactivité)**



**US 3.1 :** Configurer Spring WebSocket (STOMP).



**US 3.2 :** Faire en sorte que, dès que l'Initiator reçoit un Execution Report via FIX, il le "pousse" instantanément vers un topic WebSocket (/topic/orders).



Objectif : Éviter que l'utilisateur n'ait à rafraîchir la page pour voir le changement de statut.



**Sprint 4 : Frontend Vue.js 2 (L'interface)**



**US 4.1 :** Créer l'application Vue.js 2 avec vue-cli.



**US 4.2 :** Créer le composant OrderForm : Un formulaire (Symbole, Quantité, Prix) qui appelle l'API Spring Boot.



**US 4.3 :** Créer le composant OrderList : Un tableau dynamique qui affiche les ordres et se met à jour en temps réel via le client WebSocket (SockJS/StompJS).

