package br.com.projeto.mdfe.controller;

import br.com.projeto.mdfe.model.Empresa;
import br.com.projeto.mdfe.model.Motorista;
import br.com.projeto.mdfe.model.Veiculo;
import br.com.projeto.mdfe.repository.EmpresaRepository;
import br.com.projeto.mdfe.repository.MotoristaRepository;
import br.com.projeto.mdfe.repository.VeiculoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/cadastros") public class CadastroController {
 private final EmpresaRepository e; private final MotoristaRepository m; private final VeiculoRepository v;
 public CadastroController(EmpresaRepository e, MotoristaRepository m, VeiculoRepository v){this.e=e;this.m=m;this.v=v;}
 @PostMapping("/empresas") public Empresa salvarEmpresa(@RequestBody Empresa x){return e.save(x);} @GetMapping("/empresas") public List<Empresa> empresas(){return e.findAll();}
 @PostMapping("/motoristas") public Motorista salvarMotorista(@RequestBody Motorista x){return m.save(x);} @GetMapping("/motoristas") public List<Motorista> motoristas(){return m.findAll();}
 @PostMapping("/veiculos") public Veiculo salvarVeiculo(@RequestBody Veiculo x){return v.save(x);} @GetMapping("/veiculos") public List<Veiculo> veiculos(){return v.findAll();}
}