<#
.SYNOPSIS
    Arrete PostgreSQL puis archive son volume Docker (sauvegarde a froid).

.DESCRIPTION
    Le conteneur postgres est arrete pour garantir un repertoire de donnees coherent,
    puis le volume nomme est archive dans un fichier tar.gz prefixe par la date.

.EXAMPLE
    ./scripts/database/Backup-Database.ps1

.EXAMPLE
    ./scripts/database/Backup-Database.ps1 -BackupDirectory D:\backups -KeepStopped
#>
[CmdletBinding()]
param(
    # Repertoire de destination des archives.
    [string]$BackupDirectory = (Join-Path $PSScriptRoot '..\..\backups'),

    # Nom du volume Docker contenant les donnees PostgreSQL.
    [string]$VolumeName = 'data_postgres_data',

    # Nom du projet Compose (cle "name:" du docker-compose.yml).
    [string]$ProjectName = 'data',

    # Nom du service PostgreSQL dans la stack Compose.
    [string]$ServiceName = 'postgres',

    # Ne pas redemarrer PostgreSQL apres la sauvegarde.
    [switch]$KeepStopped
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

$volumeExists = & docker volume inspect $VolumeName 2>$null
if ($LASTEXITCODE -ne 0) {
    throw "Le volume Docker '$VolumeName' n'existe pas."
}

$BackupDirectory = (New-Item -ItemType Directory -Path $BackupDirectory -Force).FullName
$timestamp = Get-Date -Format 'yyyy-MM-dd_HH-mm-ss'
$archiveName = "$timestamp-$VolumeName.tar.gz"
$archivePath = Join-Path $BackupDirectory $archiveName

Write-Host "Arret du service '$ServiceName' (projet '$ProjectName')..."
Invoke-Docker @('compose', '-p', $ProjectName, 'stop', $ServiceName)

try {
    Write-Host "Archivage du volume '$VolumeName' vers $archivePath..."
    Invoke-Docker @(
        'run', '--rm',
        '-v', "${VolumeName}:/volume:ro",
        '-v', "${BackupDirectory}:/backup",
        'alpine:latest',
        'tar', 'czf', "/backup/$archiveName", '-C', '/volume', '.'
    )
}
finally {
    if (-not $KeepStopped) {
        Write-Host "Redemarrage du service '$ServiceName'..."
        Invoke-Docker @('compose', '-p', $ProjectName, 'start', $ServiceName)
    }
}

$sizeMb = [math]::Round((Get-Item $archivePath).Length / 1MB, 2)
Write-Host "Sauvegarde terminee: $archivePath ($sizeMb Mo)"
