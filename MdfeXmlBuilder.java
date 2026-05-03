package br.com.projeto.mdfe.xml;
import br.com.projeto.mdfe.model.Mdfe; import org.springframework.stereotype.Component; import java.time.format.DateTimeFormatter;
@Component public class MdfeXmlBuilder {
 public String gerarXml(Mdfe mdfe) {
  StringBuilder infDoc = new StringBuilder();
  if(mdfe.getChavesNfe()!=null) for(String chave: mdfe.getChavesNfe()) infDoc.append("<infNFe><chNFe>").append(escape(chave)).append("</chNFe></infNFe>");
  return """
<?xml version="1.0" encoding="UTF-8"?>
<MDFe xmlns="http://www.portalfiscal.inf.br/mdfe">
  <infMDFe Id="MDFe%s" versao="3.00">
    <ide><cUF>52</cUF><tpAmb>2</tpAmb><tpEmit>2</tpEmit><mod>58</mod><serie>%d</serie><nMDF>%d</nMDF><cMDF>00000001</cMDF><cDV>0</cDV><modal>1</modal><dhEmi>%s</dhEmi><tpEmis>1</tpEmis><procEmi>0</procEmi><verProc>1.0.0</verProc><UFIni>%s</UFIni><UFFim>%s</UFFim></ide>
    <emit><CNPJ>%s</CNPJ><IE>%s</IE><xNome>%s</xNome><enderEmit><xMun>%s</xMun><UF>%s</UF></enderEmit></emit>
    <infModal versaoModal="3.00"><rodo><infANTT></infANTT><veicTracao><cInt>%s</cInt><placa>%s</placa><tara>%s</tara><capKG>%s</capKG><condutor><xNome>%s</xNome><CPF>%s</CPF></condutor></veicTracao></rodo></infModal>
    <infDoc>%s</infDoc>
    <tot><qNFe>%d</qNFe><vCarga>%s</vCarga><cUnid>01</cUnid><qCarga>%s</qCarga></tot>
  </infMDFe>
</MDFe>
""".formatted("CHAVE_A_CALCULAR", mdfe.getSerie(), mdfe.getNumero(), mdfe.getDataEmissao().format(DateTimeFormatter.ISO_DATE_TIME)+"-03:00",
    escape(mdfe.getUfCarregamento()), escape(mdfe.getUfDescarregamento()), onlyDigits(mdfe.getEmpresa().getCnpj()), escape(mdfe.getEmpresa().getInscricaoEstadual()),
    escape(mdfe.getEmpresa().getRazaoSocial()), escape(mdfe.getEmpresa().getMunicipio()), escape(mdfe.getEmpresa().getUf()),
    escape(mdfe.getVeiculo().getPlaca()), escape(mdfe.getVeiculo().getPlaca()), escape(mdfe.getVeiculo().getTara()), escape(mdfe.getVeiculo().getCapacidadeKg()),
    escape(mdfe.getMotorista().getNome()), onlyDigits(mdfe.getMotorista().getCpf()), infDoc.toString(),
    mdfe.getChavesNfe()==null?0:mdfe.getChavesNfe().size(), mdfe.getValorCarga(), mdfe.getPesoBruto());
 }
 private String escape(String s){ return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&apos;");}
 private String onlyDigits(String s){ return s==null?"":s.replaceAll("\\D","");}
}