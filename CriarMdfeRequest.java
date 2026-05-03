package br.com.projeto.mdfe.dto;
import lombok.Data; import java.math.BigDecimal; import java.util.List;
@Data public class CriarMdfeRequest {
 private Long empresaId; private Long motoristaId; private Long veiculoId; private Integer serie; private Integer numero;
 private String ufCarregamento; private String ufDescarregamento; private BigDecimal valorCarga; private BigDecimal pesoBruto;
 private List<String> chavesNfe;
}