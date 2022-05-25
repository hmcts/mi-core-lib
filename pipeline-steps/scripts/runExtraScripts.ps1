param
(
    [parameter(Mandatory = $true)] [String] $sqlServer,
    [parameter(Mandatory = $true)] [String] $database,
    [parameter(Mandatory = $true)] [String] $scriptLocation
)

Install-Module SQLServer -Force

# Note: the sample assumes that you or your DBA configured the server to accept connections using
#       that VM Identity you are running on and has granted it access to the database (in this
#       example at least the SELECT permission).

### Obtain the Access Token from the machine
$response = Invoke-WebRequest `
     -Uri 'http://169.254.169.254/metadata/identity/oauth2/token?api-version=2018-02-01&resource=https%3A%2F%2Fdatabase.windows.net'`
     -Method GET`
     -Headers @{Metadata="true"}

$access_token = ($response.Content | ConvertFrom-Json).access_token

Invoke-Sqlcmd -ServerInstance $sqlServer -Database $database -AccessToken $access_token -InputFile $scriptLocation
