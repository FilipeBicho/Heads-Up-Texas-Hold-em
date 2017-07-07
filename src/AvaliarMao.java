/* Descricao: Esta classe recebe as cartas do jogador e da mesa e avalia a mão do jogador
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 19/04/2017
 */

public class AvaliarMao {

	/* Guarda a mão final do jogador */
	private static Cartas cincoCartas[] = new Cartas[5];
	
	/* Construtor vazio */
	AvaliarMao(){}
	
	/* Verifica a avaliação da mão */
	public int avaliar(Cartas[] jogador, Cartas[] mesa, int nCartas){
		
		/* Guarda as cartas do jogador e da mesa */
		Cartas seteCartas[] = new Cartas[7];
		
		// Juntar as cartas do jogador com a mesa
		seteCartas=juntarCartas(jogador,mesa, nCartas);
		
		// Ordenar as cartas por numeros
		seteCartas=sortNumCarta(seteCartas);
		
		if(isRoyalStraightFlush(seteCartas))
			return 10;
		
		if(isStraightFlush(seteCartas))
			return 9;
		
		// Ordenar as cartas por numeros
		seteCartas=sortNumCarta(seteCartas);
		
		if(is4ofAKind(seteCartas))
			return 8;
		
		if(isFullHouse(seteCartas))
			return 7;
	
		if(isFlush(seteCartas))
			return 6;
		
		// È necessário ordenar novamente as cartas porque o método isFlush as ordenou por naipe
		seteCartas=sortNumCarta(seteCartas);
		
		if(isStraight(seteCartas))
			return 5;
		
		if(is3ofAKind(seteCartas))
			return 4;
		
		if(is2Pairs(seteCartas))
			return 3;
		
		if(is1Pair(seteCartas))
			return 2;
		
		if(isHighCard(seteCartas))
			return 1;

		return 0;
	}

	/* Retornar a mão final do jogador */
	public Cartas[] obterMaoFinal(Cartas[] jogador, Cartas[] mesa, int nCartas)
	{
		Cartas temp[] = new Cartas[5];
		
		avaliar(jogador, mesa, nCartas);
		
		for(int i = 0; i < cincoCartas.length; i++)
			temp[i] = cincoCartas[i];
		
		return temp;
	}
	
	/* Mostra a mão e a sua classificação */
	public String mostrarClassificacao(int resultado)
	{
		String resultadoStr = "Erro na classificacao";
		
		switch(resultado)
		{
		case 10:
			resultadoStr = "Royal Straigth Flush";
			break;
		case 9:
			resultadoStr = "Straigth Flush";
			break;
		case 8:
			resultadoStr = "Four of a kind";
			break;
		case 7:
			resultadoStr = "Full House";
			break;
		case 6:
			resultadoStr = "Flush";
			break;
		case 5:
			resultadoStr = "Straight";
			break;
		case 4:
			resultadoStr = "Three of a kind";
			break;
		case 3:
			resultadoStr = "2 pares";
			break;
		case 2:
			resultadoStr = "1 par";
			break;
		case 1:
			resultadoStr = "Carta alta";
			break;
		default:
			resultadoStr = "Erro na classificacao";
			break;
		}
		
		return resultadoStr;
	}

	/* Método para verificar se existe um Royal straight flush
	 * Se existir um straight flush de 10 a A's
	 */
	public static boolean isRoyalStraightFlush(Cartas[] cartas)
	{
		//Se existir straight flush, a primeira carta for um 10 e a ultima um A's
		if(isStraightFlush(cartas) && cincoCartas[0].getNumCarta() == 9 && 
				cincoCartas[4].getNumCarta() == 0)
			return true;
		return false;
	}
	
