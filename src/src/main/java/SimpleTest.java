import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTest {
    public static void main(String[] args) {
        String result = add("8,944,394,323,791,464", "14,472,334,024,676,221");
        System.out.println(result.equals("23,416,728,348,467,685"));
    }

    static String add(String a, String b) {
        a = removeCommas(a);
        b = removeCommas(b);
        int len = Math.max(a.length(), b.length());
        String revA = reverse(a);
        String revB = reverse(b);
        int carry = 0;
        String resultRev = "";
        for (int i = 0; i < len; i++) {
            int a1 = 0, b1 = 0;
            if (i < revA.length()) {
                a1 = Integer.parseInt(String.valueOf(revA.charAt(i)));
            }
            if (i < revB.length()) {
                b1 = Integer.parseInt(String.valueOf(revB.charAt(i)));
            }
            resultRev += (a1 + b1 + carry) % 10;
            carry = (a1 + b1 + carry) / 10;

        }
        if (carry != 0) {
            resultRev += carry;
        }
        resultRev = addCommas(resultRev);
        return reverse(resultRev);
    }

    static String reverse(String str) {
        String newStr = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            newStr += str.charAt(i);

        }
        return newStr;
    }

    static String removeCommas(String str) {

        return str.replace(",", "");
    }

    static String addCommas(String str) {
        String withCommas = "";
        for (int i = 1; i <= str.length(); i++) {
            if (i % 3 == 0 && i != str.length()) {
                withCommas = withCommas + str.charAt(i - 1) + ",";
            } else {
                withCommas = withCommas + str.charAt(i - 1);
            }
        }

        return withCommas;
    }

    @Test
    void test() {
        Assertions.assertEquals("23,416,728,348,467,685", add("8,944,394,323,791,464", "14,472,334,024,676,221"));
        Assertions.assertEquals("2,468", add("1,234", "1,234"));
        Assertions.assertEquals("1,134,903,170", add("701,408,733", "433,494,437"));
        Assertions.assertEquals("10,468", add("9,234", "1,234"));
        Assertions.assertEquals("1,000", add("999", "1"));
        Assertions.assertEquals("832,040", add("317,811", "514,229"));
    }

}
