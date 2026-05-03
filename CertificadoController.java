package br.com.projeto.mdfe.controller;

import br.com.projeto.mdfe.certificado.CertificadoA1Service;
import br.com.projeto.mdfe.config.MdfeProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
public class CertificadoController {

    private final MdfeProperties props;
    private final CertificadoA1Service certificadoService;

    public CertificadoController(MdfeProperties props, CertificadoA1Service certificadoService) {
        this.props = props;
        this.certificadoService = certificadoService;
    }

    @GetMapping(value = "/mdfe/certificado", produces = MediaType.TEXT_HTML_VALUE)
    public String telaCertificado() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Importar Certificado A1</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background: #f6f6f6; }
                        .box { background: white; padding: 25px; border-radius: 8px; max-width: 600px; margin: auto; box-shadow: 0 2px 10px #ccc; }
                        h1 { font-size: 22px; }
                        label { display: block; margin-top: 15px; font-weight: bold; }
                        input { width: 100%; padding: 10px; margin-top: 5px; }
                        button { margin-top: 20px; padding: 12px 20px; background: #0b5ed7; color: white; border: none; border-radius: 5px; cursor: pointer; }
                        a { display: block; margin-top: 15px; }
                    </style>
                </head>
                <body>
                    <div class="box">
                        <h1>Importar Certificado A1 (.pfx)</h1>

                        <form method="post" action="/mdfe/certificado/importar" enctype="multipart/form-data">
                            <label>Arquivo .pfx</label>
                            <input type="file" name="arquivo" accept=".pfx,.p12" required>

                            <label>Senha do certificado</label>
                            <input type="password" name="senha" required>

                            <button type="submit">Importar certificado</button>
                        </form>

                        <a href="/mdfe/certificado/testar">Testar certificado atual</a>
                    </div>
                </body>
                </html>
                """;
    }

    @PostMapping(value = "/mdfe/certificado/importar", produces = MediaType.TEXT_HTML_VALUE)
    public String importarCertificado(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("senha") String senha
    ) throws Exception {

        if (arquivo.isEmpty()) {
            return "<h2>Arquivo vazio.</h2><a href='/mdfe/certificado'>Voltar</a>";
        }

        String caminho = props.getCertificado().getCaminho();

        File destino = new File(caminho);
        File pasta = destino.getParentFile();

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(destino)) {
            fos.write(arquivo.getBytes());
        }

        props.getCertificado().setSenha(senha);

        return "<h2>Certificado importado com sucesso!</h2>" +
                "<p>Arquivo salvo em: " + caminho + "</p>" +
                "<p>A senha foi atualizada para esta execução do sistema.</p>" +
                "<p><b>Importante:</b> para manter após reiniciar, atualize também a senha no application.yml.</p>" +
                "<a href='/mdfe/certificado/testar'>Testar certificado</a><br>" +
                "<a href='/mdfe/certificado'>Voltar</a>";
    }

    @GetMapping(value = "/mdfe/certificado/testar", produces = MediaType.TEXT_HTML_VALUE)
    public String testarCertificado() {
        try {
            X509Certificate cert = certificadoService.obterCertificado();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

            return "<h2>Certificado carregado com sucesso!</h2>" +
                    "<p><b>Emitido para:</b> " + cert.getSubjectX500Principal().getName() + "</p>" +
                    "<p><b>Emitido por:</b> " + cert.getIssuerX500Principal().getName() + "</p>" +
                    "<p><b>Validade inicial:</b> " + fmt.format(cert.getNotBefore().toInstant()) + "</p>" +
                    "<p><b>Validade final:</b> " + fmt.format(cert.getNotAfter().toInstant()) + "</p>" +
                    "<a href='/mdfe/certificado'>Voltar</a>";

        } catch (Exception e) {
            return "<h2>Erro ao carregar certificado</h2>" +
                    "<pre>" + e.getClass().getName() + "\n" + e.getMessage() + "</pre>" +
                    "<a href='/mdfe/certificado'>Voltar</a>";
        }
    }
}
