package br.com.projeto.mdfe.model;
import jakarta.persistence.*; import lombok.Data;
@Data @Entity public class Veiculo {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String placa; private String renavam; private String uf; private String tara; private String capacidadeKg;
 private String tipoRodado; private String tipoCarroceria;
}