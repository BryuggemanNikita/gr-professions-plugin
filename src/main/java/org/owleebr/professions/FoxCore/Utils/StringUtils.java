package org.owleebr.professions.FoxCore.Utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    private static final Map<Character, String> translitMap = new HashMap<>();

    static {
        // Простая таблица транслитерации
        translitMap.put('а', "a");   translitMap.put('б', "b");
        translitMap.put('в', "v");   translitMap.put('г', "g");
        translitMap.put('д', "d");   translitMap.put('е', "e");
        translitMap.put('ё', "yo");  translitMap.put('ж', "zh");
        translitMap.put('з', "z");   translitMap.put('и', "i");
        translitMap.put('й', "y");   translitMap.put('к', "k");
        translitMap.put('л', "l");   translitMap.put('м', "m");
        translitMap.put('н', "n");   translitMap.put('о', "o");
        translitMap.put('п', "p");   translitMap.put('р', "r");
        translitMap.put('с', "s");   translitMap.put('т', "t");
        translitMap.put('у', "u");   translitMap.put('ф', "f");
        translitMap.put('х', "kh");  translitMap.put('ц', "ts");
        translitMap.put('ч', "ch");  translitMap.put('ш', "sh");
        translitMap.put('щ', "shch");translitMap.put('ъ', "");
        translitMap.put('ы', "y");   translitMap.put('ь', "");
        translitMap.put('э', "e");   translitMap.put('ю', "yu");
        translitMap.put('я', "ya");

        // Заглавные буквы (чтобы не потерялись)
        translitMap.put('А', "A");   translitMap.put('Б', "B");
        translitMap.put('В', "V");   translitMap.put('Г', "G");
        translitMap.put('Д', "D");   translitMap.put('Е', "E");
        translitMap.put('Ё', "Yo");  translitMap.put('Ж', "Zh");
        translitMap.put('З', "Z");   translitMap.put('И', "I");
        translitMap.put('Й', "Y");   translitMap.put('К', "K");
        translitMap.put('Л', "L");   translitMap.put('М', "M");
        translitMap.put('Н', "N");   translitMap.put('О', "O");
        translitMap.put('П', "P");   translitMap.put('Р', "R");
        translitMap.put('С', "S");   translitMap.put('Т', "T");
        translitMap.put('У', "U");   translitMap.put('Ф', "F");
        translitMap.put('Х', "Kh");  translitMap.put('Ц', "Ts");
        translitMap.put('Ч', "Ch");  translitMap.put('Ш', "Sh");
        translitMap.put('Щ', "Shch");translitMap.put('Ъ', "");
        translitMap.put('Ы', "Y");   translitMap.put('Ь', "");
        translitMap.put('Э', "E");   translitMap.put('Ю', "Yu");
        translitMap.put('Я', "Ya");
    }

    public static String transliterate(String input) {
        input.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                sb.append("_"); // пробел → "_"
            } else if (translitMap.containsKey(ch)) {
                sb.append(translitMap.get(ch)); // кириллица → латиница
            } else if (Character.isLetterOrDigit(ch) || ch == '_') {
                sb.append(ch); // оставляем только буквы, цифры и "_"
            }
            // все остальные символы (.,!? и т.д.) игнорируются
        }

        return sb.toString();
    }
}
