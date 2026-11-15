# Authentification - Compte utilisateur en base ou URL d'authentification

L'application Web peut authentifier ses utilisateurs de deux façons, au choix :

- **URL d'authentification (OIDC / Keycloak)** : mode historique, activé par défaut
- **Compte utilisateur enregistré en base de données** : l'utilisateur se connecte avec un compte de la table `user_account` (domaine *User Account*)

Le choix se fait depuis la page **Configuration URL d'authentification** (`/auth-config`).

## Objectif et périmètre

- permettre de se passer d'un fournisseur d'identité externe
- réutiliser les comptes déjà gérés dans l'application (rôles `ADMIN` et `USER`)
- conserver le mode OIDC existant, sans changement de comportement tant qu'il n'est pas désactivé

Hors périmètre actuel :

- l'application mobile Flutter continue d'utiliser OIDC
- aucune restriction d'accès par rôle au niveau des endpoints : `ADMIN` et `USER` accèdent aux mêmes ressources
- pas de renouvellement de jeton : il faut se reconnecter à l'expiration

## Parcours utilisateur

### Désactiver l'authentification par URL

1. Ouvrir `/auth-config`.
2. Dans la section **URL d'authentification active**, désactiver l'interrupteur.
   Les champs URL, realm, client ID et scope sont alors grisés et ne sont plus obligatoires.
3. Cliquer sur **Enregistrer**. La page indique que l'authentification appliquée est « compte utilisateur base de donnees ».
4. Ouvrir `/login` (ou cliquer sur **Connexion** dans l'en-tête) et saisir le nom d'utilisateur et le mot de passe d'un compte enregistré en base, par exemple `tiogars` (rôle `ADMIN`).

Pour revenir à OIDC : réactiver l'interrupteur puis enregistrer, ou cliquer sur **Réinitialiser** (qui restaure aussi l'URL, le realm, le client ID et le scope par défaut).

### Se connecter et se déconnecter

- `/login` affiche le formulaire de connexion quand le mode « compte en base » est actif. Si OIDC est actif, la page l'indique et propose la connexion OIDC.
- Une fois connecté, le nom d'utilisateur apparaît dans l'en-tête ; la page `/auth/account` affiche le compte connecté.
- **Déconnexion** supprime la session locale et renvoie vers `/login`.

## Règles métier et validations

- le compte doit exister et être **activé** ; le mot de passe est comparé au hash stocké (PBKDF2, comme à la création du compte)
- les identifiants vides sont refusés (HTTP 400) ; des identifiants incorrects renvoient toujours le même message (HTTP 401) afin de ne pas révéler si le compte existe
- le jeton émis est un JWT signé RS256, valable **8 heures** par défaut (`data.auth.jwt.validity`), portant le rôle du compte
- la gateway accepte à la fois les jetons locaux et les jetons OIDC. `DATA_GATEWAY_AUTH_OIDC_ENABLED=false` force le rejet des jetons OIDC côté serveur
- le point d'entrée `POST /auth/login` est soumis à une limite de débit plus stricte que le reste de l'API (10 requêtes par minute par client par défaut)
- la clé de signature est générée en mémoire au démarrage du `data-server` : un redémarrage invalide les jetons en cours et l'utilisateur doit se reconnecter
- le réglage de l'interrupteur est stocké dans le navigateur (Local Storage, clé `auth-oidc-enabled`)

## Configuration

Variables d'environnement de la `data-gateway` :

| Variable | Défaut | Rôle |
|---|---|---|
| `DATA_GATEWAY_AUTH_OIDC_ENABLED` | `true` | Accepter les jetons OIDC |
| `DATA_GATEWAY_AUTH_LOCAL_ENABLED` | `true` | Accepter les jetons des comptes en base et exposer `/auth/login` |
| `DATA_GATEWAY_AUTH_LOCAL_ISSUER` | `data-server` | Émetteur (`iss`) attendu dans les jetons locaux |
| `DATA_GATEWAY_AUTH_LOCAL_ALLOWED_ROLES` | `ADMIN,USER` | Rôles autorisés pour les jetons locaux |
| `DATA_GATEWAY_RATE_LIMIT_LOGIN_CAPACITY` | `10` | Tentatives de connexion par période |

Propriétés du `data-server` : `data.auth.jwt.issuer` (défaut `data-server`, doit correspondre à `DATA_GATEWAY_AUTH_LOCAL_ISSUER`) et `data.auth.jwt.validity` (défaut `PT8H`).

La clé publique est publiée sur `GET /auth/jwks` du `data-server`. Cette route n'est pas exposée par la gateway : celle-ci la lit directement via `data.gateway.downstream-base-url`.

## Responsive mobile / desktop

La page de connexion et la page `/auth-config` s'empilent verticalement sur mobile et sont centrées avec une largeur maximale sur desktop ; aucune donnée tabulaire n'est concernée.

## Dépannage

| Symptôme | Cause probable | Action |
|---|---|---|
| « Nom utilisateur ou mot de passe incorrect. » | Mauvais identifiants, compte inexistant ou désactivé | Vérifier le compte dans *User Account* |
| « Trop de tentatives de connexion. » | Limite de débit atteinte (HTTP 429) | Attendre une minute |
| Redirection vers `/login` après un redémarrage | Clé de signature régénérée, jeton invalide | Se reconnecter |
| `/login` propose seulement la connexion OIDC | L'interrupteur est encore actif | Le désactiver dans `/auth-config` puis enregistrer |
| Accès conservé après changement de rôle ou désactivation de compte | Le jeton en cours reste valide jusqu'à expiration | Attendre l'expiration ou redémarrer le `data-server` |
| 401 côté gateway avec un jeton local | `data.auth.jwt.issuer` ≠ `DATA_GATEWAY_AUTH_LOCAL_ISSUER` | Aligner les deux valeurs |

## Glossaire

- **OIDC** : OpenID Connect, protocole d'authentification utilisé avec Keycloak
- **JWT** : jeton signé transmis dans l'en-tête `Authorization: Bearer`
- **JWKS** : ensemble de clés publiques permettant à la gateway de vérifier la signature des jetons
