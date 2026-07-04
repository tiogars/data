# Roblox API unfriend

[Unfriend endpoint](https://create.roblox.com/docs/fr-fr/cloud/reference/domains/friends#friends_post_v1_users__targetUserId__unfriend)

## Exemple avec clé API

Le retrait d'ami se fait via une requête POST vers l'endpoint Friends Open Cloud.
La clé est envoyée dans l'en-tête x-api-key.

Exemple curl :

		curl --request POST "https://apis.roblox.com/friends/v1/users/<TARGET_USER_ID>/unfriend" \
			--header "x-api-key: <CLE_API>"

Exemple PowerShell (Windows) :

		$headers = @{ "x-api-key" = "<CLE_API>" }
		Invoke-RestMethod -Method Post \
			-Uri "https://apis.roblox.com/friends/v1/users/<TARGET_USER_ID>/unfriend" \
			-Headers $headers

Remarques :

- TARGET_USER_ID correspond à l'identifiant Roblox de l'ami à retirer.
- La clé API doit avoir les permissions Friends requises.
- Si vous recevez token invalid, vérifier d'abord que l'en-tête est bien x-api-key (et non Authorization: Bearer).

