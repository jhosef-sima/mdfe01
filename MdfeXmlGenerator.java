package br.com.projeto.mdfe.xml;

import br.com.projeto.mdfe.model.Empresa;
import br.com.projeto.mdfe.model.Mdfe;
import br.com.projeto.mdfe.model.Motorista;
import br.com.projeto.mdfe.model.Veiculo;
import br.com.projeto.mdfe.util.ChaveMdfeUtil;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MdfeXmlGenerator {

    public String gerarXmlMdfe(Mdfe mdfe) {

        Empresa empresa = mdfe.getEmpresa();
        Motorista motorista = mdfe.getMotorista();
        Veiculo veiculo = mdfe.getVeiculo();

        String dataEmissao = OffsetDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

        String serie = String.valueOf(mdfe.getSerie());
        String numero = String.valueOf(mdfe.getNumero());
        String codigoMdfe = String.format("%08d", mdfe.getNumero());

        String chave = ChaveMdfeUtil.gerarChave(
                "52",
                "2605",
                somenteNumeros(empresa.getCnpj()),
                "58",
                serie,
                numero,
                "1",
                codigoMdfe
        );

        String dv = chave.substring(43);

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<MDFe xmlns=\"http://www.portalfiscal.inf.br/mdfe\">");
        xml.append("<infMDFe Id=\"MDFe").append(chave).append("\" versao=\"3.00\">");

        xml.append("<ide>");
        xml.append("<cUF>52</cUF>");
        xml.append("<tpAmb>2</tpAmb>");
        xml.append("<tpEmit>2</tpEmit>");
        xml.append("<mod>58</mod>");
        xml.append("<serie>").append(serie).append("</serie>");
        xml.append("<nMDF>").append(numero).append("</nMDF>");
        xml.append("<cMDF>").append(codigoMdfe).append("</cMDF>");
        xml.append("<cDV>").append(dv).append("</cDV>");
        xml.append("<modal>1</modal>");
        xml.append("<dhEmi>").append(dataEmissao).append("</dhEmi>");
        xml.append("<tpEmis>1</tpEmis>");
        xml.append("<procEmi>0</procEmi>");
        xml.append("<verProc>1.0</verProc>");
        xml.append("<UFIni>").append(mdfe.getUfCarregamento()).append("</UFIni>");
        xml.append("<UFFim>").append(mdfe.getUfDescarregamento()).append("</UFFim>");
        xml.append("<infMunCarrega>");
        xml.append("<cMunCarrega>").append(empresa.getCodigoMunicipio()).append("</cMunCarrega>");
        xml.append("<xMunCarrega>").append(empresa.getMunicipio()).append("</xMunCarrega>");
        xml.append("</infMunCarrega>");
        xml.append("</ide>");

        xml.append("<emit>");
        xml.append("<CNPJ>").append(somenteNumeros(empresa.getCnpj())).append("</CNPJ>");
        xml.append("<IE>").append(somenteNumeros(empresa.getInscricaoEstadual())).append("</IE>");
        xml.append("<xNome>").append(empresa.getRazaoSocial()).append("</xNome>");
        if (empresa.getNomeFantasia() != null && !empresa.getNomeFantasia().isBlank()) {
            xml.append("<xFant>").append(empresa.getNomeFantasia()).append("</xFant>");
        }
        xml.append("<enderEmit>");
        xml.append("<xLgr>NAO INFORMADO</xLgr>");
        xml.append("<nro>0</nro>");
        xml.append("<xBairro>NAO INFORMADO</xBairro>");
        xml.append("<cMun>").append(empresa.getCodigoMunicipio()).append("</cMun>");
        xml.append("<xMun>").append(empresa.getMunicipio()).append("</xMun>");
        xml.append("<CEP>74000000</CEP>");
        xml.append("<UF>").append(empresa.getUf()).append("</UF>");
        xml.append("</enderEmit>");
        xml.append("</emit>");

        xml.append("<infModal versaoModal=\"3.00\">");
        xml.append("<rodo>");
        xml.append("<infANTT/>");
        xml.append("<veicTracao>");
        xml.append("<placa>").append(veiculo.getPlaca()).append("</placa>");
        xml.append("<RENAVAM>").append(somenteNumeros(veiculo.getRenavam())).append("</RENAVAM>");
        xml.append("<tara>").append(veiculo.getTara()).append("</tara>");
        xml.append("<capKG>").append(veiculo.getCapacidadeKg()).append("</capKG>");
        xml.append("<condutor>");
        xml.append("<xNome>").append(motorista.getNome()).append("</xNome>");
        xml.append("<CPF>").append(somenteNumeros(motorista.getCpf())).append("</CPF>");
        xml.append("</condutor>");
        xml.append("<tpRod>").append(veiculo.getTipoRodado()).append("</tpRod>");
        xml.append("<tpCar>").append(veiculo.getTipoCarroceria()).append("</tpCar>");
        xml.append("<UF>").append(veiculo.getUf()).append("</UF>");
        xml.append("</veicTracao>");
        xml.append("</rodo>");
        xml.append("</infModal>");

        xml.append("<infDoc>");
        xml.append("<infMunDescarga>");
        xml.append("<cMunDescarga>").append(empresa.getCodigoMunicipio()).append("</cMunDescarga>");
        xml.append("<xMunDescarga>").append(empresa.getMunicipio()).append("</xMunDescarga>");

        for (String chaveNfe : mdfe.getChavesNfe()) {
            xml.append("<infNFe>");
            xml.append("<chNFe>").append(somenteNumeros(chaveNfe)).append("</chNFe>");
            xml.append("</infNFe>");
        }

        xml.append("</infMunDescarga>");
        xml.append("</infDoc>");

        xml.append("<tot>");
        xml.append("<qNFe>").append(mdfe.getChavesNfe().size()).append("</qNFe>");
        xml.append("<vCarga>").append(mdfe.getValorCarga()).append("</vCarga>");
        xml.append("<cUnid>01</cUnid>");
        xml.append("<qCarga>").append(mdfe.getPesoBruto()).append("</qCarga>");
        xml.append("</tot>");

        xml.append("</infMDFe>");

        xml.append("<infMDFeSupl>");
        xml.append("<qrCodMDFe><![CDATA[https://dfe-portal.svrs.rs.gov.br/mdfe/qrCode?chMDFe=")
                .append(chave)
                .append("&tpAmb=2]]></qrCodMDFe>");
        xml.append("</infMDFeSupl>");

        xml.append("</MDFe>");

        mdfe.setChaveAcesso(chave);
        mdfe.setXmlGerado(xml.toString());

        return xml.toString();
    }

    private String somenteNumeros(String valor) {
        if (valor == null) return "";
        return valor.replaceAll("\\D", "");
    }
}