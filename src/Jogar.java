/* Descricao: Esta classe define 3 tipos de jogo em modo de texto
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 20/05/2017
 */

import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class Jogar {

	// Distribuir as cartas pelos jogadores
	Dealer dealer = new Dealer();
	// Guarda as cartas de ambos os jogadores
	Cartas jogador[][] = new Cartas[2][2];
	// Guarda as mãos de ambos os jogadores
	Cartas maoJogador[][] = new Cartas[2][5];
	// Guarda as mâos temporariamente
	Cartas tempMao[][] = new Cartas[2][5];
	// Guarda as cartas da mesa
	Cartas mesa[] = new Cartas[5];
	// Avaliar as mãos dos jogadores
	AvaliarMao avaliar = new AvaliarMao();
	// Calcular o vencedor entre os jogadores
	CalcularVencedor calcular = new CalcularVencedor();
	// Guarda o resultado das mâos dos jogadores
	int resultado[] = new int[2];
	// Efetuar ronda de apostas
	Apostas apostar = new Apostas();
	//Calcular as probabilidades das mâos dos jogadores
	Probabilidades prob = new Probabilidades();
	// Guarda as probabilidades dos jogadores após o flop
	float[] probabilidades = new float[2];
	// Obtem as probabilidades após o flop
	float[] probTemp = new float[3];
	// Guardar as estatisticas do jogador
	Estatisticas guardar = new Estatisticas();
	// Guardar as estatisticas do oponente
	Estatisticas guardarOponente = new Estatisticas();
	
	// Construtor vazio
	Jogar(){}
	
	// Utilizador joga contra outro utilizador
	public void jogadorVSjogador(int modoJogo, String nome) throws IOException
	{

		// Fazer o upload das estatisticas do utilizador
		guardar.obterCartasJogadas(nome);
		guardar.obterCartasGanhas(nome);
		guardar.obterMaoAlta(nome);
		int jogadasGanhas = 0;
		
		Random dealerRandom = new Random();
		// Um jogador aleatório começa como dealer
		int posDealer= dealerRandom.nextInt((1-0)+1) + 0;
		
		// fichas[0] - jogador 1
		// fichas[1] - jogador 2
		// fichas[2] - pote
		// fichas[3] - opcao: 0 - O jogo continua 
		//							 1 - O jogo para
		float[] fichas = new float[4];	
		
		// Ambos os jogadores comecam com 1500 fichas
		fichas[0] = 1500; 
		fichas[1] = 1500;
		float tempFichas = 1500;
		
		boolean jogo = true;
		
		int i = 0;
		
		// Inicio do jogo
		while(jogo)
		{
			System.out.println();
			System.out.println("**************************************** Jogo nº " + (i+1) + "****************************************");
			i++;
			System.out.println();
			
			// Baralhar as cartas
			Baralho baralho = new Baralho();
			
			// Distribuir as duas cartas pelos jogadores
			dealer.darCartas(baralho, jogador[0], jogador[1]);

			// Adicionar Cartas
			guardar.cartasJogadas(jogador[0]);
			
			// Jogadores vêem as suas cartas
			mostrar_esconderCartas(jogador, posDealer);

			// Se ambos os jogadores ainda tiverem fichas
			if(fichas[0] > 0 && fichas[1] > 0)
				mostrarFichasJogadores(fichas);
			
			// Apostas antes do flop
			fichas = apostar.apostasPreFlop(fichas, posDealer, probabilidades,modoJogo,jogador);
			
			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;
				continue;
			}
			
			// Dar o flop 
			dealer.darFlop(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>> Flop: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + "<<<<<<<<");
			
			// Se algum dos jogador não tiver dinheiro mostra as cartas de ambos com as respectivas probabilidades
			if(fichas[0] == 0 || fichas[1] == 0)
			{
				probabilidades  = prob.probabilidadeJogadorvsJogadorFlop(jogador[0], jogador[1], mesa);
				mostrarJogadoresAllin(fichas, jogador, probabilidades);
			}
			else
				mostrarFichasJogadores(fichas);

			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,5);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,5);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do flop
				fichas = apostar.apostas(fichas, posDealer, probabilidades,modoJogo,1,mesa, tempMao);

			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;				
				continue;
			}
			
			// Dar o turn 
			dealer.darTurn(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>> Turn: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + " " + mesa[3] + "<<<<<<<<");
			
			// Se algum dos jogador não tiver dinheiro mostra as cartas de ambos
			if(fichas[0] == 0 || fichas[1] == 0)
			{
				probabilidades  = prob.probabilidadeJogadorvsJogadorTurn(jogador[0], jogador[1], mesa);			
				mostrarJogadoresAllin(fichas, jogador, probabilidades);
			}
			else
				mostrarFichasJogadores(fichas);

			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,6);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,6);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do turn
				fichas = apostar.apostas(fichas, posDealer, probabilidades, modoJogo, 2, mesa, tempMao);
			
			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;
				continue;
			}
			
			// Dar o river e avaliar o jogo dos 2 jogadores com 7 cartas
			dealer.darRiver(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>>>> River: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + " " + mesa[3]+ " " + mesa[4] + " <<<<<<<<" );
			
			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,7);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,7);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do river
				fichas = apostar.apostas(fichas, posDealer, probabilidades, modoJogo, 3, mesa, tempMao);

			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2]=0;
				continue;
			}
			
			System.out.println("---------------------------------------");
			System.out.print("(" + fichas[0] + ") jogador 1: " + jogador[0][0]);
			System.out.println("" + jogador[0][1]);
			System.out.print("(" + fichas[1] + ") jogador 2: " + jogador[1][0]);
			System.out.println("" + jogador[1][1]);
			System.out.println("Pote: " + fichas[2]);
			System.out.println("---------------------------------------");

			// resultado do jogador 1
			resultado[0] = avaliar.avaliar(jogador[0],mesa, 7);
			// resultado do jogador 2
			resultado[1] = avaliar.avaliar(jogador[1],mesa, 7);
			
			// Obter a mão final de cadajogador
			maoJogador[0] = avaliar.obterMaoFinal(jogador[0], mesa,7);
			maoJogador[1] = avaliar.obterMaoFinal(jogador[1], mesa,7);
		
			System.out.print("Jogador1: ");
			mostrarMao(maoJogador[0]);
			System.out.print(avaliar.mostrarClassificacao(resultado[0]));
			System.out.println();
			System.out.print("Jogador2: ");
			mostrarMao(maoJogador[1]);
			System.out.print(avaliar.mostrarClassificacao(resultado[1]));

			
			// Calcular qual o jogador vencedor
			calcVencedor(calcular,avaliar, maoJogador[0], maoJogador[1], resultado, fichas);
			
			
			// Se o utilizador ganhar guarda as cartas
			if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 1)
			{
				guardar.cartasGanhas(jogador[0]);
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				tempFichas = fichas[0];
			}
			
			guardar.maoMaisAlta(maoJogador[0], resultado[0]);
			
			// Trocar a posicao do dealer
			posDealer = (posDealer == 0) ? 1 : 0;
			fichas[2] = 0;
			if(fichas[0] <= 0)
			{
				System.out.println(" !!!!!!!!!!!!!!!! Jogador 2 venceu o jogo !!!!!!!!!!");
				jogo = false;
			}
			if(fichas[1] <= 0)
			{
				System.out.println(" !!!!!!!!!!!!!!!! Jogador 1 venceu o jogo !!!!!!!!!!");
				jogo = false;
			}
		}
		
		guardar.totalJogadas(i, jogadasGanhas);
		guardar.guardarCartasJogadas(nome);
		guardar.guardarCartasGanhas(nome);
		guardar.guardarMaoAlta(nome);
		guardar.guardarTotais(nome);
		guardar.mostrarEstatisticas();
	}
	
	// Utilizador joga contra o computador
	public void jogadorVScomputador(int modoJogo, String nome) throws IOException
	{
		
		String nomeOponente = "";

		Random dealerRandom = new Random();
		
		// Escolhe o dealer
		int posDealer= dealerRandom.nextInt((1-0)+1) + 0;
		
		// fichas[0] - jogador 1
		// fichas[1] - jogador 2
		// fichas[2] - pote
		// fichas[3] - opcao: 0 - O jogo continua 
		//							 1 - O jogo para
		float[] fichas = new float[4];	
		
		// Ambos os jogadores comecam com 1500 fichas
		fichas[0] = 1500; 
		fichas[1] = 1500;
		float tempFichas = 1500;
		
		boolean jogo = true;
		
		int i = 0;
		
		if(modoJogo == 2)
			nomeOponente = "Bot certo";
		if(modoJogo == 3)
			nomeOponente = "Bot agressivo";
		if(modoJogo == 4)
			nomeOponente = "Bot misto";
		if(modoJogo == 5)
			nomeOponente = "Bot formula";
		
		System.out.println("\n >>>>>>>>>>>>>>>>>Jogo contra " +  nomeOponente  + "<<<<<<<<<<<<<<<<<<<<<<<<<");
		
		// Fazer o upload das estatisticas do utilizador
		guardar.obterCartasJogadas(nome);
		guardar.obterCartasGanhas(nome);
		guardar.obterMaoAlta(nome);
		int jogadasGanhas = 0;
		// Fazer o upload das estatisticas do oponente
		guardarOponente.obterCartasJogadas(nomeOponente);
		guardarOponente.obterCartasGanhas(nomeOponente);
		guardarOponente.obterMaoAlta(nomeOponente);
		int jogadasGanhasOponente = 0;
		
		// Inicio do jogo
		while(jogo)
		{
			System.out.println();
			System.out.println("**************************************** Jogo nº " + (i+1) + "****************************************");
			i++;
			System.out.println();
			
			// Baralhar as cartas
			Baralho baralho = new Baralho();
			
			// Distribuir as duas cartas pelos jogadores
			dealer.darCartas(baralho, jogador[0], jogador[1]);

			// Adicionar Cartas
			guardar.cartasJogadas(jogador[0]);
			
			// Obter as probabilidades de vitoria do computador no preflop
			probabilidades[1] = (float) prob.probabilidadesPreFlop(jogador[1]);
			
			// Mostra as cartas e as fichas do jogador
			mostrarFichasJogador(fichas, jogador);

			// Apostas antes do flop
			fichas = apostar.apostasPreFlop(fichas, posDealer, probabilidades,modoJogo, jogador);
			
			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				else 
					jogadasGanhasOponente++;
				
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;
				continue;
			}
			
			// Dar o flop 
			dealer.darFlop(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>> Flop: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + "<<<<<<<<");

			// Obter as probabilidades do computador
			probTemp = prob.probabilidadesFlop(jogador[1], mesa);
			probabilidades[1] = probTemp[1];
			
			// Se algum dos jogador não tiver dinheiro mostra as cartas de ambos
			if(fichas[0] == 0 || fichas[1] == 0)
			{
				probabilidades  = prob.probabilidadeJogadorvsJogadorFlop(jogador[0], jogador[1], mesa);
				mostrarJogadoresAllin(fichas, jogador, probabilidades);
			}
			else
				mostrarFichasJogador(fichas, jogador);

			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,5);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,5);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do flop
				fichas = apostar.apostas(fichas, posDealer, probabilidades,modoJogo,1, mesa, tempMao);

			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				else 
					jogadasGanhasOponente++;
				
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;				
				continue;
			}
			
			// Dar o turn 
			dealer.darTurn(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>> Turn: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + " " + mesa[3] + "<<<<<<<<");
			
			// Obter as probabilidades do computador
			probTemp = prob.probabilidadesTurn(jogador[1], mesa);
			probabilidades[1] = probTemp[1];
			
			// Se algum dos jogador não tiver dinheiro mostra as cartas de ambos
			if(fichas[0] == 0 || fichas[1] == 0)
			{
				probabilidades  = prob.probabilidadeJogadorvsJogadorTurn(jogador[0], jogador[1], mesa);				
				mostrarJogadoresAllin(fichas, jogador, probabilidades);
			}
			else
				mostrarFichasJogador(fichas, jogador);

			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,6);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,6);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do turn
				fichas = apostar.apostas(fichas, posDealer, probabilidades,modoJogo,2, mesa, tempMao);
			
			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				else 
					jogadasGanhasOponente++;
				
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;
				continue;
			}
			
			// Dar o river e avaliar o jogo dos 2 jogadores com 7 cartas
			dealer.darRiver(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>>>> River: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + " " + mesa[3]+ " " + mesa[4] + " <<<<<<<<" );
			
			// Obter as probabilidades de ganhar do computador
			probTemp = prob.probabilidadesRiver(jogador[1], mesa);
			probabilidades[1] = probTemp[1];
			
			mostrarFichasJogador(fichas, jogador);
			
			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,7);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,7);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do river
				fichas = apostar.apostas(fichas, posDealer, probabilidades,modoJogo,3, mesa, tempMao);

			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				if(fichas[0] > tempFichas)
					jogadasGanhas++;
				else 
					jogadasGanhasOponente++;
				
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2]=0;
				continue;
			}
			
			System.out.println("---------------------------------------");
			System.out.print("(" + fichas[0] + ") jogador 1: " + jogador[0][0]);
			System.out.println("" + jogador[0][1]);
			System.out.print("(" + fichas[1] + ") jogador 2: " + jogador[1][0]);
			System.out.println("" + jogador[1][1]);
			System.out.println("Pote: " + fichas[2]);
			System.out.println("---------------------------------------");

			// resultado do jogador 1
			resultado[0] = avaliar.avaliar(jogador[0],mesa, 7);
			// resultado do jogador 2
			resultado[1] = avaliar.avaliar(jogador[1],mesa, 7);
			
			// Obter a mão final de cadajogador
			maoJogador[0] = avaliar.obterMaoFinal(jogador[0], mesa,7);
			maoJogador[1] = avaliar.obterMaoFinal(jogador[1], mesa,7);
			
			System.out.print("Jogador1: ");
			mostrarMao(maoJogador[0]);
			System.out.print(avaliar.mostrarClassificacao(resultado[0]));
			System.out.println();
			System.out.print("Jogador2: ");
			mostrarMao(maoJogador[1]);
			System.out.print(avaliar.mostrarClassificacao(resultado[1]));

			
			// Calcular o vencedor
			calcVencedor(calcular,avaliar, maoJogador[0], maoJogador[1], resultado, fichas);

			//Guarda cartas se o utilizador ganhou
			if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 1)
			{
				guardar.cartasGanhas(jogador[0]);
				jogadasGanhas++;
				tempFichas = fichas[0];
			}
			if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 2)
			{
				guardarOponente.cartasGanhas(jogador[1]);
				jogadasGanhasOponente++;
			}
				
			// Guardar a mão mais alta
			guardar.maoMaisAlta(maoJogador[0], resultado[0]);
			guardarOponente.maoMaisAlta(maoJogador[1], resultado[1]);
			
			// Trocar a posicao do dealer
			posDealer = (posDealer == 0) ? 1 : 0;
			fichas[2] = 0;
			if(fichas[0] <= 0)
			{
				System.out.println(" !!!!!!!!!!!!!!!! Computador venceu o jogo !!!!!!!!!!");
				jogo = false;
			}
			if(fichas[1] <= 0)
			{
				System.out.println(" !!!!!!!!!!!!!!!! Jogador  venceu o jogo !!!!!!!!!!");
				jogo = false;
			}
		}
		
		System.out.println("\n\n Estatisticas de " + nome);
		guardar.totalJogadas(i, jogadasGanhas);
		guardar.guardarCartasJogadas(nome);
		guardar.guardarCartasGanhas(nome);
		guardar.guardarMaoAlta(nome);
		guardar.guardarTotais(nome);
		guardar.mostrarEstatisticas();
		System.out.println("\n Estatisticas de " + nomeOponente);
		guardarOponente.totalJogadas(i, jogadasGanhasOponente);
		guardarOponente.guardarCartasJogadas(nomeOponente);
		guardarOponente.guardarCartasGanhas(nomeOponente);
		guardarOponente.guardarMaoAlta(nomeOponente);
		guardarOponente.guardarTotais(nomeOponente);
		guardarOponente.mostrarEstatisticas();
	}
	
	// Computador joga contra si mesmo
	public void computadorVScomputador(int modoJogo) throws IOException
	{
		String nome1 = "", nome2="";
		
		if(modoJogo == 6)
		{
			nome1 = "Bot certo";
			nome2 = "Bot agressivo";
		}
		if(modoJogo == 7)
		{
			nome1 = "Bot certo";
			nome2 = "Bot misto";
		}
		if(modoJogo == 8)
		{
			nome1 = "Bot certo";
			nome2 = "Bot formula ";
		}
		if(modoJogo == 9)
		{
			nome1 = "Bot agressivo";
			nome2 = "Bot misto";
		}
		if(modoJogo == 10)
		{
			nome1 = "Bot agressivo";
			nome2 = "Bot formula";
		}
		if(modoJogo == 11)
		{
			nome1 = "Bot misto";
			nome2 = "Bot formula";
		}
		
		// Fazer o upload das estatisticas do utilizador
		guardar.obterCartasJogadas(nome1);
		guardar.obterCartasGanhas(nome1);
		guardar.obterMaoAlta(nome1);
		guardar.obterTotais(nome1);
		int jogadasGanhas1 = 0;
		guardarOponente.obterCartasJogadas(nome2);
		guardarOponente.obterCartasGanhas(nome2);
		guardarOponente.obterMaoAlta(nome2);
		guardarOponente.obterTotais(nome2);
		int jogadasGanhas2 = 0;
		
		Random dealerRandom = new Random();
		
		// Escolhe o dealer
		int posDealer= dealerRandom.nextInt((1-0)+1) + 0;
		
		// fichas[0] - jogador 1
		// fichas[1] - jogador 2
		// fichas[2] - pote
		// fichas[3] - opcao: 0 - O jogo continua 
		//							 1 - O jogo para
		float[] fichas = new float[4];	
		
		// Ambos os jogadores comecam com 1500 fichas
		fichas[0] = 1500; 
		fichas[1] = 1500;
		float tempFichas = 1500;
		
		boolean jogo = true;
		
		int i = 0;
		
		// Inicio do jogo
		while(jogo)
		{
			System.out.println();
			System.out.println("**************************************** Jogo nº " + (i+1) + "****************************************");
			i++;
			System.out.println();
			
			// Baralhar as cartas
			Baralho baralho = new Baralho();
			
			// Distribuir as duas cartas pelos jogadores
			dealer.darCartas(baralho, jogador[0], jogador[1]);

			// Adicionar Cartas
			guardar.cartasJogadas(jogador[0]);
			guardarOponente.cartasJogadas(jogador[1]);
			
			// Obter as probabilidades de vitoria de ambos os jogadores no preFlop
			probabilidades[0] = (float) prob.probabilidadesPreFlop(jogador[0]);
			probabilidades[1] = (float) prob.probabilidadesPreFlop(jogador[1]);
			
			mostrarComputadores(fichas, jogador, probabilidades);

			// Apostas antes do flop
			fichas = apostar.apostasPreFlop(fichas, posDealer, probabilidades,modoJogo, jogador);
			
			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				// Incrementar as jogadas ganhas dos bots
				if(fichas[0] > tempFichas)
					jogadasGanhas1++;
				else
					jogadasGanhas2++;
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;
				continue;
			}
	
			// Dar o flop 
			dealer.darFlop(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>> Flop: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + "<<<<<<<<");
			
			// Obter as probabilidades de ganhar de ambos os jogadores
			probTemp = prob.probabilidadesFlop(jogador[0], mesa);
			probabilidades[0] = probTemp[1];
			probTemp = prob.probabilidadesFlop(jogador[1], mesa);
			probabilidades[1] = probTemp[1];
			
			// Se algum dos computadores não tiver dinheiro mostra as probabilidades de ambos
			if(fichas[0] == 0 || fichas[1] == 0)
			{
				probabilidades  = prob.probabilidadeJogadorvsJogadorFlop(jogador[0], jogador[1], mesa);
				//mostrarJogadoresAllin(fichas, jogador, probabilidades);
			}
			else
				//mostrarComputadores(fichas, jogador, probabilidades);
			
			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,5);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,5);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do flop
				fichas = apostar.apostas(fichas, posDealer, probabilidades,modoJogo,1, mesa, tempMao);

			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				// Incrementar as jogadas ganhas dos bots
				if(fichas[0] > tempFichas)
					jogadasGanhas1++;
				else
					jogadasGanhas2++;
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;				
				continue;
			}
			
			// Dar o turn 
			dealer.darTurn(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>> Turn: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + " " + mesa[3] + "<<<<<<<<");
			
			// Obter as probabilidades de ganhar de ambos os jogadores
			probTemp = prob.probabilidadesTurn(jogador[0], mesa);
			probabilidades[0] = probTemp[1];
			probTemp = prob.probabilidadesTurn(jogador[1], mesa);
			probabilidades[1] = probTemp[1];
			
			// Se algum dos computadores não tiver dinheiro mostra as probabilidades de ambos
			if(fichas[0] == 0 || fichas[1] == 0)
			{
				probabilidades  = prob.probabilidadeJogadorvsJogadorTurn(jogador[0], jogador[1], mesa);
				mostrarJogadoresAllin(fichas, jogador, probabilidades);
			}
			else
				mostrarComputadores(fichas, jogador, probabilidades);

			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,6);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,6);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do turn
				fichas = apostar.apostas(fichas, posDealer, probabilidades,modoJogo,2, mesa, tempMao);

			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				// Incrementar as jogadas ganhas dos bots
				if(fichas[0] > tempFichas)
					jogadasGanhas1++;
				else
					jogadasGanhas2++;
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2] = 0;
				continue;
			}
			// Dar o river e avaliar o jogo dos 2 jogadores com 7 cartas
			dealer.darRiver(baralho, mesa);
			System.out.println();
			System.out.println(">>>>>>>>>> River: " + mesa[0] + " " + mesa[1] + " " + mesa[2] + " " + mesa[3]+ " " + mesa[4] + " <<<<<<<<" );
			
			// Obter as probabilidades de ganhar de ambos os jogadores
			probTemp = prob.probabilidadesRiver(jogador[0], mesa);
			probabilidades[0] = probTemp[1];
			probTemp = prob.probabilidadesRiver(jogador[1], mesa);
			probabilidades[1] = probTemp[1];

			// Obter mao temporaria para passar para as apostas
			tempMao[0] = avaliar.obterMaoFinal(jogador[0], mesa,7);
			tempMao[1] = avaliar.obterMaoFinal(jogador[1], mesa,7);
			
			// Verificar se amobos os jogadores ainda tem fichas para apostar
			if(fichas[0] > 0 && fichas[1] > 0)
				//Fazer aposta depois do river
				fichas = apostar.apostas(fichas, posDealer, probabilidades,modoJogo,3, mesa, tempMao);

			// Verificar se a jogada continua
			// Se fichasJogador[3] = 1 a jogada termina
			if(fichas[3] == 1)
			{
				// Incrementar as jogadas ganhas dos bots
				if(fichas[0] > tempFichas)
					jogadasGanhas1++;
				else
					jogadasGanhas2++;
				tempFichas = fichas[0];
				
				// Trocar a posicao do dealer
				posDealer = (posDealer == 0) ? 1 : 0;
				fichas[2]=0;
				continue;
			}
			
			System.out.println("---------------------------------------");
			System.out.print("(" + fichas[0] + ") jogador 1: " + jogador[0][0]);
			System.out.println("" + jogador[0][1]);
			System.out.print("(" + fichas[1] + ") jogador 2: " + jogador[1][0]);
			System.out.println("" + jogador[1][1]);
			System.out.println("Pote: " + fichas[2]);
			System.out.println("---------------------------------------");
			
			// resultado do jogador 1
			resultado[0] = avaliar.avaliar(jogador[0],mesa, 7);
			// resultado do jogador 2
			resultado[1] = avaliar.avaliar(jogador[1],mesa, 7);
			
			// Obter a mão final de cadajogador
			maoJogador[0] = avaliar.obterMaoFinal(jogador[0], mesa,7);
			maoJogador[1] = avaliar.obterMaoFinal(jogador[1], mesa,7);
			

			System.out.print("Jogador1: ");
			mostrarMao(maoJogador[0]);
			System.out.print(avaliar.mostrarClassificacao(resultado[0]));
			System.out.println();
			System.out.print("Jogador2: ");
			mostrarMao(maoJogador[1]);
			System.out.print(avaliar.mostrarClassificacao(resultado[1]));

			calcVencedor(calcular,avaliar, maoJogador[0], maoJogador[1], resultado, fichas);
			
			if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 1)
			{
				guardar.cartasGanhas(jogador[0]);
				// Incrementar as jogadas ganhas dos bots
				if(fichas[0] > tempFichas)
					jogadasGanhas1++;
				tempFichas = fichas[0];
			}
			if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 2)
			{
				guardarOponente.cartasGanhas(jogador[1]);
				jogadasGanhas2++;
			}
			
			guardar.maoMaisAlta(maoJogador[0], resultado[0]);
			guardarOponente.maoMaisAlta(maoJogador[1], resultado[1]);
			
			// Trocar a posicao do dealer
			posDealer = (posDealer == 0) ? 1 : 0;
			fichas[2] = 0;
			
			if(fichas[0] <= 0)
			{
				if(modoJogo == 6)
					System.out.println(" !!!!!!!!!!!!!!!! Bot agressivo Venceu o jogo !!!!!!!!!!");
				if(modoJogo == 7 || modoJogo == 9)
					System.out.println(" !!!!!!!!!!!!!!!! Bot misto Venceu o jogo !!!!!!!!!!");
				if(modoJogo == 8 || modoJogo == 10 || modoJogo == 11)
					System.out.println(" !!!!!!!!!!!!!!!! Bot baseado em probabilidade Venceu o jogo !!!!!!!!!!");
				// Ambos os jogadores comecam com 1500 fichas
				fichas[0] = 1500; 
				fichas[1] = 1500;
				jogo = false;
			}
			if(fichas[1] <= 0)
			{
				if(modoJogo == 6 || modoJogo == 7 || modoJogo == 8)
					System.out.println(" !!!!!!!!!!!!!!!! Bot certo Venceu o jogo !!!!!!!!!!");
				if(modoJogo == 9 || modoJogo == 10)
					System.out.println(" !!!!!!!!!!!!!!!! Bot agressivo Venceu o jogo !!!!!!!!!!");
				if(modoJogo == 11)
					System.out.println(" !!!!!!!!!!!!!!!! Bot misto Venceu o jogo !!!!!!!!!!");
				// Ambos os jogadores comecam com 1500 fichas
				fichas[0] = 1500; 
				fichas[1] = 1500;
				jogo = false;
			}
		}
		
		System.out.println("\n\n Estatisticas de " + nome1);
		guardar.totalJogadas(i, jogadasGanhas1);
		guardar.guardarCartasJogadas(nome1);
		guardar.guardarCartasGanhas(nome1);
		guardar.guardarMaoAlta(nome1);
		guardar.guardarTotais(nome1);
		guardar.mostrarEstatisticas();
		
		System.out.println("\n Estatisticas de " + nome2);
		guardarOponente.totalJogadas(i, jogadasGanhas2);
		guardarOponente.guardarCartasJogadas(nome2);
		guardarOponente.guardarCartasGanhas(nome2);
		guardarOponente.guardarMaoAlta(nome2);
		guardarOponente.guardarTotais(nome2);
		guardarOponente.mostrarEstatisticas();
	}
	
	
	// Mostrar as fichas de ambos os jogadores e o pote
	public static void mostrarFichasJogadores(float[] fichas)
	{
		System.out.println("---------------------------------------");
		System.out.println("Jogador 1:" + fichas[0]);
		System.out.println("Jogador 2:" + fichas[1]);
		System.out.println("Pote:" + fichas[2]);
		System.out.println("---------------------------------------");
	}
	
	// Mostrar as fichas de ambos os jogadores e o pote
	public static void mostrarFichasJogador(float[] fichas, Cartas[][] jogador)
	{
		System.out.println("---------------------------------------");
		System.out.print("(" + fichas[0] + ") jogador : " + jogador[0][0]);
		System.out.println("" + jogador[0][1]);
		System.out.println("Pote: " + fichas[2]); 
		System.out.println("---------------------------------------");
	}
	
	// Mostrar as cartas, fichas e probabilidades de ambos os jogadores
	public static void mostrarJogadoresAllin(float[] fichas,Cartas[][] jogador, float[] probabilidades)
	{
		System.out.println("---------------------------------------");
		System.out.print("(" + fichas[0] + ") jogador 1: " + jogador[0][0]);
		System.out.println("" + jogador[0][1] + " -> " + probabilidades[0]);
		System.out.print("(" + fichas[1] + ") jogador 2: " + jogador[1][0]);
		System.out.println("" + jogador[1][1]+ " -> " + probabilidades[1]);
		System.out.println("Pote: " + fichas[2]);
		System.out.println("---------------------------------------");
	}
	
	/* Mostrar as cartas dos jogadores e o pote */ 
	public static void mostrarComputadores(float fichas[], Cartas[][] jogador, float[] preProbabilidades)
	{
		System.out.println("---------------------------------------");
		System.out.print("(" + fichas[0] + ") jogador 1: " + jogador[0][0]);
		System.out.println("" + jogador[0][1] + " -> " + preProbabilidades[0]);
		System.out.print("(" + fichas[1] + ") jogador 2: " + jogador[1][0]);
		System.out.println("" + jogador[1][1]+ " -> " + preProbabilidades[1]);
		System.out.println("Pote: " + fichas[2]);
		System.out.println("---------------------------------------");
	}
	
	/* Mostrar mão do Jogador */
	public static void mostrarMao(Cartas maoJogador[])
	{
		for(int i=0; i < maoJogador.length; i++)
			System.out.print(maoJogador[i] + " ");
	}
	
	/* Mostrar vencedor */
	public static void calcVencedor(CalcularVencedor vencedor, AvaliarMao avaliar,
			Cartas maoJogador1[], Cartas maoJogador2[], int[] resultado,float[] fichasJogador ){
	
		System.out.println();
		switch(vencedor.calcularVencedor(maoJogador1, maoJogador2, resultado))
		{
			case 0: 
				System.out.println("Empate com " + avaliar.mostrarClassificacao(resultado[0]));
				// Dividir o pote pelos 2 jogadores
				fichasJogador[0] += fichasJogador[2] / 2;
				fichasJogador[1] += fichasJogador[2] / 2;
				break;
			case 1: 
				System.out.println("Jogador 1 ganha " +fichasJogador[2]+ " com " + avaliar.mostrarClassificacao(resultado[0]));
				
				// Jogador 1 leva o pote
				fichasJogador[0] += fichasJogador[2];
				break;
			case 2: 
				System.out.println("Jogador 2 ganha " + +fichasJogador[2]+ " com " + avaliar.mostrarClassificacao(resultado[1]));
				// Jogador 2 leva o pote
				fichasJogador[1] += fichasJogador[2];
				break;
			default: 
				System.out.println("Erro a calcular o vencedor(Main).");
				break;
		}
	}
	
	// Estratégia desenvolvida para o utilizador não saber as cartas do oponente
	public void mostrar_esconderCartas(Cartas[][] jogador, int posDealer)
	{
		// Confirmação que o utilizador quer ver as suas cartas
		int[] confirmacao = new int[2];
		// Confirmação que o utilizador viu as suas cartas
		int[] sair = new int[2];
		
		// Recebe o input do utilizador
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		// Calcular quem é o oponente
		int oponente = (posDealer == 0) ? 1 : 0;
		
		// O primeiro jogador confirma que quer ver as suas cartas
		System.out.println("Jogador " + (posDealer+1) + " pressione " + (posDealer+1) + " para ver as suas cartas e memorize-as!");
		confirmacao[posDealer] = input.nextInt();
		while(confirmacao[posDealer] != (posDealer+1))
		{
			System.out.println("Pressione " + (posDealer+1) + " para ver as suas cartas!");
			confirmacao[posDealer] = input.nextInt();
		}
		// Mostra as cartas do primeiro jogador
		System.out.println("--------");
		System.out.println(jogador[posDealer][0] + " " + jogador[posDealer][1]);
		System.out.println("--------");
		
		// O jogador esconde as suas cartas
		System.out.println("Jogador " + (posDealer+1) + " pressione " + 0 + " para esconder as cartas e passar ao proximo jogador!");
		sair[posDealer] = input.nextInt();
		while(sair[posDealer] != 0)
		{
			System.out.println("Pressione " + (posDealer+1) + " para esconder as cartas e passar ao proximo jogador!");
			sair[posDealer] = input.nextInt();
		}
		// Estratégia para limpar o ecrã
		for(int clear = 0; clear < 1000; clear++)
		    System.out.println() ;
		
		// O segundo jogador confirma que quer ver as suas cartas
		System.out.println("Jogador " + (oponente+1) + " pressione " + (oponente+1) + " para ver as suas cartas e memorize-as!");
		confirmacao[oponente] = input.nextInt();
		while(confirmacao[oponente] != (oponente+1))
		{
			System.out.println("Pressione " + (oponente+1) + " para ver as suas cartas!");
			confirmacao[oponente] = input.nextInt();
		}
		// Mostra as cartas do primeiro jogador
		System.out.println("--------");
		System.out.println(jogador[oponente][0] + " " + jogador[oponente][1]);
		System.out.println("--------");
		
		// O jogador esconde as suas cartas
		System.out.println("Jogador " + (oponente+1) + " pressione " + 0 + " para comecar o jogo!");
		sair[oponente] = input.nextInt();
		while(sair[oponente] != 0)
		{
			System.out.println("Pressione " + (oponente+1) + " para comecar o jogo!");
			sair[oponente] = input.nextInt();
		}
		for(int clear = 0; clear < 1000; clear++)
		    System.out.println() ;
	}

	
}
