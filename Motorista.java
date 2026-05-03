package br.com.projeto.mdfe.model;
import jakarta.persistence.*; import lombok.Data;
@Data @Entity public class Motorista {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String nome; private String cpf;
}