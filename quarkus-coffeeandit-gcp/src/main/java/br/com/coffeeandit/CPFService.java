package br.com.coffeeandit;

import java.util.InputMismatchException;

public class CPFService {

    public boolean isCPFValido(String cpfValue) {
        if (cpfValue.equals("00000000000") ||
                cpfValue.equals("11111111111") ||
                cpfValue.equals("22222222222") || cpfValue.equals("33333333333") ||
                cpfValue.equals("44444444444") || cpfValue.equals("55555555555") ||
                cpfValue.equals("66666666666") || cpfValue.equals("77777777777") ||
                cpfValue.equals("88888888888") || cpfValue.equals("99999999999") ||
                (cpfValue.length() != 11))
            return (false);

        char dig10;
        char dig11;
        int sm;
        int i;
        int r;
        int num;
        int peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (cpfValue.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48);
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (cpfValue.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            return (dig10 == cpfValue.charAt(9)) && (dig11 == cpfValue.charAt(10));
        } catch (InputMismatchException erro) {
            return (false);
        }
    }
}
