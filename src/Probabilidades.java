/* Descricao: Esta classe calcula vários tipo de probabilidades
 * probabilidade de ambos os jogadores ganharem sabendo as cartas de ambos
 * probabilidade da mão de um jogador ganhar a jogada sem saber as cartas do oponente
 * probabilidade de sair uma certa mão de poker no futuro
 * probabilidade de o oponente ter uma certa mão
 * probabilidade de ganhar com as suas cartas antes do flop
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 1/05/2017
 */

import java.util.ArrayList;
import java.util.Random;
public class Probabilidades {

	// As duas cartas do oponente
	Cartas oponente[] = new Cartas[2];
	// A mão do oponente
	Cartas maoOponente[] = new Cartas[5];
	// Calcula o vencedor entre o jogador e o oponente
	CalcularVencedor vencedor = new CalcularVencedor();
	/* Inicializar lista de cartas */
	private ArrayList <Cartas> cartas;
	
	// Construtor vazio
	Probabilidades(){}
	
	// Método para calcular a probabilidade de cada jogador ganhar a jogada a partir do flop
	public float[] probabilidadeJogadorvsJogadorFlop(Cartas[] jogador1,Cartas[] jogador2, Cartas[] mesa)
	{
		
		// Guarda as restantes 45 cartas do baralho
		Cartas[] restantes = new Cartas[45];			//52-4-3
		// Recebe as cartas do baralho sem repeticoes
		Cartas[][] cartasFinais = new Cartas[2][990];	//(45*44)/2
		// Guarda a mão do jogadores
		Cartas[][] maoJogador = new Cartas[2][5];
		// Guarda as 7 cartas, 4 dos jogadores mais 3 da mesa
		Cartas[] cartasJuntas = new Cartas[7];
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda em string as cartas dos jogadores e da mesa
		String[] strCartasJuntas = new String[7];
		// Guarda as 45 cartas restantes convertidas para string
		String[] strCartasRestantes = new String[45];
		// Guarda o resultado das mãos
		int[] resultado = new int[2];
		/* Calcula a quantidade de vitorias, empates e derrotas
		 * 0 - empate
		 * 1 - vitoria
		 * 2 - derrota
		 */
		float[] percentagemVitoria = new float[3];
		percentagemVitoria[0] = 0;
		percentagemVitoria[1] = 0;
		percentagemVitoria[2] = 0;
		/* Retorna as percentagens de vitoria de cada jogador
		 * 0 - percentagem de vitoria do jogador 1
		 * 1 - percentagem de vitoria do jogador 2
		 */
		float[] percentagens = new float[2];

		int total = 0;
		
		// Juntar as cartas dos jogadores com as do flop
		cartasJuntas[0] = jogador1[0];
		cartasJuntas[1] = jogador1[1];
		cartasJuntas[2] = jogador2[0];
		cartasJuntas[3] = jogador2[1];
		cartasJuntas[4] = mesa[0];
		cartasJuntas[5] = mesa[1];
		cartasJuntas[6] = mesa[2];
		
		// Obtem as cartas dos jogadores e da mesa convertidas para string
		strCartasJuntas = converterCartaparaString(cartasJuntas,strCartasJuntas);
		
		// Obtem as cartas do baralho exepto as cartas juntas
		restantes = adicionarCartasRestantes(restantes,strCartasJuntas,7);
		
		// Obtem as cartas restantes convertidas para string
		strCartasRestantes = converterCartaparaString(restantes, strCartasRestantes);

		// Obtem as cartas restantes sem repeticoes
		cartasFinais=semRepeticao(restantes,strCartasRestantes,7);
		
		// Calcula todas as possibilidades para os dois jogadores e 
		// retorna a percentagens de vitorias de cada jogador
		for(int i=0; i < cartasFinais[0].length; i++)
		{
			mesa[3] = cartasFinais[0][i];
			mesa[4] = cartasFinais[1][i];
			
			// Obtem uma mao aleatoria do jogador 1
			maoJogador[0] = avaliar.obterMaoFinal(jogador1, mesa, 7);
			
			// Obter o resultado da mao aleatoria
			resultado[0] = avaliar.avaliar(jogador1,mesa, 7);
			
			// Obtem uma mao aleatoria do jogador 2
			maoJogador[1] = avaliar.obterMaoFinal(jogador2, mesa, 7);
			
			// Obter o resultado da mao do oponente
			resultado[1] = avaliar.avaliar(jogador2,mesa, 7);
			
			// Calcular a quantidade de vitoria, empates ou derrotas entre jogadores
			percentagemVitoria[vencedor.calcularVencedor(maoJogador[0], maoJogador[1], resultado)]++;
			total++;
		}
		
		// Converter os resultado para percentagens
		for(int i=0; i<percentagemVitoria.length; i++)	
		{
			percentagemVitoria[i] = (float) percentagemVitoria[i]/total;
			percentagemVitoria[i] = arredondar2casas(percentagemVitoria[i]);
		}
		
		// Divide as percentagem de empate pelos dois jogadores
		percentagens[0] = percentagemVitoria[1]+percentagemVitoria[0]/2;
		percentagens[1] = percentagemVitoria[2]+percentagemVitoria[0]/2;
	
		return percentagens;

	}
	// Método para calcular a probabilidade de cada jogador ganhar a jogada a partir do flop
	public float[] probabilidadeJogadorvsJogadorTurn(Cartas[] jogador1,Cartas[] jogador2, Cartas[] mesa)
	{
		// Guarda as restantes 44 cartas do baralho
		Cartas[] restantes = new Cartas[44];		//52-8
		// Guarda a mão do jogador 1
		Cartas[][] maoJogador = new Cartas[2][5];
		// Guarda as 8 cartas, 4 dos jogador mais 4 da mesa
		Cartas[] cartasJuntas = new Cartas[8];
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda em string as cartas dos jogadores e da mesa
		String[] strCartasJuntas = new String[8];
		// Guarda o resultado das mãos
		int[] resultado = new int[2];
		/* Calcula a quantidade de vitorias, empates e derrotas
		 * 0 - empate
		 * 1 - vitoria
		 * 2 - derrota
		 */
		float[] percentagemVitoria = new float[3];
		percentagemVitoria[0] = 0;
		percentagemVitoria[1] = 0;
		percentagemVitoria[2] = 0;
		/* Retorna as percentagens de vitoria de cada jogador
		 * 0 - percentagem de vitoria do jogador 1
		 * 1 - percentagem de vitoria do jogador 2
		 */
		float[] percentagens = new float[2];

		int total = 0;
		
		// Obter a mao final do jogador 1
		maoJogador[0] = avaliar.obterMaoFinal(jogador1, mesa, 6);
		
		// Obter a mao final do jogador 2
		maoJogador[1] = avaliar.obterMaoFinal(jogador2, mesa, 6);
		
		// Juntar as cartas dos jogadores com as do flop e do turn
		cartasJuntas[0] = jogador1[0];
		cartasJuntas[1] = jogador1[1];
		cartasJuntas[2] = jogador2[0];
		cartasJuntas[3] = jogador2[1];
		cartasJuntas[4] = mesa[0];
		cartasJuntas[5] = mesa[1];
		cartasJuntas[6] = mesa[2];
		cartasJuntas[7] = mesa[3];
		
		// Obtem as cartas dos jogadores e da mesa convertidas para string
		strCartasJuntas = converterCartaparaString(cartasJuntas,strCartasJuntas);
		
		// Obtem as cartas do baralho exepto as cartas juntas
		restantes = adicionarCartasRestantes(restantes,strCartasJuntas,8);

		// Calcula todas as possibilidades para os dois jogadores e 
		// retorna a percentagens de vitorias de cada jogador
		for(int i=0; i < restantes.length; i++)
		{
			mesa[4] = restantes[i];
			
			// Obtem uma mao aleatoria do jogador 1
			maoJogador[0] = avaliar.obterMaoFinal(jogador1, mesa, 7);
			
			// Obtem o resultado da mao aleatoria
			resultado[0] = avaliar.avaliar(jogador1,mesa, 7);
			
			// Obtem uma mao aleatoria do jogador 2
			maoJogador[1] = avaliar.obterMaoFinal(jogador2, mesa, 7);
			// Obtem o resultado da mao aleatoria
			resultado[1] = avaliar.avaliar(jogador2,mesa, 7);
			
			// Calcular a quantidade de vitoria, empates ou derrotas entre jogador e oponente
			percentagemVitoria[vencedor.calcularVencedor(maoJogador[0], maoJogador[1], resultado)]++;
			total++;
		}
		
		// Converter os resultado para percentagens
		for(int i=0; i<percentagemVitoria.length; i++)	
		{
			percentagemVitoria[i] = (float) percentagemVitoria[i]/total;
			percentagemVitoria[i] = arredondar2casas(percentagemVitoria[i]);
		}

		// Divide as percentagem de empate pelos dois jogadores
		percentagens[0] = percentagemVitoria[1]+percentagemVitoria[0]/2;
		percentagens[1] = percentagemVitoria[2]+percentagemVitoria[0]/2;
	
		return percentagens;
	}

