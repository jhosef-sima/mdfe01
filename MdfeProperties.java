package br.com.projeto.mdfe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mdfe")
public class MdfeProperties {

    private Certificado certificado = new Certificado();
    private Sefaz sefaz = new Sefaz();

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public Sefaz getSefaz() {
        return sefaz;
    }

    public void setSefaz(Sefaz sefaz) {
        this.sefaz = sefaz;
    }

    public static class Certificado {
        private String caminho;
        private String senha;

        public String getCaminho() {
            return caminho;
        }

        public void setCaminho(String caminho) {
            this.caminho = caminho;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

    public static class Sefaz {
        private String autorizacaoUrl;

        public String getAutorizacaoUrl() {
            return autorizacaoUrl;
        }

        public void setAutorizacaoUrl(String autorizacaoUrl) {
            this.autorizacaoUrl = autorizacaoUrl;
        }
    }
}
