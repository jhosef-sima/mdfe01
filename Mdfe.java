package br.com.projeto.mdfe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data @Entity public class Mdfe {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Integer serie; private Integer numero; private String ufCarregamento; private String ufDescarregamento;
 private BigDecimal valorCarga; private BigDecimal pesoBruto; private LocalDateTime dataEmissao;
 @ManyToOne private Empresa empresa; @ManyToOne private Motorista motorista; @ManyToOne private Veiculo veiculo;
 @ElementCollection private List<String> chavesNfe = new ArrayList<>();
 @Lob private String xmlGerado; @Lob private String xmlAssinado;
 private String chaveAcesso; private String protocoloAutorizacao; private String status;
}