	/* Método para calcular a probabilidade do jogador ganhar com as cartas do flop, turn e river
	 * contra todas as mãos possiveis do oponente
	 */
	public float[] probabilidadesFlop(Cartas[] jogador, Cartas[] mesa)
	{
		// Guarda as restantes cartas do baralho
		Cartas[] restantesOponente = new Cartas[47];	//52-5
		// Guarda as cartas já saidas do baralho
		Cartas[] restantesMesa = new Cartas[45];		//52-4-3
		// Recebe as cartas do baralho sem repeticoes
		Cartas[][] cartasFinaisOponente = new Cartas[2][1081];	// (47*46)/2
		// Recebe as cartas do baralho que ainda não sairam sem repetições
		Cartas[][] cartasFinaisMesa = new Cartas[2][990];	//(45*44)/2
		// Guarda a mão do jogador
		Cartas[] maoJogador = new Cartas[5];
		// Guarda as cartas da mao do jogador convertida para string
		String[] strMaoJogador = new String[5];
		// Guarda as cartas saidas do baralho convertidas para string
		String[] strCartasSaidas = new String[7];
		// Guarda as 47 cartas restantes convertidas para string
		String[] strCartasRestantes = new String[47];
		// Gaurda as 45 cartas já saidas do baralho
		String[] strRestantesSaidas = new String[45];
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda as cartas que já sairam do baralho
		Cartas[] cartasSaidas = new Cartas[7]; 		//2+2+3
		// Guarda o resultado das mãos
		int[] resultado = new int[2];
		/* Calcula a quantidade de vitorias, empates e derrotas
		 * 0 - empate
		 * 1 - vitoria
		 * 2 - derrota
		 */
		float[] percentagemVitoria = new float[3];
		percentagemVitoria[0] = 0;
		percentagemVitoria[1] = 0;
		percentagemVitoria[2] = 0;
		int total = 0;
		
		// Obter a mao final do jogador
		maoJogador = avaliar.obterMaoFinal(jogador, mesa, 5);

		cartasSaidas[0] = maoJogador[0];
		cartasSaidas[1] = maoJogador[1];
		cartasSaidas[2] = maoJogador[2];
		cartasSaidas[3] = maoJogador[3];
		cartasSaidas[4] = maoJogador[4];

		// Obtem a mão do jogador convertida para string
		strMaoJogador = converterCartaparaString(maoJogador,strMaoJogador);
		
		// Obtem as cartas do baralho excepto as cartas na mão do jogador
		restantesOponente = adicionarCartasRestantes(restantesOponente,strMaoJogador,5);
			
		// Converter as restantes cartas do baralho para string
		strCartasRestantes = converterCartaparaString(restantesOponente, strCartasRestantes);
		
		// Recebe as cartas do baralho sem repetição
		cartasFinaisOponente=semRepeticao(restantesOponente,strCartasRestantes,5);
		
		// Procura todos os jogos possiveis entre o jogador e o oponente com as cartas do flop
		for(int i=cartasFinaisOponente[0].length-1; i >=1; i=i-20)
		{
			oponente[0] = cartasFinaisOponente[0][i];
			oponente[1] = cartasFinaisOponente[1][i];
			
			cartasSaidas[5] = oponente[0];
			cartasSaidas[6] = oponente[1];
			
			// Obtem as cartas saidas convertidas para string
			strCartasSaidas = converterCartaparaString(cartasSaidas,strCartasSaidas);
			
			// Obtem as cartas do baralho excepto as cartas já saidas
			restantesMesa = adicionarCartasRestantes(restantesMesa,strCartasSaidas,7);
			
			// Converter as restantes cartas do baralho para string
			strRestantesSaidas = converterCartaparaString(restantesMesa, strRestantesSaidas);
			
			// Recebe as cartas do baralho sem repetição
			cartasFinaisMesa=semRepeticao(restantesMesa,strRestantesSaidas,7);
			
			// Procura todos os jogos possiveis entre o jogador e o oponente com as cartas do flop
			for(int j=0; j < cartasFinaisMesa[0].length-1; j=j+20)
			{

				mesa[3] = cartasFinaisMesa[0][j];
				mesa[4] = cartasFinaisMesa[1][j+1];
				
				// Obter a mao final do jogador
				maoJogador = avaliar.obterMaoFinal(jogador, mesa, 7);

				// Obter o resultado da mao
				resultado[0] = avaliar.avaliar(jogador,mesa, 7);
				
				// Obtem a mao final do oponente
				maoOponente = avaliar.obterMaoFinal(oponente, mesa, 7);
				
				// Obtem o resultado da mao do oponente
				resultado[1] = avaliar.avaliar(oponente,mesa, 7);
				
				// Calcular a quantidade de vitoria, empates ou derrotas entre jogador e oponente
				percentagemVitoria[vencedor.calcularVencedor(maoJogador, maoOponente, resultado)]++;

				total++;
			}
		}

		// Converter os resultado para percentagens
		for(int i=0; i<percentagemVitoria.length; i++)	
		{
			percentagemVitoria[i] = (float) percentagemVitoria[i]/total;
			percentagemVitoria[i] = arredondar2casas(percentagemVitoria[i]);
		}
		
		return percentagemVitoria;
	}
	


