package uk.gov.hmcts.reform.mi.micore.vault;

public interface KeyVault {

    public String readSecretFromVault(String keyVaultUrl, String secretName);

    public void writeSecretToVault(String keyVaultUrl, String secretName, String secretValue);
}
