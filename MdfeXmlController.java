package br.com.projeto.mdfe.controller;

import br.com.projeto.mdfe.model.Mdfe;
import br.com.projeto.mdfe.repository.MdfeRepository;
import br.com.projeto.mdfe.xml.MdfeXmlGenerator;
import br.com.projeto.mdfe.xml.XmlAssinadorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MdfeXmlController {

    private final MdfeXmlGenerator generator;
    private final XmlAssinadorService assinador;
    private final MdfeRepository mdfeRepository;

    public MdfeXmlController(
            MdfeXmlGenerator generator,
            XmlAssinadorService assinador,
            MdfeRepository mdfeRepository
    ) {
        this.generator = generator;
        this.assinador = assinador;
        this.mdfeRepository = mdfeRepository;
    }

    // ✅ AGORA CORRETO
    @GetMapping(value = "/mdfe/xml/gerar/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public String gerarXml(@PathVariable("id") Long id) throws Exception {

        Mdfe mdfe = mdfeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MDF-e não encontrado"));

        String xml = generator.gerarXmlMdfe(mdfe);

        return assinador.assinarInfMdfe(xml);
    }
}