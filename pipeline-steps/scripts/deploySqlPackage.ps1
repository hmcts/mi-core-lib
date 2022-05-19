param
(
    [parameter(Mandatory = $false)] [String] $projectName,
    [parameter(Mandatory = $false)] [String] $action,
    [parameter(Mandatory = $false)] [String] $tsn,
    [parameter(Mandatory = $false)] [String] $tdn,
    [parameter(Mandatory = $false)] [String] $tu,
    [parameter(Mandatory = $false)] [String] $tp,
    [parameter(Mandatory = $false)] [String] $sf,
    [parameter(Mandatory = $false)] [String] $blockDataLoss,
    [parameter(Mandatory = $false)] [String] $dropObjects,
    [parameter(Mandatory = $false)] [String] $extra,
    [parameter(Mandatory = $false)] [String] $env
)


Function Merge-Hashtables {
    $Output = @{}
    ForEach ($Hashtable in ($Input + $Args)) {
        If ($Hashtable -is [Hashtable]) {
            ForEach ($Key in $Hashtable.Keys) {$Output.$Key = $Hashtable.$Key}
        }
    }
    $Output
}

Function Get-Variables {
    $properties_path = "$Env:BUILD_SOURCESDIRECTORY/$projectName/env"
    $variables=''
    $DefaultProps = convertfrom-stringdata (get-content $properties_path/default.properties -raw)
    $AllProps = @()

    if (Test-Path $properties_path/$env.properties -PathType Leaf) {
        $EnvProps = convertfrom-stringdata (get-content $properties_path/$env.properties -raw)
        $AllProps = Merge-Hashtables $DefaultProps $EnvProps
    } else {
        $AllProps = $DefaultProps
    }

    foreach ($h in $AllProps.Keys) {
        $variables+=" /v:${h}=$($AllProps.Item($h))"
    }
    $variables
}

Function Set-Managed-Identity-Auth {
    $response = Invoke-WebRequest `
         -Uri 'http://169.254.169.254/metadata/identity/oauth2/token?api-version=2018-02-01&resource=https%3A%2F%2Fdatabase.windows.net'`
         -Method GET`
         -Headers @{Metadata="true"}

    ($response.Content | ConvertFrom-Json).access_token
}

$Command='sqlpackage'

$vars = Get-Variables

$Command="$Command /a:$action /tsn:$tsn /tdn:$tdn /sf:$sf /p:BlockOnPossibleDataLoss=$blockDataLoss /p:DropObjectsNotInSource=$dropObjects $vars"

Write-Host $Command

if ($tu) {
    $Command="$Command /tu:$tu /tp:'$tp'"
} else {
    $access_token = Set-Managed-Identity-Auth
    $Command="$Command /AccessToken:$access_token"
}

$Command | Invoke-Expression
