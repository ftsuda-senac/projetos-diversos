package br.senac.tads.dsw.exemplolocal;

import java.security.SecureRandom;
import java.util.Random;

// Adaptado de https://receitasdecodigo.com.br/java/classe-java-completa-para-gerar-e-validar-cpf-e-cnpj
public class GenerateDocumentNumbers {

    private static final int MAX_INT_VALUE = 10;

    private Random random = new SecureRandom();

    private int calculateCpfDv(int[] n) {
        int d1 = n[8] * 2 + n[7] * 3 + n[6] * 4 + n[5] * 5 + n[4] * 6 + n[3] * 7 + n[2] * 8 + n[1] * 9 + n[0] * 10;
        d1 = 11 - (d1 % 11);
        if (d1 >= 10) {
            d1 = 0;
        }

        int d2 = d1 * 2 + n[8] * 3 + n[7] * 4 + n[6] * 5 + n[5] * 6 + n[4] * 7 + n[3] * 8 + n[2] * 9 + n[1] * 10
                + n[0] * 11;
        d2 = 11 - (d2 % 11);
        if (d2 >= 10) {
            d2 = 0;
        }
        return 10 * d1 + d2;
    }

    public String generateCpf(boolean format) {
        int[] n = new int[9];
        for (int i = 0; i < 9; i++) {
            n[i] = random.nextInt(MAX_INT_VALUE);
        }
        int dv = calculateCpfDv(n);
        String result;
        if (format) {
            result = "" + n[0] + n[1] + n[2] + '.' + n[3] + n[4] + n[5] + '.' + n[6] + n[7] + n[8] + '-'
                    + String.format("%02d", dv);
        } else {
            result = "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + n[8] + String.format("%02d", dv);
        }
        return result;
    }

    public boolean isCpf(String cpf) {
        cpf = removeFormatMask(cpf);

        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222")
                || cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
                || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
                || cpf.equals("99999999999") || (cpf.length() > 11)) {
            return false;
        }

        String number = cpf.substring(0, cpf.length() - 2);
        int dv = Integer.parseInt(cpf.substring(cpf.length() - 2, cpf.length()));
        number = String.format("%09d", Integer.parseInt(number));