	/* Método para verificar se existe um straight flush
	 * Se existir um straigh e um flush nas cinco cartas
	 */
	public static boolean isStraightFlush(Cartas[] cartas)
	{
		// Se existir uma sequência nas sete cartas
		if(isStraight(cartas))
		{
			// E se as cinco cartas forem todas do mesmo naipe
			if(isFlush(cincoCartas))
			{
				// Se existir sequência baixa de cor com A's então A's conta como carta baixa
				if(cincoCartas[4].getNumCarta() == 0 && cincoCartas[0].getNumCarta() == 1)
				{
					// A's vai para a 1 posição e as outras cartas avançam uma posição
					Cartas temp[] = new Cartas[5];
					for(int i=0; i< cincoCartas.length; i++)
						temp[i] = cincoCartas[i];
					
					cincoCartas[0] = temp[4];
					cincoCartas[1] = temp[0];
					cincoCartas[2] = temp[1];
					cincoCartas[3] = temp[2];
					cincoCartas[4] = temp[3];
				}
				return true;
			}	
		}
		
		// Se existir um flush nas sete cartas
		if(isFlush(cartas))
		{
			// E se as cinco cartas tiverem em sequencia
			if(isStraight(cincoCartas))
			{
				// Se existir sequência baixa de cor com A's então A's conta como carta baixa
				if(cincoCartas[4].getNumCarta() == 0 && cincoCartas[0].getNumCarta() == 1)
				{
					// A's vai para a 1 posição e as outras cartas avançam uma posição
					Cartas temp[] = new Cartas[5];
					for(int i=0; i< cincoCartas.length; i++)
						temp[i] = cincoCartas[i];
					
					cincoCartas[0] = temp[4];
					cincoCartas[1] = temp[0];
					cincoCartas[2] = temp[1];
					cincoCartas[3] = temp[2];
					cincoCartas[4] = temp[3];
				}
				return true;
			}	
		}
		
		return false;
	}
	
	/* Método para verificar se existe um Four of a Kind 
	 * Se existir 4 cartas com o mesmo número
	 */
	public static boolean is4ofAKind(Cartas[] cartas)
	{
		int count = 0;
		
		for(int i=0; i < cartas.length-1; i++)
		{
			// Se as cartas tiverem o mesmo numero count incrementa uma unidade
			if(cartas[i].getNumCarta() == cartas[i+1].getNumCarta())
				count++;
			// count faz reset
			else
				count = 0;
			
			// Se encontrou 4 cartas com o mesmo número
			if(count == 3)
			{
				cincoCartas[0] = cartas[i+1]; 
				cincoCartas[1] = cartas[i]; 
				cincoCartas[2] = cartas[i-1]; 
				cincoCartas[3] = cartas[i-2]; 
				cincoCartas[4] = kicker(cartas, cincoCartas[0].getNumCarta(),1);
				return true;
			}
		}
		return false;
	}
	
	/* Método para verificar se exite um full house
	 * Existe um full house se existir um trio e um par
	 */
	public static boolean isFullHouse(Cartas[] cartas)
	{
		// Se encontrar um trio
		if(is3ofAKind(cartas))
		{
			Cartas[] trio = new Cartas[3];
			
			// Guardar o trio tempariamente
			trio[0] = cincoCartas[0];
			trio[1] = cincoCartas[1];
			trio[2] = cincoCartas[2];
					
			// Encontrar 1 par
			for(int i = cartas.length-1; i > 0; i--)
			{
				//Verifica se existe par de A's
				if(cartas[0].getNumCarta() == cartas[1].getNumCarta() && 
						cartas[0].getNumCarta() != trio[0].getNumCarta())
				{
					cincoCartas[0] = trio[0];
					cincoCartas[1] = trio[1];
					cincoCartas[2] = trio[2];
					cincoCartas[3] = cartas[0];
					cincoCartas[4] = cartas[1];
					return true;
				}
				
				// Se a carta tiver o mesmo numero que a proxima carta e for diferente do trio
				if(cartas[i].getNumCarta() == cartas[i-1].getNumCarta() && 
						cartas[i].getNumCarta() != trio[0].getNumCarta())
				{
					cincoCartas[0] = trio[0];
					cincoCartas[1] = trio[1];
					cincoCartas[2] = trio[2];
					cincoCartas[3] = cartas[i];
					cincoCartas[4] = cartas[i-1];
					return true;
				}
			}
		}
		return false;
	}
	
