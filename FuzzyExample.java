import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class FuzzyExample {

    public static void main(String[] args) throws Exception {
        try {
            String fileName = "files/fuzzy_velocity.fcl";
            int velocity = 10;
            int close_to_left = 60;
            int close_to_right = 30;
            FIS fis = FIS.load(fileName,false);

//wyswietl wykresy funkcji fuzyfikacji i defuzyfikacji
            FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();
            fuzzyRuleSet.chart();
//zadaj wartosci wejsciowe
            fuzzyRuleSet.setVariable("velocity", velocity);
            fuzzyRuleSet.setVariable("close_to_left", close_to_left);
            fuzzyRuleSet.setVariable("close_to_right", close_to_right);
//logika sterownika
            fuzzyRuleSet.evaluate();

//graficzna prezentacja wyjscia
            fuzzyRuleSet.getVariable("change_velocity").chartDefuzzifier(true);
            System.out.println("Change vlocity: " + fuzzyRuleSet.getVariable("change_velocity").defuzzify());


//System.out.println(fuzzyRuleSet);

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Niepoprawna liczba parametrow. Przyklad: java FuzzyExample string<plik_fcl> int<poziom natezenia> int<pora dnia>");
        } catch (NumberFormatException ex) {
            System.out.println("Niepoprawny parametr. Przyklad: java FuzzyExample string<plik_fcl> int<poziom natezenia> int<pora dnia>");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}