/* Descricao: Esta classe serve para calcular as apostas de cada jogador
 * Esta classe apenas funciona para o modo de texto
 * Pagar as blinds
 * Fazer apostas
 * Fazer check
 * Fazer fold
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 19/04/2017
 */

import java.util.Random;
import java.util.Scanner;

public class Apostas {

	// Escolhe o tipo de simulador
	Simulador computador = new Simulador();
	// Guarda a probabilidade dos jogadores de ganharem a jogada
	float[] percentagemVitoria = new float[2];
	// Guarda o dinheiro do pote temperatura 
	float tempPote=0;
	// Recebe o modo de jogo pretendido
	int modoJogo;
	// Recebe o numero da ronda de apostas
	int ronda;
	// Dealer
	int dealer;
	// Recebe as cartas dos jogador
	Cartas[][] cartasJogadores = new Cartas[2][2];
	// Recebe as cartas da mesa
	Cartas[] mesa = new Cartas[5];
	// Recebe a mão dos jogadores
	Cartas[][] maoJogadores = new Cartas[2][5];
	
	// Usado para simular um jogador de poker
	// 0 - opcão de aposta
	// 1 - valor da aposta (usado apenas em apostaValor)
	int[] opcao = new int[2];

	private int smallBlind = 10, 	// Valor da small blind
			bigBlind = 20,			// Valor da big blind 
			pote = 2;				// Posicao onde fica guardado o pote em fichas
	
	// Se um jogador fez a aposta inicial
	private boolean apostaInicial = true;
	// Se um jogador fez o check inicial
	private boolean checkInicial = false;			
	
	/* Apostas realizadas pelos jogadores
	 * 0 - Aposta do jogador 1
	 * 1 - Aposta do jogador 2
	 */
	private float[] aposta = new float[2];
	
	/* 0 - Fichas do jogador 1
	 * 1 - Fichas do jogador 2 
	 * 2 - Fichas no pote
	 * 3 - Define se jogada continua ou nao
	 */
	private float[] fichas = new float[4];
	
	/* Confirmacao que pode passar a proxima fase
	 * Passa a proxima fase se ambos os jogadores confirmarem (ambos com 1 )
	 * 0 - confirmacao do jogador 1
	 * 1 - confirmacao do jogador 2
	 */
	private int[] confirmacao = new int[2];
	
	// Guarda o valor das fichas de ambos os jogadores
	// É usado apenas para apostar blinds
	private float[] fichasIniciais = new float[2];
	
	// Recebe o input do utilizador
	Scanner input = new Scanner(System.in);
	
	// Construtor vazio
	Apostas(){}
	
	// Este método apenas faz reset aos valores das variavéis
	public void reset()
	{
		aposta[0] = 0;
		aposta[1] = 0;
		confirmacao[0] = 0;
		confirmacao[1] = 0;
		fichas[pote] = 0;
		fichas[3] = 0;
	}
	
