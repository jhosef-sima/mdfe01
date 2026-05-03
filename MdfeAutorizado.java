package br.com.projeto.mdfe.model;

import jakarta.persistence.*;

@Entity
public class MdfeAutorizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chave;
    private String protocolo;
    private String status;
    private String motivo;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String xmlProc;

    public Long getId() {
        return id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getXmlProc() {
        return xmlProc;
    }

    public void setXmlProc(String xmlProc) {
        this.xmlProc = xmlProc;
    }
}
