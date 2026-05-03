package br.com.projeto.mdfe.sefaz;

import br.com.projeto.mdfe.config.MdfeProperties;
import jakarta.xml.soap.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

@Component
public class SefazSoapClient {

    private final MdfeProperties props;

    public SefazSoapClient(MdfeProperties props) {
        this.props = props;
    }

    public String enviarAutorizacao(String xmlAssinado) throws Exception {

        System.setProperty("javax.net.ssl.keyStore", props.getCertificado().getCaminho());
        System.setProperty("javax.net.ssl.keyStorePassword", props.getCertificado().getSenha());
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.trustStoreType", "Windows-ROOT");

        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage msg = mf.createMessage();

        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();

        SOAPElement el = body.addChildElement(
                "mdfeDadosMsg",
                "",
                "http://www.portalfiscal.inf.br/mdfe/wsdl/MDFeRecepcaoSinc"
        );

        // 🔥 COMPACTAÇÃO CORRETA
        String xmlCompactado = compactarXml(xmlAssinado);
        el.addTextNode(xmlCompactado);

        msg.getMimeHeaders().addHeader("SOAPAction", "");
        msg.saveChanges();

        try (SOAPConnection conn = SOAPConnectionFactory.newInstance().createConnection()) {

            java.net.URL endpoint = new java.net.URL(props.getSefaz().getAutorizacaoUrl());

            SOAPMessage resp = conn.call(msg, endpoint);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            resp.writeTo(out);

            return out.toString(StandardCharsets.UTF_8);
        }
    }

    // ✅ MÉTODO DENTRO DA CLASSE
    private String compactarXml(String xml) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try (GZIPOutputStream gzip = new GZIPOutputStream(byteStream)) {
            gzip.write(xml.getBytes("UTF-8"));
        }

        return Base64.getEncoder().encodeToString(byteStream.toByteArray());
    }
}