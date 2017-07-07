/* Descricao: Esta classe serve para desenvolver algoritmos que simulem um jogador de poker
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 12/05/2017
 */

public class Simulador {
	
	// Avaliar as mãos dos jogadores
	AvaliarMao avaliar = new AvaliarMao();
	// Guarda as probabilidades 
	float[] probabilidades = new float[11];
	// Objeto para aceder às probabilidades
	Probabilidades prob = new Probabilidades();

	/* Construtor vazio */
	Simulador(){}
	
	/* Algoritmo Mathematically Fair Strategy - MFS 
	 * Este simlulador de poker joga consoate as probabilidades de vencer
	 * W - probabilidade de o jogador vencer
	 * L - probabilidade de o jogador perder
	 * A - soma da aposta feita pelo adversário
	 * J - soma da aposta feita pelo jogador
	 */
	public int[] MFS(int jogador, float[] fichas, float tempPote, float[] percentagemVitoria,float[] aposta, int max)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		float igualarAposta = (aposta[oponente] - aposta[jogador]);	// Fichas necessárias para fazer call 
		float pote = fichas[2]+tempPote;				// Fichas no pote 	
		float W = percentagemVitoria[jogador];			// Probabilidade de ganhar
		float L = 100 - W;								// Probabilidade de perder
		float V;										// Guarda o resultado de MFS
		int T = 20;										// Constante 
		
		int[] opcao = new int[2];
		
		/* Apostas utilizadas com o menuCheck 
		 * Ou seja, quando não existe apostas
		 */
		if(igualarAposta == 0 && max != 2)
		{
			// Check
			if(W <= 55)
				opcao[0] = 1;
			// Apostar big blind
			if(W > 55 && W <= 65)
			{
				// Se o jogador tiver 20 fichas ou menos então faz all in
				if(fichas[jogador]<= 20)
					opcao[0] = 4;
				else
					opcao[0] = 2;
			}
			// Apostar pote
			if(W > 65 && W <= 75)
			{
				opcao[0] = 3;
				
				// Se tiver dinheiro para apostar o pote
				if(fichas[jogador] <= 40)
					opcao[0]=4;
				if(pote < fichas[jogador])
					opcao[1] =  Math.round(pote);
				else
					opcao[0] =  4 ;	
			}
			// Apostar pote + 10 * big blind
			if(W > 75 && W <= 85)
			{
				opcao[0] = 3;
				
				// Se tiver dinheiro para apostar o pote + 10 * big blind
				if(fichas[jogador] <= 40)
					opcao[0]=2;
				if((pote+400) < fichas[jogador])
					opcao[1] = Math.round(pote+400);
				else
					opcao[0] = 4;	
			}
			// Apostar tudo
			if(W > 85 )
				opcao[0] = 4;
		} 
		// Apostas utilizadas em menuFold_Call e menuFold_Allin
		else
		{
			V = (W*aposta[oponente]-L*igualarAposta)/100;
			
			// Opçôes no menu Fold_Allin
			if(max == 2)
			{
				// Fold 
				if(V <= 5*T )
					opcao[0] = 1;
				// Call
				else 
					opcao[0] = 2;
			}
			//Apostas utilizadas com o menuCheck 
			else
			{
				// Fold 
				if(V <= 0 )
					opcao[0] = 1;
				// Call 
				if(V > 0 && V <= T)
					opcao[0] = 2;
				// Apostar blind
				if(V > T && V <= 2*T)
					if(fichas[jogador]<= 20)
						opcao[0]=5;
					else
						opcao[0] = 3;
				// Apostar pote
				if(V > 2*T && V <= 5*T)
				{
					opcao[0] = 4;
					
					// Se tiver dinheiro para apostar o pote
					if(fichas[jogador] <= 40)
						opcao[0]=5;
					if(pote < fichas[jogador])
						opcao[1] = Math.round(pote) ;	
					else
						opcao[0] =  5;	
				}
				// Apostar pote + 10*big blind
				if(V > 5*T && V <= 10*T)
				{
					opcao[0] = 4;
					
					// Se tiver dinheiro para apostar o pote + 10 * big blind
					if(fichas[jogador] <= 40)
						opcao[0]=5;
					if((pote+400) < fichas[jogador])
						opcao[1] = Math.round(pote+400) ;	
					else
						opcao[0] = 5;	
				}
				// Apostar tudo
				if(V > 10*T)
					opcao[0] = 5;
			}
		}

