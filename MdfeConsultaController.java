package br.com.projeto.mdfe.controller;

import br.com.projeto.mdfe.model.MdfeAutorizado;
import br.com.projeto.mdfe.repository.MdfeAutorizadoRepository;
import br.com.projeto.mdfe.service.DamdfePdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MdfeConsultaController {

    private final MdfeAutorizadoRepository repository;
    private final DamdfePdfService damdfePdfService;

    public MdfeConsultaController(MdfeAutorizadoRepository repository, DamdfePdfService damdfePdfService) {
        this.repository = repository;
        this.damdfePdfService = damdfePdfService;
    }

    @GetMapping(value = "/mdfe/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public String verXml(@PathVariable("id") Long id) {
        MdfeAutorizado mdfe = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MDF-e não encontrado: " + id));

        return mdfe.getXmlProc();
    }

    @GetMapping(value = "/mdfe/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> baixarPdf(@PathVariable("id") Long id) throws Exception {
        MdfeAutorizado mdfe = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MDF-e não encontrado: " + id));

        byte[] pdf = damdfePdfService.gerarPdf(mdfe);

        String nomeArquivo = "DAMDFE-" + mdfe.getChave() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