	// Método para verificar se exite um flush
	public static boolean isFlush(Cartas[] cartas)
	{
		
		int count=0, flag=-1;
		
		Cartas flush[] = new Cartas[7];
		
		// Ordenar as cartas pelo seu naipe
		cartas=sortNaipeCarta(cartas);
		
		// Encontrar um flush 
		for(int i = 0; i < cartas.length-1; i++)
		{
			// Se encontrou duas cartas seguidas com o mesmo naipe
			if(cartas[i].getNaipe() == cartas[i+1].getNaipe())
			{
				count ++;
				
				// Se encontrou 5 cartas do mesmo naipe
				if(count == 4)
				{

					flush[0] = cartas[i+1]; 
					flush[1] = cartas[i]; 
					flush[2] = cartas[i-1]; 
					flush[3] = cartas[i-2]; 
					flush[4] = cartas[i-3]; 
					flag = 5;
				}
				// Se encontrou 6 cartas do mesmo naipe
				if(count == 5)
				{
					flush[0] = cartas[i+1]; 
					flush[1] = cartas[i]; 
					flush[2] = cartas[i-1]; 
					flush[3] = cartas[i-2]; 
					flush[4] = cartas[i-3]; 
					flush[5] = cartas[i-4]; 
					flag = 6;
				}
				//  Se todas as cartas forem do mesmo naipe
				if(count == 6)
				{
					flush[0] = cartas[i+1]; 
					flush[1] = cartas[i]; 
					flush[2] = cartas[i-1]; 
					flush[3] = cartas[i-2]; 
					flush[4] = cartas[i-3]; 
					flush[5] = cartas[i-4]; 
					flush[6] = cartas[i-5]; 
					flag = 7;
				}
			}	
			else
				count = 0;
		}
		
		/* Existem 5 cartas para flush
		 * Atribuir as cinco cartas á mão do jogador */	
		if(flag == 5)
		{
			flush = sortNumCarta(flush);
			// Se existir um A's é a carta alta
			if(flush[2].getNumCarta() == 0 )
			{
				cincoCartas[0] = flush[3];	// Carta mais baixa
				cincoCartas[1] = flush[4];
				cincoCartas[2] = flush[5];
				cincoCartas[3] = flush[6];
				cincoCartas[4] = flush[2];	// A's
			}
			// Se não existir um A's
			else
			{
				cincoCartas[0] = flush[2];	// Carta mais baixa
				cincoCartas[1] = flush[3];
				cincoCartas[2] = flush[4];
				cincoCartas[3] = flush[5];
				cincoCartas[4] = flush[6];	// Carta mais alta
			}
			return true;
		}
		
		/* Existem 6 cartas para flush
		 * Atribuir as cinco cartas mais altas á mão do jogador */	
		if(flag == 6)
		{
			flush = sortNumCarta(flush);
			
			// Se existir um A's então A's é a carta alta
			if(flush[1].getNumCarta() == 0 )
			{
				cincoCartas[0] = flush[3];	// Carta mais baixa
				cincoCartas[1] = flush[4];
				cincoCartas[2] = flush[5];
				cincoCartas[3] = flush[6];
				cincoCartas[4] = flush[1];	// A's
			}
			// Se não existir um A's
			else
			{
				cincoCartas[0] = flush[2];	// Carta mais baixa
				cincoCartas[1] = flush[3];
				cincoCartas[2] = flush[4];
				cincoCartas[3] = flush[5];
				cincoCartas[4] = flush[6];	// Carta mais alta
			}

			return true;
		}
		/* Todas as cartas são do mesmo naipe
		 * Atribuir as cinco cartas mais altas á mão do jogador */	
		if(flag == 7)
		{
			flush = sortNumCarta(flush);
			
			// Se existir um A's então A's é a carta alta
			if(flush[0].getNumCarta() == 0 )
			{
				cincoCartas[0] = flush[3];	// Carta mais baixa
				cincoCartas[1] = flush[4];
				cincoCartas[2] = flush[5];
				cincoCartas[3] = flush[6];
				cincoCartas[4] = flush[0];	// A's
			}
			// Se não existir um A's
			else
			{
				cincoCartas[0] = flush[2];	// Carta mais baixa
				cincoCartas[1] = flush[3];
				cincoCartas[2] = flush[4];
				cincoCartas[3] = flush[5];
				cincoCartas[4] = flush[6];	// Carta mais alta
			}

			return true;
		}
		return false;
	}
	
