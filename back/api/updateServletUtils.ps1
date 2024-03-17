# Chemin relatif du fichier source principal
$fichierSourcePrincipal = ".\movie\src\main\java\servlet\ServletUtils.java"

# Chemin relatif du dossier contenant les fichiers d'exceptions
$dossierExceptions = ".\movie\src\main\java\exceptions"

# Liste des répertoires de destination relatifs
$repertoiresDestination = @(".\cart", ".\comment", ".\stats", ".\user")

foreach ($repertoire in $repertoiresDestination)
{
    # Copie du fichier principal
    $cheminDestinationPrincipal = Join-Path -Path $repertoire -ChildPath "src\main\java\servlet\ServletUtils.java"
    Write-Host "Copie de $fichierSourcePrincipal vers $cheminDestinationPrincipal"
    Copy-Item -Path $fichierSourcePrincipal -Destination $cheminDestinationPrincipal -Force

    # Construction du chemin de destination pour le dossier d'exceptions
    $cheminDestinationExceptions = Join-Path -Path $repertoire -ChildPath "src\main\java\exceptions"

    # Création du dossier de destination pour les exceptions s'il n'existe pas
    if (-not (Test-Path -Path $cheminDestinationExceptions))
    {
        New-Item -ItemType Directory -Path $cheminDestinationExceptions
    }

    # Copie de tous les fichiers du dossier d'exceptions
    Get-ChildItem -Path $dossierExceptions -File | ForEach-Object {
        $cheminFichierDestination = Join-Path -Path $cheminDestinationExceptions -ChildPath $_.Name
        Write-Host "Copie de $($_.FullName) vers $cheminFichierDestination"
        Copy-Item -Path $_.FullName -Destination $cheminFichierDestination -Force
    }
}
