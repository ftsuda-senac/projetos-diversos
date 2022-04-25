/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.tads.geradordadospessoais;

import java.security.SecureRandom;
import java.util.Random;


// Adaptado de https://receitasdecodigo.com.br/java/classe-java-completa-para-gerar-e-validar-cpf-e-cnpj
public class DocumentNumberGenerator {

    private static final int MAX_INT_VALUE = 10;

    private final Random random = new SecureRandom();

    public String generateCpf(boolean format) {
        int[] n = new int[9];
        for (int i = 0; i < 9; i++) {
            n[i] = random.nextInt(MAX_INT_VALUE);
        }
        int dv = DocumentUtil.calculateCpfDv(n);
        String result;
        if (format) {
            result = "" + n[0] + n[1] + n[2] + '.' + n[3] + n[4] + n[5] + '.' + n[6] + n[7] + n[8] + '-' + String.format("%02d", dv);
        } else {
            result = "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + n[8] + String.format("%02d", dv);
        }
        return result;
    }

    public String generateCnpj(boolean format) {
        int[] n = new int[12];
        final int randomValues = 8; // 12 - 4 do "0001"
        for (int i = 0; i < randomValues; i++) {
            n[i] = random.nextInt(MAX_INT_VALUE);
        }
        n[8] = 0;
        n[9] = 0;
        n[10] = 0;
        n[11] = 1;

        int dv = DocumentUtil.calculateCnpjDv(n);
        String result = null;
        if (format) {
            result = "" + n[0] + n[1] + "." + n[2] + n[3] + n[4] + "." + n[5] + n[6] + n[7] + "/" + n[8] + n[9] + n[10] + n[11] + "-" + String.format("%02d", dv);
        } else {
            result = "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + n[8] + n[9] + n[10] + n[11] + String.format("%02d", dv);
        }
        return result;
    }

    public String generateRg(boolean format) {
        // numeros gerados
        int[] n = new int[8];
        for (int i = 0; i < 8; i++) {
            n[i] = random.nextInt(MAX_INT_VALUE);
        }
        String dig = DocumentUtil.calculateRgDv(n);
        String result = null;
        if (format) {
            result = "" + n[0] + n[1] + "." + n[2] + n[3] + n[4] + "." + n[5] + n[6] + n[7] + "-" + dig;
        } else {
            result = "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + dig;
        }
        return result;
    }

    public String generateVoterCard(String code) {
        return generateVoterCard(StateCode.valueOf(code.toUpperCase()));
    }

    public String generateVoterCard(StateCode state) {
        int[] n = new int[10];
        final int randomValues = 8; // 10 - 2 do stateCode
        for (int i = 0; i < randomValues; i++) {
            n[i] = random.nextInt(MAX_INT_VALUE);
        }
        String code = state.code;
        n[8] = Character.getNumericValue(code.charAt(0));
        n[9] = Character.getNumericValue(code.charAt(1));

        int stateCode = Integer.parseInt(code);
        boolean exceSpMg = stateCode == 1 || stateCode == 2;

        int dv = DocumentUtil.calculateVoterCardDv(n, exceSpMg);
        return "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + n[8] + n[9] + String.format("%02d", dv);
    }

    public static String formatCnpj(String cnpj) {
        // máscara do CNPJ: 99.999.999.9999-99
        return (cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "."
                + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14));
    }

    public static void main(String[] args) {

        DocumentNumberGenerator generator = new DocumentNumberGenerator();

        System.out.println("===== Gerando CPFs");
        for (int i = 0; i < 10; i++) {
            String cpf = generator.generateCpf(true);
            System.out.println(String.format("CPF: %s, Valido: %s", cpf, DocumentUtil.validCpf(cpf)));
        }

        System.out.println("===== Gerando CNPJs");
        for (int i = 0; i < 10; i++) {
            String cnpj = generator.generateCnpj(true);
            System.out.println(String.format("CNPJ: %s, Valido: %s", cnpj, DocumentUtil.validCnpj(cnpj)));
        }
        System.out.println("===== Gerando RGs");
        for (int i = 0; i < 10; i++) {
            String rg = generator.generateRg(true);
            System.out.println(String.format("RG: %s, Valido: %s", rg, DocumentUtil.validRg(rg)));
        }

        System.out.println("===== Gerando Titulo eleitor");
        for (int i = 0; i < 10; i++) {
            String te = generator.generateVoterCard(StateCode.SP);
            System.out.println(String.format("Tit Eleitor: %s, Valido: %s\n", te, DocumentUtil.validVoterCard(te)));
        }

    }

    // Codigos usados no titulo de eleitor (ver https://pt.wikipedia.org/wiki/T%C3%ADtulo_eleitoral)
    public static enum StateCode {

        SP("01"), MG("02"), RJ("03"), RS("04"), BA("05"), PR("06"), CE("07"), PE("08"),
        SC("09"), GO("10"), MA("11"), PB("12"), PA("13"), ES("14"), PI("15"), RN("16"),
        AL("17"), MT("18"), MS("19"), DF("20"), SE("21"), AM("22"), RO("23"), AC("24"),
        AP("25"), RR("26"), TO("27"), ZZ("28");

        private String code;

        private StateCode(String code) {
            this.code = code;
        }

        public static StateCode getByCode(String code) {
            for (StateCode sc : values()) {
                if (sc.code.equals(code)) {
                    return sc;
                }
            }
            return null;
        }

        public static StateCode randomState() {
            Random random = new SecureRandom();
            int index = random.nextInt(values().length);
            return values()[index];
        }
    }
}