	// Método para verificar se a mão tem 5 cartas em sequência 
	public static boolean isStraight(Cartas[] cartas)
	{
		int count = 0;
		
		// Verficar se existe sequência de A's a 10's 
		if(cartas[0].getNumCarta() == 0 && cartas[cartas.length-1].getNumCarta() == 12 && 
				cartas[cartas.length-2].getNumCarta() == 11 && cartas[cartas.length-3].getNumCarta() == 10 &&
						cartas[cartas.length-4].getNumCarta() == 9 )
		{
			cincoCartas[4] = cartas[0]; // A
			cincoCartas[3] = cartas[cartas.length-1]; // R
			cincoCartas[2] = cartas[cartas.length-2]; // Q
			cincoCartas[1] = cartas[cartas.length-3]; // J
			cincoCartas[0] = cartas[cartas.length-4]; //10	
			return true;
		}
		/* Verificar se existe outras sequências 
		 * Ciclo da carta mais alta até à carta mais baixa
		 * Se o contador encontrar 5 cartas seguidas em que a carta i-1 
		 * é apenas um valor menor que a carta i então encontrou uma sequência  */
		else
		{
			for(int i = cartas.length-1; i > 0; i--)
			{
				// Se houver um par no meio do straight passa diretamente para a proxima iteração
				if(cartas[i].getNumCarta() == cartas[i-1].getNumCarta())
					continue;
				
				// Se encontrou duas cartas seguidas 
				if(cartas[i].getNumCarta() == cartas[i-1].getNumCarta()+1)
				{
					count ++;
					// Se encontrou uma sequência
					if(count == 4)
					{
						cincoCartas[0] = cartas[i-1]; // carta mais baixa
						cincoCartas[1] = cartas[i]; 
						cincoCartas[2] = cartas[i+1]; 
						cincoCartas[3] = cartas[i+2]; 
						cincoCartas[4] = cartas[i+3]; // cartas mais alta
						
						return true;
					}
				}
				// Se as cartas não forem seguidas então o contador faz reset
				else
					count = 0;
			}
		}
		return false;
	}

	/* Método para encontrar um Trio 
	 * Se existir 3 cartas com o mesmo número
	 */
	public static boolean is3ofAKind(Cartas[] cartas)
	{
		int count = 0;

		// Verificar se existe um trio de A's
		if(cartas[0].getNumCarta() == 0 && cartas[1].getNumCarta() == 0 && cartas[2].getNumCarta() == 0)
		{
			cincoCartas[0] = cartas[0];
			cincoCartas[1] = cartas[1];
			cincoCartas[2] = cartas[2];
			cincoCartas[3] = kicker(cartas,cincoCartas[0].getNumCarta(),1);
			cincoCartas[4] = kicker(cartas,cincoCartas[0].getNumCarta(),2);
			return true;
		}
		
		//Verificar se existe um trio sem ser de A's
		for(int i=cartas.length-1; i>0; i--)
		{
			// Se as cartas tiverem o mesmo numero count incrementa uma unidade
			if(cartas[i].getNumCarta() == cartas[i-1].getNumCarta())
				count++;
			// count faz reset
			else
				count = 0;
			
			// Se encontrou 3 cartas com o mesmo número
			if(count == 2)
			{
				cincoCartas[0] = cartas[i+1]; 
				cincoCartas[1] = cartas[i]; 
				cincoCartas[2] = cartas[i-1]; 
				cincoCartas[3] = kicker(cartas, cincoCartas[0].getNumCarta(),1); 
				cincoCartas[4] = kicker(cartas, cincoCartas[0].getNumCarta(),2);
				return true;
			}
		}
		
		return false;
	}
	
	/* Método para encontrar dois pares
	 * Existe dois pares se já existe um par e encontra o segundo par
	 */
	public static boolean is2Pairs(Cartas[] cartas)
	{
		// Encontrar o primeiro par através da função is1Pair()
		is1Pair(cartas);
		
		// temp fica com o primeiro par
		Cartas[] temp = new Cartas[2];		
		temp[0] = cincoCartas[0];
		temp[1] = cincoCartas[1];
		
		// Encontrar o segundo par
		for(int i = cartas.length-1; i > 0; i--)
		{
			// Se a carta tiver o mesmo numero que a proxima carta e for diferente do primeiro par
			if(cartas[i].getNumCarta() == cartas[i-1].getNumCarta() && 
					cartas[i].getNumCarta() != temp[0].getNumCarta())
			{
				// 1 par
				cincoCartas[0] = temp[0];
				cincoCartas[1] = temp[1];
				// 2 par
				cincoCartas[2] = cartas[i]; 
				cincoCartas[3] = cartas[i-1];
				
				// Encontrar o kicker
				
				// Se existir um A's como kicker 
				// O primeiro par não é par de A's e a primeira carta de cartas é 1 A's
				if(cincoCartas[0].getNumCarta() != 0 && cartas[0].getNumCarta() == 0)
				{
					cincoCartas[4] = cartas[0];
					return true;
				}
				// Se não existir 1 A's
				else
				{
					for(int j = cartas.length-1; j >= 0; j--)
					{
						// Se cartas tiver um número diferente do primeiro par e do segundo
						if(cartas[j].getNumCarta() != cincoCartas[0].getNumCarta() &&
								cartas[j].getNumCarta() != cincoCartas[2].getNumCarta())
						{
							cincoCartas[4] = cartas[j];
							return true;
						}
					}		
				}
			}
		}	
		return false;
	}
	
