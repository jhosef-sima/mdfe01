package br.com.projeto.mdfe.model;
import jakarta.persistence.*; import lombok.Data;
@Data @Entity public class Empresa {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String cnpj; private String razaoSocial; private String nomeFantasia; private String inscricaoEstadual;
 private String uf; private String municipio; private String codigoMunicipio;
}