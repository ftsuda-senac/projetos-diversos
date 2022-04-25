package br.senac.tads.geradordadospessoais;

public abstract class DocumentUtil {

    private DocumentUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static String removeFormatMask(String value) {
        return value.replace(".", "").replace("-", "").replace("/", "");
    }

    public static String normalize(String value, int size, int sizeDv) {
        value = removeFormatMask(value);
        String number = value.substring(0, value.length() - sizeDv);
        String dv = "";
        if (sizeDv > 0) {
            dv = value.substring(value.length() - sizeDv); // Corta o dv, pois pode ser string "X" para RG
        }
        String format = "%0" + String.valueOf(size - sizeDv) + "d";
        return String.format(format, Long.parseLong(number)) + dv;
    }

    public static int calculateCpfDv(int[] n) {
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

    public static boolean validCpf(String cpf) {
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

    public static int calculateCnpjDv(int[] n) {
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

    public static boolean validCnpj(String cnpj) {
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

    public static String calculateRgDv(int[] n) {
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

    // Essa regra de validação só se aplica para RG do estado de SP
    // Cada estado possui um formato próprio para o RG
    public static boolean validRg(String rg) {
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
     * @param exceSpMg Flag para indicar se doc foi emitido em SP ou MG (ver https://siga0984.wordpress.com/2019/05/01/algoritmos-validacao-de-titulo-de-eleitor/)
     * 
     * @return
     */
    public static int calculateVoterCardDv(int[] n, boolean exceSpMg) {
        int d1 = 2 * n[0] + 3 * n[1] + 4 * n[2] + 5 * n[3] + 6 * n[4] + 7 * n[5] + 8 * n[6] + 9 * n[7];
        d1 = d1 % 11;
        if (d1 == 0) {
            d1 = exceSpMg ? 1 : 0;
        } else if (d1 > 9) {
            d1 = 0;
        }

        int d2 = 7 * n[8] + 8 * n[9] + 9 * d1;
        d2 = d2 % 11;
        if (d2 == 0) {
            d2 = exceSpMg ? 1 : 0;
        } else if (d2 > 9) {
            d2 = 0;
        }
        return 10 * d1 + d2;
    }

    public static boolean validVoterCard(String voterCard) {
        if (voterCard == null || voterCard.length() > 12) {
            return false;
        }
        String number = voterCard.substring(0, voterCard.length() - 2);
        String stateCodeStr = voterCard.substring(voterCard.length() - 4, voterCard.length() - 2);
        int stateCode = Integer.parseInt(stateCodeStr);
        boolean exce = stateCode == 1 || stateCode == 2;

        int dv = Integer.parseInt(voterCard.substring(voterCard.length() - 2, voterCard.length()));
        number = String.format("%010d", Long.parseLong(number));

        int[] n = new int[10];
        for (int i = 0; i < 10; i++) {
            n[i] = Character.getNumericValue(number.charAt(i));
        }

        int calcDv = calculateVoterCardDv(n, exce);
        return dv == calcDv;
    }

}