	// Método para realizar as apostas antes do flop
	public float[] apostasPreFlop(float[] fichas, int dealer, float[] preProbabilidades, int modoJogo, Cartas[][] cartasJogador)
	{
		// Obter as cartas dos jogadores
		this.cartasJogadores = cartasJogador;
		
		// Ronda inical
		this.ronda = 0;
		// Guardar o dealer
		this.dealer = dealer;
		
		// Obter as percentagens 
		percentagemVitoria[0] = (float) preProbabilidades[0];
		percentagemVitoria[1] = (float) preProbabilidades[1];
		
		// Inicializar as variaveis com o valor 0
		reset();
		
		// Aposta da smallblind
		apostaInicial = true;
		
		// Fichas iniciais de cada jogador
		fichasIniciais[0] = fichas[0];
		fichasIniciais[1] = fichas[1];
		
		// Obter modo de jogo
		this.modoJogo = modoJogo;
		
		this.fichas = fichas;
		int blind;
		
		// Se o jogador 1 for o dealer então o jogador 2 é a big blind e vice-versa
		blind = (dealer == 0) ? 1 : 0;
		
		// Se a blind não tiver dinheiro para pagar a bigBlind então faz all in automa	ticamente
		if(fichas[blind] <= bigBlind)
		{
			// Se o número de fichas da blind for menor que a smallblind então fazem ambos allin
			if(fichas[blind] <= smallBlind)
			{
				// A blind é obrigado a apostar o dinheiro todo
				aposta[blind] = fichas[blind];
				// A blind paga o all in
				fichas[blind] -= aposta[blind];
				
				// O dealer apenas paga o all in da blind que é menor que a small blind
				fichas[dealer] -=aposta[blind];
				
				// O pote é duas vezes o valor do all in da blind
				fichas[pote] = 2*aposta[blind];
				
				// Confirma-se a jogada do dois jogadores
				confirmacao[0] = 1;
				confirmacao[1] = 1;
				
				return fichas;
			}
			// Se for maior que a small blind então o dealer tem de pagar a small blind e 
			//tem a opcao de fazer fold ou call
			else
			{
				// Dealer tem que apostar a smallBlind
				aposta[dealer] = smallBlind; 
				// O dealer paga a small blind
				fichas[dealer] -= aposta[dealer];
				
				// Calcular o pote
				fichas[pote] = aposta[dealer]+aposta[blind];
				
				//Não conta como jogada inicial
				apostaInicial = false;
				// A blind tem que fazer allin
				allIn(blind);
				
				return fichas;
			}
		}
		// Se o dealer náo tiver dinheiro para pagar a smallBlind faz all automaticamente
		if(fichas[dealer] <= smallBlind)
		{
			// O dealer é obrigado a apostar o dinheiro todo
			aposta[dealer] = fichas[dealer];
			// O dealer paga o all in
			fichas[dealer] -= aposta[dealer];
			
			// A blind apenas paga o all in do dealer que é menor que a blind
			fichas[blind] -=aposta[dealer];
			
			// O pote é duas vezes o valor do all in do dealer
			fichas[pote] = 2*aposta[dealer];
			
			// Confirma-se a jogada do dois jogadores
			confirmacao[0] = 1;
			confirmacao[1] = 1;
			
			return fichas;
			
		}
		// Se o dealer e a blind tiverem dinheiro para pagar a small e big blind
		if(fichas[blind] > bigBlind && fichas[dealer] > smallBlind) 
		{
			// O dealer aposta a smallBlind
			aposta[dealer] = smallBlind; 
			// A blind aposta a big blind
			aposta[blind] = bigBlind;
			
			// O dealer paga a small blind
			fichas[dealer] -= aposta[dealer];
			
			// A blind paga a bigBlind 
			fichas[blind] -= aposta[blind];
			
			// Calcular o pote
			fichas[pote] = aposta[dealer]+aposta[blind];
			
			//A small blind tem que fazer fold, call ou apostar
			menuFold_Call(dealer);
		}
		
		/* Condição se não existir confirmação de ambos os jogadores */
		if(confirmacao[0] != 1 && confirmacao[1] != 1 )
			System.out.println("Erro nas Apostas");
		
		return fichas;
	}
	
	// Método para realizar as apostas após o flop
	public float[] apostas(float[] fichas, int dealer, float[] probabilidades, int modoJogo, int ronda,Cartas[] mesa, Cartas[][] maoJogador)
	{
		// Obter as cartas da mesa
		this.mesa = mesa;
		
		// Obter a mão dos jogadores
		this.maoJogadores = maoJogador;
		
		// Receber ronda de apostas
		this.ronda = ronda;
		
		// Guardar o dealer
		this.dealer = dealer;
				
		// Guarda o valor do pote 
		tempPote = fichas[pote];
		
		// Obter as percentagens de ambos os jogadores
		percentagemVitoria[0] = probabilidades[0];
		percentagemVitoria[1] = probabilidades[1];
		
		// Inicializar as variaveis com o valor 0
		reset();
		
		// Não existe aposta inicial
		apostaInicial = false;
		// Se o primeiro jogador fizer check ou outro ainda têm que responder
		checkInicial = true;	
		
		// Usado apenas para apostar o valor de uma blind
		fichasIniciais[0] = fichas[0];
		fichasIniciais[1] = fichas[1];
		
		// Obter modo de jogo
		this.modoJogo = modoJogo;
		
		this.fichas = fichas;
		int jogador;
		
		// Se o jogador 1 for o dealer então o jogador 2 joga primeiro e vice-versa
		jogador = (dealer == 0) ? 1 : 0;
		
		//O jogador pode fazer check ou apostar
		menuCheck(jogador);
		 
		// Atualizar o valor do pote
		fichas[pote]+=tempPote;
		tempPote=0;
		
		/* Ciclo enquanto nao existir confirmacao de ambos os jogadores */
		if(confirmacao[0] != 1 && confirmacao[1] != 1 )
			System.out.println("Erro nas Apostas");

		return fichas;
	}
	
	/* Opcoes nas apostas:
	 * Fold - O jogador desiste
	 * Call - Iguala a aposta do adversário
	 * Aposta blind - Faz uma aposta no valor da blind
	 * Aposta valor - Faz uma aposta num valor definido pelo utilizador
	 * All-in - Aposta o dinheiro todo
	 */
	public void menuFold_Call(int jogador)
	{
		// Mostra o menu apenas quando utilizador jogar
		if(modoJogo == 1 || (modoJogo == 2 && jogador == 0) || (modoJogo == 3 && jogador == 0)
				|| (modoJogo == 4 && jogador == 0) || (modoJogo == 5 && jogador == 0))
		{
			System.out.println("Jogador " + (jogador+1) + " defina a sua aposta: ");
			System.out.println("1 - Fold ");
			System.out.println("2 - Call ");
			System.out.println("3 - Apostas blind");
			System.out.println("4 - Apostar valor");
			System.out.println("5 - All in");
		}
		
		opcao = modoJogo(jogador,modoJogo,5);

		switch(opcao[0])
		{
			case 1: 
				fold(jogador);
				break;
			case 2: 
				call(jogador);
				break;
			case 3: 
				apostaBlind(jogador);
				break;
			case 4: 
				apostaValor(jogador);
				break;
			case 5: 
				allIn(jogador);
				break;
			default:
				System.out.println("Opcao Errada - Volte a escolher novamente");
				menuFold_Call(jogador);
		}
		

		/* Se o dealer fez call na primeira aposta 
		então mostra o menuCheck ao oponente */
		if (opcao[0] == 2 && apostaInicial == true)
		{
			// Trocar de jogador
			jogador = (jogador == 0) ? 1 : 0;	
			
			// Já não conta como jogada inicial
			apostaInicial = false;
			
			// Chamar menu com check
			menuCheck(jogador);
		}
		
	}

	/* Opcoes nas apostas:
	 * Fold - O jogador desiste
	 * All-in - Aposta o dinheiro todo
	 */
	public void menuFold_Allin(int jogador)
	{
		// Mostra o menu apenas quando utilizador jogar
		if(modoJogo == 1 || (modoJogo == 2 && jogador == 0) || (modoJogo == 3 && jogador == 0)
				|| (modoJogo == 4 && jogador == 0) || (modoJogo == 5 && jogador == 0))
		{
			System.out.println("Jogador " + (jogador+1) + " defina a sua aposta: ");
			System.out.println("1 - Fold ");
			System.out.println("2 - Call");
		}
		
		opcao = modoJogo(jogador,modoJogo,2);
		
		switch(opcao[0])
		{
			/* Em check o jogador apenas confirma e a jogada continua */
			case 1: 
				fold(jogador);
				break;
			case 2: 
				call(jogador);
				break;
			default:
				System.out.println("Opcao Errada - Volte a escolher novamente");
				menuFold_Allin(jogador);		
		}	
	}
	
	/* Opcoes nas apostas:
	 * Check - As apostas dos dois jogadores estão iguais e passa à proxima fase
	 * Aposta blind - Faz uma aposta no valor da blind
	 * Aposta valor - Faz uma aposta num valor definido pelo utilizador
	 * All-in - Aposta o dinheiro todo
	 */
	public void menuCheck(int jogador)
	{	
		// Mostra o menu apenas quando utilizador jogar
		if(modoJogo == 1 || (modoJogo == 2 && jogador == 0) || (modoJogo == 3 && jogador == 0)
				|| (modoJogo == 4 && jogador == 0) || (modoJogo == 5 && jogador == 0))
		{
			System.out.println("Jogador " + (jogador+1) + " defina a sua aposta: ");
			System.out.println("1 - Check ");
			System.out.println("2 - Apostas blind");
			System.out.println("3 - Apostar valor");
			System.out.println("4 - All in");
		}
		
		opcao = modoJogo(jogador,modoJogo,4);
		
		switch(opcao[0])
		{
			// Em check o jogador apenas confirma e a jogada continua 
			case 1:
				// O jogador confirma a sua jogada
				confirmacao[jogador] = 1;
				System.out.println("O jogador " + (jogador+1) + " fez check.");
				// Se foi o primeiro check então o oponente então ainda fala
				if(checkInicial == true && apostaInicial == false)
				{
					checkInicial = false;
					
					// Troca de jogador
					jogador = (jogador == 0) ? 1 : 0;
					//Volta a chamar este menu com o novo jogador
					menuCheck(jogador);
				}		
				break;
			case 2: 
				apostaBlind(jogador);
				break;
			case 3: 
				apostaValor(jogador);
				break;	
			case 4: 
				allIn(jogador);
				break;
			default:
				System.out.println("Opcao Errada - Volte a escolher novamente");
				menuCheck(jogador);
		}	
	}

	/* Método quando um jogador faz fold
	 * Se um jogador fizer fold o outro ganha o pote e termina a jogada
	 */
	public void fold(int jogador)
	{
		
		System.out.println("O jogador " + (jogador+1) + " fez fold");
		
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		// Jogador fez fold
		// Oponente ganha o pote
		fichas[oponente] += fichas[pote] + tempPote;
		System.out.println("O jogador " + (oponente+1) + " ganha " + (fichas[pote]+tempPote));
		
		// O ciclo de apostas termina
		confirmacao[0] = 1;
		confirmacao[1] = 1;
		// A jogada terminha
		fichas[3] = 1;
		
		// O pote faz reset 
		fichas[pote] = 0;
	}

	/* Método quando um jogador iguala a aposta do adversário 
	 * Se um jogador fizer call à aposta do adversário 
	 * a ronda de apostas termina e passa-se à proxima fase
	 * */
	public void call(int jogador)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		// Guarda a aposta antiga
		float tempAposta;
		
		// Se o call for maior que o dinheiro do jogador então jogador faz all-in 
		if((aposta[oponente] - aposta[jogador]) >= fichas[jogador] )
		{
			// temp fica com a aposta antiga
			tempAposta = aposta[jogador];
			
			// jogador aposta as fichas todas
			aposta[jogador] = fichas[jogador];
			
			System.out.println("O jogador " + (jogador+1) + " fez All in com (" + aposta[jogador] + ")");
		
			// Jogador paga a aposta
			fichas[jogador] -= aposta[jogador];
			
			// Atualizar o valor da aposta do jogador
			aposta[jogador] += tempAposta;
			
			// Apenas desconta do oponente o dinheiro do jogador
			fichas[oponente] = (fichas[oponente] + aposta[oponente]) - aposta[jogador];
			
			// O valor do pote é atualizado para 2 vezes a aposta do jogador
			fichas[pote]= aposta[jogador]*2;	
		}
		else
		{
			// Guardar a aposta antiga
			tempAposta = aposta[jogador];
			
			// A aposta é a diferenca da aposta antiga com a aposta do oponente
			aposta[jogador] = aposta[oponente] - aposta[jogador];
			
			// Jogador fez call
			System.out.println("O jogador " + (jogador+1) + " fez Call (" + aposta[jogador] + ")");
			
			// Atualizar as fichas do jogador
			fichas[jogador] -= aposta[jogador];
			
			// Atualizar o valor da aposta do jogador
			aposta[jogador] += tempAposta;
			
			// Atualizar o valor do pote
			fichas[pote]= aposta[jogador] + aposta[oponente];	
					
		}
		
		checkInicial = false;
		
		// O ciclo de apostas termina
		confirmacao[jogador] = 1;
		// A jogada continua
		fichas[3] = 0;
	}
	
	/* Método para fazer uma aposta no valor da big blind */
	public void apostaBlind(int jogador)
	{	
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		//flag é ativa se entrar num if
		int flag = 0;
		
		// Se for a ronda incial a aposta é de smallBlind + bigBlind
		if(apostaInicial == true && (bigBlind+smallBlind) < fichas[jogador] )
		{
			// Jogador faz uma aposta no valor da blind
			System.out.println("O jogador " + (jogador+1) + " fez uma aposta no valor de " + (bigBlind+smallBlind));
			
			// Guardar a aposta antiga
			float tempAposta = aposta[jogador];
			
			// Jogador aposta a smallblind obrigatoria e bigBlind
			aposta[jogador] = (bigBlind+smallBlind); 
			
			// Jogador paga a aposta
			fichas[jogador] -= aposta[jogador];
			
			// Atualizar o valor da aposta
			aposta[jogador] += tempAposta; 
			
			// Atualizar o valor do pote
			fichas[pote] = aposta[jogador]+aposta[oponente];
			
			flag=1;
		}
		// Se não for a jogada incial e o jogador tiver dinheiro para apostar uma blind 
		if(apostaInicial == false && 2*bigBlind <= fichas[jogador] && flag ==0 )
		{
			
			// Jogador faz uma aposta no valor da blind
			System.out.println("O jogador " + (jogador+1) + " fez uma aposta no valor de " + (int)(bigBlind+aposta[oponente]));
			
			// Jogador aposta a smallblind obrigatoria e bigBlind+smallBlind 
			aposta[jogador] = aposta[oponente] + bigBlind; 
			
			// Jogador paga a aposta
			fichas[jogador] = fichasIniciais[jogador] - aposta[jogador];
				
			// Atualizar o valor do pote
			fichas[pote] = aposta[jogador]+aposta[oponente];
			
			flag = 1;
		
		}
		// Se a aposta de uma blind  for maior que as fichas do jogador entao jogador faz all in
		else if((bigBlind*2) > fichas[jogador] && flag ==0)
		{
			allIn(jogador);
			return;
		}
		
		// Jogador confirma mas oponente tem que responder
		confirmacao[jogador] = 1;
		confirmacao[oponente] = 0;
		
		// Já não conta como ronda inicial
		apostaInicial=false;
		
		// Trocar de jogador
		jogador = (jogador == 0) ? 1 : 0;
		
		// Chamar menu fold e call
		if(fichas[jogador] > 0) 
			menuFold_Call(jogador);	
		else
			allIn(jogador);
	}
	
	/* Método para fazer uma aposta no valor da big blind */
	public void apostaValor(int jogador)
	{
		
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		// Pedir ao utilizador para introduzir o valor da aposta
		if(fichas[jogador] < (2*bigBlind))
			opcao[1] = (int)fichas[jogador];	
		else
		{
			if((modoJogo == 1) || (modoJogo == 2 && jogador == 0) || (modoJogo == 3 && jogador == 0) 
					|| (modoJogo == 4 && jogador == 0) || (modoJogo == 5 && jogador == 0))
			{
				System.out.println("Escolha um valor entre " + (2*bigBlind) + " e " +  fichas[jogador]);
				opcao[1] = input.nextInt();
				// Valor tem ser 2 vezes o valor da big blind e menor que o numero das fichas do jogador
				while(opcao[1] < (2*bigBlind) || opcao[1] >= fichas[jogador])
				{
					System.out.println("(Valor invalido!) "
							+ "Escolha um valor entre " + (2*bigBlind) + "e " +  fichas[jogador]);
					opcao[1] = input.nextInt();
				}
				
			}
		}
		
		// Guarda o valor antigo da aposta
		float tempAposta = aposta[jogador];
		
		// Aposta definida pelo jogador
		aposta[jogador] = opcao[1]; 

		System.out.println("O jogador " + (jogador+1) + " fez uma aposta no valor de " 
				+ opcao[1]);
		
		// Jogador paga a aposta
		fichas[jogador] -= aposta[jogador];
		
		aposta[jogador] += tempAposta;
		
		// Atualizar o valor do pote
		fichas[pote] = aposta[jogador]+ aposta[oponente];
		
		// Jogador confirma mas oponente tem que responder
		confirmacao[jogador] = 1;
		confirmacao[oponente] = 0;
		
		// Já não conta como ronda inicial
		apostaInicial=false;
		
		// Trocar de jogador
		jogador = (jogador == 0) ? 1 : 0;
		
		// Se o oponente não tiver fichas para cobrir a aposta tem que fazer fold ou all in
		if(fichas[jogador] <=  opcao[1])
			menuFold_Allin(jogador);
		else
			menuFold_Call(jogador);
	}
	
	/* Método para aposta o valor total do jogador */
	public void allIn(int jogador)
	{

		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		// Guarda o valor da aposta antiga
		float tempAposta = aposta[jogador];
		
		// Aposta as fichas do jogador mais a aposta antiga
		aposta[jogador] = fichas[jogador] + tempAposta;
		
		System.out.println("O jogador " + (jogador+1) + " fez all in com " 
				+ aposta[jogador]);
		
		// Jogador paga a aposta
		fichas[jogador] -= (aposta[jogador]-tempAposta);
		
		// Atualizar as fichas do pote
		fichas[pote] = (aposta[jogador]+ aposta[oponente]);
		
		// Jogador confirma mas oponente tem que responder
		confirmacao[jogador] = 1;
		confirmacao[oponente] = 0;
		
		// Já não conta como ronda inicial
		apostaInicial=false;
		
		// Trocar de jogador
		jogador = (jogador == 0) ? 1 : 0;
		
		// Se o oponente ainda tiver fichas
		if(fichas[jogador] > 0)
			menuFold_Allin(jogador);
	}
	
	// Retorna a opcão consoante o modo de jogo
	public int[] modoJogo(int jogador, int modoJogo, int menu)
	{
		switch(modoJogo)
		{
			// Jogador Vs Jogador 
			case 1:
				opcao[0] = input.nextInt();
				break;
			// Jogador VS Computador (bot com apostas certas)
			case 2:
				if(jogador == 0)
					opcao[0] = input.nextInt();
				else
					opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				break;
			// Jogador VS Computador (bot agressivo)
			case 3:
				if(jogador == 0)
					opcao[0] = input.nextInt();
				else
					opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				break;
			// Jogador VS Computador (bot misto)
			case 4:
				if(jogador == 0)
					opcao[0] = input.nextInt();
				else
					{
					Random botRandom = new Random();
					
					// Escolhe o bot
					int bot= botRandom.nextInt((1-0)+1) + 0;
					
					if(bot == 0)
						opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
					else
						opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
					}
				break;
			// Jogador VS Computador (Baseado em probabilidades)
			case 5:
				if(jogador == 0)
					opcao[0] = input.nextInt();
				else
					opcao = computador.MFS(jogador, fichas,tempPote,percentagemVitoria,aposta,menu);
				break;
			// Computador VS Computador (certo vs agressivo)
			case 6:
				if(jogador == 0)
					opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				else
					opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				break;
			// Computador VS Computador (certo vs misto)
			case 7:
				if(jogador == 0)
					opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				else
				{
					Random botRandom = new Random();
					
					// Escolhe o bot
					int bot= botRandom.nextInt((1-0)+1) + 0;
					
					if(bot == 0)
						opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
					else
						opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				}
				break;
			// Computador VS Computador (certo vs baseado em probabilidades)
			case 8:
				if(jogador == 0)
					opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				else
					opcao = computador.MFS(jogador, fichas,tempPote,percentagemVitoria,aposta,menu);
				break;
			// Computador VS Computador (agressivo vs misto)
			case 9:
				if(jogador == 0)
					opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				else
				{
					Random botRandom = new Random();
					
					// Escolhe o bot
					int bot= botRandom.nextInt((1-0)+1) + 0;
					
					if(bot == 0)
						opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
					else
						opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				}
				break;
			// Computador VS Computador (agressivo vs baseado em probabilidades)
			case 10:
				if(jogador == 0)
					opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
							menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				else
					opcao = computador.MFS(jogador, fichas,tempPote,percentagemVitoria,aposta,menu);
				break;
			// Computador VS Computador (misto vs baseado em probabilidades)
			case 11:
				if(jogador == 0)
				{
					Random botRandom = new Random();
					
					// Escolhe o bot
					int bot= botRandom.nextInt((1-0)+1) + 0;
					
					if(bot == 0)
						opcao = computador.bot_certo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
					else
						opcao = computador.bot_agressivo(jogador, dealer, fichas, aposta, tempPote, ronda, 
								menu,cartasJogadores[jogador],mesa, percentagemVitoria);
				}
				else
					opcao = computador.MFS(jogador, fichas,tempPote,percentagemVitoria,aposta,menu);
				break;
			
		}
		return opcao;
	}
	
}