	/* Método para calcular a probabilidade no turn 
	 * Este método recebe as cartas do jogador e da mesa
	 * e calcula a probabilidade de a mao ganhar a jogada 
	 */
	public float[] probabilidadesTurn(Cartas[] jogador, Cartas[] mesa)
	{
		// Guarda as restantes cartas do baralho
		Cartas[] restantesOponente = new Cartas[46];	//52-2-4
		// Guarda as cartas já saidas do baralho
		Cartas[] restantesMesa = new Cartas[44];		//52-2-2-4
		// Recebe as cartas do baralho sem repeticoes
		Cartas[][] cartasFinaisOponente = new Cartas[2][1035];	//(46*45)/2
		// Recebe as cartas do baralho que ainda não sairam sem repetições
		Cartas[][] cartasFinaisMesa = new Cartas[2][44];
		// Guarda a mão do jogador
		Cartas[] maoJogador = new Cartas[5];
		// Guarda as 6 cartas, 2 do jogador mais 4 da mesa
		Cartas[] cartasJuntas = new Cartas[6];
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda as cartas do jogador convertida para string
		String[] strCartasJogador = new String[6];
		// Guarda as cartas saidas do baralho convertidas para string
		String[] strCartasSaidas = new String[8];
		// Guarda as 46 cartas restantes convertidas para string
		String[] strCartasRestantes = new String[46];
		// Gaurda as 45 cartas já saidas do baralho
		String[] strRestantesSaidas = new String[44];
		// Guarda as cartas que já sairam do baralho
		Cartas[] cartasSaidas = new Cartas[8]; 		//2+2+4
		// Guarda o resultado das mãos
		int[] resultado = new int[2];
		/* Calcula a quantidade de vitorias, empates e derrotas
		 * 0 - empate
		 * 1 - vitoria
		 * 2 - derrota
		 */
		float[] percentagemVitoria = new float[3];
		percentagemVitoria[0] = 0;
		percentagemVitoria[1] = 0;
		percentagemVitoria[2] = 0;
		int total = 0;
		
		// Obter a mao final do jogador
		maoJogador = avaliar.obterMaoFinal(jogador, mesa, 6);

		// Obter o resultado da mao 
		resultado[0] = avaliar.avaliar(jogador,mesa, 6);
		
		// Junta as 6 cartas, 2 do jogador e 4 da mesa
		cartasJuntas = avaliar.juntarCartas(jogador, mesa, 6);
		
		cartasSaidas[0] = cartasJuntas[0];
		cartasSaidas[1] = cartasJuntas[1];
		cartasSaidas[2] = cartasJuntas[2];
		cartasSaidas[3] = cartasJuntas[3];
		cartasSaidas[4] = cartasJuntas[4];
		cartasSaidas[5] = cartasJuntas[5];
		
		// Obtem a mão do jogador convertida para string
		strCartasJogador = converterCartaparaString(cartasJuntas,strCartasJogador);
		
		// Obtem as cartas do baralho excepto as cartas na mão do jogador
		restantesOponente = adicionarCartasRestantes(restantesOponente,strCartasJogador,6);
		
		// Obtem as cartas do baralho excepto as cartas na mão do jogador
		strCartasRestantes = converterCartaparaString(restantesOponente, strCartasRestantes);
		
		// Recebe as cartas do baralho sem repetição
		cartasFinaisOponente=semRepeticao(restantesOponente,strCartasRestantes,6);
		
		// Procura todos os jogos possiveis entre o jogador e o oponente com as cartas do flop e turn
		for(int i=cartasFinaisOponente[0].length-1; i >= 1 ; i=i-10)
		{
			oponente[0] = cartasFinaisOponente[0][i];
			oponente[1] = cartasFinaisOponente[1][i];

			cartasSaidas[6] = oponente[0];
			cartasSaidas[7] = oponente[1];
			
			// Obtem as cartas saidas convertidas para string
			strCartasSaidas = converterCartaparaString(cartasSaidas,strCartasSaidas);
			
			// Obtem as cartas do baralho excepto as cartas já saidas
			restantesMesa = adicionarCartasRestantes(restantesMesa,strCartasSaidas,8);
			
			// Converter as restantes cartas do baralho para string
			strRestantesSaidas = converterCartaparaString(restantesMesa, strRestantesSaidas);
			
			// Recebe as cartas do baralho sem repetição
			cartasFinaisMesa=semRepeticao(restantesMesa,strRestantesSaidas,8);
			
			
			// Procura todos os jogos possiveis entre o jogador e o oponente com as cartas do flop
			for(int j=0; j < cartasFinaisMesa[0].length; j=j+10)
			{
				mesa[4] = cartasFinaisMesa[1][j];
				
				// Obter a mao final do jogador
				maoJogador = avaliar.obterMaoFinal(jogador, mesa, 7);

				// Obter o resultado da mao
				resultado[0] = avaliar.avaliar(jogador,mesa, 7);
				
				// Obtem a mao final do oponente
				maoOponente = avaliar.obterMaoFinal(oponente, mesa, 7);
				
				// Obtem o resultado da mao do oponente
				resultado[1] = avaliar.avaliar(oponente,mesa, 7);
				
				// Calcular a quantidade de vitoria, empates ou derrotas entre jogador e oponente
				percentagemVitoria[vencedor.calcularVencedor(maoJogador, maoOponente, resultado)]++;

				total++;
			}
			

		}
		
		// Converter os resultado para percentagens
		for(int i=0; i<percentagemVitoria.length; i++)	
		{
			percentagemVitoria[i] = (float) percentagemVitoria[i]/total;
			percentagemVitoria[i] = arredondar2casas(percentagemVitoria[i]);
		}
			
		return percentagemVitoria;
	}
	
