package br.senac.tads.filerenamer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileRenamerV4 {

    private static final Pattern NOME_PATTERN = Pattern.compile("Nome: (.+) \\(\\d+\\)");
    private static final Pattern DATA_PATTERN = Pattern.compile("Data do envio: (.+)");
    private static final Pattern ARQUIVO_ORIGINAL_PATTERN = Pattern.compile("Nome do arquivo original: (.+)");

    private static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final String EXCEL_FILE_PREFIX = "zz_entregas";
    private static final String EXCEL_FILE_EXTENSION = ".xlsx";

    public static void main(String[] args) {

        // INFORMAR O DIRETÓRIO PAI ONDE TEM OS DEMAIS ARQUIVOS DE ENTREGAS QUE SERÃO
        // DESCOMPACTADOS E ADICIONADOS COMO ABA NA PLANILHA DE ENTREGAS
        String parentDirectory = "E:\\senac\\25-2\\99-avaliacao-disciplina";
        Path parentDirectoryPath = Paths.get(parentDirectory);

        if (!Files.exists(parentDirectoryPath)) {
            System.err.println(MessageFormat.format("O caminho \"{0}\" informado não existe", parentDirectory));
            System.exit(0);
            return;
        }

        if (!Files.isDirectory(parentDirectoryPath)) {
            System.err.println(MessageFormat.format("O caminho \"{0}\" informado não é um diretório", parentDirectory));
            System.exit(0);
            return;
        }

        // ETAPA 1: Descompactar arquivos ZIP
        System.out.println(MessageFormat.format("Descompactando arquivos ZIP do caminho \"{0}\"...", parentDirectory));
        List<Path> diretoriosDescompactados = descompactarArquivosZip(parentDirectoryPath);

        if (diretoriosDescompactados.isEmpty()) {
            System.out.println("Nenhum arquivo ZIP encontrado para processar.");
            System.exit(0);
            return;
        }

        // ETAPA 2: Processar cada diretório descompactado
        Map<String, List<DadosAluno>> dadosPorDiretorio = new LinkedHashMap<>();

        for (Path diretorio : diretoriosDescompactados) {
            System.out.println(MessageFormat.format("\nProcessando diretório: {0}", diretorio.getFileName()));

            // Renomear arquivos com o nome do aluno
            System.out.println("  Renomeando arquivos...");
            renomearArquivos(diretorio.toString());

            // Extrair dados dos arquivos TXT
            System.out.println("  Extraindo dados...");
            List<DadosAluno> listaAlunos = extrairDadosDoDiretorio(diretorio);

            if (!listaAlunos.isEmpty()) {
                String nomeDiretorio = diretorio.getFileName().toString();
                dadosPorDiretorio.put(nomeDiretorio, listaAlunos);
                System.out.println(MessageFormat.format("  Extraídos {0} registros.", listaAlunos.size()));
            } else {
                System.out.println("  Nenhum arquivo de entrega encontrado.");
            }
        }

        // ETAPA 3: Gerar planilha Excel consolidada com múltiplas abas
        if (!dadosPorDiretorio.isEmpty()) {
            String excelFileName = gerarNomeArquivoExcel(parentDirectoryPath);
            String excelPath = parentDirectory + "\\" + excelFileName;
            gerarPlanilhaExcelMultiplasAbas(dadosPorDiretorio, excelPath);
            System.out.println(MessageFormat.format("\nPlanilha gerada: {0}", excelPath));
        } else {
            System.out.println("\nNenhum dado encontrado para gerar planilha.");
        }

        System.out.println("\nFeito");
    }

    private static List<Path> descompactarArquivosZip(Path parentDirectoryPath) {
        List<Path> diretoriosDescompactados = new ArrayList<>();

        try (Stream<Path> paths = Files.list(parentDirectoryPath)) {
            List<Path> arquivosZip = paths
                    .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().toLowerCase().endsWith(".zip"))
                    .collect(Collectors.toList());

            for (Path arquivoZip : arquivosZip) {
                String nomeZip = arquivoZip.getFileName().toString();
                String nomeDiretorio = nomeZip.substring(0, nomeZip.lastIndexOf('.'));
                Path diretorioDestino = parentDirectoryPath.resolve(nomeDiretorio);

                if (Files.exists(diretorioDestino)) {
                    System.out.println(MessageFormat.format("  Diretório já existe, ignorando: {0}", nomeDiretorio));
                    diretoriosDescompactados.add(diretorioDestino);
                    continue;
                }

                System.out.println(MessageFormat.format("  Descompactando: {0}", nomeZip));
                Files.createDirectories(diretorioDestino);

                try (ZipInputStream zis = new ZipInputStream(new FileInputStream(arquivoZip.toFile()))) {
                    ZipEntry entry;
                    while ((entry = zis.getNextEntry()) != null) {
                        Path entryPath = diretorioDestino.resolve(entry.getName());

                        if (entry.isDirectory()) {
                            Files.createDirectories(entryPath);
                        } else {
                            Files.createDirectories(entryPath.getParent());
                            Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                        zis.closeEntry();
                    }
                }

                diretoriosDescompactados.add(diretorioDestino);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return diretoriosDescompactados;
    }

    private static List<DadosAluno> extrairDadosDoDiretorio(Path directoryPath) {
        List<DadosAluno> listaAlunos = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(directoryPath)) {
            List<Path> arquivosTxt = paths
                    .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".txt"))
                    .filter(p -> !temSufixoNumerado(p.getFileName().toString()))
                    .collect(Collectors.toList());

            for (Path arquivoTxt : arquivosTxt) {
                DadosAluno dados = extrairDadosDoArquivo(arquivoTxt);
                if (dados != null) {
                    listaAlunos.add(dados);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return listaAlunos;
    }

    private static boolean temSufixoNumerado(String fileName) {
        String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        return Pattern.matches(".*_\\d+$", nameWithoutExt);
    }

    private static String gerarNomeArquivoExcel(Path directoryPath) {
        String nomeDiretorio = directoryPath.getFileName().toString();
        String sufixo = "_" + normalizarNome(nomeDiretorio);
        return EXCEL_FILE_PREFIX + sufixo + EXCEL_FILE_EXTENSION;
    }

    private static String normalizarNome(String nome) {
        String normalizado = Normalizer.normalize(nome, Normalizer.Form.NFD);
        normalizado = normalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        normalizado = normalizado.replaceAll("[^a-zA-Z0-9\\-_.]", "");
        return normalizado;
    }

    private static String normalizarNomeAba(String nome) {
        String normalizado = Normalizer.normalize(nome, Normalizer.Form.NFD);
        normalizado = normalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        // Troca espaços e hífens por underline
        normalizado = normalizado.replaceAll("[\\s\\-]+", "_");
        // Remove caracteres especiais, mantendo apenas letras, números e underlines
        normalizado = normalizado.replaceAll("[^a-zA-Z0-9_]", "");
        // Limita a 31 caracteres (limite do Excel para nomes de abas)
        if (normalizado.length() > 31) {
            normalizado = normalizado.substring(0, 31);
        }
        return normalizado;
    }

    private static void renomearArquivos(String directory) {
        Pattern patt = Pattern.compile("Nome: (.+) \\(");
        Map<String, String> mapFileAndName = new LinkedHashMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.filter(p1 -> Files.isRegularFile(p1) && p1.getFileName().toString().endsWith(".txt"))
                    .forEach(p1 -> {
                        Path originalFile = p1.getName(p1.getNameCount() - 1);
                        String[] fileParts = originalFile.toString().split("\\.");
                        String originalFileName;
                        if (fileParts.length <= 2) {
                            originalFileName = fileParts[0];
                        } else {
                            originalFileName = String.join(".", Arrays.copyOfRange(fileParts, 0, fileParts.length - 1));
                        }

                        try (BufferedReader br = new BufferedReader(new FileReader(p1.toFile()))) {
                            String st;
                            while ((st = br.readLine()) != null) {
                                Matcher matcher = patt.matcher(st);
                                if (matcher.find() && matcher.groupCount() > 0) {
                                    String personName = matcher.group(1);
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
    }

    private static DadosAluno extrairDadosDoArquivo(Path arquivoTxt) {
        DadosAluno dados = new DadosAluno();
        List<String> arquivosOriginais = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoTxt.toFile()))) {
            String linha;
            String secaoAtual = null;
            StringBuilder conteudoSecao = new StringBuilder();

            while ((linha = br.readLine()) != null) {
                Matcher nomeMatcher = NOME_PATTERN.matcher(linha);
                if (nomeMatcher.find()) {
                    dados.setNome(nomeMatcher.group(1));
                    continue;
                }

                Matcher dataMatcher = DATA_PATTERN.matcher(linha);
                if (dataMatcher.find()) {
                    String dataOriginal = dataMatcher.group(1);
                    dados.setDataEnvio(converterData(dataOriginal));
                    continue;
                }

                Matcher arquivoMatcher = ARQUIVO_ORIGINAL_PATTERN.matcher(linha);
                if (arquivoMatcher.find()) {
                    arquivosOriginais.add(arquivoMatcher.group(1));
                    continue;
                }

                if (linha.equals("Campo do envio:")) {
                    salvarSecao(dados, secaoAtual, conteudoSecao.toString());
                    secaoAtual = "campo_envio";
                    conteudoSecao = new StringBuilder();
                    continue;
                }

                if (linha.equals("Comentários:")) {
                    salvarSecao(dados, secaoAtual, conteudoSecao.toString());
                    secaoAtual = "comentarios";
                    conteudoSecao = new StringBuilder();
                    continue;
                }

                if (linha.equals("Arquivos:")) {
                    salvarSecao(dados, secaoAtual, conteudoSecao.toString());
                    secaoAtual = "arquivos";
                    conteudoSecao = new StringBuilder();
                    continue;
                }

                if (secaoAtual != null && !secaoAtual.equals("arquivos")) {
                    if (conteudoSecao.length() > 0) {
                        conteudoSecao.append("\n");
                    }
                    conteudoSecao.append(linha);
                }
            }

            salvarSecao(dados, secaoAtual, conteudoSecao.toString());
            dados.setArquivos(String.join(" | ", arquivosOriginais));

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return dados;
    }

    private static void salvarSecao(DadosAluno dados, String secao, String conteudo) {
        if (secao == null || conteudo == null) {
            return;
        }

        String textoLimpo = conteudo.trim();

        if (secao.equals("campo_envio")) {
            if (!textoLimpo.equals("Não há dados de texto de envio para este exercício.")) {
                dados.setCampoEnvio(textoLimpo);
            }
        } else if (secao.equals("comentarios")) {
            if (!textoLimpo.equals("Não há comentários de alunos para este exercício.")) {
                dados.setComentarios(textoLimpo);
            }
        }
    }

    private static String converterData(String dataOriginal) {
        try {
            String dataSemFuso = dataOriginal.replace(" BRT", "").replace(" BRST", "");

            Pattern datePattern = Pattern.compile(
                    "(\\w+-?\\w*), (\\d{1,2}) de (\\w+) de (\\d{4}) (\\d{2})h(\\d{2})min(\\d{2})s");
            Matcher matcher = datePattern.matcher(dataSemFuso);

            if (matcher.find()) {
                int dia = Integer.parseInt(matcher.group(2));
                String mesNome = matcher.group(3);
                int ano = Integer.parseInt(matcher.group(4));
                int hora = Integer.parseInt(matcher.group(5));
                int minuto = Integer.parseInt(matcher.group(6));
                int segundo = Integer.parseInt(matcher.group(7));

                int mes = converterMes(mesNome);

                LocalDateTime dateTime = LocalDateTime.of(ano, mes, dia, hora, minuto, segundo);
                return dateTime.format(OUTPUT_DATE_FORMATTER);
            }
        } catch (Exception ex) {
            System.err.println("Erro ao converter data: " + dataOriginal);
            ex.printStackTrace();
        }
        return dataOriginal;
    }

    private static int converterMes(String mesNome) {
        return switch (mesNome.toLowerCase()) {
            case "janeiro" -> 1;
            case "fevereiro" -> 2;
            case "março" -> 3;
            case "abril" -> 4;
            case "maio" -> 5;
            case "junho" -> 6;
            case "julho" -> 7;
            case "agosto" -> 8;
            case "setembro" -> 9;
            case "outubro" -> 10;
            case "novembro" -> 11;
            case "dezembro" -> 12;
            default -> 1;
        };
    }

    private static void gerarPlanilhaExcelMultiplasAbas(Map<String, List<DadosAluno>> dadosPorDiretorio, String caminhoArquivo) {
        // Apaga arquivo existente se houver
        Path arquivoExcel = Paths.get(caminhoArquivo);
        try {
            Files.deleteIfExists(arquivoExcel);
        } catch (IOException ex) {
            System.err.println("Erro ao apagar arquivo existente: " + caminhoArquivo);
            ex.printStackTrace();
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            String[] colunas = {"Nome", "Data de Envio", "Campo de Envio", "Comentários", "Arquivos"};

            for (Entry<String, List<DadosAluno>> entry : dadosPorDiretorio.entrySet()) {
                String nomeAba = normalizarNomeAba(entry.getKey());
                List<DadosAluno> listaAlunos = entry.getValue();

                Sheet sheet = workbook.createSheet(nomeAba);

                // Cria cabeçalho
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < colunas.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(colunas[i]);
                    cell.setCellStyle(headerStyle);
                }

                // Preenche dados
                int rowNum = 1;
                for (DadosAluno aluno : listaAlunos) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(aluno.getNome() != null ? aluno.getNome() : "");
                    row.createCell(1).setCellValue(aluno.getDataEnvio() != null ? aluno.getDataEnvio() : "");
                    row.createCell(2).setCellValue(aluno.getCampoEnvio() != null ? aluno.getCampoEnvio() : "");
                    row.createCell(3).setCellValue(aluno.getComentarios() != null ? aluno.getComentarios() : "");
                    row.createCell(4).setCellValue(aluno.getArquivos() != null ? aluno.getArquivos() : "");
                }

                // Ajusta largura das colunas
                for (int i = 0; i < colunas.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // Salva arquivo
            try (FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)) {
                workbook.write(fileOut);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Classe interna para armazenar dados do aluno
    static class DadosAluno {
        private String nome;
        private String dataEnvio;
        private String campoEnvio;
        private String comentarios;
        private String arquivos;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDataEnvio() {
            return dataEnvio;
        }

        public void setDataEnvio(String dataEnvio) {
            this.dataEnvio = dataEnvio;
        }

        public String getCampoEnvio() {
            return campoEnvio;
        }

        public void setCampoEnvio(String campoEnvio) {
            this.campoEnvio = campoEnvio;
        }

        public String getComentarios() {
            return comentarios;
        }

        public void setComentarios(String comentarios) {
            this.comentarios = comentarios;
        }

        public String getArquivos() {
            return arquivos;
        }

        public void setArquivos(String arquivos) {
            this.arquivos = arquivos;
        }
    }
}
