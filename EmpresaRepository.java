package br.com.projeto.mdfe.repository;
import br.com.projeto.mdfe.model.Empresa; import org.springframework.data.jpa.repository.JpaRepository;
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {}