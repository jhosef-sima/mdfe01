package br.com.projeto.mdfe.repository;
import br.com.projeto.mdfe.model.Mdfe; import org.springframework.data.jpa.repository.JpaRepository;
public interface MdfeRepository extends JpaRepository<Mdfe, Long> {}