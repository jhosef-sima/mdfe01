package br.com.projeto.mdfe.repository;
import br.com.projeto.mdfe.model.Veiculo; import org.springframework.data.jpa.repository.JpaRepository;
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {}