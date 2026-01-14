package ManipulacaodeArquivos;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Revisao {

    static class Pessoa {
        String telefone;
        String identidade;
        String Endereco;

        Pessoa(String telefone, String identidade, String Endereco) {
            this.telefone = telefone;
            this.identidade = identidade;
            this.Endereco = Endereco;
        }
    }

    private static final Map<String, Pessoa> contatos = new HashMap<>();
    private static final String ARQUIVO = "contatos.txt";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
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
            input.nextLine();

            switch (opcao) {
                case 1 -> cadastrarContato(input);
                case 2 -> buscarContato(input);
                case 3 -> listarContatos();
                case 4 -> removerContato(input);
                case 5 -> salvarContatos();
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 5);

        input.close();
    }

    // ================= ARQUIVOS =================

    private static void carregarContatos() {
        try {
            File file = new File(ARQUIVO);
            if (!file.exists()) return;

            Scanner leitor = new Scanner(file);
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                String[] dados = linha.split(";");
                if (dados.length == 4) {
                    contatos.put(dados[0], new Pessoa(dados[1], dados[2], dados[3]));
                }
            }
            leitor.close();
        } catch (Exception e) {
            System.out.println("Erro ao carregar contatos.");
        }
    }

    private static void salvarContatos() {
        try {
            FileWriter escritor = new FileWriter(ARQUIVO);
            for (Map.Entry<String, Pessoa> entry : contatos.entrySet()) {
                Pessoa p = entry.getValue();
                escritor.write(entry.getKey() + ";" + p.telefone + ";" + p.identidade + ";" + p.Endereco + "\n");
            }
            escritor.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar contatos.");
        }
    }

    // ================= FUNÇÕES =================

    private static void cadastrarContato(Scanner input) {
        System.out.print("Digite o nome: ");
        String nome = input.nextLine().trim();

        if (nome.isEmpty() || contatos.containsKey(nome)) {
            System.out.println("Nome inválido ou já existente!");
            return;
        }

        System.out.print("Digite o telefone: ");
        String telefone = input.nextLine().trim();
        System.out.print("Digite a identidade: ");
        String identidade = input.nextLine().trim();
        System.out.print("Digite o nome da rua: ");
        String Endereco = input.nextLine().trim();

        contatos.put(nome, new Pessoa(telefone, identidade, Endereco));
        System.out.println("Contato cadastrado!");
        salvarContatos();
    }

    private static void buscarContato(Scanner input) {
        System.out.print("Digite o nome: ");
        String nome = input.nextLine();

        Pessoa p = contatos.get(nome);
        if (p != null) {
            System.out.println("Telefone: " + p.telefone);
            System.out.println("Identidade: " + p.identidade);
            System.out.println("Endereço: " + p.Endereco);
        } else {
            System.out.println("Contato não encontrado!");
        }
    }

    private static void listarContatos() {
        if (contatos.isEmpty()) {
            System.out.println("Agenda vazia!");
            return;
        }

        List<String> nomes = new ArrayList<>(contatos.keySet());
        Collections.sort(nomes);

        for (String nome : nomes) {
            Pessoa p = contatos.get(nome);
            System.out.println(nome + " - " + p.telefone + " - " + p.identidade + " - " + p.Endereco );
        }
    }

    private static void removerContato(Scanner input) {
        System.out.print("Digite o nome: ");
        String nome = input.nextLine();

        if (contatos.remove(nome) != null) {
            System.out.println("Contato removido!");
        } else {
            System.out.println("Contato não encontrado!");
        }
    }
}
