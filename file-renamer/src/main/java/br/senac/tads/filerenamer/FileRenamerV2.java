package br.senac.tads.filerenamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileRenamerV2 {

    public static void main(String[] args) {
//        if (args == null || args.length == 0) {
//            System.out.println(
//                    """
//                FileRenamer
//                    Objetivo: Renomear os arquivos de atividades baixados do Blackboard com o nome do aluno indicado no arquivo .txt
//                    Uso: java -jar file-renamer-1.0.0.jar [diretório-absoluto]
//                    Onde: [diretório-absoluto] => Diretório onde os arquivos baixados do Blackboard foram descompactados
//                """);
//            System.exit(0);
//            return;
//        }
//        String directory = args[0];

        String directory = "C:\\senac\\2023-2\\turmaa\\ADO3";
        Path directoryPath = Paths.get(directory);

        if (!Files.exists(directoryPath)) {
            System.err.println(MessageFormat.format("O caminho \"{0}\" informado não existe", directory));
            System.exit(0);
            return;
        }

        if (!Files.isDirectory(directoryPath)) {
            System.err.println(MessageFormat.format("O caminho \"{0}\" informado não é um diretório", directory));
            System.exit(0);
            return;
        }

        System.out.println(MessageFormat.format("Renomeando arquivos do caminho \"{0}\"...", directory));
        Pattern patt = Pattern.compile("Nome: (.+) \\(");

        Map<String, String> mapFileAndName = new LinkedHashMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.filter(p1 -> Files.isRegularFile(p1) && p1.getFileName().toString().endsWith(".txt"))
                    .forEach(p1 -> {

                        Path originalFile = p1.getName(p1.getNameCount() - 1);
                        String[] fileParts =  originalFile.toString().split("\\.");
                        String originalFileName = null;
                        if (fileParts.length <= 2) {
                            originalFileName = fileParts[0];
                        } else {
                            originalFileName = String.join(".", Arrays.copyOfRange(fileParts, 0, fileParts.length - 1));
                        }

                        // System.out.println(originalFileName);
                        String personName = null;
                        try (BufferedReader br = new BufferedReader(new FileReader(p1.toFile()))) {
                            String st;
                            while ((st = br.readLine()) != null) {
                                Matcher matcher = patt.matcher(st);
                                if (matcher.find() && matcher.groupCount() > 0) {
                                    personName = matcher.group(1);
                                    mapFileAndName.put(originalFileName, personName);
                                    break;
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (Entry<String, String> entry : mapFileAndName.entrySet()) {
            try (Stream<Path> paths2 = Files.walk(Paths.get(directory))) {
                paths2.filter(p2 -> Files.isRegularFile(p2) && p2.getName(p2.getNameCount() - 1).toString().startsWith(entry.getKey()))
                        .forEach(p2 -> {
                            String[] fileParts = p2.getFileName().toString().split("\\.");
                            String fileExtension = fileParts[fileParts.length - 1].toLowerCase();
                            String fileName;
                            Path newPath;
                            int i = 0;
                            do {
                                i++;
                                if (i < 2) {
                                    fileName = entry.getValue().replaceAll("\\s+", "-");
                                } else {
                                     fileName = entry.getValue().replaceAll("\\s+", "-") + "_" + i;
                                }
                                newPath = Paths.get(directory, fileName + "." + fileExtension);
                            } while (Files.exists(newPath));

                            //System.out.println(newPath.toString());
                            try {
                                Files.move(p2, newPath, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Feito");
    }

}
