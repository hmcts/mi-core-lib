#!/usr/bin/env bash
set -e
DYLD_PRINT_LIBRARIES=1

arguments=''
function getProperty() {
    if [ -z "$2" ]
    then
        echo ""
    else
        echo "$1$2" 
    fi
}

function setManagedIdentityAccessToken() {
    if [ -z "$1" ]
    then
      access_token=$(curl -X GET "http://169.254.169.254/metadata/identity/oauth2/token?api-version=2018-02-01&resource=https%3A%2F%2Fdatabase.windows.net" -H "Metadata: true" | jq -r '.access_token')

      # If access_token is empty then managed identity has been migrated to workload identity.
      if [ -z "$access_token" ]
      then
          IDENTITY_TOKEN=$(cat $AZURE_FEDERATED_TOKEN_FILE)
    
          federated_token=$(curl -s --location --request POST "$AZURE_AUTHORITY_HOST/$AZURE_TENANT_ID/oauth2/v2.0/token" \
            --form 'grant_type="client_credentials"' \
            --form 'client_id="'$AZURE_CLIENT_ID'"' \
            --form 'scope="https://database.windows.net/.default"' \
            --form 'client_assertion_type="urn:ietf:params:oauth:client-assertion-type:jwt-bearer"' \
            --form 'client_assertion="'$IDENTITY_TOKEN'"')
    
          access_token=$(echo $federated_token | jq -r '.access_token')
      fi
      
      echo "/AccessToken:$access_token"
    else
        echo ""
    fi
}

arguments="$arguments $(getProperty /a: $DeploymentAction)"
arguments="$arguments $(getProperty /ssn: $ServerName)"
arguments="$arguments $(getProperty /sdn: $DatabaseName)"
arguments="$arguments $(getProperty /su: $SqlUsername)"
arguments="$arguments $(getProperty /sp: $SqlPassword)"
arguments="$arguments $(getProperty /tf: $TargetFile)"
arguments="$arguments $(getProperty /sf: $DacpacFile)"
arguments="$arguments $(getProperty /tdn: $TargetDatabaseName)"
arguments="$arguments $(getProperty /tsn: $TargetServerName)"
arguments="$arguments $(getProperty /tp: $TargetPassword)"
arguments="$arguments $(getProperty /tu: $TargetUser)"

arguments="$arguments $(setManagedIdentityAccessToken $SqlUsername)"

if [ -z "$arguments" ]
then
    echo "empty arguments"
else
    echo $AdditionalArguments

    arguments="$arguments $AdditionalArguments"
fi

sqlpackage $arguments
