package org.example;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args){
        Options options = new Options();
        options.addOption("a", false, "Ascending (default)");
        options.addOption("d", false, "Descending");
        options.addOption("i", false, "Integer");
        options.addOption("s", false, "String");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            boolean ascending = cmd.hasOption("a"); // По умолчанию всегда true, зависит от значения descending
            boolean descending = cmd.hasOption("d");
            boolean integers = cmd.hasOption("i");
            boolean strings = cmd.hasOption("s");

            boolean sort_direction = !descending; // Определение сортировки по возрастанию или по убыванию

            // Проверка наличия обязательных параметров
            if (!integers && !strings) {
                throw new Exception("Укажите тип данных: -i для целых чисел или -s для строк.");
            }
            if (integers && strings) {
                throw new Exception("Укажите тип данных: -i для целых чисел или -s для строк.");
            }
            if (ascending && descending) {
                throw new Exception("Укажите один флаг для сортировки: -a для сортировки по возрастанию (по умолчанию) или -d для сортировки по убыванию.");
            }

            // Получаем значение выходного файла
            if (cmd.getArgs().length == 0){
                throw new Exception("Необходимо указать имя выходного файла.");
            }
            String outputStr = cmd.getArgs()[0];
            Path outputFile = Paths.get(outputStr);

            // Получаем значения вводных файлов
            if ((cmd.getArgs().length - 1) < 1) {
                throw new Exception("Укажите входной(ые) файл(ы)."); // Предвиденная ошибка
            }
            String[] inputFiles = new String[cmd.getArgs().length - 1];
            for (int i = 1; i < cmd.getArgs().length; i++) {
                inputFiles[i - 1] = cmd.getArgs()[i];
            }

            // Обработка аргументов и выполнение сортировки
            List<String> for_print = new ArrayList<>();
            if (integers) {
                List<Integer> unsorted = new ArrayList<>();
                for (String inputFile: inputFiles) {
                    try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile), StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!(line.contains(" "))){ // если в строке есть пробел - пропускаем эту строку
                                unsorted.add(Integer.parseInt(line));
                            }
                        }
                    } catch (java.nio.file.NoSuchFileException e) {
                        System.out.println("Файл не найден, прерываю обработку файлов. Файл: " + inputFile);
                        break;
                    } catch (java.lang.NumberFormatException e){
                        System.out.println("Найден несовпадающий по типу элемент, прерываю перебор файлов. Последний файл: " + inputFile);
                        break;
                    } catch (Exception e) {
                        throw new Exception(e);
                    }
                }
                // SORTING LIST
                List<Integer> sorted = Sorting.merge_sort(unsorted);

                for (Integer element : sorted){
                    for_print.add(Integer.toString(element));
                }

            } else {
                List<String> unsorted = new ArrayList<>();
                for (String inputFile: inputFiles) {
                    try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile), StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!(line.contains(" "))){
                                unsorted.add(line);
                            }
                        }
                    } catch (java.nio.file.NoSuchFileException e) {
                        System.out.println("Файл не найден, прерываю обработку файлов. Файл: " + inputFile);
                        break;
                    } catch (Exception e) {
                        throw new Exception(e);
                    }
                }
                // SORTING LIST
                List<String> sorted = Sorting.merge_sort(unsorted);

                for_print.addAll(sorted);
            }

            if (!sort_direction) {
                Collections.reverse(for_print);
                Files.write(outputFile, for_print, StandardCharsets.UTF_8); // WRITING FILE
            } else {
                Files.write(outputFile, for_print, StandardCharsets.UTF_8); // WRITING FILE
            }
            System.out.println("Сортировка окончена. Выходной файл: " + outputStr);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java Main [flags] [out.txt] [in1.txt [in2.txt [..]]]", options);
        }
    }
}
