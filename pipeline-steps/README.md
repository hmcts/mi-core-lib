# MI Azure DevOps pipelines

Configuration for Azure pipelines


## Stages availables


### Build and deploy 
Builds Java Application and and deploy the application in  AKS cluster
Path: ./templates/build-deploy-stage.yaml

#### Accepted parameters

| Name                             | Default                             | Description |
|----------------------------------|-------------------------------------|-----------------------------------------------------  |
| env                              | test                                | Environment use to deploy an execute integration test |
| promEnv                          | stg                                 | Promotion environment |
| applicationName                  | stg                                 | Application name |
| chartEnv                         | test                                | Chart environment(Normally same as env)  |
| azureContainerRegistry           | $(azure.container.registry)         | Azure container registry |
| azureAksCluster                  | $(azure.aks.cluster)                | Azure AKS cluster name |
| azureSubscriptionEndpoint        | $(azure.subscription.endpoint)      | Azure subscription endpoint |
| azureAksResourceGroup            | $(azure.aks.resourcegroup)          | Azure AKS  resource group|
| azurePromAcr                     | $(azure.prom.acr)                   | Azure promotion container registry|
| azurePromSubscriptionEndpoint    | $(azure.prom.subscription.endpoint) | Azure promotion subscription endpoint |
| azureKeyMapping                  | {}                                  | Maps from vault secrets (SecretKey, environment variable name) |
| azureSecretKeys                   | '*'                                 | coma separated azure keys |
| azureVault                       | ''                                  | Azure vault |
| preIntegrationTest               | false                               | Enable pre-deployment task |
| postIntegrationTest              | true                                | Enable integration test(post deployment) |
| functionalTest                   | false                               | Enable functional test |
| mutationTest                     | false                               | Enable mutation test|
| ithcEnv                          | ithc                                | ITHC environment |
| ithcAcr                          | ssprivateithc                       | ITHC container registry |
| ithcSubscriptionEndpoint         | DTS-SHAREDSERVICES-ITHC             | ITHC Subscription endpoint |
