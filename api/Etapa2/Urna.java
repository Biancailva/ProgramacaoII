
import java.util.*;

public class UrnaEletronica {

    private static Map<Integer, String> candidatos = new HashMap<>();
    private static Map<Integer, Integer> votosCandidatos = new HashMap<>();
    private static List<String> logAuditoria = new ArrayList<>();
    private static Set<String> eleitoresVotaram = new HashSet<>();
    
    private static int votosNulos = 0;
    private static int votosBranco = 0;
    private static boolean votacaoAtiva = false;
    private static boolean candidatosCadastrados = false;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===== URNA ELETRÔNICA =====");
            System.out.println("1) Mesário cadastrar candidatos");
            System.out.println("2) Mesário iniciar/encerrar votação");
            System.out.println("3) Eleitor registrar voto");
            System.out.println("4) Mesário finalizar votação");
            System.out.println("5) Relatório final");
            System.out.println("6) Log de auditoria");
            System.out.println("7) Sair");
            System.out.print("Opção: ");
            opcao = input.nextInt();
            input.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarCandidatos(input);
                    break;
                case 2:
                    alternarVotacao(input);
                    break;
                case 3:
                    registrarVoto(input);
                    break;
                case 4:
                    finalizarVotacao(input);
                    break;
                case 5:
                    exibirRelatorioFinal();
                    break;
                case 6:
                    exibirLogAuditoria();
                    break;
                case 7:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 7);
    }

    public static void cadastrarCandidatos(Scanner input) {
        if (votacaoAtiva) {
            System.out.println("Não é possível cadastrar candidatos durante a votação!");
            return;
        }

        System.out.println("\n--- CADASTRO DE CANDIDATOS ---");
        System.out.print("Quantos candidatos deseja cadastrar? ");
        int quantidade = input.nextInt();
        input.nextLine();

        candidatos.clear();
        votosCandidatos.clear();

        for (int i = 0; i < quantidade; i++) {
            System.out.println("\nCandidato " + (i + 1) + ":");
            
            System.out.print("Número: ");
            int numero = input.nextInt();
            input.nextLine();
            
            if (candidatos.containsKey(numero)) {
                System.out.println("Número já cadastrado! Tente outro.");
                i--;
                continue;
            }
            
            System.out.print("Nome: ");
            String nome = input.nextLine();
            
            candidatos.put(numero, nome);
            votosCandidatos.put(numero, 0);
        }
        
        candidatosCadastrados = true;
        System.out.println("\nCadastro concluído! " + quantidade + " candidatos registrados.");
    }

    public static void alternarVotacao(Scanner input) {
        if (!candidatosCadastrados && !votacaoAtiva) {
            System.out.println("É necessário cadastrar candidatos antes de iniciar a votação!");
            return;
        }
        
        if (votacaoAtiva) {
            System.out.print("Deseja realmente encerrar a votação? (S/N): ");
            String resposta = input.nextLine();
            
            if (resposta.equalsIgnoreCase("S")) {
                votacaoAtiva = false;
                System.out.println("Votação encerrada temporariamente.");
            }
        } else {
            votacaoAtiva = true;
            System.out.println("Votação iniciada! Eleitores podem votar.");
        }
    }

    public static void registrarVoto(Scanner input) {
        if (!votacaoAtiva) {
            System.out.println("A votação não está ativa no momento!");
            return;
        }

        System.out.println("\n--- REGISTRAR VOTO ---");
        
        System.out.print("Identificação do eleitor (CPF ou matrícula): ");
        String idEleitor = input.nextLine();
        
        if (eleitoresVotaram.contains(idEleitor)) {
            System.out.println("Este eleitor já votou! Voto duplicado não permitido.");
            return;
        }
        
        System.out.println("\nOpções:");
        System.out.println("0 - Voto em Branco");
        System.out.println("-1 - Voto Nulo");
        System.out.println("Ou digite o número do candidato:");
        
        if (!candidatos.isEmpty()) {
            System.out.println("\nCandidatos disponíveis:");
            for (Map.Entry<Integer, String> entry : candidatos.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }
        
        System.out.print("\nSeu voto: ");
        int voto = input.nextInt();
        input.nextLine();
        
        String tipoVoto = "";
        
        if (voto == 0) {
            votosBranco++;
            tipoVoto = "BRANCO";
            System.out.println("Voto em branco registrado.");
        } else if (voto == -1) {
            votosNulos++;
            tipoVoto = "NULO";
            System.out.println("Voto nulo registrado.");
        } else if (candidatos.containsKey(voto)) {
            votosCandidatos.put(voto, votosCandidatos.get(voto) + 1);
            tipoVoto = candidatos.get(voto);
            System.out.println("Voto para " + tipoVoto + " registrado.");
        } else {
            votosNulos++;
            tipoVoto = "NULO (número inválido)";
            System.out.println("Número não encontrado. Voto registrado como nulo.");
        }
        
        eleitoresVotaram.add(idEleitor);
        
        String logEntry = "Eleitor: " + idEleitor + " | Voto: " + 
                         (tipoVoto.contains("BRANCO") || tipoVoto.contains("NULO") ? tipoVoto : voto + " - " + tipoVoto);
        logAuditoria.add(logEntry);
        
        System.out.println("Voto computado com sucesso!");
    }

    public static void finalizarVotacao(Scanner input) {
        if (!votacaoAtiva) {
            System.out.println("A votação já está encerrada!");
            return;
        }
        
        System.out.print("Deseja finalizar definitivamente a votação? (S/N): ");
        String resposta = input.nextLine();
        
        if (resposta.equalsIgnoreCase("S")) {
            votacaoAtiva = false;
            System.out.println("\nVOTACÃO FINALIZADA!");
            System.out.println("Total de eleitores que votaram: " + eleitoresVotaram.size());
            exibirRelatorioFinal();
        }
    }

    public static void exibirRelatorioFinal() {
        System.out.println("\n===== RELATÓRIO FINAL =====");
        
        int totalVotosValidos = 0;
        
        for (int votos : votosCandidatos.values()) {
            totalVotosValidos += votos;
        }
        
        int totalGeral = totalVotosValidos + votosBranco + votosNulos;
        
        if (totalGeral == 0) {
            System.out.println("Nenhum voto registrado ainda.");
            return;
        }
        
        System.out.println("Total de votos: " + totalGeral);
        System.out.println("Total de eleitores que votaram: " + eleitoresVotaram.size());
        System.out.println("\n--- RESULTADOS POR CATEGORIA ---");
        
        System.out.println("\nCANDIDATOS:");
        for (Map.Entry<Integer, String> entry : candidatos.entrySet()) {
            int numero = entry.getKey();
            String nome = entry.getValue();
            int votos = votosCandidatos.get(numero);
            double percentual = totalVotosValidos > 0 ? (votos * 100.0) / totalVotosValidos : 0;
            
            System.out.printf("%d - %s: %d votos (%.2f%% dos válidos)\n", 
                            numero, nome, votos, percentual);
        }
        
        double percentBranco = (votosBranco * 100.0) / totalGeral;
        double percentNulo = (votosNulos * 100.0) / totalGeral;
        
        System.out.printf("\nVotos em Branco: %d (%.2f%% do total)\n", votosBranco, percentBranco);
        System.out.printf("Votos Nulos: %d (%.2f%% do total)\n", votosNulos, percentNulo);
        
        System.out.println("\n--- VENCEDOR ---");
        
        if (totalVotosValidos == 0) {
            System.out.println("Nenhum voto válido registrado.");
            return;
        }
        
        int maiorVotos = 0;
        int candidatoVencedor = -1;
        List<Integer> candidatosSegundoTurno = new ArrayList<>();
        
        for (Map.Entry<Integer, Integer> entry : votosCandidatos.entrySet()) {
            int votos = entry.getValue();
            
            if (votos > maiorVotos) {
                maiorVotos = votos;
                candidatoVencedor = entry.getKey();
                candidatosSegundoTurno.clear();
                candidatosSegundoTurno.add(entry.getKey());
            } else if (votos == maiorVotos && votos > 0) {
                candidatosSegundoTurno.add(entry.getKey());
            }
        }
        
        if (candidatosSegundoTurno.size() > 1) {
            System.out.println("SEGUNDO TURNO NECESSÁRIO!");
            System.out.println("Empate entre os candidatos:");
            for (int num : candidatosSegundoTurno) {
                System.out.println(num + " - " + candidatos.get(num) + ": " + votosCandidatos.get(num) + " votos");
            }
            System.out.println("\nRegra: Segundo turno necessário quando há empate entre os mais votados.");
        } else if (candidatoVencedor != -1) {
            double percentualVencedor = (maiorVotos * 100.0) / totalVotosValidos;
            System.out.printf("VENCEDOR: %d - %s\n", candidatoVencedor, candidatos.get(candidatoVencedor));
            System.out.printf("Votos: %d (%.2f%% dos votos válidos)\n", maiorVotos, percentualVencedor);
        }
    }

    public static void exibirLogAuditoria() {
        System.out.println("\n===== LOG DE AUDITORIA =====");
        
        if (logAuditoria.isEmpty()) {
            System.out.println("Nenhum voto registrado ainda.");
            return;
        }
        
        System.out.println("Total de registros: " + logAuditoria.size());
        System.out.println("------------------------------");
        
        for (int i = 0; i < logAuditoria.size(); i++) {
            System.out.println((i + 1) + ") " + logAuditoria.get(i));
        }
    }

    // FUNÇÕES AUXILIARES
    private static void exibirCandidatos() {
        if (candidatos.isEmpty()) {
            System.out.println("Nenhum candidato cadastrado.");
            return;
        }
        
        System.out.println("\nCandidatos cadastrados:");
        for (Map.Entry<Integer, String> entry : candidatos.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}