	/* Método para calcular a probabilidade no river 
	 * Este método recebe as cartas do jogador e da mesa
	 * e calcula a probabilidade de a mao ganhar a jogada 
	 */
	public float[] probabilidadesRiver(Cartas[] jogador, Cartas[] mesa)
	{
		// Guarda as restantes 45 cartas do baralho
		Cartas[] restantes = new Cartas[45];		//52-2-5
		// Recebe as cartas do baralho sem repeticoes	
		Cartas[][] cartasFinais = new Cartas[2][990];	//(45*44)/2
		// Guarda a mão do jogador
		Cartas[] maoJogador = new Cartas[5];
		// Guarda as 7 cartas, 2 do jogador mais 5 da mesa
		Cartas[] cartasJuntas = new Cartas[7];
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda as cartas do jogador convertida para string
		String[] strCartasJogador = new String[7];
		// Guarda as 45 cartas restantes convertidas para string
		String[] strCartasRestantes = new String[45];
		// Guarda o resultado das mãos
		int[] resultado = new int[2];
		/* Calcula a quantidade de vitorias, empates e derrotas
		 * 0 - empate
		 * 1 - vitoria
		 * 2 - derrota
		 */
		float[] percentagemVitoria = new float[3];
		percentagemVitoria[0] = 0;
		percentagemVitoria[1] = 0;
		percentagemVitoria[2] = 0;
		int total = 0;
		
		// Obter a mao final do jogador
		maoJogador = avaliar.obterMaoFinal(jogador, mesa, 7);
		
		// Obter o resultado da mao 
		resultado[0] = avaliar.avaliar(jogador,mesa, 7);
		
		// Junta as 6 cartas, 2 do jogador e 5 da mesa
		cartasJuntas = avaliar.juntarCartas(jogador, mesa, 7);
		
		// Obtem a mão do jogador convertida para string
		strCartasJogador = converterCartaparaString(cartasJuntas,strCartasJogador);
		
		// Obtem as cartas do baralho excepto as cartas na mão do jogador
		restantes = adicionarCartasRestantes(restantes,strCartasJogador,7);
		
		// Obtem as cartas do baralho excepto as cartas na mão do jogador
		strCartasRestantes = converterCartaparaString(restantes, strCartasRestantes);
		
		// Recebe as cartas do baralho sem repetição
		cartasFinais=semRepeticao(restantes,strCartasRestantes,7);
		
		// Procura todos os jogos possiveis entre o jogador e o oponente com as cartas do flop, turn e river
		for(int i=0; i < cartasFinais[0].length; i++)
		{
			oponente[0] = cartasFinais[0][i];
			oponente[1] = cartasFinais[1][i];

			// Obter a mao final do oponente
			maoOponente = avaliar.obterMaoFinal(oponente, mesa, 7);
			// Obter o resultado da mao do oponente
			resultado[1] = avaliar.avaliar(oponente,mesa, 7);
			
			// Calcular a quantidade de vitoria, empates ou derrotas entre jogador e oponente
			percentagemVitoria[vencedor.calcularVencedor(maoJogador, maoOponente, resultado)]++;
			total++;
		}
		
		// Converter os resultado para percentagens
		for(int i=0; i<percentagemVitoria.length; i++)	
		{
			percentagemVitoria[i] = (float) percentagemVitoria[i]/total;
			percentagemVitoria[i] = arredondar2casas(percentagemVitoria[i]);
		}
	
		return percentagemVitoria;
		
	}
	
	// Método que calcula a probabilidade de sair uma determinada jogada nas duas ultimas cartas 
	public float[] potencialFlop(Cartas[] jogador, Cartas[] mesa)
	{
		// Guarda as restantes cartas do baralho
		Cartas[] restantes = new Cartas[47];		//52-2-3
		// Guarda a mão do jogador
		Cartas[] maoJogador = new Cartas[5];
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda as cartas do jogador convertidas para string
		String[] strCartasJogador = new String[5];
		// Guarda as cartas restantes convertidas para string
		String[] strCartasRestantes = new String[47];
		// Recebe as cartas do baralho sem repeticoes
		Cartas[][] cartasFinais = new Cartas[2][1081];	//(47*46)/2
		// Guarda o resultado das mãos
		int resultado;
		// Calcula a percentagem de saida de cada mao
		float[] percentagemResultado = new float[11];
		// Numero de mãos calculadas 
		int total = 0;
		
		// Obter a mão final do jogador
		maoJogador = avaliar.obterMaoFinal(jogador, mesa, 5);
		
		// Obtem a mão do jogador convertida para string
		strCartasJogador = converterCartaparaString(maoJogador,strCartasJogador);
		
		// Obtem as cartas do baralho excepto as cartas na mão do jogador
		restantes = adicionarCartasRestantes(restantes,strCartasJogador,5);
		
		// Converter as restantes cartas do baralho para string
		strCartasRestantes = converterCartaparaString(restantes, strCartasRestantes);
		
		// Recebe as cartas do baralho sem repetição
		cartasFinais=semRepeticao(restantes,strCartasRestantes,5);
		
		// Procura todas as jogadas possiveis no turn e no river
		for(int i=0; i < cartasFinais[0].length; i++)
		{
			mesa[3] = cartasFinais[0][i];
			mesa[4] = cartasFinais[1][i];
			
			resultado = avaliar.avaliar(jogador,mesa, 7);
			
			// Calcular a probabilidade de cada resultado 
			percentagemResultado[resultado]++;

			total++;
		}
		
		// Converter os resultado para percentagens
		for(int i=1; i<percentagemResultado.length; i++)	
		{
			percentagemResultado[i] = (float) percentagemResultado[i]/total;
			percentagemResultado[i] = arredondar2casas(percentagemResultado[i]);
		}
		
		return percentagemResultado;
	}
	