		return opcao;
	}
	

	/* Simulador de um jogador que joga apenas quando tem as melhores mãos */
	public int[] bot_certo(int jogador, int dealer, float[] fichas, float[] aposta, float tempPote, int ronda, 
			int menu, Cartas[] cartas, Cartas[] mesa, float[] probabilidades) 
	{
		int[] opcao = new int[2];
			
		// Verificar em que ronda se encontra o jogo
		// Pre-Flop
		if(ronda == 0)
			opcao = preFlop_certo(jogador, dealer, fichas, tempPote, aposta, cartas, menu);
		else
		{	
			// Flop e turn 
			if(ronda == 1 || ronda == 2)
				opcao = flop_certo(jogador, dealer, fichas, tempPote, aposta, probabilidades, cartas, mesa, menu, ronda);
			if(ronda == 3)
				opcao = river_certo(jogador, dealer, fichas, tempPote, aposta, probabilidades, cartas, mesa, menu);
		}
			

		return opcao;
	}
	
	// Apostas antes de flop, apenas aposta com as melhores cartas
	public int[] preFlop_certo(int jogador, int dealer, float[] fichas, float tempPote, float[] aposta, Cartas[] cartas, int max)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		// Guarda as opcoes de apostas 
		int[] opcao = new int[2];
		// Valor da big blind
		int bb = 20;
		// Fichas do jogador e do oponente convertidas para big blinds
		int stack_jogador;
		int stack_oponente; 
		
		if(fichas[jogador] > bb)
			stack_jogador = (int)fichas[jogador] / bb;
		else
			stack_jogador = 0;
		if(fichas[oponente] > bb)
			stack_oponente = (int)fichas[oponente] / bb;
		else
			stack_oponente = 0;
		// Recebe o número do grupo a que as cartas do jogador pertence
		int grupoCartas = grupo(cartas);
		// Valor do raise do oponente
		int call;
		if(stack_jogador > 0 && stack_oponente > 0)
			call = (int)(aposta[oponente] - aposta[jogador]);
		else
			call = 0;

		// O oponente não aumentou
		if(call <= bb) 
		{
			// Se o jogador ou o oponente tem menos que 8 blinds então faz all in
			if(stack_jogador < 8 || (stack_oponente+call) < 8)
				opcao[0] = max;
			// O jogador ou o oponente tem mais que 8bb e menos que 12
			if((stack_jogador >= 8 && stack_jogador < 12) || (stack_oponente >= 8 && stack_oponente < 12))
			{
				// Se é dealer
				if(jogador == dealer)
				{
					// Faz all in com cartas do grupo 8 ou melhor
					if(grupoCartas <= 8)
						opcao[0] = max;
					// Faz fold
					else
						opcao[0] = 1;
				}
				else
				{
					// Faz all in com cartas do grupo 7 ou melhor
					if(grupoCartas <= 7)
						opcao[0] = max;
					// Faz fold
					else
						opcao[0] = 1;
				}			
			}
			// O jogador ou o oponente tem mais que 12 big blinds
			if(stack_jogador >= 12 || stack_oponente >=12)
			{
				// Jogador é dealer
				if(jogador == dealer)
				{	
					// Se pertencer ao grupo 8 ou melhor
					if(grupoCartas <= 8)
					{
						// Se pertencer ao grupo 5 ou melhor aposta 6 blinds
						if(grupoCartas <= 5)
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 6*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 6*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
						// Aposta 3 big blinds	
						else
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 3*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 3*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
						
					}	
					// Fazer Fold / check
					else
						opcao[0] = 1;
				}
				// Jogador é blind
				else
				{
					// Se pertencer ao grupo 7 ou melhor 
					if(grupoCartas <= 7)
					{
						// Se pertencer ao grupo 4 ou melhor aposta 8 blinds
						if(grupoCartas <= 4)
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 8*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 8*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
						// Aumentar 4 big blinds
						else
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 4*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 4*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
					}	
					// Fazer Check
					else
						opcao[0] = 1;
				}	
			}
		}
		
		// O oponente apostou
		if(call > bb)
		{
			// Se o jogador ou o oponente tem menos que 2 big blinds faz call ou allin
			if(stack_jogador < 2 || (stack_oponente+call) < 2)
			{
				// menu fold_call 
				if(max == 5)
					opcao[0] = 2;
				// menu fold_allin 
				if(max == 2)
					opcao[0] = max;
			}
			// Se o jogador ou o oponente tem entre 2 e 4 big blinds
			if( (stack_jogador >= 2 && stack_jogador < 4) || ((stack_oponente+call) <= 2 && (stack_oponente+call) < 4))
			{
				// Se pertencer ao grupo 7 ou melhor faz allin
				if(grupoCartas <= 7)
					opcao[0]=max;
				//Fazer fold
				else
					opcao[0] = 1;
			}
			// Se o jogador ou o oponente tem entre 4 e 12 big blinds
			if( (stack_jogador >= 4 && stack_jogador < 12) || ((stack_oponente+call) <= 4 && (stack_oponente+call) < 12))
			{
				// Se pertencer ao grupo 6 ou melhor faz allin
				if(grupoCartas <= 6)
					opcao[0]=max;
				//Fazer fold
				else
					opcao[0] = 1;
			}
			// Se o jogador ou o oponente tem mais que 12 big blinds
			if(stack_jogador >= 12 || (stack_oponente+call) >=12)
			{
				// Se o oponente fez uma aposta menor que 3 big blinds
				if(call <= 3*bb)
				{
					// Se pertencer ao grupo 7 ou melhor 
					if(grupoCartas <= 7)
					{
						// Se pertencer ao grupo 5 ou melhor aposta 4x a aposta do oponente
						if(grupoCartas <= 5)
						{
							// Verificar se tem dinheiro para subir 2*call
							if(fichas[jogador] > 4*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (4*call);
							}
							else
								opcao[0] = max;
							
							// Se for menu fold_all in
							if(max == 2)
								opcao[0] = max;
						}
						// Fazer call
						else
							opcao[0] = 2;
					}
					// Fazer fold
					else
						opcao[0] = 1;	
				}
				// Se o oponente fez uma aposta maior que 3 big blinds
				if(call > 3*bb && call <= 6*bb)
				{
					// Se pertencer ao grupo 6 ou melhor 
					if(grupoCartas <= 6)
					{
						// Se pertencer ao grupo 4 ou melhor aposta 2x a aposta do oponente
						if(grupoCartas <= 4)
						{
							// Se pertencer ao grupo 2 ou melhor aposta 4x a aposta do oponente
							if(grupoCartas <= 2)
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							else
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							
						}
						// Faz call
						else
							opcao[0] = 2;
					}
					// Faz fold
					else
						opcao[0] = 1;
				}
				// Se o oponente fez uma aposta entre 6 e 12 big blinds
				if(call > 6*bb && call <= 12*bb)
				{
					// Se pertencer ao grupo 5 ou melhor 
					if(grupoCartas <= 5)
					{
						// Se pertencer ao grupo 3 ou melhor aposta 2x a aposta do oponente
						if(grupoCartas <= 3)
						{
							// Se pertencer ao grupo 2 ou melhor aposta 4x a aposta do oponente
							if(grupoCartas <= 2)
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							else
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}					
						}
						// Faz call
						else
							opcao[0] = 2;
					}
					// Faz fold
					else
						opcao[0] = 1;
				}
				// Se o oponente fez uma aposta maior que 12 big blinds
				if(call > 12*bb)
				{
					// Se pertencer ao grupo 4 ou melhor 
					if(grupoCartas <= 4)
					{
						// Se pertencer ao grupo 3 ou melhor aposta 2x a aposta do oponente
						if(grupoCartas <= 3)
						{
							// Se pertencer ao grupo 2 ou melhor aposta 4x a aposta do oponente
							if(grupoCartas <= 2)
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							else
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							
						}
						// Faz call
						else
							opcao[0] = 2;
					}
					// Faz fold
					else
						opcao[0] = 1;
				}		
			}		
		}	
		return opcao;
	}
	
	// Estratégia de apostas quando as apostas estão na ronda do flop e do turn
	public int[] flop_certo(int jogador, int dealer, float[] fichas,float tempPote, float[] aposta,float[] probabilidades, 
				Cartas[] cartas, Cartas[] mesa, int max, int ronda)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		// Guarda as opcoes de apostas 
		int[] opcao = new int[2];
		// Valor da big blind
		int bb = 20;
		// Fichas do jogador e do oponente convertidas para big blinds
		int stack_jogador = (int)fichas[jogador] / bb;
		int stack_oponente = (int)fichas[oponente] / bb;
		// Valor do pote
		float pote = fichas[2] + tempPote;
		// Valor do raise do oponente
		float call = (aposta[oponente] - aposta[jogador]);
		// Recebe o valor das potOdds
		float potOdds;
		// Recebe o resultado da mão do jogador
		int resultado;
		// Recebe a probabilidade de o oponente ter certa mão
		float[] percentagemMaoOponente = new float[11];
		// Recebe a probabilidade de sair uma determinada mão
		float[] percentagemMaoPotencial = new float[11];
		// Recebe o somatório da probabilidade de o oponente ter pior jogo
		float probabilidadePiorJogo = 0;
		// Recebe o somatório da probabilidade de o jogador melhorar o seu jogo
		float probabilidadeMelhorarMao = 0;		
		
		if(ronda == 1)
		{
			// Obter o resultado da mâo do jogador
			resultado = avaliar.avaliar(cartas,mesa, 5);
			// Obter a mão potencial do oponente
			percentagemMaoOponente = prob.potencialMaoFlop(mesa);
			// Obter a probabilidade de sair certa mão
			percentagemMaoPotencial = prob.potencialFlop(cartas, mesa);
			// Calcular a probabilidade de ter pior jogo que o oponente
			for(int i = resultado; i< percentagemMaoOponente.length; i++)
				probabilidadePiorJogo += percentagemMaoOponente[i];
			// Calcular a probabilidade de melhor o jogo 
			for(int i = resultado+1; i< percentagemMaoPotencial.length; i++)
				probabilidadeMelhorarMao += percentagemMaoPotencial[i];
		}
		else if(ronda == 2)
		{
			// Obter o resultado da mâo do jogador
			resultado = avaliar.avaliar(cartas,mesa, 6);
			// Obter a mão potencial do oponente
			percentagemMaoOponente = prob.potencialMaoTurn(mesa);
			// Obter a probabilidade de sair certa mão
			percentagemMaoPotencial = prob.potencialTurn(cartas, mesa);
			// Calcular a probabilidade de ter pior jogo que o oponente
			for(int i = resultado; i< percentagemMaoOponente.length; i++)
				probabilidadePiorJogo += percentagemMaoOponente[i];
			// Calcular a probabilidade de melhor o jogo 
			for(int i = resultado+1; i< percentagemMaoPotencial.length; i++)
				probabilidadeMelhorarMao += percentagemMaoPotencial[i];
		}
		
		// Se o oponente não apostou
		if(call == 0 )
		{
			// Se a probabilidade de ganhar for menor que 50 faz check
			if(probabilidades[jogador] < 50)
				opcao[0] = 1;
			// Se a probabilidade de ganhar for maior que 50 fica em jogo
			else
			{
				// Se o jogador ou o oponente tem menos que 8 big blinds então faz all in
				if(stack_jogador < 8 || stack_oponente < 8)
					opcao[0] = max;
				// Se o jogador ou o oponente tem mais que 8 big blinds e menos que 12 big blinds 
				if((stack_jogador >= 8 && stack_jogador < 12) || (stack_oponente >= 8 && stack_oponente < 12))
				{
					// Se a probabilidade de melhorar o jogo for maior que 70 então aumenta 5BB
					if(probabilidadeMelhorarMao > 70)
					{
							opcao[0] = 4;
							opcao[1] = 5*bb;
					}
					// Se a probabilidade de ter pior jogo for menor que 40 faz allin
					else if(probabilidadePiorJogo < 40)
						opcao[0] = max;
					// Senao faz check
					else
						opcao[0] = 1;			
				}
				// O jogador ou o oponente tem mais que 8 bb e menos que 12 bb
				if(stack_jogador >= 12 || stack_oponente >=12)
				{
					// Se a probabilidade de ter pior jogo for menor que 40
					if(probabilidadePiorJogo < 40)
					{
						// Se a probabilidade de ter pior jogo for menor que 20 aposta 12 bb
						if(probabilidadePiorJogo < 20)
						{
							opcao[0] = 3;
							opcao[1] = bb*12;
						}
						// Senão aposta 8 big blinds
						else
						{
							opcao[0] = 3;
							opcao[1] = bb*8;
						}
					}
					// Se a probabilidade de ter melhor jogo for menor que 40
					else
					{
						// Se a probabilidade de melhorar a mão for maior que 80 aposta 8 big blind
						if(probabilidadeMelhorarMao > 80)
						{
							opcao[0] = 3;
							opcao[1] = bb*8;
						}
						// Senao faz check
						else
							opcao[0] = 1;
					}
				}
			}
		}	

		// Se o oponente apostou
		else
		{
			// Calcular potOdds
			potOdds = call / (pote+call);
			potOdds *= 100;
			// Se pot odds for maior que a probabilidade de ganhar então faz fold
			if(potOdds > probabilidades[jogador] || probabilidades[jogador] < 50)
				opcao[0] = 1;
			// Senão continua em jogo
			else
			{
				// O jogador ou o oponente tem menos que 4 big blinds faz call ou allin
				if(stack_jogador < 4 || (stack_oponente + call) < 4)
				{
					// menu fold_call 
					if(max == 5)
					{
						/* Se a probabilidade de ganhar for maior que 50 
						 * ou a probabilidade de ter um jogo pior que o oponente for menor que 50
						 * ou a probabilidade de melhorar a mâo ser maior que 50
						 * faz all-in
						 */
						if(probabilidadePiorJogo < 50 || probabilidadeMelhorarMao > 50)
							opcao[0] = max;
						// Senão faz call
						else
							opcao[0] = 2;
					}
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem entre 4 e 8 big blinds
				else if( (stack_jogador >= 4 && stack_jogador < 8) || ((stack_oponente+call) <= 4 && (stack_oponente+call) < 8))
				{ 			
					// menu fold_call 
					if(max == 5)
					{
						/* Se a probabilidade de ganhar for maior que 60 
						 * ou a probabilidade de ter um jogo pior que o oponente for menor que 40
						 * ou a probabilidade de melhorar a mâo ser maior que 60
						 * faz all-in
						 */
						if(probabilidades[jogador] > 60 || probabilidadePiorJogo < 40 || probabilidadeMelhorarMao > 60)
							opcao[0] = max;
						// Senão faz call
						else
							opcao[0] = 2;
					}
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem entre 8 e 15 big blinds
				else if( (stack_jogador >= 8 && stack_jogador < 15) || ((stack_oponente+call) <= 8 && (stack_oponente+call) < 15))
				{
					/* Se a probabilidade de ganhar for maior que 75  
					 * ou a probabilidade de ter um jogo pior que o oponente for menor que 35
					 * ou a probabilidade de melhorar a mâo ser maior que 75
					 * faz all-in
					 */
					if(probabilidades[jogador] > 75 || probabilidadePiorJogo < 35 || probabilidadeMelhorarMao > 75)
						opcao[0] = max;
					/* Se a probabilidade de ganhar for maior que 65 
					 * ou a probabilidade de ter um jogo pior que o oponente for menor que 35
					 * ou a probabilidade de melhorar a mâo ser maior que 70
					 * Aposta 2 * call
					 */
					else if(probabilidades[jogador] > 65 || probabilidadePiorJogo < 40 || probabilidadeMelhorarMao > 70)
					{
						// Verificar se tem dinheiro para subir 2*call
						if(fichas[jogador] > 2*call)
						{
							opcao[0] = 4;
							opcao[1] = (int) (2*call);
						}
						else
							opcao[0] = max;
						
						// menu fold_allin 
						if(max == 2)
							opcao[0] = max;
					}
					else
						opcao[0] = 2;
				}
				// O jogador ou o oponente tem mais que 15 big blinds
				else if(stack_jogador >= 15 || (stack_oponente+call) >=15)
				{
					// Se o oponente fez uma aposta menor que  5 big blinds
					if(call <= 5*bb)
					{
						// Se o jogador for o dealer
						if(dealer == jogador)
						{
							 /* * A probabilidade de ganhar for maior que 75 
								* ou a probabilidade de ter um jogo pior que o oponente for menor que 35
							 */
							if(probabilidades[jogador] > 75 || probabilidadePiorJogo < 35 )
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// menu fold_allin 
								if(max == 2)
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 60
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 60)
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// menu fold_allin 
								if(max == 2)
									opcao[0] = max;
							}
							/* A probabilidade de melhor o jogo for maior que 60
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 60)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// menu fold_allin 
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
						// Se o jogador for a blind
						if(dealer != jogador)
						{
							 /* * A probabilidade de ganhar for maior que 80 ou 
								* a probabilidade de ter um jogo pior que o oponente for menor que 30
							 */
							if(probabilidades[jogador] > 80 || probabilidadePiorJogo < 30 )
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
									
									// Se for menu fold_all in
									if(max == 2)
										opcao[0] = max;
								}
								// Senão faz allin
								else
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 65
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 65)
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
									
									// Se for menu fold_all in
									if(max == 2)
										opcao[0] = max;
								}
								// Senão faz allin
								else
									opcao[0] = max;
							}
							/* A probabilidade de melhor o jogo for maior que 65
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 65)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
						
					}
					// Se o oponente fez uma aposta maior que  5 big blinds
					if(call > 5*bb)
					{
						
						// Se o jogador for o dealer
						if(dealer == jogador)
						{
							 /* A probabilidade de ganhar for maior que 75 e
							 * a probabilidade de ter um jogo pior que o oponente for menor que 35
							 * Quadriplica a aposta
							 */
							if(probabilidades[jogador] > 75 && probabilidadePiorJogo < 35)
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 65
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 65)
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							/* A probabilidade de melhor o jogo for maior que 80
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 80)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
						
						// Se o jogador for a blind
						if(dealer != jogador)
						{
							 /* A probabilidade de ganhar for maior que 80 e
							 * a probabilidade de ter um jogo pior que o oponente for menor que 30
							 * Quadriplica a aposta
							 */
							if(probabilidades[jogador] > 80 && probabilidadePiorJogo < 30)
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 75 
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 75)
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							/* A probabilidade de melhor o jogo for maior que 85
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 85)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
					}		
				}	
			}
		}

		return opcao;
	}

	// Apostas no river
	public int[] river_certo(int jogador, int dealer, float[] fichas,float tempPote, float[] aposta,float[] probabilidades, 
			Cartas[] cartas, Cartas[] mesa, int max)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		// Guarda as opcaoes de apostas 
		int[] opcao = new int[2];
		// Valor da big blind
		int bb = 20;
		// Fichas do jogador e do oponente convertidas para big blinds
		int stack_jogador = (int)fichas[jogador] / bb;
		int stack_oponente = (int)fichas[oponente] / bb;
		// Valor do pote
		float pote = fichas[2] + tempPote;
		// Valor do raise do oponente
		float call = (aposta[oponente] - aposta[jogador]);
		// Recebe o valor das potOdds
		float potOdds;
			
		// Se não existe apostas
		if(call == 0 )
		{
			// Se a probabilidade de ganhar for menor que 50 faz check
			if(probabilidades[jogador] < 50)
				opcao[0] = 1;
			else
			{
				// Se o jogador ou o oponente tem menos que 8 blinds então faz all in
				if(stack_jogador < 8 || stack_oponente < 8)
					opcao[0] = max;
				// O jogador ou o oponente tem mais que 8bb e menos que 12
				if((stack_jogador >= 8 && stack_jogador < 12) || (stack_oponente >= 8 && stack_oponente < 12))
				{
					// Se a probabilidade de ganhar o jogo for maior que 70
					if(probabilidades[jogador] > 70)
						opcao[0] = max;
					// Senao faz check
					else
						opcao[0] = 1;
				}
				// O jogador ou o oponente tem mais que 8bb e menos que 12
				if(stack_jogador >= 12 || stack_oponente >=12)
				{
					// Se a probabilidade de ganhar o jogo for maior que 70
					if(probabilidades[jogador] > 70)
					{
						
						// Se a probabilidade de ganhar o jogo for maior que 80 aposta 12 blinds
						if(probabilidades[jogador] > 80)
						{
							opcao[0] = 3;
							opcao[1] = bb*12;
						}
						// Senão aposta 8 big blinds
						else
						{
							opcao[0] = 3;
							opcao[1] = bb*8;
						}
					}
					// Senão faz check
					else
						opcao[0] = 1;	
				}		
			}	
		}
		// Se o oponente apostou
		else
		{
			// Calcular potODds
			potOdds = call / (pote+call);
			potOdds *= 100;
			// Se pot odds for maior que a probabilidade de ganhar então faz fold
			if(potOdds > probabilidades[jogador] || probabilidades[jogador] < 50)
				opcao[0] = 1;
			// Senão continua em jogo
			else
			{
				// O jogador ou o oponente tem menos que 4 big blinds faz call ou allin
				if(stack_jogador < 4 || (stack_oponente+call) < 4)
				{
					// menu fold_call 
					if(max == 5)
					{
						// Se a probabilidade de ganhar o jogo for maior que 60
						if(probabilidades[jogador] > 60)
							opcao[0] = max;
						else
							opcao[0] = 2;
					}
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem entre 4 e 8 big blinds
				if( (stack_jogador >= 4 && stack_jogador < 8) || ((stack_oponente+call) <= 4 && (stack_oponente+call) < 8))
				{ 
					
					// menu fold_call 
					if(max == 5)
					{
						// Se a probabilidade de ganhar o jogo for maior que 70
						if(probabilidades[jogador] > 70)
							opcao[0] = max;
						// Senão faz check
						else
							opcao[0] = 2;
					}
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem entre 8 e 15 big blinds
				if( (stack_jogador >= 8 && stack_jogador < 15) || ((stack_oponente+call) <= 8 && (stack_oponente+call) < 15))
				{
					// Se a probabilidade de ganhar o jogo for maior que 75
					if(probabilidades[jogador] > 75)
						opcao[0] = max;
					// Senão faz check
					else
						opcao[0] = 2;
				
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				
				// O jogador ou o oponente tem mais que 15 big blinds
				if(stack_jogador >= 15 || (stack_oponente+call) >=15)
				{
					// Se o oponente fez uma aposta menor que  5 big blinds
					if(call <= 5*bb)
					{
						// Se a probabilidade de ganhar o jogo for maior que 85
						if(probabilidades[jogador] > 85)
						{
							// Verificar se é possivel quadriplicar aposta
							// Se sim quadriplica a aposta do oponente senão faz allin
							if(fichas[jogador] > 4*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (4*call);
							}
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
						}
						else
							opcao[0] = 2;
					
						// menu fold_allin 
						if(max == 2)
							opcao[0] = max;
					}
					// Se o oponente fez uma aposta maior que  5 big blinds
					if(call > 5*bb)
					{
						// Se a probabilidade de ganhar o jogo for maior que 95 faz allin
						if(probabilidades[jogador] > 95)
							opcao[0] = max;
						// Se a probabilidade de ganhar o jogo for maior que 90 quadriplica a aposta
						else if(probabilidades[jogador] > 90)
						{
							// Verificar se tem dinheiro para subir 4*call
							if(fichas[jogador] > 4*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (4*call);
							}
							// Senão faz allin
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
							
						}
						// Se a probabilidade de ganhar o jogo for maior que 80 triplica a aposta
						else if(probabilidades[jogador] > 80)
						{
							// Verificar se tem dinheiro para subir 3*call
							if(fichas[jogador] > 3*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (3*call);
							}
							// Senão faz allin
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
						}
						// Se a probabilidade de ganhar o jogo for maior que 70 faz check
						else if(probabilidades[jogador] > 70)
						{
							// Verificar se tem dinheiro para fazer call
							if(fichas[jogador] > call)
								opcao[0] = 2;
							// Senão faz allin
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
						}
						// Fazer fold
						else
							opcao[0] = 1;
					}		
				}
			}	
		}
		
		return opcao;
	}
	
	/* Simulador de um jogador que joga de forma agressiva, ou seja, joga também cartas menos fortes e faz apostas mais altas */
	public int[] bot_agressivo(int jogador, int dealer, float[] fichas, float[] aposta, float tempPote, int ronda, 
			int menu, Cartas[] cartas, Cartas[] mesa, float[] probabilidades) 
	{
		
		int[] opcao = new int[2];
			
		// Verificar em que ronda se encontra o jogo
		// Pre-Flop
		if(ronda == 0)
			opcao = preFlop_agressivo(jogador, dealer, fichas, tempPote, aposta, cartas, menu);
		else
		{
			// Flop e turn 
			if(ronda == 1 || ronda == 2)
				opcao = flop_agressivo(jogador, dealer, fichas, tempPote, aposta, probabilidades, cartas, mesa, menu, ronda);
			if(ronda == 3)
				opcao = river_agressivo(jogador, dealer, fichas, tempPote, aposta, probabilidades, cartas, mesa, menu);
		}
			

		return opcao;
	}
	
	// Apostas antes de flop
	public int[] preFlop_agressivo(int jogador, int dealer, float[] fichas, float tempPote, float[] aposta, Cartas[] cartas, int max)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		// Guarda as opcaoes de apostas 
		int[] opcao = new int[2];
		// Valor da big blind
		int bb = 20;
		// Fichas do jogador e do oponente convertidas para big blinds
		int stack_jogador;
		int stack_oponente; 
		if(fichas[jogador] > bb)
			stack_jogador = (int)fichas[jogador] / bb;
		else
			stack_jogador = 0;
		if(fichas[oponente] > bb)
			stack_oponente = (int)fichas[oponente] / bb;
		else
			stack_oponente = 0;
		// Recebe o número do grupo a que as cartas do jogador pertence
		int grupoCartas = grupo(cartas);
		// Valor do raise do oponente
		int call;
		if(stack_jogador > 0 && stack_oponente > 0)
			call = (int)(aposta[oponente] - aposta[jogador]);
		else
			call = 0;

		// O oponente não aumentou
		if(call <= bb) 
		{
			// Se o jogador ou o oponente tem menos que 8 blinds então faz all in
			if(stack_jogador < 8 || (stack_oponente+call) < 8)
				opcao[0] = max;
			// O jogador ou o oponente tem mais que 8bb e menos que 12
			if((stack_jogador >= 8 && stack_jogador < 12) || ((stack_oponente+call) >= 8 && (stack_oponente+call) < 12))
			{	
				// Faz all in com cartas do grupo 8 ou melhor
				if(grupoCartas <= 8)
					opcao[0] = max;
				// Faz fold
				else
					opcao[0] = 1;
				
			}
			// O jogador ou o oponente tem mais que 8bb e menos que 12
			if(stack_jogador >= 12 || (stack_oponente+call) >=12)
			{
				// Jogador é dealer
				if(jogador == dealer)
				{	
					// Se pertencer ao grupo 8
					if(grupoCartas <= 8)
					{
						// Se pertencer ao grupo 6 ou melhor aposta 10 blinds
						if(grupoCartas <= 6)
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 10*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 10*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
						// Aumentar 5 big blinds	
						else
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 5*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 5*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
						
					}	
					// Fazer Fold / check
					else
						opcao[0] = 1;
				}
				// Jogador é blind
				else
				{
					// Se pertencer ao grupo 8 ou melhor
					if(grupoCartas <= 8)
					{
						// Se pertencer ao grupo 5 ou melhor aposta 12 blinds
						if(grupoCartas <= 5)
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 12*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 12*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
						// Aumentar 8 big blinds
						else
						{
							// menu fold_call 
							if(max == 5)
							{
								opcao[0] = 4;
								opcao[1] = 8*bb;
							}
							// menu check 
							if(max == 4)
							{
								opcao[0] = 3;
								opcao[1] = 8*bb;
							}
							//Menu fold_call
							if(max == 2)
								opcao[0] = max;
						}
					}	
					// Fazer Check
					else
						opcao[0] = 1;
				}	
			}
		}
		
		// Com aumento do oponente
		if(call > bb)
		{
			// O jogador ou o oponente tem menos que 2 big blinds faz call ou allin
			if(stack_jogador < 2 || (stack_oponente+call) < 2)
			{
				// menu fold_call 
				if(max == 5)
					opcao[0] = 2;
				// menu fold_allin 
				if(max == 2)
					opcao[0] = max;
			}
			// O jogador ou o oponente tem entre 2 e 4 big blinds
			if( (stack_jogador >= 2 && stack_jogador < 4) || ((stack_oponente+call) <= 2 && (stack_oponente+call) < 4))
			{
				// Se pertencer ao grupo 8 ou melhor faz allin
				if(grupoCartas <= 8)
					opcao[0]=max;
				//Fazer fold
				else
					opcao[0] = 1;
			}
			// O jogador ou o oponente tem entre 4 e 12 big blinds
			if( (stack_jogador >= 4 && stack_jogador < 12) || ((stack_oponente+call) <= 4 && (stack_oponente+call) < 12))
			{
				// Se pertencer ao grupo 8 ou melhor faz allin
				if(grupoCartas <= 7)
					opcao[0]=max;
				//Fazer fold
				else
					opcao[0] = 1;
			}
			// O jogador ou o oponente tem mais que 12 big blinds
			if(stack_jogador >= 12 || (stack_oponente+call) >=12)
			{
				// Se o oponente fez uma aposta menor que 5 big blinds
				if(call <= 5*bb)
				{
					// Se pertencer ao grupo 8 ou melhor
					if(grupoCartas <= 8)
					{
						// Se pertencer ao grupo 6 ou melhor aposta 5x a aposta
						if(grupoCartas <= 6)
						{
							// Verificar se tem dinheiro para subir 2*call
							if(fichas[jogador] > 5*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (5*call);
							}
							else
								opcao[0] = max;
							
							// Se for menu fold_all in
							if(max == 2)
								opcao[0] = max;
						}
						// Fazer call
						else
							opcao[0] = 2;
					}
					// Fazer fold
					else
						opcao[0] = 1;	
				}
				// Se o oponente fez uma aposta maior que 5 big blinds e menor que 10 big blinds
				if(call > 5*bb && call <= 10*bb)
				{
					// Se pertencer ao grupo 7 ou melhor 
					if(grupoCartas <= 7)
					{
						// Se pertencer ao grupo 5 ou melhor aposta 3x a aposta
						if(grupoCartas <= 5)
						{
							// Se pertencer ao grupo 3 ou melhor aposta 6x a aposta
							if(grupoCartas <= 3)
							{
								// Verificar se tem dinheiro para subir 6*call
								if(fichas[jogador] > 6*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (6*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							else
							{
								// Verificar se tem dinheiro para subir 3*call
								if(fichas[jogador] > 3*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (3*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}			
						}
						// Faz call
						else
							opcao[0] = 2;
					}
					// Faz fold
					else
						opcao[0] = 1;
				}
				// Se o oponente fez uma aposta entre 10 e 15 big blinds
				if(call > 10*bb && call <= 15*bb)
				{
					// Se pertencer ao grupo 6 ou melhor 
					if(grupoCartas <= 6)
					{
						// Se pertencer ao grupo 5 ou melhor aposta 2x a aposta
						if(grupoCartas <= 5)
						{
							// Se pertencer ao grupo 3 ou melhor aposta 4x a aposta
							if(grupoCartas <= 3)
							{
								// Verificar se tem dinheiro para subir 4*call
								if(fichas[jogador] > 4*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (4*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							else
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}				
						}
						// Faz call
						else
							opcao[0] = 2;
					}
					// Faz fold
					else
						opcao[0] = 1;
				}
				// Se o oponente fez uma aposta maior que 15 big blinds
				if(call > 15*bb)
				{
					// Se pertencer ao grupo 6 ou melhor 
					if(grupoCartas <= 6)
					{
						// Se pertencer ao grupo 5 ou melhor aposta 3x a aposta
						if(grupoCartas <= 5)
						{
							// Se pertencer ao grupo 4 ou melhor aposta 4x a aposta
							if(grupoCartas <= 4)
							{
								// Verificar se tem dinheiro para subir 5*call
								if(fichas[jogador] > 5*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (5*call);
								}
								else
									opcao[0] = max;
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							else
							{
								// Verificar se tem dinheiro para subir 3*call
								if(fichas[jogador] > 3*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (3*call);
								}
								else
									opcao[0] = max;
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							
						}
						// Faz call
						else
							opcao[0] = 2;
					}
					// Faz fold
					else
						opcao[0] = 1;
				}		
			}		
		}
		
		return opcao;
	}
	
	// Estratégia de apostas quando as apostas estão na ronda do flop
	public int[] flop_agressivo(int jogador, int dealer, float[] fichas,float tempPote, float[] aposta,float[] probabilidades, 
				Cartas[] cartas, Cartas[] mesa, int max, int ronda)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		// Guarda as opcoes de apostas 
		int[] opcao = new int[2];
		// Valor da big blind
		int bb = 20;
		// Fichas do jogador e do oponente convertidas para big blinds
		int stack_jogador = (int)fichas[jogador] / bb;
		int stack_oponente = (int)fichas[oponente] / bb;
		// Valor do pote
		float pote = fichas[2] + tempPote;
		// Valor do raise do oponente
		float call = (aposta[oponente] - aposta[jogador]);
		// Recebe o valor das potOdds
		float potOdds;
		// Recebe o resultado da mão do jogador
		int resultado;
		// Recebe a probabilidade de o oponente ter certa mão
		float[] percentagemMaoOponente = new float[11];
		// Recebe a probabilidade de sair uma determinada mão
		float[] percentagemMaoPotencial = new float[11];
		// Recebe o somatório da probabilidade de o oponente ter pior jogo
		float probabilidadePiorJogo = 0;
		// Recebe o somatório da probabilidade de o jogador melhorar o seu jogo
		float probabilidadeMelhorarMao = 0;		
		
		if(ronda == 1)
		{
			// Obter o resultado da mâo do jogador
			resultado = avaliar.avaliar(cartas,mesa, 5);
			// Obter a mão potencial do oponente
			percentagemMaoOponente = prob.potencialMaoFlop(mesa);
			// Obter a probabilidade de sair certa mão
			percentagemMaoPotencial = prob.potencialFlop(cartas, mesa);
			// Calcular a probabilidade de ter pior jogo que o oponente
			for(int i = resultado; i< percentagemMaoOponente.length; i++)
				probabilidadePiorJogo += percentagemMaoOponente[i];
			// Calcular a probabilidade de melhor o jogo 
			for(int i = resultado+1; i< percentagemMaoPotencial.length; i++)
				probabilidadeMelhorarMao += percentagemMaoPotencial[i];
		}
		else if(ronda == 2)
		{
			// Obter o resultado da mâo do jogador
			resultado = avaliar.avaliar(cartas,mesa, 6);
			// Obter a mão potencial do oponente
			percentagemMaoOponente = prob.potencialMaoTurn(mesa);
			// Obter a probabilidade de sair certa mão
			percentagemMaoPotencial = prob.potencialTurn(cartas, mesa);
			// Calcular a probabilidade de ter pior jogo que o oponente
			for(int i = resultado; i< percentagemMaoOponente.length; i++)
				probabilidadePiorJogo += percentagemMaoOponente[i];
			// Calcular a probabilidade de melhor o jogo 
			for(int i = resultado+1; i< percentagemMaoPotencial.length; i++)
				probabilidadeMelhorarMao += percentagemMaoPotencial[i];
		}
		
		// Se não existe apostas
		if(call == 0 )
		{
			// Se a probabilidade de ganhar for menor que 50 faz check
			if(probabilidades[jogador] < 50)
				opcao[0] = 1;
			// Se a probabilidade de ganhar for maior que 50 fica em jogo
			else
			{
				// Se o jogador ou o oponente tem menos que 8 big blinds então faz all in
				if(stack_jogador < 8 || stack_oponente < 8)
					opcao[0] = max;
				// Se o jogador ou o oponente tem mais que 8big blinds e menos que 12 big blinds 
				if((stack_jogador >= 8 && stack_jogador < 12) || (stack_oponente >= 8 && stack_oponente < 12))
				{
					// Se a probabilidade de melhorar o jogo for maior que 60 então aumenta 7BB
					if(probabilidadeMelhorarMao > 60)
					{
							opcao[0] = 4;
							opcao[1] = 7*bb;
					}
					// Se a probabilidade de ter pior jogo for menor que 50 faz allin
					else if(probabilidadePiorJogo < 50)
						opcao[0] = max;
					// Senao faz check
					else
						opcao[0] = 1;			
				}
				// O jogador ou o oponente tem mais que 8bb e menos que 12
				if(stack_jogador >= 12 || stack_oponente >=12)
				{
					// Se a probabilidade de ter pior jogo for menor que 60 aposta 8 big blinds
					if(probabilidadePiorJogo < 60)
					{
						// Se a probabilidade de ter pior jogo for menor que 40 aposta 12 blinds
						if(probabilidadePiorJogo < 40)
						{
							opcao[0] = 3;
							opcao[1] = bb*12;
						}
						// Senão aposta 8 big blinds
						else
						{
							opcao[0] = 3;
							opcao[1] = bb*8;
						}
					}
					// Se a probabilidade de ter melhor jogo for menor que 60
					else
					{
						// Se a probabilidade de melhorar a mão for maior que 70 aposta 10 big blind
						if(probabilidadeMelhorarMao > 70)
						{
							opcao[0] = 3;
							opcao[1] = bb*10;
						}
						// Senao faz check
						else
							opcao[0] = 1;
					}
				}
			}
		}	

		// Se o oponente apostou
		else
		{
			// Calcular potOdds
			potOdds = call / (pote+call);
			potOdds *= 100;
			// Se pot odds for maior que a probabilidade de ganhar então faz fold
			if(potOdds > probabilidades[jogador] || probabilidades[jogador] < 50)
				opcao[0] = 1;
			// Senão continua em jogo
			else
			{
				// O jogador ou o oponente tem menos que 4 big blinds faz call ou allin
				if(stack_jogador < 4 || (stack_oponente + call) < 4)
				{
					// menu fold_call 
					if(max == 5)
					{
						 /* Se a probabilidade de ter um jogo pior que o oponente for menor que 60
						 * ou a probabilidade de melhorar a mâo ser maior que 40
						 * faz all-in
						 */
						if(probabilidadePiorJogo < 60 || probabilidadeMelhorarMao > 40)
							opcao[0] = max;
						// Senão faz call
						else
							opcao[0] = 2;
					}
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem entre 4 e 8 big blinds
				else if( (stack_jogador >= 4 && stack_jogador < 8) || ((stack_oponente+call) <= 4 && (stack_oponente+call) < 8))
				{ 			
					// menu fold_call 
					if(max == 5)
					{
						/* Se a probabilidade de ganhar for maior que 50 ou 
						 * a probabilidade de ter um jogo pior que o oponente for menor que 50
						 * ou a probabilidade de melhorar a mâo ser maior que 50
						 * faz all-in
						 */
						if(probabilidades[jogador] > 50 || probabilidadePiorJogo < 50 || probabilidadeMelhorarMao > 50)
							opcao[0] = max;
						// Senão faz call
						else
							opcao[0] = 2;
					}
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem entre 8 e 15 big blinds
				else if( (stack_jogador >= 8 && stack_jogador < 15) || ((stack_oponente+call) <= 8 && (stack_oponente+call) < 15))
				{
					/* Se a probabilidade de ganhar for maior que 70 ou 
					 * a probabilidade de ter um jogo pior que o oponente for menor que 40
					 * ou a probabilidade de melhorar a mâo ser maior que 70
					 * faz all-in
					 */
					if(probabilidades[jogador] > 70 || probabilidadePiorJogo < 40 || probabilidadeMelhorarMao > 70)
						opcao[0] = max;
					/* Se a probabilidade de ganhar for maior que 55 ou 
					 * a probabilidade de ter um jogo pior que o oponente for menor que 45
					 * ou a probabilidade de melhorar a mâo ser maior que 60
					 * Aposta 2 * call
					 */
					else if(probabilidades[jogador] > 55 || probabilidadePiorJogo < 50 || probabilidadeMelhorarMao > 60)
					{
						// Verificar se tem dinheiro para subir 3*call
						if(fichas[jogador] > 3*call)
						{
							opcao[0] = 4;
							opcao[1] = (int) (3*call);
						}
						else
							opcao[0] = max;
					}
					else
						opcao[0] = 2;
				
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem mais que 15 big blinds
				else if(stack_jogador >= 15 || (stack_oponente+call) >=15)
				{
					// Se o oponente fez uma aposta menor que  7 big blinds
					if(call <= 7*bb)
					{
						// Se o jogador for o dealer
						if(dealer == jogador)
						{
							 /* * A probabilidade de ganhar for maior que 65 ou 
								* a probabilidade de ter um jogo pior que o oponente for menor que 45
							 */
							if(probabilidades[jogador] > 65 || probabilidadePiorJogo < 45 )
							{
								// Verificar se tem dinheiro para subir 5*call
								if(fichas[jogador] > 5*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (5*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// menu fold_allin 
								if(max == 2)
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 50
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 50)
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// menu fold_allin 
								if(max == 2)
									opcao[0] = max;
							}
							/* A probabilidade de melhor o jogo for maior que 50
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 50)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// menu fold_allin 
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
						// Se o jogador for a blind
						if(dealer != jogador)
						{
							 /* * A probabilidade de ganhar for maior que 70 ou 
								* a probabilidade de ter um jogo pior que o oponente for menor que 40
							 */
							if(probabilidades[jogador] > 70 || probabilidadePiorJogo < 40 )
							{
								// Verificar se tem dinheiro para subir 5*call
								if(fichas[jogador] > 5*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (5*call);
									
									// Se for menu fold_all in
									if(max == 2)
										opcao[0] = max;
								}
								// Senão faz allin
								else
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 55
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 55)
							{
								// Verificar se tem dinheiro para subir 3*call
								if(fichas[jogador] > 3*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (3*call);
									
									// Se for menu fold_all in
									if(max == 2)
										opcao[0] = max;
								}
								// Senão faz allin
								else
									opcao[0] = max;
							}
							/* A probabilidade de melhorar o jogo for maior que 55
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 55)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
						
					}
					// Se o oponente fez uma aposta maior que  5 big blinds
					if(call > 7*bb)
					{					
						// Se o jogador for o dealer
						if(dealer == jogador)
						{
							 /* A probabilidade de ganhar for maior que 65 e
							 * a probabilidade de ter um jogo pior que o oponente for menor que 45
							 * Quadriplica a aposta
							 */
							if(probabilidades[jogador] > 65 && probabilidadePiorJogo < 45)
							{
								// Verificar se tem dinheiro para subir 5*call
								if(fichas[jogador] > 5*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (5*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 65 
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 65)
							{
								// Verificar se tem dinheiro para subir 2*call
								if(fichas[jogador] > 2*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (2*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							/* A probabilidade de melhor o jogo for maior que 60
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 60)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
						
						// Se o jogador for a blind
						if(dealer != jogador)
						{
							 /* A probabilidade de ganhar for maior que 70 e
							 * a probabilidade de ter um jogo pior que o oponente for menor que 40
							 * Quadriplica a aposta
							 */
							if(probabilidades[jogador] > 70 && probabilidadePiorJogo < 40)
							{
								// Verificar se tem dinheiro para subir 5*call
								if(fichas[jogador] > 5*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (5*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							 /* A probabilidade de ganhar for maior que 65 
							 * Duplica a aposta
							 */
							else if(probabilidades[jogador] > 65)
							{
								// Verificar se tem dinheiro para subir 3*call
								if(fichas[jogador] > 3*call)
								{
									opcao[0] = 4;
									opcao[1] = (int) (3*call);
								}
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							/* A probabilidade de melhor o jogo for maior que 70
							 * Faz call
							 */
							else if(probabilidadeMelhorarMao > 70)
							{
								// Verificar se tem dinheiro para fazer call
								if(fichas[jogador] > call)
									opcao[0] = 2;
								// Senão faz allin
								else
									opcao[0] = max;
								
								// Se for menu fold_all in
								if(max == 2)
									opcao[0] = max;
							}
							// Senão faz fold
							else
								opcao[0] = 1;
						}
					}		
				}	
			}
		}
		return opcao;
	}

	// Apostas no river
	public int[] river_agressivo(int jogador, int dealer, float[] fichas,float tempPote, float[] aposta,float[] probabilidades, 
			Cartas[] cartas, Cartas[] mesa, int max)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		// Guarda as opcaoes de apostas 
		int[] opcao = new int[2];
		// Valor da big blind
		int bb = 20;
		// Fichas do jogador e do oponente convertidas para big blinds
		int stack_jogador = (int)fichas[jogador] / bb;
		int stack_oponente = (int)fichas[oponente] / bb;
		// Valor do pote
		float pote = fichas[2] + tempPote;
		// Valor do raise do oponente
		float call = (aposta[oponente] - aposta[jogador]);
		// Recebe o valor das potOdds
		float potOdds;
			
		// Se não existe apostas
		if(call == 0 )
		{
			// Se a probabilidade de ganhar for menor que 50 faz check
			if(probabilidades[jogador] < 50)
				opcao[0] = 1;
			else
			{
				// Se o jogador ou o oponente tem menos que 8 blinds então faz all in
				if(stack_jogador < 8 || stack_oponente < 8)
					opcao[0] = max;
				// O jogador ou o oponente tem mais que 8bb e menos que 12
				if((stack_jogador >= 8 && stack_jogador < 12) || (stack_oponente >= 8 && stack_oponente < 12))
				{
					// Se a probabilidade de ganhar o jogo for maior que 60
					if(probabilidades[jogador] > 60)
						opcao[0] = max;
					// Senao faz check
					else
						opcao[0] = 1;
				}
				// O jogador ou o oponente tem mais que 8bb e menos que 12
				if(stack_jogador >= 12 || stack_oponente >=12)
				{
					// Se a probabilidade de ganhar o jogo for maior que 60
					if(probabilidades[jogador] > 60)
					{
						
						// Se a probabilidade de ganhar o jogo for maior que 70 aposta 12 blinds
						if(probabilidades[jogador] > 70)
						{
							opcao[0] = 3;
							opcao[1] = bb*12;
						}
						// Senão aposta 8 big blinds
						else
						{
							opcao[0] = 3;
							opcao[1] = bb*8;
						}
					}
					// Senão faz check
					else
						opcao[0] = 1;	
				}		
			}	
		}
		// Se o oponente apostou
		else
		{
			// Calcular potODds
			potOdds = call / (pote+call);
			potOdds *= 100;
			// Se pot odds for maior que a probabilidade de ganhar então faz fold
			if(potOdds > probabilidades[jogador] || probabilidades[jogador] < 50)
				opcao[0] = 1;
			// Senão continua em jogo
			else
			{
				// O jogador ou o oponente tem menos que 4 big blinds faz call ou allin
				if(stack_jogador < 4 || (stack_oponente+call) < 4)
					opcao[0] = max;
				// O jogador ou o oponente tem entre 4 e 8 big blinds
				if( (stack_jogador >= 4 && stack_jogador < 8) || ((stack_oponente+call) <= 4 && (stack_oponente+call) < 8))
				{ 
					
					// menu fold_call 
					if(max == 5)
					{
						// Se a probabilidade de ganhar o jogo for maior que 60
						if(probabilidades[jogador] > 60)
							opcao[0] = max;
						// Senão faz check
						else
							opcao[0] = 2;
					}
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				// O jogador ou o oponente tem entre 8 e 15 big blinds
				if( (stack_jogador >= 8 && stack_jogador < 15) || ((stack_oponente+call) <= 8 && (stack_oponente+call) < 15))
				{
					// Se a probabilidade de ganhar o jogo for maior que 60
					if(probabilidades[jogador] > 60)
						opcao[0] = max;
					// Senão faz check
					else
						opcao[0] = 2;
				
					// menu fold_allin 
					if(max == 2)
						opcao[0] = max;
				}
				
				// O jogador ou o oponente tem mais que 15 big blinds
				if(stack_jogador >= 15 || (stack_oponente+call) >=15)
				{
					// Se o oponente fez uma aposta menor que 8 big blinds
					if(call <= 8*bb)
					{
						// Se a probabilidade de ganhar o jogo for maior que 75
						if(probabilidades[jogador] > 75)
						{
							// Verificar se é possivel aposta 6x a aposta
							// Se sim quadriplica a aposta do oponente senão faz allin
							if(fichas[jogador] > 6*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (6*call);
							}
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
						}
						else
							opcao[0] = 2;
					
						// menu fold_allin 
						if(max == 2)
							opcao[0] = max;
					}
					// Se o oponente fez uma aposta maior que 8 big blinds
					if(call > 5*bb)
					{
						// Se a probabilidade de ganhar o jogo for maior que 85 faz allin
						if(probabilidades[jogador] > 85)
							opcao[0] = max;
						// Se a probabilidade de ganhar o jogo for maior que 80 aposta 6x a aposta
						else if(probabilidades[jogador] > 80)
						{
							// Verificar se tem dinheiro para subir 6*call
							if(fichas[jogador] > 6*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (6*call);
							}
							// Senão faz allin
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
							
						}
						// Se a probabilidade de ganhar o jogo for maior que 70 quadriplica a aposta
						else if(probabilidades[jogador] > 70)
						{
							// Verificar se tem dinheiro para subir 4*call
							if(fichas[jogador] > 4*call)
							{
								opcao[0] = 4;
								opcao[1] = (int) (4*call);
							}
							// Senão faz allin
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
						}
						// Se a probabilidade de ganhar o jogo for maior que 50 faz call
						else if(probabilidades[jogador] > 50)
						{
							// Verificar se tem dinheiro para fazer call
							if(fichas[jogador] > call)
								opcao[0] = 2;
							// Senão faz allin
							else
								opcao[0] = max;
							
							// menu fold_allin 
							if(max == 2)
								opcao[0] = max;
						}
						// Fazer fold
						else
							opcao[0] = 1;
					}		
				}
			}	
		}
		
		return opcao;
	}
	
	// Definir o grupo a que cada mão pertence
	public int grupo(Cartas[] carta)
	{
		int grupo = 9;
	
		
		int[] nCarta = new int[2];
		int[] sCarta = new int[2];
		
		Boolean _AA = false;
		Boolean _KK = false;
		Boolean _QQ = false;
		Boolean _AKs = false;
		Boolean _AKo = false;
		Boolean _JJ = false;
		Boolean _AQs = false;
		Boolean _AQo = false;
		Boolean _TT = false;
		Boolean _99 = false;
		Boolean _AJs = false;
		Boolean _KQs = false;
		Boolean _88 = false;
		Boolean _77 = false;
		Boolean _AJo = false;
		Boolean _AT = false;
		Boolean _KQo = false;
		Boolean _KJs = false;
		Boolean _66 = false;
		Boolean _55 = false;
		Boolean _A9s_A2s = false;
		Boolean _KJo = false;
		Boolean _KTs = false;
		Boolean _QJs = false;
		Boolean _QTs = false;
		Boolean _JTs = false;
		Boolean _44 = false;
		Boolean _33 = false;
		Boolean _22 = false;
		Boolean _A9o_A2o = false;
		Boolean _KTo = false;
		Boolean _QJo = false;
		Boolean _QTo = false;
		Boolean _JTo = false;
		Boolean _T9s = false;
		Boolean _98s = false;
		Boolean _87s = false;
		Boolean _76s = false;
		Boolean _65s = false;
		Boolean _54s = false;
		Boolean _K9 = false;
		Boolean _K8 = false;
		Boolean _Q9s = false;
		Boolean _Q8s = false;
		Boolean _J9s = false;
		Boolean _T8s = false;
		Boolean _T9o = false;
		Boolean _97s = false;
		Boolean _98o = false;
		Boolean _86s = false;
		Boolean _87o = false;
		Boolean _75s = false;
		Boolean _76o = false;
		Boolean _64s = false;
		
		nCarta[0] = carta[0].getNumCarta();
		nCarta[1] = carta[1].getNumCarta();
		
		sCarta[0] = carta[0].getNaipe();
		sCarta[1] = carta[1].getNaipe();
		
		// AA
		if(nCarta[0] == 0 && nCarta[1] == 0)
			_AA = true;
		// KK
		if(nCarta[0] == 12 && nCarta[1] == 12)
			_KK = true;
		
		// Grupo 1
		if(_AA || _KK)
			grupo = 1;
		
		/* ------------------------------------------------ */
		
		// QQ
		if(nCarta[0] == 11 && nCarta[1] == 11)
			_QQ = true;
		// AK suited
		if((nCarta[0] == 0 && nCarta[1] == 12 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 12 && nCarta[1] == 0 && sCarta[0] ==  sCarta[1]))
			_AKs = true;
		// AK outsuited
		if((nCarta[0] == 0 && nCarta[1] == 12 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 12 && nCarta[1] == 0 && sCarta[0] !=  sCarta[1]))
			_AKo = true;
		// JJ
		if(nCarta[0] == 10 && nCarta[1] == 10)
			_JJ = true;
		
		// Grupo 2
		if(_QQ || _AKs || _AKo || _JJ)
			grupo = 2;
		
		/* ------------------------------------------------ */
		
		// AQ suited
		if((nCarta[0] == 0 && nCarta[1] == 11 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 11 && nCarta[1] == 0 && sCarta[0] ==  sCarta[1]))
			_AQs = true;
		// AQ outsuited
		if((nCarta[0] == 0 && nCarta[1] == 11 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 11 && nCarta[1] == 0 && sCarta[0] !=  sCarta[1]))
			_AQo = true;
		// TT
		if(nCarta[0] == 9 && nCarta[1] == 9)
			_TT = true;
		// 99
		if(nCarta[0] == 8 && nCarta[1] == 8)
			_99 = true;
			
		// Grupo 3
		if(_AQs || _AQo || _TT || _99)
			grupo = 3;
		
		/* ------------------------------------------------ */
		
		// AJ suited
		if((nCarta[0] == 0 && nCarta[1] == 10 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 10 && nCarta[1] == 0 && sCarta[0] ==  sCarta[1]))
			_AJs = true;
		// KQ suited
		if((nCarta[0] == 12 && nCarta[1] == 11 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 11 && nCarta[1] == 12 && sCarta[0] ==  sCarta[1]))
			_KQs = true;
		// 88
		if(nCarta[0] == 7 && nCarta[1] == 7)
			_88 = true;
		// 77
		if(nCarta[0] == 6 && nCarta[1] == 6)
			_77 = true;
			
		// Grupo 4
		if(_AJs || _KQs || _88 || _77)
			grupo = 4;
		
		/* ------------------------------------------------ */
		
		// AJ out suited
		if((nCarta[0] == 0 && nCarta[1] == 10 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 10 && nCarta[1] == 0 && sCarta[0] !=  sCarta[1]))
			_AJo = true;
		// AT
		if((nCarta[0] == 0 && nCarta[1] == 9 ) || (nCarta[0] == 9 && nCarta[1] == 0))
			_AT = true;
		// KQ out suited
		if((nCarta[0] == 12 && nCarta[1] == 11 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 11 && nCarta[1] == 12 && sCarta[0] !=  sCarta[1]))
			_KQo = true;
		// KJ suited
		if((nCarta[0] == 12 && nCarta[1] == 10 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 10 && nCarta[1] == 12 && sCarta[0] ==  sCarta[1]))
			_KJs = true;
		// 66
		if(nCarta[0] == 5 && nCarta[1] == 5)
			_66 = true;
		// 55
		if(nCarta[0] == 4 && nCarta[1] == 4)
			_55 = true;
		
		// Grupo 5
		if(_AJo || _AT || _KQo || _KJs || _66 || _55)
			grupo = 5;
		
		/* ------------------------------------------------ */
		
		// A9 suited até A2 suited
		if((nCarta[0] == 0 && (nCarta[1] > 1 && nCarta[1] < 8) && sCarta[0] == sCarta[1]) ||
				nCarta[1] == 0 && (nCarta[0] > 1 && nCarta[0] < 8) && sCarta[0] == sCarta[1])
			_A9s_A2s = true;
		// KJ out suited
		if((nCarta[0] == 12 && nCarta[1] == 10 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 10 && nCarta[1] == 12 && sCarta[0] !=  sCarta[1]))
			_KJo = true;
		// KT suited
		if((nCarta[0] == 12 && nCarta[1] == 9 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 9 && nCarta[1] == 12 && sCarta[0] ==  sCarta[1]))
			_KTs = true;
		// QJ suited
		if((nCarta[0] == 11 && nCarta[1] == 10 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 10 && nCarta[1] == 11 && sCarta[0] ==  sCarta[1]))
			_QJs = true;
		// QT suited
		if((nCarta[0] == 11 && nCarta[1] == 9 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 9 && nCarta[1] == 11 && sCarta[0] ==  sCarta[1]))
			_QTs = true;
		// JT suited
		if((nCarta[0] == 10 && nCarta[1] == 9 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 9 && nCarta[1] == 10 && sCarta[0] ==  sCarta[1]))
			_JTs = true;
		// 44
		if(nCarta[0] == 3 && nCarta[1] == 3)
			_44 = true;
		// 33
		if(nCarta[0] == 2 && nCarta[1] == 2)
			_33 = true;
		// 22
		if(nCarta[0] == 1 && nCarta[1] == 1)
			_22 = true;
		
		// Grupo 6
		if(_A9s_A2s || _KJo || _KTs || _QJs || _QTs || _JTs || _44 || _33 || _22)
			grupo = 6;
	
		/* ------------------------------------------------ */
		
		// A9 out suited até A2 out suited
		if((nCarta[0] == 0 && (nCarta[1] > 1 && nCarta[1] < 8) && sCarta[0] != sCarta[1]) ||
				nCarta[1] == 0 && (nCarta[0] > 1 && nCarta[0] < 8) && sCarta[0] != sCarta[1])
			_A9o_A2o = true;
		// KT out suited
		if((nCarta[0] == 12 && nCarta[1] == 9 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 9 && nCarta[1] == 12 && sCarta[0] !=  sCarta[1]))
			_KTo = true;
		// QJ out suited
		if((nCarta[0] == 11 && nCarta[1] == 10 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 10 && nCarta[1] == 11 && sCarta[0] !=  sCarta[1]))
			_QJo = true;
		// QT out suited
		if((nCarta[0] == 11 && nCarta[1] == 9 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 9 && nCarta[1] == 11 && sCarta[0] !=  sCarta[1]))
			_QTo = true;
		// JT out suited
		if((nCarta[0] == 10 && nCarta[1] == 9 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 9 && nCarta[1] == 10 && sCarta[0] !=  sCarta[1]))
			_JTo = true;
		// T9 suited
		if((nCarta[0] == 9 && nCarta[1] == 8 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 8 && nCarta[1] == 9 && sCarta[0] ==  sCarta[1]))
			_T9s = true;
		// 98 suited
		if((nCarta[0] == 8 && nCarta[1] == 7 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 7 && nCarta[1] == 8 && sCarta[0] ==  sCarta[1]))
			_98s = true;
		// 87 suited
		if((nCarta[0] == 7 && nCarta[1] == 6 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 6 && nCarta[1] == 7 && sCarta[0] ==  sCarta[1]))
			_87s = true;
		// 76 suited
		if((nCarta[0] == 6 && nCarta[1] == 5 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 5 && nCarta[1] == 6 && sCarta[0] ==  sCarta[1]))
			_76s = true;
		// 65 suited
		if((nCarta[0] == 5 && nCarta[1] == 4 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 4 && nCarta[1] == 5 && sCarta[0] ==  sCarta[1]))
			_65s = true;
		// 54 suited
		if((nCarta[0] == 4 && nCarta[1] == 3 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 3 && nCarta[1] == 4 && sCarta[0] ==  sCarta[1]))
			_54s = true;
		
		// Grupo 7
		if(_A9o_A2o || _KTo || _QJo || _QTo || _JTo || _T9s || _98s || _87s ||_76s ||_65s ||_54s )
			grupo = 7;
				
		/* ------------------------------------------------ */
		// K9
		if((nCarta[0] == 12 && nCarta[1] == 8 ) || (nCarta[0] == 8 && nCarta[1] == 12))
			_K9 = true;
		// K8
		if((nCarta[0] == 12 && nCarta[1] == 7 ) || (nCarta[0] == 7 && nCarta[1] == 12))
			_K8 = true;
		// Q9 suited
		if((nCarta[0] == 11 && nCarta[1] == 8 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 8 && nCarta[1] == 11 && sCarta[0] ==  sCarta[1]))
			_Q9s = true;
		// Q8 suited
		if((nCarta[0] == 11 && nCarta[1] == 7 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 7 && nCarta[1] == 11 && sCarta[0] ==  sCarta[1]))
			_Q8s = true;
		// J9 suited
		if((nCarta[0] == 10 && nCarta[1] == 8 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 8 && nCarta[1] == 10 && sCarta[0] ==  sCarta[1]))
			_J9s = true;
		// T8 suited
		if((nCarta[0] == 9 && nCarta[1] == 7 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 7 && nCarta[1] == 9 && sCarta[0] ==  sCarta[1]))
			_T8s = true;
		// T9 out suited
		if((nCarta[0] == 9 && nCarta[1] == 8 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 8 && nCarta[1] == 9 && sCarta[0] !=  sCarta[1]))
			_T9o = true;
		// 97 suited
		if((nCarta[0] == 8 && nCarta[1] == 6 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 6 && nCarta[1] == 8 && sCarta[0] ==  sCarta[1]))
			_97s = true;
		// 98 out suited
		if((nCarta[0] == 8 && nCarta[1] == 7 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 7 && nCarta[1] == 8 && sCarta[0] !=  sCarta[1]))
			_98o = true;
		// 86 suited
		if((nCarta[0] == 7 && nCarta[1] == 5 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 5 && nCarta[1] == 7 && sCarta[0] ==  sCarta[1]))
			_86s = true;
		// 87 out suited
		if((nCarta[0] == 7 && nCarta[1] == 6 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 6 && nCarta[1] == 7 && sCarta[0] !=  sCarta[1]))
			_87o = true;
		// 75 suited
		if((nCarta[0] == 6 && nCarta[1] == 4 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 4 && nCarta[1] == 6 && sCarta[0] ==  sCarta[1]))
			_75s = true;
		// 76 out suited
		if((nCarta[0] == 6 && nCarta[1] == 5 && sCarta[0] !=  sCarta[1] ) || 
				(nCarta[0] == 5 && nCarta[1] == 6 && sCarta[0] !=  sCarta[1]))
			_76o = true;
		// 64 suited
		if((nCarta[0] == 5 && nCarta[1] == 3 && sCarta[0] ==  sCarta[1] ) || 
				(nCarta[0] == 3 && nCarta[1] == 5 && sCarta[0] ==  sCarta[1]))
			_65s = true;
		
		// Grupo 8
		if( _K9 || _K8 || _Q9s || _Q8s || _J9s || _T8s || _T9o || _97s || _98o ||
				_86s || _87o || _75s || _76o || _64s)
			grupo = 8;
		
		return grupo;		
	}
	
}
