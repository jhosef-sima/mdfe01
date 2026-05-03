package br.com.projeto.mdfe.controller;

import br.com.projeto.mdfe.model.Mdfe;
import br.com.projeto.mdfe.model.MdfeAutorizado;
import br.com.projeto.mdfe.repository.MdfeAutorizadoRepository;
import br.com.projeto.mdfe.repository.MdfeRepository;
import br.com.projeto.mdfe.sefaz.SefazSoapClient;
import br.com.projeto.mdfe.xml.MdfeXmlGenerator;
import br.com.projeto.mdfe.xml.XmlAssinadorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MdfeEnvioController {

    private final MdfeXmlGenerator generator;
    private final XmlAssinadorService assinador;
    private final SefazSoapClient sefazSoapClient;
    private final MdfeAutorizadoRepository repository;
    private final MdfeRepository mdfeRepository;

    public MdfeEnvioController(
            MdfeXmlGenerator generator,
            XmlAssinadorService assinador,
            SefazSoapClient sefazSoapClient,
            MdfeAutorizadoRepository repository,
            MdfeRepository mdfeRepository
    ) {
        this.generator = generator;
        this.assinador = assinador;
        this.sefazSoapClient = sefazSoapClient;
        this.repository = repository;
        this.mdfeRepository = mdfeRepository;
    }

    // 🔥 NOVO ENDPOINT CORRETO
    @GetMapping(value = "/mdfe/enviar/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String enviarMdfe(@PathVariable("id") Long id) {

        try {
            // 🔥 BUSCA DO BANCO
            Mdfe mdfe = mdfeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("MDF-e não encontrado"));

            // 🔥 GERA XML AUTOMÁTICO
            String xml = generator.gerarXmlMdfe(mdfe);

            // 🔥 ASSINA
            String xmlAssinado = assinador.assinarInfMdfe(xml);

            // 🔥 ENVIA
            String retorno = sefazSoapClient.enviarAutorizacao(xmlAssinado);

            // 🔥 SALVA XML GERADO
            mdfe.setXmlGerado(xml);
            mdfe.setXmlAssinado(xmlAssinado);
            mdfeRepository.save(mdfe);

            // 🔥 SALVA AUTORIZADO
            salvarAutorizado(retorno);

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();

            StringBuilder sb = new StringBuilder();
            sb.append("ERRO AO ENVIAR MDF-e:\n\n");

            Throwable t = e;
            while (t != null) {
                sb.append(t.getClass().getName())
                        .append("\n")
                        .append(t.getMessage())
                        .append("\n\n");

                t = t.getCause();
            }

            return sb.toString();
        }
    }

    private void salvarAutorizado(String xmlRetorno) {

        try {
            if (!xmlRetorno.contains("<cStat>100</cStat>")) {
                return;
            }

            String chave = extrair(xmlRetorno, "<chMDFe>", "</chMDFe>");
            String protocolo = extrair(xmlRetorno, "<nProt>", "</nProt>");
            String status = extrair(xmlRetorno, "<cStat>", "</cStat>");
            String motivo = extrair(xmlRetorno, "<xMotivo>", "</xMotivo>");

            MdfeAutorizado mdfe = new MdfeAutorizado();
            mdfe.setChave(chave);
            mdfe.setProtocolo(protocolo);
            mdfe.setStatus(status);
            mdfe.setMotivo(motivo);
            mdfe.setXmlProc(xmlRetorno);

            repository.save(mdfe);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extrair(String xml, String inicio, String fim) {
        int i = xml.indexOf(inicio);
        int f = xml.indexOf(fim);

        if (i == -1 || f == -1) {
            return "";
        }

        return xml.substring(i + inicio.length(), f);
    }

    @GetMapping(value = "/mdfe/listar", produces = MediaType.TEXT_PLAIN_VALUE)
    public String listar() {

        StringBuilder sb = new StringBuilder();

        for (MdfeAutorizado m : repository.findAll()) {
            sb.append("ID: ").append(m.getId()).append("\n");
            sb.append("Chave: ").append(m.getChave()).append("\n");
            sb.append("Protocolo: ").append(m.getProtocolo()).append("\n");
            sb.append("Status: ").append(m.getStatus()).append("\n");
            sb.append("Motivo: ").append(m.getMotivo()).append("\n");
            sb.append("-----------------------------\n");
        }

        return sb.toString();
    }
}