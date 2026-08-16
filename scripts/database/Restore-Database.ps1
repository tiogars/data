<#
.SYNOPSIS
    Restaure le volume PostgreSQL a partir d'une archive produite par Backup-Database.ps1.

.DESCRIPTION
    Le conteneur postgres est arrete, le contenu du volume est purge puis remplace
    par celui de l'archive fournie. Operation destructive: confirmation requise.

.EXAMPLE
    ./scripts/database/Restore-Database.ps1 -ArchivePath ./backups/2026-08-16_10-00-00-data_postgres_data.tar.gz

.EXAMPLE
    ./scripts/database/Restore-Database.ps1 -ArchivePath D:\backups\dump.tar.gz -Force
#>
[CmdletBinding()]
param(
    # Chemin de l'archive tar.gz a restaurer.
    [Parameter(Mandatory)]
    [string]$ArchivePath,

    # Nom du volume Docker cible.
    [string]$VolumeName = 'data_postgres_data',

    # Nom du projet Compose (cle "name:" du docker-compose.yml).
    [string]$ProjectName = 'data',

    # Nom du service PostgreSQL dans la stack Compose.
    [string]$ServiceName = 'postgres',

    # Ne pas redemarrer PostgreSQL apres la restauration.
    [switch]$KeepStopped,

    # Ignorer la demande de confirmation.
    [switch]$Force
)

$ErrorActionPreference = 'Stop'

function Invoke-Docker {
    param([Parameter(Mandatory)][string[]]$Arguments)

    & docker @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "Echec de la commande: docker $($Arguments -join ' ')"
    }
}

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw 'Docker est introuvable dans le PATH.'
}

$archive = Get-Item -Path $ArchivePath
if ($archive.PSIsContainer) {
    throw "Le chemin '$ArchivePath' est un repertoire, une archive est attendue."
}

$archiveDirectory = $archive.DirectoryName
$archiveName = $archive.Name

if (-not $Force) {
    $answer = Read-Host "Le contenu du volume '$VolumeName' sera efface et remplace par '$archiveName'. Continuer ? (o/N)"
    if ($answer -notin @('o', 'O', 'y', 'Y')) {
        Write-Host 'Restauration annulee.'
        return
    }
}

Write-Host "Arret du service '$ServiceName' (projet '$ProjectName')..."
Invoke-Docker @('compose', '-p', $ProjectName, 'stop', $ServiceName)

# Cree le volume s'il n'existe pas encore (restauration sur environnement vierge).
Invoke-Docker @('volume', 'create', $VolumeName)

try {
    Write-Host "Restauration de $archiveName dans le volume '$VolumeName'..."
    Invoke-Docker @(
        'run', '--rm',
        '-v', "${VolumeName}:/volume",
        '-v', "${archiveDirectory}:/backup:ro",
        'alpine:latest',
        'sh', '-c', "rm -rf /volume/* /volume/..?* /volume/.[!.]* 2>/dev/null; tar xzf '/backup/$archiveName' -C /volume"
    )
}
finally {
    if (-not $KeepStopped) {
        Write-Host "Redemarrage du service '$ServiceName'..."
        Invoke-Docker @('compose', '-p', $ProjectName, 'start', $ServiceName)
    }
}

Write-Host 'Restauration terminee.'
