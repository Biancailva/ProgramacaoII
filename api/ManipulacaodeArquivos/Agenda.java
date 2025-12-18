package Etapa2;

import java.util.*;
import java.io.*;

public class Agenda {

    // chave = nome; valor = telefone
    private static Map<String, String> contatos = new HashMap<>();
    private static final String ARQUIVO = "contatos.txt";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // 🔹 Carregar contatos ao iniciar o sistema
        carregarContatos();

        int opcao;
        do {
            System.out.println("\n======= MENU =======");
            System.out.println("1 - Cadastrar contato");
            System.out.println("2 - Buscar contato");
            System.out.println("3 - Listar todos os contatos");
            System.out.println("4 - Remover contato");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = input.nextInt();
            input.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1:
                    cadastrarContato(input);
                    break;
                case 2:
                    buscarContato(input);
                    break;
                case 3:
                    listarContatos();
                    break;
                case 4:
                    removerContato(input);
                    break;
                case 5:
                    System.out.println("Salvando contatos...");
                    salvarContatos(); // 🔹 Salvar ao sair
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 5);

        input.close();
    }

    // ================= ARQUIVOS =================

    private static void carregarContatos() {
        File file = new File(ARQUIVO);

        if (!file.exists()) {
            return; // Se não existir, começa vazio
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 2) {
                    contatos.put(dados[0], dados[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar contatos!");
        }
    }

    private static void salvarContatos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Map.Entry<String, String> entry : contatos.entrySet()) {
                bw.write(entry.getKey() + ";" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar contatos!");
        }
    }

    // ================= FUNÇÕES =================

    public static void cadastrarContato(Scanner input) {
        System.out.print("Digite o nome: ");
        String nome = input.nextLine().trim();

        if (nome.isEmpty() || contatos.containsKey(nome)) {
            System.out.println("Nome inválido ou já existente!");
            return;
        }

        System.out.print("Digite o telefone: ");
        String telefone = input.nextLine().trim();

        if (telefone.isEmpty()) {
            System.out.println("Telefone inválido!");
            return;
        }

        contatos.put(nome, telefone);
        System.out.println("Contato cadastrado com sucesso!");
    }

    public static void buscarContato(Scanner input) {
        System.out.print("Digite o nome: ");
        String nome = input.nextLine();

        if (contatos.containsKey(nome)) {
            System.out.println("Telefone: " + contatos.get(nome));
        } else {
            System.out.println("Contato não encontrado!");
        }
    }

    public static void listarContatos() {
        if (contatos.isEmpty()) {
            System.out.println("Agenda vazia!");
            return;
        }

        List<String> nomes = new ArrayList<>(contatos.keySet());
        Collections.sort(nomes);

        for (String nome : nomes) {
            System.out.println(nome + " - " + contatos.get(nome));
        }
    }

    public static void removerContato(Scanner input) {
        System.out.print("Digite o nome: ");
        String nome = input.nextLine();

        if (contatos.remove(nome) != null) {
            System.out.println("Contato removido!");
        } else {
            System.out.println("Contato não encontrado!");
        }
    }
}
