package br.com.projeto.mdfe.xml;

import org.springframework.stereotype.Service;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;

@Service public class XmlValidadorService {
 public void validar(String xml, String caminhoXsdPrincipal) throws Exception {
  File xsd=new File(caminhoXsdPrincipal); if(!xsd.exists()) throw new IllegalStateException("XSD não encontrado: "+caminhoXsdPrincipal);
  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd).newValidator().validate(new StreamSource(new StringReader(xml)));
 }
}