	// Método que calcula a probabilidade de sair uma determinada jogada na ultima carta 
	public float[] potencialTurn(Cartas[] jogador, Cartas[] mesa)
	{
		// Guarda as restantes cartas do baralho
		Cartas[] restantes = new Cartas[46];		//52 - 2 - 4
		// Guarda as 6 cartas, 2 do jogador mais 4 da mesa
		Cartas[] cartasJuntas = new Cartas[6];		// 2 + 4
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda as cartas do jogador convertida para string
		String[] strCartasJogador = new String[6];
		// Guarda as cartas restantes convertidas para string
		String[] strCartasRestantes = new String[46];
		// Guarda o resultado das mãos
		int resultado;
		// Calcula a percentagem de saida de cada mao
		float[] percentagemResultado = new float[11];
		// Numero de mãos calculadas 
		int total = 0;
		
		// Obter o resultado da mao 
		resultado = avaliar.avaliar(jogador,mesa, 6);
		
		// Junta as 6 cartas, 2 do jogador e 5 da mesa
		cartasJuntas = avaliar.juntarCartas(jogador, mesa, 6);
		
		// Obtem a mão do jogador convertida para string
		strCartasJogador = converterCartaparaString(cartasJuntas,strCartasJogador);
		
		// Obtem as cartas do baralho excepto as cartas na mão do jogador
		restantes = adicionarCartasRestantes(restantes,strCartasJogador,6);
		
		// Converter as restantes cartas do baralho para string
		strCartasRestantes = converterCartaparaString(restantes, strCartasRestantes);
		
		// Procura todas as jogadas possiveis no river
		for(int i=0; i < restantes.length; i++)
		{
			// Carta river
			mesa[4] = restantes[i];

			// Obter o resultado da mao 
			resultado = avaliar.avaliar(jogador,mesa, 7);
			
			// Calcular a probabilidade de cada resultado 
			percentagemResultado[resultado]++;
			
			total++;
		}
		
		// Converter os resultado para percentagens
		for(int i=1; i<percentagemResultado.length; i++)	
		{
			percentagemResultado[i] = (float) percentagemResultado[i]/total;
			percentagemResultado[i] = arredondar2casas(percentagemResultado[i]);
		}
		
		return percentagemResultado;
	}
	
	// Método que calcula qual a mão que o oponente poderá ter
	public float[] potencialMaoFlop(Cartas[] mesa)
	{
		// Guarda em string as cartas restantes da mesa
		String[] strMesa = new String[3]; 
		// Guarda as restantes cartas do baralho
		Cartas[] restantes = new Cartas[49]; //52-3
		// Guarda as restantes cartas do baralho convertidas para string
		String[] strRestantes = new String[49];
		// Guarda as cartas temporarias
		Cartas[] cartasTemp = new Cartas[2];
		// Recebe as cartas do baralho sem repeticoes
		Cartas[][] cartasFinais = new Cartas[2][1176];	//(49*48)/2
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda o resultado das mãos
		int resultado;
		// Calcula a percentagem de saida de cada mao
		float[] percentagemResultado = new float[11];
		// Numero de mãos calculadas 
		int total = 0;
		
		// Obtem as 3 cartas convertidas para string
		for(int i=0; i<3;i++)
			strMesa[i] = Integer.toString( mesa[i].getNumCarta())
					+ Integer.toString( mesa[i].getNaipe());
		
		// Obtem as cartas do baralho expeto essas 3
		restantes = adicionarCartasRestantes(restantes, strMesa, 3);
		// Obtem as cartas do baralho convertidas para string
		strRestantes = converterCartaparaString(restantes, strRestantes);
		// Obtem as cartas do baralho sem repeticao
		cartasFinais = semRepeticao(restantes, strRestantes,3);
		
		// Procura todas as jogadas possiveis no turn e no river
		for(int i=0; i < cartasFinais[0].length; i++)
		{
			cartasTemp[0] = cartasFinais[0][i];
			cartasTemp[1] = cartasFinais[1][i];
			
			resultado = avaliar.avaliar(cartasTemp,mesa, 5);
			
			// Calcular a probabilidade de cada resultado 
			percentagemResultado[resultado]++;

			total++;
		}
		
		
		
		// Converter os resultado para percentagens
		for(int i=1; i<percentagemResultado.length; i++)	
		{
			percentagemResultado[i] = (float) percentagemResultado[i]/total;
			percentagemResultado[i] = arredondar2casas(percentagemResultado[i]);
		}

		return percentagemResultado;
	}
	
	// Método que calcula a probabilidade de sair uma determinada jogada na ultima carta 
	public float[] potencialMaoTurn(Cartas[] mesa)
	{
		// Guarda em string as cartas restantes da mesa
		String[] strMesa = new String[4]; 
		// Guarda as restantes cartas do baralho
		Cartas[] restantes = new Cartas[48]; //52-4
		// Guarda as restantes cartas do baralho convertidas para string
		String[] strRestantes = new String[48];
		// Guarda as cartas temporarias
		Cartas[] cartasTemp = new Cartas[2];
		// Recebe as cartas do baralho sem repeticoes
		Cartas[][] cartasFinais = new Cartas[2][1128];	//(48*47)/2
		// Objeto para avaliar as mãos
		AvaliarMao avaliar = new AvaliarMao();
		// Guarda o resultado das mãos
		int resultado;
		// Calcula a percentagem de saida de cada mao
		float[] percentagemResultado = new float[11];
		// Numero de mãos calculadas 
		int total = 0;
		
		// Obtem as 4 cartas convertidas para string
		for(int i=0; i<4;i++)
			strMesa[i] = Integer.toString( mesa[i].getNumCarta())
					+ Integer.toString( mesa[i].getNaipe());
		
		// Obtem as cartas do baralho expeto essas 4
		restantes = adicionarCartasRestantes(restantes, strMesa, 4);
		// Obtem as cartas do baralho convertidas para string
		strRestantes = converterCartaparaString(restantes, strRestantes);
		// Obtem as cartas do baralho sem repeticao
		cartasFinais = semRepeticao(restantes, strRestantes,4);
		
		// Procura todas as jogadas possiveis no turn e no river
		for(int i=0; i < cartasFinais[0].length; i++)
		{
			cartasTemp[0] = cartasFinais[0][i];
			cartasTemp[1] = cartasFinais[1][i];
			
			resultado = avaliar.avaliar(cartasTemp,mesa, 6);
			
			// Calcular a probabilidade de cada resultado 
			percentagemResultado[resultado]++;

			total++;
		}
		
		// Converter os resultado para percentagens
		for(int i=1; i<percentagemResultado.length; i++)	
		{
			percentagemResultado[i] = (float) percentagemResultado[i]/total;
			percentagemResultado[i] = arredondar2casas(percentagemResultado[i]);
		}
				
		return percentagemResultado;
	}
	
