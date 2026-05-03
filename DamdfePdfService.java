package br.com.projeto.mdfe.service;

import br.com.projeto.mdfe.model.MdfeAutorizado;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class DamdfePdfService {

    public byte[] gerarPdf(MdfeAutorizado mdfe) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4, 28, 28, 28, 28);
        PdfWriter.getInstance(document, out);

        document.open();

        Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font subtitulo = new Font(Font.HELVETICA, 11, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 9, Font.NORMAL);
        Font pequeno = new Font(Font.HELVETICA, 8, Font.NORMAL);
        Font destaque = new Font(Font.HELVETICA, 10, Font.BOLD);

        adicionarCabecalho(document, titulo, subtitulo, normal);
        adicionarChave(document, mdfe, destaque, normal);
        adicionarProtocolo(document, mdfe, subtitulo, normal);
        adicionarBlocoPrincipal(document, mdfe, subtitulo, normal);
        adicionarObservacao(document, pequeno);

        document.close();

        return out.toByteArray();
    }

    private void adicionarCabecalho(Document document, Font titulo, Font subtitulo, Font normal) throws Exception {
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{70, 30});

        PdfPCell esquerda = new PdfPCell();
        esquerda.setPadding(8);
        esquerda.addElement(new Paragraph("DAMDFE", titulo));
        esquerda.addElement(new Paragraph("Documento Auxiliar do Manifesto Eletronico de Documentos Fiscais", normal));
        esquerda.addElement(new Paragraph("Nao possui valor fiscal isoladamente. Deve acompanhar o MDF-e autorizado.", normal));
        tabela.addCell(esquerda);

        PdfPCell direita = new PdfPCell();
        direita.setPadding(8);

        Paragraph modal = new Paragraph("MODAL RODOVIARIO", subtitulo);
        modal.setAlignment(Element.ALIGN_CENTER);
        direita.addElement(modal);

        Paragraph ambiente = new Paragraph("HOMOLOGACAO", subtitulo);
        ambiente.setAlignment(Element.ALIGN_CENTER);
        direita.addElement(ambiente);

        Paragraph via = new Paragraph("Via impressa", normal);
        via.setAlignment(Element.ALIGN_CENTER);
        direita.addElement(via);

        tabela.addCell(direita);

        document.add(tabela);
        document.add(new Paragraph(" "));
    }

    private void adicionarChave(Document document, MdfeAutorizado mdfe, Font destaque, Font normal) throws Exception {
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidthPercentage(100);

        PdfPCell celula = new PdfPCell();
        celula.setPadding(8);

        Paragraph titulo = new Paragraph("CHAVE DE ACESSO", destaque);
        titulo.setAlignment(Element.ALIGN_CENTER);
        celula.addElement(titulo);

        Paragraph chave = new Paragraph(formatarChave(mdfe.getChave()), new Font(Font.COURIER, 12, Font.BOLD));
        chave.setAlignment(Element.ALIGN_CENTER);
        celula.addElement(chave);

        Paragraph consulta = new Paragraph("Consulta: https://dfe-portal.svrs.rs.gov.br/mdfe", normal);
        consulta.setAlignment(Element.ALIGN_CENTER);
        celula.addElement(consulta);

        tabela.addCell(celula);
        document.add(tabela);
        document.add(new Paragraph(" "));
    }

    private void adicionarProtocolo(Document document, MdfeAutorizado mdfe, Font subtitulo, Font normal) throws Exception {
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{50, 50});

        tabela.addCell(celulaComTitulo("PROTOCOLO DE AUTORIZACAO", mdfe.getProtocolo(), subtitulo, normal));
        tabela.addCell(celulaComTitulo("STATUS", mdfe.getStatus() + " - " + mdfe.getMotivo(), subtitulo, normal));

        document.add(tabela);
        document.add(new Paragraph(" "));
    }

    private void adicionarBlocoPrincipal(Document document, MdfeAutorizado mdfe, Font subtitulo, Font normal) throws Exception {
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{50, 50});

        tabela.addCell(celulaComTitulo("EMITENTE", extrairOuPadrao(mdfe.getXmlProc(), "<xNome>", "</xNome>", "Nao informado"), subtitulo, normal));
        tabela.addCell(celulaComTitulo("CNPJ", extrairOuPadrao(mdfe.getXmlProc(), "<CNPJ>", "</CNPJ>", "Nao informado"), subtitulo, normal));

        tabela.addCell(celulaComTitulo("UF INICIO", extrairOuPadrao(mdfe.getXmlProc(), "<UFIni>", "</UFIni>", "Nao informado"), subtitulo, normal));
        tabela.addCell(celulaComTitulo("UF FIM", extrairOuPadrao(mdfe.getXmlProc(), "<UFFim>", "</UFFim>", "Nao informado"), subtitulo, normal));

        tabela.addCell(celulaComTitulo("PLACA", extrairOuPadrao(mdfe.getXmlProc(), "<placa>", "</placa>", "Nao informado"), subtitulo, normal));
        tabela.addCell(celulaComTitulo("MOTORISTA", extrairMotorista(mdfe.getXmlProc()), subtitulo, normal));

        tabela.addCell(celulaComTitulo("VALOR DA CARGA", "R$ " + extrairOuPadrao(mdfe.getXmlProc(), "<vCarga>", "</vCarga>", "0.00"), subtitulo, normal));
        tabela.addCell(celulaComTitulo("PESO / QUANTIDADE", extrairOuPadrao(mdfe.getXmlProc(), "<qCarga>", "</qCarga>", "0.0000"), subtitulo, normal));

        document.add(tabela);
        document.add(new Paragraph(" "));
    }

    private void adicionarObservacao(Document document, Font pequeno) throws Exception {
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidthPercentage(100);

        PdfPCell celula = new PdfPCell();
        celula.setPadding(8);
        celula.addElement(new Paragraph("OBSERVACAO", new Font(Font.HELVETICA, 10, Font.BOLD)));
        celula.addElement(new Paragraph(
                "Este DAMDFE foi gerado para impressao A4. Para uso oficial em producao, recomenda-se ajustar o layout conforme o Manual do MDF-e, incluindo QR Code grafico, dados completos do veiculo, documentos fiscais vinculados e informacoes de percurso.",
                pequeno
        ));

        tabela.addCell(celula);
        document.add(tabela);
    }

    private PdfPCell celulaComTitulo(String titulo, String valor, Font fontTitulo, Font fontValor) {
        PdfPCell celula = new PdfPCell();
        celula.setPadding(7);
        celula.addElement(new Paragraph(titulo, fontTitulo));
        celula.addElement(new Paragraph(valor == null ? "" : valor, fontValor));
        return celula;
    }

    private String formatarChave(String chave) {
        if (chave == null) return "";
        return chave.replaceAll("(.{4})", "$1 ").trim();
    }

    private String extrairMotorista(String xml) {
        String bloco = extrair(xml, "<condutor>", "</condutor>");
        if (bloco.isEmpty()) {
            return "Nao informado";
        }
        return extrairOuPadrao(bloco, "<xNome>", "</xNome>", "Nao informado");
    }

    private String extrairOuPadrao(String xml, String inicio, String fim, String padrao) {
        String valor = extrair(xml, inicio, fim);
        return valor.isEmpty() ? padrao : valor;
    }

    private String extrair(String xml, String inicio, String fim) {
        if (xml == null) return "";
        int i = xml.indexOf(inicio);
        if (i == -1) return "";
        int f = xml.indexOf(fim, i + inicio.length());
        if (f == -1) return "";
        return xml.substring(i + inicio.length(), f);
    }
}
