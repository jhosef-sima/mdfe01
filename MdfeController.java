package br.com.projeto.mdfe.controller;
import br.com.projeto.mdfe.dto.CriarMdfeRequest; import br.com.projeto.mdfe.model.Mdfe; import br.com.projeto.mdfe.repository.MdfeRepository; import br.com.projeto.mdfe.service.MdfeService; import org.springframework.http.MediaType; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/mdfe") public class MdfeController {
 private final MdfeService service; private final MdfeRepository repo; public MdfeController(MdfeService s, MdfeRepository r){service=s;repo=r;}
 @PostMapping public Mdfe criar(@RequestBody CriarMdfeRequest r){return service.criar(r);}
 @PostMapping("/{id}/assinar") public Mdfe assinar(@PathVariable Long id) throws Exception {return service.assinar(id);}
 @PostMapping("/{id}/enviar-homologacao") public String enviar(@PathVariable Long id) throws Exception {return service.enviarHomologacao(id);}
 @GetMapping(value="/{id}/xml", produces=MediaType.APPLICATION_XML_VALUE) public String xml(@PathVariable Long id){return repo.findById(id).orElseThrow().getXmlGerado();}
 @GetMapping(value="/{id}/xml-assinado", produces=MediaType.APPLICATION_XML_VALUE) public String xmlAssinado(@PathVariable Long id){return repo.findById(id).orElseThrow().getXmlAssinado();}
}