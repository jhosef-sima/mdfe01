package br.com.projeto.mdfe.util;

public class ChaveMdfeUtil {

    public static String gerarChave(
            String cUF,
            String anoMes,
            String cnpj,
            String modelo,
            String serie,
            String numero,
            String tipoEmissao,
            String codigoNumerico
    ) {
        String chaveSemDV =
                cUF +
                anoMes +
                cnpj +
                modelo +
                preencherEsquerda(serie, 3) +
                preencherEsquerda(numero, 9) +
                tipoEmissao +
                preencherEsquerda(codigoNumerico, 8);

        int dv = calcularDV(chaveSemDV);

        return chaveSemDV + dv;
    }

    public static int calcularDV(String chaveSemDV) {
        int peso = 2;
        int soma = 0;

        for (int i = chaveSemDV.length() - 1; i >= 0; i--) {
            int numero = Character.getNumericValue(chaveSemDV.charAt(i));
            soma += numero * peso;

            peso++;
            if (peso > 9) {
                peso = 2;
            }
        }

        int resto = soma % 11;
        int dv = 11 - resto;

        if (dv >= 10) {
            dv = 0;
        }

        return dv;
    }

    private static String preencherEsquerda(String valor, int tamanho) {
        return String.format("%" + tamanho + "s", valor).replace(' ', '0');
    }
}