        int[] n = new int[9];
        for (int i = 0; i < 9; i++) {
            n[i] = Character.getNumericValue(number.charAt(i));
        }
        int calcDv = calculateCpfDv(n);
        return calcDv == dv;
    }

    private int calculateCnpjDv(int[] n) {
        int d1 = n[11] * 2 + n[10] * 3 + n[9] * 4 + n[8] * 5 + n[7] * 6 + n[6] * 7 + n[5] * 8 + n[4] * 9 + n[3] * 2
                + n[2] * 3 + n[1] * 4 + n[0] * 5;
        d1 = 11 - (d1 % 11);
        if (d1 >= 10) {
            d1 = 0;
        }

        int d2 = d1 * 2 + n[11] * 3 + n[10] * 4 + n[9] * 5 + n[8] * 6 + n[7] * 7 + n[6] * 8 + n[5] * 9 + n[4] * 2
                + n[3] * 3 + n[2] * 4 + n[1] * 5 + n[0] * 6;
        d2 = 11 - (d2 % 11);
        if (d2 >= 10) {
            d2 = 0;
        }
        return 10 * d1 + d2;
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

        int dv = calculateCnpjDv(n);
        String result = null;
        if (format) {
            result = "" + n[0] + n[1] + "." + n[2] + n[3] + n[4] + "." + n[5] + n[6] + n[7] + "/" + n[8] + n[9] + n[10]
                    + n[11] + "-" + String.format("%02d", dv);
        } else {
            result = "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + n[8] + n[9] + n[10] + n[11]
                    + String.format("%02d", dv);
        }
        return result;
    }

    public boolean isCnpj(String cnpj) {
        cnpj = removeFormatMask(cnpj);

        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222")
                || cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
                || cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888")
                || cnpj.equals("99999999999999") || (cnpj.length() > 14)) {
            return false;
        }

        String number = cnpj.substring(0, cnpj.length() - 2);
        int dv = Integer.parseInt(cnpj.substring(cnpj.length() - 2, cnpj.length()));
        number = String.format("%012d", Long.parseLong(number));

        int[] n = new int[12];
        for (int i = 0; i < 12; i++) {
            n[i] = Character.getNumericValue(number.charAt(i));
        }
        int calcDv = calculateCnpjDv(n);
        return dv == calcDv;
    }

    private String calculateRgDv(int[] n) {
        int d = 9 * n[7] + 8 * n[6] + 7 * n[5] + 6 * n[4] + 5 * n[3] + 4 * n[2] + 3 * n[1] + 2 * n[0];
        d = 11 - (d % 11);

        String dig = String.valueOf(d);
        if (d == 10) {
            dig = "X";
        } else if (d == 11) {
            dig = "0";
        }
        return dig;
    }

    public String generateRg(boolean format) {
        // numeros gerados
        int[] n = new int[8];
        for (int i = 0; i < 8; i++) {
            n[i] = random.nextInt(MAX_INT_VALUE);
        }
        String dig = calculateRgDv(n);
        String result = null;
        if (format) {
            result = "" + n[0] + n[1] + "." + n[2] + n[3] + n[4] + "." + n[5] + n[6] + n[7] + "-" + dig;
        } else {
            result = "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + dig;
        }
        return result;
    }

    public boolean isRg(String rg) {
        rg = removeFormatMask(rg);
        if (rg.length() > 9) {
            return false;
        }
        String number = rg.substring(0, rg.length() - 1); // Corta o dv, pois pode ser string
        String dv = String.valueOf(rg.charAt(rg.length() - 1));
        number = String.format("%08d", Integer.parseInt(number)); // Garante que numero tenha 8 digitos com zeros a
                                                                  // esquerda.

        int[] n = new int[8];
        for (int i = 0; i < 8; i++) {
            n[i] = Character.getNumericValue(number.charAt(i));
        }
        String calcDv = calculateRgDv(n);
        return dv.equals(calcDv);
    }

    /**
     * 
     * @param n
     * @param stateSpMg Flag para indicar se doc foi emitido em SP ou MG (ver https://siga0984.wordpress.com/2019/05/01/algoritmos-validacao-de-titulo-de-eleitor/)
     * 
     * @return
     */
    public int calculateVoterCardDv(int[] n, boolean stateSpMg) {
        int d1 = 2 * n[0] + 3 * n[1] + 4 * n[2] + 5 * n[3] + 6 * n[4] + 7 * n[5] + 8 * n[6] + 9 * n[7];
        d1 = d1 % 11;
        if (d1 == 0) {
            d1 = stateSpMg ? 1 : 0;
        } else if (d1 > 9) {
            d1 = 0;
        }

        int d2 = 7 * n[8] + 8 * n[9] + 9 * d1;
        d2 = d2 % 11;
        if (d2 == 0) {
            d2 = stateSpMg ? 1 : 0;
        } else if (d2 > 9) {
            d2 = 0;
        }
        return 10 * d1 + d2;
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
        boolean stateSpMg = state == StateCode.SP || state == StateCode.MG;

        int dv = calculateVoterCardDv(n, stateSpMg);
        return "" + n[0] + n[1] + n[2] + n[3] + n[4] + n[5] + n[6] + n[7] + n[8] + n[9] + String.format("%02d", dv);
    }

    public boolean isVoterCard(String voterCard) {
        if (voterCard.length() > 12) {
            return false;
        }
        String number = voterCard.substring(0, voterCard.length() - 2);
        String stateCodeStr = voterCard.substring(voterCard.length() - 4, voterCard.length() - 2);
        int dv = Integer.parseInt(voterCard.substring(voterCard.length() - 2, voterCard.length()));
        number = String.format("%010d", Long.parseLong(number));
        StateCode state = StateCode.getByCode(stateCodeStr);
        boolean stateSpMg = state == StateCode.SP || state == StateCode.MG;

        int[] n = new int[10];
        for (int i = 0; i < 10; i++) {
            n[i] = Character.getNumericValue(number.charAt(i));
        }
        int calcDv = calculateVoterCardDv(n, stateSpMg);
        return dv == calcDv;
    }

    private String removeFormatMask(String doc) {
        return doc.replace(".", "").replace("-", "").replace("/", "");
    }

    public static String formatCnpj(String cnpj) {
        // máscara do CNPJ: 99.999.999.9999-99
        return (cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "."
                + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14));
    }

    public static void main(String[] args) {
        GenerateDocumentNumbers generator = new GenerateDocumentNumbers();
        System.out.println("===== VALIDANDO CPFs");
        for (int i = 0; i < 10; i++) {
            String cpf = generator.generateCpf(true);
            System.out.println(String.format("CPF: %s, Valido: %s", cpf, generator.isCpf(cpf)));
        }

        System.out.println("===== VALIDANDO CNPJs");
        for (int i = 0; i < 10; i++) {
            String cnpj = generator.generateCnpj(true);
            System.out.println(String.format("CNPJ: %s, Valido: %s", cnpj, generator.isCnpj(cnpj)));
        }

        System.out.println("===== VALIDANDO RGs");
        for (int i = 0; i < 10; i++) {
            String rg = generator.generateRg(true);
            System.out.println(String.format("RG: %s, Valido: %s", rg, generator.isRg(rg)));
        }

        System.out.println("===== VALIDANDO Titulo eleitor");
        for (int i = 0; i < 10; i++) {
            String te = generator.generateVoterCard(StateCode.SP);
            System.out.println(String.format("Tit Eleitor: %s, Valido: %s", te, generator.isVoterCard(te)));
        }

    }

    // Codigos usados no titulo de eleitor (ver https://pt.wikipedia.org/wiki/T%C3%ADtulo_eleitoral)
    private static enum StateCode {

        SP("01"), MG("02"), RJ("03"), RS("04"), BA("05"), PR("06"), CE("07"), PE("08"), SC("09"), GO("10"), MA(
                "11"), PB("12"), PA("13"), ES("14"), PI("15"), RN("16"), AL("17"), MT("18"), MS(
                        "19"), DF("20"), SE("21"), AM("22"), RO("23"), AC("24"), AP("25"), RR("26"), TO("27"), ZZ("28");

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
    }
}
