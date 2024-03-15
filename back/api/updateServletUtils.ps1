# Chemin relatif du fichier source
$fichierSource = ".\movie\src\main\java\servlet\ServletUtils.java"

# Liste des répertoires de destination relatifs
$repertoiresDestination = @(".\cart", ".\comment", ".\stats", ".\user")

foreach ($repertoire in $repertoiresDestination)
{
    # Construire le chemin de destination
    $cheminDestination = Join-Path -Path $repertoire -ChildPath "src\main\java\servlet\ServletUtils.java"


    # Copier le fichier si ce dernier n'existe pas déjà

    Write-Host "Copie de $fichierSource vers $cheminDestination"
    Copy-Item -Path $fichierSource -Destination $cheminDestination -Force
}