	/* Método para encontrar um par 
	 * Encontra um par se existir duas cartas com o mesmo número
	 */
	public static boolean is1Pair(Cartas[] cartas)
	{		
		// Se a primeira e a segunda carta foram A's então existe um par de A's 
		if(cartas[0].getNumCarta() == 0 && cartas[1].getNumCarta() == 0)
		{
			cincoCartas[0] = cartas[0];
			cincoCartas[1] = cartas[1];
			cincoCartas[2] = kicker(cartas, 0, 1); // Devolve a carta mais alta diferente de A's
			cincoCartas[3] = kicker(cartas, 0, 2); // Devolve a segunda carta mais alta diferente de A's
			cincoCartas[4] = kicker(cartas, 0, 3); // Devolve a terceira carta mais alta diferente de A's
			return true;
		}
		
		// Se não existir 1 par de A's então procura o par com número de carta maior
		for(int i = cartas.length-1; i > 0; i--)
		{
			// Se a carta tiver o mesmo numero que a proxima carta
			if(cartas[i].getNumCarta() == cartas[i-1].getNumCarta())
			{
				cincoCartas[0] = cartas[i];
				cincoCartas[1] = cartas[i-1];
				cincoCartas[2] = kicker(cartas, cincoCartas[0].getNumCarta(), 1); // Devolve a carta mais alta diferente de A's
				cincoCartas[3] = kicker(cartas, cincoCartas[0].getNumCarta(), 2); // Devolve a segunda carta mais alta diferente de A's
				cincoCartas[4] = kicker(cartas, cincoCartas[0].getNumCarta(), 3); // Devolve a terceira carta mais alta diferente de A's
				return true;
			}
		}
		return false;
	}

	/* Método para encontrar Carta alta 
	 * Existe apenas carta alta se os outros falharem
	 */
	public static boolean isHighCard(Cartas[] cartas)
	{
		// Se existir um A's
		if(cartas[0].getNumCarta() == 0)
		{
			cincoCartas[0] = cartas[cartas.length-4];
			cincoCartas[1] = cartas[cartas.length-3];
			cincoCartas[2] = cartas[cartas.length-2];
			cincoCartas[3] = cartas[cartas.length-1];
			cincoCartas[4] = cartas[0]; 				//A's
			return true;
		}
		// Se não existir A's
		else
		{
			cincoCartas[0] = cartas[cartas.length-5];
			cincoCartas[1] = cartas[cartas.length-4];
			cincoCartas[2] = cartas[cartas.length-3];
			cincoCartas[3] = cartas[cartas.length-2];
			cincoCartas[4] = cartas[cartas.length-1]; 				
			return true;
		}
	}
	
	/* Método que retorna uma Carta alta 
	 * Recebe como argumento as sete cartas, o numero da carta em que a carta alta tem de ser diferente
	 * a posicao da carta alta
	 */
	public static Cartas kicker(Cartas[] cartas,int numCarta,int posicao)
	{
		// vai guardar as cartas altas diferentes de numCarta 
		Cartas cartasAltas[] = new Cartas[5];
		int j=0;
		
		// Encontrar as cartas diferentes de numCarta
		for (int i = cartas.length-1; i>=0 ; i--)
		{
			if(cartas[i].getNumCarta() != numCarta)
			{
				// CartasAltas ficam do mais alto para o mais baixo
				cartasAltas[j] = cartas[i];
				j++;
			}
		}

		// Se existir um A's nas cartas altas fica o A's como carta mais alta
		if(cartasAltas[j-1].getNumCarta() == 0 && posicao == 1)
			return cartasAltas[j-1];
		// Retorna a carta mais alta sem contar com o A's
		if(cartasAltas[j-1].getNumCarta() == 0 && posicao == 2)
			return cartasAltas[0];
		// Retorna a segunda carta alta sem contar com o A's
		if(cartasAltas[j-1].getNumCarta() == 0 && posicao == 3)
			return cartasAltas[1];
		
		//Se não existir um A's nas cartas altas retorna a carta na posicao posicao-1
		if(cartasAltas[j-posicao].getNumCarta() != 0)
			return cartasAltas[posicao-1];
		
		return null;
	}
	
