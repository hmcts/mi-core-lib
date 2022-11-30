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
