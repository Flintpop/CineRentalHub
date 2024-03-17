# Chemin relatif du fichier source principal
$fichierSourcePrincipal = ".\movie\src\main\java\servlet\ServletUtils.java"

# Chemin relatif du dossier contenant les fichiers d'exceptions
$dossierExceptions = ".\movie\src\main\java\exceptions"

# Chemin relatif du dossier POJO
$dossierPojo = ".\movie\src\main\java\mariadbPojo"

# Liste des répertoires de destination relatifs
$repertoiresDestination = @(".\cart", ".\comment", ".\stats", ".\user")

foreach ($repertoire in $repertoiresDestination)
{
    # Copie du fichier principal
    $cheminDestinationPrincipal = Join-Path -Path $repertoire -ChildPath "src\main\java\servlet\ServletUtils.java"
    Write-Host "Copie de $fichierSourcePrincipal vers $cheminDestinationPrincipal"
    Copy-Item -Path $fichierSourcePrincipal -Destination $cheminDestinationPrincipal -Force

    # Exceptions
    $cheminDestinationExceptions = Join-Path -Path $repertoire -ChildPath "src\main\java\exceptions"
    if (-not (Test-Path -Path $cheminDestinationExceptions))
    {
        New-Item -ItemType Directory -Path $cheminDestinationExceptions
    }
    Get-ChildItem -Path $dossierExceptions -File | ForEach-Object {
        $cheminFichierDestination = Join-Path -Path $cheminDestinationExceptions -ChildPath $_.Name
        Write-Host "Copie de $($_.FullName) vers $cheminFichierDestination"
        Copy-Item -Path $_.FullName -Destination $cheminFichierDestination -Force
    }

    # POJOs, sauf si le répertoire est .\stats
    if ($repertoire -ne ".\stats")
    {
        $cheminDestinationPojo = Join-Path -Path $repertoire -ChildPath "src\main\java\mariadbPojo"
        if (-not (Test-Path -Path $cheminDestinationPojo))
        {
            New-Item -ItemType Directory -Path $cheminDestinationPojo
        }
        Get-ChildItem -Path $dossierPojo -File | ForEach-Object {
            $cheminFichierDestination = Join-Path -Path $cheminDestinationPojo -ChildPath $_.Name
            Write-Host "Copie de $($_.FullName) vers $cheminFichierDestination"
            Copy-Item -Path $_.FullName -Destination $cheminFichierDestination -Force
        }
    }
}
