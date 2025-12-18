package ManipulacaodeArquivos;
import java.util.*;

public class Prova {
    //-> chave = nome; valor = telefone
    private static Map<String, String> contatos = new HashMap<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int opcao;

        do{
            System.out.println("\n======= MENU =======");
            System.out.println("1 - Cadastrar contato");
            System.out.println("2 - Buscar contato");
            System.out.println("3 - Listar todos os contatos");
            System.out.println("4 - Remover contato");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = input.nextInt();
            input.nextLine(); // Limpar buffer

            switch (opcao){
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
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 5);

        input.close();
    }

    public static void cadastrarContato(Scanner input){
        System.out.println("\n===== Cadastrar Novo Contato =====");

        System.out.print("Digite o nome: ");
        String nome = input.nextLine().trim();

        // Validar se o nome não está vazio
        if (nome.isEmpty()) {
            System.out.println("ERRO: O nome não pode ser vazio!");
            return;
        }

        // Verificar se o nome já existe
        if (contatos.containsKey(nome)) {
            System.out.println("ERRO: Este nome já está cadastrado!");
            return;
        }

        System.out.print("Digite o telefone: ");
        String telefone = input.nextLine().trim();

        // Validar se o telefone não está vazio
        if (telefone.isEmpty()) {
            System.out.println("ERRO: O telefone não pode ser vazio!");
            return;
        }

        // Adicionar ao mapa
        contatos.put(nome, telefone);
        System.out.println("SUCESSO: Contato '" + nome + "' cadastrado!");
    }

    public static void buscarContato(Scanner input){
        System.out.println("\n===== Buscar Contato =====");
        System.out.print("Digite o nome para buscar: ");
        String nome = input.nextLine();

        if (nome.isEmpty()) {
            System.out.println("ERRO: O nome não pode ser vazio!");
            return;
        }

        // Verificar se o contato existe
        if (contatos.containsKey(nome)) {
            String telefone = contatos.get(nome);
            System.out.println("SUCESSO: Contato encontrado!");
            System.out.println("Nome: " + nome);
            System.out.println("Telefone: " + telefone);
        } else {
            System.out.println("ERRO: Contato '" + nome + "' não encontrado!");
        }
    }

    public static void listarContatos(){
        System.out.println("\n===== Lista de Contatos =====");

        if (contatos.isEmpty()) {
            System.out.println("A agenda está vazia!");
            return;
        }

        // Criar lista com os nomes para ordenar
        List<String> nomesOrdenados = new ArrayList<>(contatos.keySet());
        Collections.sort(nomesOrdenados); // Ordenar alfabeticamente

        System.out.println("Total de contatos: " + contatos.size());
        System.out.println("---------------------------");

        // Exibir contatos ordenados
        for (String nome : nomesOrdenados) {
            String telefone = contatos.get(nome);
            System.out.println("Nome: " + nome + " | Telefone: " + telefone);
        }
    }

    public static void removerContato(Scanner input){
        System.out.println("\n===== Remover Contato =====");
        System.out.print("Digite o nome do contato a remover: ");
        String nome = input.nextLine();

        if (nome.isEmpty()) {
            System.out.println("ERRO: O nome não pode ser vazio!");
            return;
        }

        if (contatos.containsKey(nome)) {
            contatos.remove(nome);
            System.out.println("SUCESSO: Contato '" + nome + "' removido!");
        } else {
            System.out.println("ERRO: Contato '" + nome + "' não encontrado!");
        }
    }
}

