package br.com.projeto.mdfe.certificado;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Service
public class CertificadoA1Service {

 @Value("${mdfe.certificado.caminho}")
 private String caminho;

 @Value("${mdfe.certificado.senha}")
 private String senha;

 public KeyStore carregarKeyStore() throws Exception {
  KeyStore keyStore = KeyStore.getInstance("PKCS12");

  try (FileInputStream fis = new FileInputStream(caminho)) {
   keyStore.load(fis, senha.toCharArray());
  }

  return keyStore;
 }

 public String obterAlias() throws Exception {
  KeyStore keyStore = carregarKeyStore();
  Enumeration<String> aliases = keyStore.aliases();

  while (aliases.hasMoreElements()) {
   String alias = aliases.nextElement();

   if (keyStore.isKeyEntry(alias)) {
    return alias;
   }
  }

  throw new RuntimeException("Nenhum alias encontrado no certificado.");
 }

 public PrivateKey obterPrivateKey() throws Exception {
  KeyStore keyStore = carregarKeyStore();
  String alias = obterAlias();

  return (PrivateKey) keyStore.getKey(alias, senha.toCharArray());
 }

 public X509Certificate obterCertificado() throws Exception {
  KeyStore keyStore = carregarKeyStore();
  String alias = obterAlias();

  return (X509Certificate) keyStore.getCertificate(alias);
 }
}