	// Método que retorna um array com as cartas do jogador e da mesa juntas
	public Cartas[] juntarCartas(Cartas[] jogador, Cartas[] mesa, int nCartas){
		
		Cartas[] juntaCartas = new Cartas[nCartas];
		
		// Cartas do jogador
		juntaCartas[0] = jogador[0];
		juntaCartas[1] = jogador[1];
		
		// Cartas da mesa
		int j=0;
		for(int i = 2; i < juntaCartas.length; i++)
		{
			juntaCartas[i] = mesa[j];
			j++;
		}
		
		return juntaCartas;
	}
	
	// Método que retorna as cartas ordenadas de forma ascendente pelo seu número
	public static Cartas[] sortNumCarta(Cartas[] cartas)
	{
		// Verificar se o array está vazio
		if(cartas == null || cartas.length == 0)
		{
			System.out.println("o Array seteCartas esta vazio!");
			return null;
		}

		int[] numSeteCartas = new int[7];
		
		// Inicializar numSeteCartas com as cartas recebidas em argumento
		for(int i=0; i<cartas.length; i++)
		{
			// Se a carta for igual a null então atribuir o valor -1
			if(cartas[i] == null)
				numSeteCartas[i] = -1;
			else
				numSeteCartas[i] = cartas[i].getNumCarta();
		}
				
		quickSort(cartas,numSeteCartas,0,cartas.length-1);
		
		return cartas;
	}
	
	/* Ordenar as cartas pelo seu naipe */
	public static Cartas[] sortNaipeCarta(Cartas[] cartas)
	{
		// Verificar se o array está vazio
		if(cartas == null || cartas.length == 0)
		{
			System.out.println("o Array cartas esta vazio!");
			return null;
		}

		int[] naipeSeteCartas = new int[7];
		
		// Inicializar naipeSeteCartas com as cartas recebidas em argumento
		for(int i=0; i<cartas.length; i++)
		{
			// Se a carta for igual a null então atribuir o valor -1
			if(cartas[i] == null)
				naipeSeteCartas[i] = -1;
			else
				naipeSeteCartas[i] = cartas[i].getNaipe();
		}
				
		quickSort(cartas,naipeSeteCartas,0,cartas.length-1);
		
		return cartas;
	}
	
	/* Ordenar um array de forma crescente 
	 * Complexidade temporal: O(nlogn)
	 */
	private static void quickSort(Cartas[] cartas, int[] tempCartas, int indiceBaixo,int indiceAlto)
	{
		int i = indiceBaixo;
		int j = indiceAlto;
		
		// Calcular o número pivot, o número do meio do array 
		int pivot = tempCartas[indiceBaixo+(indiceAlto-indiceBaixo)/2];
		
		//Dividir em dois arrays 
		while(i<=j)
		{
			/*Em cada iteração, identificamos o numero do lado esquerdo que é maior que o pivot
			 * e tambem identificamos o numero da direito que é menor que o pivot
			 * Depois efetuamos a troca
			 */
			
			while(tempCartas[i] < pivot)
				i++;
			while(tempCartas[j] > pivot)
				j--;
			if(i <= j)
			{
				int tempNum = tempCartas[i];
				tempCartas[i] = tempCartas[j];
				tempCartas[j] = tempNum;
				
				Cartas temp = cartas[i];
				cartas[i] = cartas[j];
				cartas[j] = temp;
                //mover o indice para a proxima posicao em ambos os lados
                i++;
                j--;
			}
			
			// Chamar o quickSort recursimavemte
			if (indiceBaixo < j)
	            quickSort(cartas,tempCartas,indiceBaixo, j);
	        if (i < indiceAlto)
	            quickSort(cartas,tempCartas,i, indiceAlto);	
		}
	}	
}
