package br.com.projeto.mdfe.service;

import br.com.projeto.mdfe.dto.CriarMdfeRequest;
import br.com.projeto.mdfe.model.Mdfe;
import br.com.projeto.mdfe.repository.EmpresaRepository;
import br.com.projeto.mdfe.repository.MdfeRepository;
import br.com.projeto.mdfe.repository.MotoristaRepository;
import br.com.projeto.mdfe.repository.VeiculoRepository;
import br.com.projeto.mdfe.sefaz.SefazSoapClient;
import br.com.projeto.mdfe.xml.MdfeXmlBuilder;
import br.com.projeto.mdfe.xml.XmlAssinadorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service public class MdfeService {
 private final MdfeRepository mdfeRepository; private final EmpresaRepository empresaRepository; private final MotoristaRepository motoristaRepository; private final VeiculoRepository veiculoRepository; private final MdfeXmlBuilder xmlBuilder; private final XmlAssinadorService assinador; private final SefazSoapClient sefaz;
 public MdfeService(MdfeRepository m, EmpresaRepository e, MotoristaRepository mo, VeiculoRepository v, MdfeXmlBuilder xb, XmlAssinadorService a, SefazSoapClient s){mdfeRepository=m;empresaRepository=e;motoristaRepository=mo;veiculoRepository=v;xmlBuilder=xb;assinador=a;sefaz=s;}
 public Mdfe criar(CriarMdfeRequest r){ Mdfe x=new Mdfe(); x.setEmpresa(empresaRepository.findById(r.getEmpresaId()).orElseThrow()); x.setMotorista(motoristaRepository.findById(r.getMotoristaId()).orElseThrow()); x.setVeiculo(veiculoRepository.findById(r.getVeiculoId()).orElseThrow()); x.setSerie(r.getSerie()); x.setNumero(r.getNumero()); x.setUfCarregamento(r.getUfCarregamento()); x.setUfDescarregamento(r.getUfDescarregamento()); x.setValorCarga(r.getValorCarga()); x.setPesoBruto(r.getPesoBruto()); x.setChavesNfe(r.getChavesNfe()); x.setDataEmissao(LocalDateTime.now()); x.setStatus("DIGITADO"); x.setXmlGerado(xmlBuilder.gerarXml(x)); return mdfeRepository.save(x); }
 public Mdfe assinar(Long id) throws Exception { Mdfe x=mdfeRepository.findById(id).orElseThrow(); x.setXmlAssinado(assinador.assinarInfMdfe(x.getXmlGerado())); x.setStatus("ASSINADO"); return mdfeRepository.save(x); }
 public String enviarHomologacao(Long id) throws Exception { Mdfe x=mdfeRepository.findById(id).orElseThrow(); if(x.getXmlAssinado()==null) throw new IllegalStateException("MDF-e ainda não foi assinado."); String ret=sefaz.enviarAutorizacao(x.getXmlAssinado()); x.setStatus("ENVIADO_SEFAZ"); mdfeRepository.save(x); return ret; }
}