package br.com.projeto.mdfe.repository;
import br.com.projeto.mdfe.model.Motorista; import org.springframework.data.jpa.repository.JpaRepository;
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {}