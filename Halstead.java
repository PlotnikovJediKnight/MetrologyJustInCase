package sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Halstead {

    private ArrayList<String> allFileLines;
    private String groovyProgram;


    public Halstead(File file) throws IOException {
        allFileLines = (ArrayList<String>) Files.readAllLines(file.toPath());

        StringBuilder sb = new StringBuilder();
        for (String s : allFileLines) {
            sb.append(s);
            sb.append("\n");
        }

        groovyProgram = sb.toString();

        final String id = "[A-z[\\\\u00C0-\\\\u00D6[\\\\u00D8-\\\\u00F6[\\\\u00F8-\\\\u00FF[\\\\u0100-\\\\uFFFE]]]]]+";
        //Множество операндов
        HashMap<String, Integer> operands = new HashMap<>();
        //Множество операторов
        HashMap<String, Integer> operators = new LinkedHashMap<>();
        //Множество ключевых слов
        HashSet<String> keyWords = new HashSet<>();
        //Biba - это строка с кодом программы целиком

        //Здесь мы избавляемся от всех комментариев в коде программы
        groovyProgram = groovyProgram.replaceAll("(?s:/\\*.*?\\*/)|//.*", "");

        //Здесь мы ищем строковые литералы
        Matcher m = Pattern.compile("\"(?:\\\\\"|[^\"])*?\"").matcher(groovyProgram);
        while (m.find()) {
            //Каждый строковы    литерал ммы добавляем в множество операндов
            //operands.put(m.group());
            String buf = m.group();
            if (!operands.containsKey(buf)) {
                operands.put(buf, 1);
            } else {
                operands.put(buf, operands.get(buf) + 1);
            }
        }


        //Теперь в исходном коде программы мы удаляем все строковые литералы
        groovyProgram = groovyProgram.replaceAll("\"(?:\\\\\"|[^\"])*?\"", "");

        //Из файла считываем все основые операторы и ключ слова
        Files.lines(Paths.get("keyWords.txt")).forEach(line -> keyWords.add(line));
        Files.lines(Paths.get("operators.txt")).forEach(line -> operators.put(line, 0));


        //Сюда будем класть "обрезанные методы"
        HashMap<String, Integer> methods = new HashMap<>();


        //Добавление пробелов перед и после скобок, замена кучи пробелов одним
        groovyProgram = groovyProgram.replace("(", "( ").replace(")", " )").replaceAll(" +", " ");

        //Удаление классов(типов)
        String[] lines = groovyProgram.split("\\n");
        for (String line : lines) {
            String[] words = line.split(" +");
            for (int i = 0; i < words.length - 1; i++) {
                if (words[i].matches(id) && words[i + 1].matches(id) && !(keyWords.contains(words[i]) || operators.containsKey(words[i]) || keyWords.contains(words[i + 1]) || operators.containsKey(words[i + 1]))) {
                    groovyProgram = groovyProgram.replace(words[i] + " " + words[i + 1], words[i + 1]);
                }
            }
        }
        //Удаление классов(объявление)
        groovyProgram = groovyProgram.replaceAll("class\\s+" + id + "\\s+[{]", " ");


        //Подсчет и удаление методов и управляющих операторов(все что со скобочками)
        sb = new StringBuilder(groovyProgram);
        Pattern p = Pattern.compile(id + "\\s*" + "[(]");
        m = p.matcher(sb);
        while (m.find()) {
            //Получаем имя метода/оператора
            String match = m.group().split("[\\s(]+")[0];
            if (methods.containsKey(match)) {
                int i = methods.get(match);
                methods.put(match, i + 1);
            } else {
                methods.put(match, 1);
            }
            //Удаление
            sb.delete(m.start(), m.end());
            m = p.matcher(sb);
        }

        //Подсчет и удаление оставшихся операторов
        for (String op : operators.keySet()) {
            int start = sb.indexOf(op);
            while (start != -1) {
                operators.put(op, operators.get(op) + 1);
                sb.replace(start, start + op.length(), " ");
                start = sb.indexOf(op);
            }
        }


        groovyProgram = sb.toString();

        //Удаление ключ слов(почти)
        for (String keyWord : keyWords) {
            groovyProgram = groovyProgram.replaceAll("[\\s\n]+" + keyWord + "\\s+", " ");
        }

        groovyProgram = groovyProgram.trim();
        //Подсчет оставшихся операндов
        String[] ops = groovyProgram.split("\\s+");
        for (String operand : ops) {
            if (operands.containsKey(operand)) {
                operands.put(operand, operands.get(operand) + 1);
            } else {
                operands.put(operand, 1);
            }
        }


        System.out.println(operands);
        System.out.println(methods);
        System.out.println(operators);

        int doCount    = 0;
        int whileCount = 0;
        int opCount    = 0;

        //Подсчет словаря операторов
        for (Map.Entry<String, Integer> it : operators.entrySet()){
            if (it.getValue() != 0  && !( it.getKey().equals("}")
                                    || it.getKey().equals("]")
                                    || it.getKey().equals(")")
                                    || it.getKey().equals(":")
                                    || it.getKey().equals("else")
                                    || it.getKey().equals("case"))){
                dictOperatorCount++;
                opCount += it.getValue();
            }
        }
//
        dictOperatorCount += methods.size();

        if (operators.containsKey("do"))
            doCount    = operators.get("do");
        if (methods.containsKey("while"))
            whileCount = methods.get("while");

        if (doCount == whileCount && doCount != 0)
            dictOperatorCount--;

        dictOperandCount = operands.size();

        //Подсчет количества операторов
        for (Map.Entry<String, Integer> it : methods.entrySet()) {
            opCount += it.getValue();
        }

        opCount -= doCount;
        operatorCount = opCount;

        for (Map.Entry<String, Integer> it : operands.entrySet()){
            operandCount += it.getValue();
            resSB.append(it.getKey() + " - " + it.getValue() + "\n");
        }

        System.out.println(dictOperatorCount + " !!!!!!!!!!!!!!!!");
        System.out.println(dictOperandCount + " !!!!!!!!!!!!!!!!");
        System.out.println(operatorCount + " !!!!!!!!!!!!!!!!");
        System.out.println(operandCount + " !!!!!!!!!!!!!!!!!");

        dictProgram = dictOperandCount + dictOperatorCount;
        programLength = operandCount + operatorCount;
        volumeLength = programLength * Math.log10(dictProgram) * 3.31;

        System.out.println(dictProgram);
        System.out.println(programLength);
        System.out.println(volumeLength);
    }


    public int dictOperatorCount = 0;
    public int dictOperandCount = 0;
    public int operatorCount = 0;
    public int operandCount = 0;
    public int dictProgram = 0;
    public int programLength = 0;
    public double volumeLength = 0;
    public StringBuilder resSB = new StringBuilder();
}
