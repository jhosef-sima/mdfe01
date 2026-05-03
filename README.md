# MDF-e Java Base

Projeto inicial para sistema MDF-e em Java 17 + Spring Boot.

## Inclui
- Cadastros de empresa, motorista e veículo
- Criação de MDF-e
- Geração inicial de XML
- Assinatura XML com certificado A1 `.pfx`
- Estrutura para validação XSD
- Cliente SOAP base para SEFAZ

## Rodar
```bash
mvn spring-boot:run
```

## H2
http://localhost:8080/h2-console

JDBC:
```text
jdbc:h2:mem:mdfedb
```

## Certificado A1
Coloque o `.pfx` em `src/main/resources/certificados/` e ajuste `application.yml`.

## Importante
O XML é um modelo inicial. Para produção, complete os grupos obrigatórios do leiaute MDF-e vigente, baixe os XSDs oficiais e configure os endpoints reais por UF/autorizador.
