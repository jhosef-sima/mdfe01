package br.com.projeto.mdfe.xml;

import br.com.projeto.mdfe.certificado.CertificadoA1Service;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public class XmlAssinadorService {

 private final CertificadoA1Service certificadoService;

 public XmlAssinadorService(CertificadoA1Service certificadoService) {
  this.certificadoService = certificadoService;
 }

 public String assinarInfMdfe(String xml) throws Exception {
  PrivateKey privateKey = certificadoService.obterPrivateKey();
  X509Certificate certificado = certificadoService.obterCertificado();

  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
  dbf.setNamespaceAware(true);

  Document doc = dbf.newDocumentBuilder()
          .parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

  NodeList nodes = doc.getElementsByTagNameNS(
          "http://www.portalfiscal.inf.br/mdfe",
          "infMDFe"
  );

  if (nodes.getLength() == 0) {
   throw new IllegalArgumentException("Tag infMDFe não encontrada.");
  }

  Element inf = (Element) nodes.item(0);
  String id = inf.getAttribute("Id");
  inf.setIdAttribute("Id", true);

  XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

  Reference ref = fac.newReference(
          "#" + id,
          fac.newDigestMethod(DigestMethod.SHA1, null),
          List.of(
                  fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null),
                  fac.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null)
          ),
          null,
          null
  );

  SignedInfo si = fac.newSignedInfo(
          fac.newCanonicalizationMethod(
                  CanonicalizationMethod.INCLUSIVE,
                  (C14NMethodParameterSpec) null
          ),
          fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
          List.of(ref)
  );

  KeyInfo ki = fac.getKeyInfoFactory().newKeyInfo(
          List.of(
                  fac.getKeyInfoFactory().newX509Data(
                          List.of(certificado)
                  )
          )
  );

  XMLSignature sig = fac.newXMLSignature(si, ki);

  DOMSignContext signContext = new DOMSignContext(
          privateKey,
          doc.getDocumentElement()
  );

  sig.sign(signContext);

  Transformer t = TransformerFactory.newInstance().newTransformer();
  t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
  t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

  StringWriter w = new StringWriter();
  t.transform(new DOMSource(doc), new StreamResult(w));

  return w.toString();
 }
}