	// Probabilidades de cada mão de poker antes do flop 
	public double probabilidadesPreFlop(Cartas[] h)
	{
		/* A A */
		if(h[0].getNumCarta()==0 && h[1].getNumCarta()==0)
			return 85.3;
		
		/* A K naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==12 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==12 && h[0].getNaipe() == h[1].getNaipe()))
			return 67;
		
		/* A K out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==12 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==12 && h[0].getNaipe() != h[1].getNaipe()))
			return 65.4;
		
		/* A Q naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==11 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==11 && h[0].getNaipe() == h[1].getNaipe()))
			return 66.1;
		
		/* A Q out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==11 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==11 && h[0].getNaipe() != h[1].getNaipe()))
			return 66.1;
		
		/* A J naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==10 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==10 && h[0].getNaipe() == h[1].getNaipe()))
			return 65.4;
		
		/* A J out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==10 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==10 && h[0].getNaipe() != h[1].getNaipe()))
			return 63.6;
		
		
		/* A 10 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()))
			return 64.7;
		
		/* A 10 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()))
			return 62.9;
		
		/* A 9 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()))
			return 63;
		
		/* A 9 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()))
			return 60.9;
		
		/* A 8 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()))
			return 62.1;
		
		/* A 8 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()))
			return 60.1;
		
		/* A 7 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()))
			return 61.1;
		
		/* A 7 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()))
			return 59.1;
		
		/* A 6 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 60;
		
		/* A 6 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 57.8;
		
		/* A 5 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 59.9;
		
		/* A 5 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 57.7;
		
		/* A 4 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 58.9;
		
		/* A 4 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 56.4;
		
		/* A 3 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 58;
		
		/* A 3 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 55.6;
		
		/* A 2 naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 57;
		
		/* A 2 out naipe */
		if((h[0].getNumCarta()==0 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==0 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 54.6;
		
		/*--------------------------------------------------------------------------------------------*/
		
		/* K K */
		if(h[0].getNumCarta()==12 && h[1].getNumCarta()==12)
			return 82.4;
		
		/* K Q naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==11 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==11 && h[0].getNaipe() == h[1].getNaipe()))
			return 63.4;
		
		/* K Q out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==11 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==11 && h[0].getNaipe() != h[1].getNaipe()))
			return 61.4;
		
		/* K J naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==10 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==10 && h[0].getNaipe() == h[1].getNaipe()))
			return 62.6;
		
		/* K J out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==10 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==10 && h[0].getNaipe() != h[1].getNaipe()))
			return 60.6;
		
		/* K 10 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()))
			return 61.9;
		
		/* K 10 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()))
			return 59.9;
		
		/* K 9 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()))
			return 60;
		
		/* K 9 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()))
			return 58;
		
		/* K 8 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()))
			return 58.5;
		
		/* K 8 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()))
			return 56.3;
		
		/* K 7 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()))
			return 57.8;
		
		/* K 7 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()))
			return 55.4;
		
		/* K 6 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 56.8;
		
		/* K 6 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 54.3;
		
		/* K 5 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 55.8;
		
		/* K 5 out naipe */
		if((h[0].getNumCarta()==12&& h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 53.3;
		
		/* K 4 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 54.7;
		
		/* K 4 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 52.1;
		
		/* K 3 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 53.8;
		
		/* K 3 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 51.2;
		
		/* K 2 naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 52.9;
		
		/* K 2 out naipe */
		if((h[0].getNumCarta()==12 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==12 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 50.2;
		
		/*-----------------------------------------------------------------------------------------*/
		
		/* Q Q */
		if(h[0].getNumCarta()==11 && h[1].getNumCarta()==11)
			return 79.9;
		
		/* Q J naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==10 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==10 && h[0].getNaipe() == h[1].getNaipe()))
			return 60.3;
		
		/* Q J out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==10 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==10 && h[0].getNaipe() != h[1].getNaipe()))
			return 58.2;
		
		/* Q 10 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()))
			return 59.5;
		
		/* Q 10 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()))
			return 57.4;
		
		/* Q 9 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()))
			return 57.9;
		
		/* Q 9 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()))
			return 55.5;
		
		/* Q 8 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()))
			return 56.2;
		
		/* Q 8 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()))
			return 53.8;
		
		/* Q 7 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()))
			return 54.5;
		
		/* Q 7 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()))
			return 51.9;
		
		/* Q 6 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 53.8;
		
		/* Q 6 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 51.1;
		
		/* Q 5 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 52.9;
		
		/* Q 5 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 50.2;
		
		/* Q 4 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 51.7;
		
		/* Q 4 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 49;
		
		/* Q 3 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 50.7;
		
		/* Q 3 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 47.9;
		
		/* Q 2 naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 49.9;
		
		/* Q 2 out naipe */
		if((h[0].getNumCarta()==11 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==11 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 47;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* J J */
		if(h[0].getNumCarta()==10 && h[1].getNumCarta()==10)
			return 77.5;
		
		/* J 10 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==9 && h[0].getNaipe() == h[1].getNaipe()))
			return 57.5;
		
		/* J 10 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==9 && h[0].getNaipe() != h[1].getNaipe()))
			return 55.4;
		
		/* J 9 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()))
			return 55.8;
		
		/* J 9 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()))
			return 53.4;
		
		/* J 8 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()))
			return 54.2;
		
		/* J 8 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()))
			return 51.7;
		
		/* J 7 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()))
			return 52.4;
		
		/* J 7 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()))
			return 49.9;
		
		/* J 6 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 50.8;
		
		/* J 6 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 47.9;
		
		/* J 5 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 50;
		
		/* J 5 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 47.1;
		
		/* J 4 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 49;
		
		/* J 4 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 46.1;
		
		/* J 3 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 47.9;
		
		/* J 3 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 45;
		
		/* J 2 naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 47.1;
		
		/* J 2 out naipe */
		if((h[0].getNumCarta()==10 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==10 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 44;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 10 10 */
		if(h[0].getNumCarta()==9 && h[1].getNumCarta()==9)
			return 75.1;
		
		/* 10 9 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==8 && h[0].getNaipe() == h[1].getNaipe()))
			return 54.3;
		
		/* 10 9 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==8 && h[0].getNaipe() != h[1].getNaipe()))
			return 51.7;
		
		/* 10 8 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()))
			return 52.6;
		
		/* 10 8 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()))
			return 50;
		
		/* 10 7 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()))
			return 51;
		
		/* 10 7 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()))
			return 48.2;
		
		/* 10 6 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 49.2;
		
		/* 10 6 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 46.3;
		
		/* 10 5 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 47.2;
		
		/* 10 5 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 44.2;
		
		/* 10 4 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 46.4;
		
		/* 10 4 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 43.4;
		
		/* 10 3 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 45.5;
		
		/* 10 3 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 42.4;
		
		/* 10 2 naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 44.7;
		
		/* 10 2 out naipe */
		if((h[0].getNumCarta()==9 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==9 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 41.5;
		
		/*-----------------------------------------------------------------------------------*/
		
		/* 9 9 */
		if(h[0].getNumCarta()==8 && h[1].getNumCarta()==8)
			return 72.1;
		
		/* 9 8 naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==7 && h[0].getNaipe() == h[1].getNaipe()))
			return 51.1;
		
		/* 9 8 out naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==7 && h[0].getNaipe() != h[1].getNaipe()))
			return 48.4;
		
		/* 9 7 naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()))
			return 49.5;
		
		/* 9 7 out naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()))
			return 46.7;
		
		/* 9 6 naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 47.7;
		
		/* 9 6 out naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 44.9;
		
		/* 9 5 naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 45.9;
		
		/* 9 5 out naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 42.9;
		
		/* 9 4 naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 43.8;
		
		/* 9 4 out naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 40.7;
		
		/* 9 3 naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 43.2;
		
		/* 9 3 out naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 39.9;
		
		/* 9 2 naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 42.3;
		
		/* 9 2 out naipe */
		if((h[0].getNumCarta()==8 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==8 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 38.9;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 8 8 */
		if(h[0].getNumCarta()==7 && h[1].getNumCarta()==7)
			return 69.1;
		
		/* 8 7 naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==6 && h[0].getNaipe() == h[1].getNaipe()))
			return 48.2;
		
		/* 8 7 out naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==6 && h[0].getNaipe() != h[1].getNaipe()))
			return 45.5;
		
		/* 8 6 naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 46.5;
		
		/* 8 6 out naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 43.6;
		
		/* 8 5 naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 44.8;
		
		/* 8 5 out naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 41.7;
		
		/* 8 4 naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 42.7;
		
		/* 8 4 out naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 39.6;
		
		/* 8 3 naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 40.8;
		
		/* 8 3 out naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 37.5;
		
		/* 8 2 naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 40.3;
		
		/* 8 2 out naipe */
		if((h[0].getNumCarta()==7 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==7 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 36.8;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 7 7 */
		if(h[0].getNumCarta()==6 && h[1].getNumCarta()==6)
			return 66.2;
		
		/* 7 6 naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==5 && h[0].getNaipe() == h[1].getNaipe()))
			return 45.7;
		
		/* 7 6 out naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==5 && h[0].getNaipe() != h[1].getNaipe()))
			return 42.7;
		
		/* 7 5 naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 43.8;
		
		/* 7 5 out naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 40.8;
		
		/* 7 4 naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 41.8;
		
		/* 7 4 out naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 38.6;
		
		/* 7 3 naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 40;
		
		/* 7 3 out naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 36.6;
		
		/* 7 2 naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 38.1;
		
		/* 7 2 out naipe */
		if((h[0].getNumCarta()==6 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==6 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 34.6;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 6 6 */
		if(h[0].getNumCarta()==5 && h[1].getNumCarta()==5)
			return 63.3;
		
		/* 6 5 naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==4 && h[0].getNaipe() == h[1].getNaipe()))
			return 43.2;
		
		/* 6 5 out naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==4 && h[0].getNaipe() != h[1].getNaipe()))
			return 40.1;
		
		/* 6 4 naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 41.4;
		
		/* 6 4 out naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 38;
		
		/* 6 3 naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 39.4;
		
		/* 6 3 out naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 35.9;
		
		/* 6 2 naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 37.5;
		
		/* 6 2 out naipe */
		if((h[0].getNumCarta()==5 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==5 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 34;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 5 5 */
		if(h[0].getNumCarta()==4 && h[1].getNumCarta()==4)
			return 60.3;
		
		/* 5 4 naipe */
		if((h[0].getNumCarta()==4 && h[1].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==4 && h[0].getNumCarta()==3 && h[0].getNaipe() == h[1].getNaipe()))
			return 41.1;
		
		/* 5 4 out naipe */
		if((h[0].getNumCarta()==4 && h[1].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==4 && h[0].getNumCarta()==3 && h[0].getNaipe() != h[1].getNaipe()))
			return 37.9;
		
		/* 5 3 naipe */
		if((h[0].getNumCarta()==4 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==4 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 39.3;
		
		/* 5 3 out naipe */
		if((h[0].getNumCarta()==4 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==4 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 35.8;
		
		/* 5 2 naipe */
		if((h[0].getNumCarta()==4 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==4 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 37.5;
		
		/* 5 2 out naipe */
		if((h[0].getNumCarta()==4 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==4 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 33.9;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 4 4 */
		if(h[0].getNumCarta()==3 && h[1].getNumCarta()==3)
			return 57;
		
		/* 4 3 naipe */
		if((h[0].getNumCarta()==3 && h[1].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==3 && h[0].getNumCarta()==2 && h[0].getNaipe() == h[1].getNaipe()))
			return 38;
		
		/* 4 3 out naipe */
		if((h[0].getNumCarta()==3 && h[1].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==3 && h[0].getNumCarta()==2 && h[0].getNaipe() != h[1].getNaipe()))
			return 34.4;
		
		/* 4 2 naipe */
		if((h[0].getNumCarta()==3 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==3 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 36.3;
		
		/* 4 2 out naipe */
		if((h[0].getNumCarta()==3 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==3 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 32.5;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 3 3 */
		if(h[0].getNumCarta()==2 && h[1].getNumCarta()==2)
			return 53.7;
		
		/* 3 2 naipe */
		if((h[0].getNumCarta()==2 && h[1].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()) ||
				(h[1].getNumCarta()==2 && h[0].getNumCarta()==1 && h[0].getNaipe() == h[1].getNaipe()))
			return 35.1;
		
		/* 3 2 out naipe */
		if((h[0].getNumCarta()==2 && h[1].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()) ||
				(h[1].getNumCarta()==2 && h[0].getNumCarta()==1 && h[0].getNaipe() != h[1].getNaipe()))
			return 31.2;
		
		/*-------------------------------------------------------------------------------------*/
		
		/* 2 2 */
		if(h[0].getNumCarta()==1 && h[1].getNumCarta()==1)
			return 50.3;
		
		return 0;
	}

	// Método para adicionar as restantes cartas ao baralho
	public Cartas[] adicionarCartasRestantes(Cartas[] restantes, String[] strCartas, int nCartas)
	{
		// Guarda as cartas do baralho convertidas para string
		String[] strCartasBaralho = new String[52];
		
		// Guarda as restantes cartas do baralho
		cartas = new ArrayList<Cartas>();
		
		int k = 0, x = 0; //interador sobre as cartas
		// Adicionar as restantes cartas do baralho
		for(short i=0; i<13; i++)
		{	
			for(short j=0; j<4;j++)
			{
				// Adiciona as cartas todas
				cartas.add(new Cartas(i,j));
				
				// Converter as cartas do baralho para string
				strCartasBaralho[k] = Integer.toString( cartas.get(k).getNumCarta())
						+ Integer.toString( cartas.get(k).getNaipe());
				// Comparar 3 cartas
				if(nCartas == 3)
				{
					// Se a carta nao tiver na mao do jogador adiciona-a às restantes
					if(!strCartasBaralho[k].equals(strCartas[0]) &&
							!strCartasBaralho[k].equals(strCartas[1]) &&
							!strCartasBaralho[k].equals(strCartas[2]))
					{
						restantes[x] = cartas.get(k);
						x++;
					}
				}
				// Comparar 4 cartas
				if(nCartas == 4)
				{
					// Se a carta nao tiver na mao do jogador adiciona-a às restantes
					if(!strCartasBaralho[k].equals(strCartas[0]) &&
							!strCartasBaralho[k].equals(strCartas[1]) &&
							!strCartasBaralho[k].equals(strCartas[2]) &&
							!strCartasBaralho[k].equals(strCartas[3]))
					{
						restantes[x] = cartas.get(k);
						x++;
					}
				}
				// Comparar 5 cartas
				if(nCartas == 5)
				{
					// Se a carta nao tiver na mao do jogador adiciona-a às restantes
					if(!strCartasBaralho[k].equals(strCartas[0]) &&
							!strCartasBaralho[k].equals(strCartas[1]) &&
							!strCartasBaralho[k].equals(strCartas[2]) &&
							!strCartasBaralho[k].equals(strCartas[3]) &&
							!strCartasBaralho[k].equals(strCartas[4]))
					{
						restantes[x] = cartas.get(k);
						x++;
					}
				}
				// Comparar 6 cartas
				if(nCartas == 6)
				{
					// Se a carta nao tiver na mao do jogador adiciona-a às restantes
					if(!strCartasBaralho[k].equals(strCartas[0]) &&
							!strCartasBaralho[k].equals(strCartas[1]) &&
							!strCartasBaralho[k].equals(strCartas[2]) &&
							!strCartasBaralho[k].equals(strCartas[3]) &&
							!strCartasBaralho[k].equals(strCartas[4]) &&
							!strCartasBaralho[k].equals(strCartas[5]))
					{
						restantes[x] = cartas.get(k);
						x++;
					}
				}
				// Comparar 7 cartas
				if(nCartas == 7)
				{
					if(!strCartasBaralho[k].equals(strCartas[0]) &&
							!strCartasBaralho[k].equals(strCartas[1]) &&
							!strCartasBaralho[k].equals(strCartas[2]) &&
							!strCartasBaralho[k].equals(strCartas[3]) &&
							!strCartasBaralho[k].equals(strCartas[4]) &&
							!strCartasBaralho[k].equals(strCartas[5]) &&
							!strCartasBaralho[k].equals(strCartas[6]))
					{
						restantes[x] = cartas.get(k);
						x++;
					}
				}
				// Comparar 8 cartas
				if(nCartas == 8)
				{
					if(!strCartasBaralho[k].equals(strCartas[0]) &&
							!strCartasBaralho[k].equals(strCartas[1]) &&
							!strCartasBaralho[k].equals(strCartas[2]) &&
							!strCartasBaralho[k].equals(strCartas[3]) &&
							!strCartasBaralho[k].equals(strCartas[4]) &&
							!strCartasBaralho[k].equals(strCartas[5]) &&
							!strCartasBaralho[k].equals(strCartas[6]) &&
							!strCartasBaralho[k].equals(strCartas[7]))
					{
						restantes[x] = cartas.get(k);
						x++;
					}
				}
				k++;
			}
		}
		
		return restantes;
	}
	
	// Método para procurar repetições na ordem das cartas
	public Cartas[][] semRepeticao(Cartas[] restantes,String[] strCartasRestantes, int nCartas)
	{
		// Recebe duas cartas temporarias
		Cartas[] tempCartas = new Cartas[2];
		// Juntas as cartas para procurar repetidas
		String[] repetidas = new String[(52-nCartas)*(52-nCartas-1)];
		// Retorna as cartas finais sem repeticôes
		Cartas[][] cartasFinais = new Cartas[2][((52-nCartas)*(52-nCartas-1))/2];
		
		int x=0,x1=0, flag=0;
		// Percorre as Cartas todas
		for(int i=0; i < restantes.length; i++)
		{
			// A primeira carta 
			tempCartas[0] = restantes[i];
			String oponente0 = strCartasRestantes[i];
			// Percorre as cartas todas com a carta anterior
			for(int j=0; j< restantes.length; j++)
			{
				// A segunda carta do oponente tem de ser diferente da primeira
				if(!strCartasRestantes[j].equals(oponente0))
				{
					tempCartas[1] = restantes[j];
					String oponente1 = strCartasRestantes[j];
					
					repetidas[x]=oponente1+" "+oponente0;
					String temp =oponente0+" "+oponente1;
					
					// Verificar se a carta já existe em repetidas
					for(int a=0; a<x; a++)
					{
						// Se já existe ativa a flag
						if(repetidas[a].equals(temp))
						{
							flag=1;	
							break;
						}
											
					}
					x++;
					// Se a carta não existe em repetidas então adiciona a cartasFinais
					if(flag==0)
					{
						cartasFinais[0][x1]=tempCartas[0]; 
						cartasFinais[1][x1]=tempCartas[1]; 
						x1++;
					}		
				}
			flag=0;
			}
		}

		return cartasFinais;
	}
	 
	// Método que recebe cartas e converte para string o valor do numero da carta e do seu naipe
	public String[] converterCartaparaString(Cartas[] cartas, String[] cartasString)
	{
		for(int i=0; i<cartas.length;i++)
			cartasString[i] = Integer.toString( cartas[i].getNumCarta())
					+ Integer.toString( cartas[i].getNaipe());
		
		return cartasString;
	}
	
	// Método para arredondar número para duas casas decimais
	public float arredondar2casas(float num)
	{
		num =(float)num*100;	
		num = Math.round(num * 100);	
		
		return num = num/100;
	}
	
	public Cartas[][] baralharSemRepeticao(Cartas[][] cartas)
	{
		
		Random gerar = new Random();
		for(int i=0; i<cartas[0].length; i++)
		{
			int indice1 = gerar.nextInt(cartas[0].length-1);	//Obter o indice1 entre 0 e 52
			int indice2 = gerar.nextInt(cartas[0].length-1);	//Obter o indice2 entre 0 e 52
			
			// temp fica com a carta da posição do indice2
			Cartas temp1 = (Cartas) cartas[0][indice2]; 
			Cartas temp2 = (Cartas) cartas[1][indice2]; 
			
			cartas[0][indice2] = cartas[0][indice1];
			cartas[1][indice2] = cartas[1][indice1];
			
			cartas[0][indice1] = temp1;
			cartas[1][indice1] = temp2;
			
		}
		
		return cartas;
	}
}
