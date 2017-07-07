/* Descricao: Esta classe recebe as cartas do jogadores e os seus respectivos resultados
 * e calcula o vencedor entre os dois jogadores
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 19/04/2017
 */

public class CalcularVencedor {

	/* Contrutor vazio */
	CalcularVencedor(){}
	
	/* Método que retorna o jogador vencedor 
	 * 1 - Ganha o jogador 1
	 * 2 - Ganha o jogador 2
	 * 0 - Empate
	 * */
	public int calcularVencedor(Cartas[] jogador1, Cartas[] jogador2, int resultado[])
	{
		int vencedor=-1;
		/* Se o resultado do jogador 1 for maior que o resultado do jogador 2 ganha o jogador 1 */
		if(resultado[0] > resultado[1])
			return 1;
		/* Se o resultado do jogador 1 for menor que o resultado do jogador 2 ganha o jogador 2 */
		if(resultado[0] < resultado[1])
			return 2;
		/* Se o resultado do jogador 1 for igual ao resultado do jogador 2 vai ao desempate */
		if(resultado[0] == resultado[1])
		{
			switch(resultado[0])
			{
				// Se existir empate com straigth flush
				case 9:
					vencedor = straightFlush(jogador1,jogador2);
					break;
				// Se existir empate com four of a kind
				case 8: 
					vencedor = fourOfAKind(jogador1, jogador2);
					break;
				// Se existir empate com full house
				case 7: 
					vencedor = fullHouse(jogador1, jogador2);
					break;
				// Se existir empate com flush
				case 6: 
					vencedor = flush(jogador1, jogador2);
					break;
				// Se existir empate com straight
				case 5: 
					vencedor = straight(jogador1, jogador2);
					break;
				// Se existir empate com trio
				case 4: 
					vencedor = threeOfAKind(jogador1, jogador2);
					break;
				// Se existir empate com 2 pares
				case 3: 
					vencedor = doisPares(jogador1, jogador2);
					break;
				// Se existir empate com 1 par
				case 2: 
					vencedor = umPar(jogador1, jogador2);
					break;
				// Se existir empate com carta alta
				case 1: 
					vencedor = cartaAlta(jogador1, jogador2);
					break;
				default: 
					System.out.println("Erro a calcular vencedor(em CalcularVencedor)!");
			}
			
			return vencedor;
		}
		return -1;
	}
	
	/* Calcular qual o melhor straight flush entre dois 
	 * Tem o melhor straight flush quem tiver a carta mais alta na ultima posicao
	 */
	public int straightFlush(Cartas[] jogador1, Cartas[] jogador2)
	{
		// Se o jogador 1 tiver a carta mais alta
		if(jogador1[jogador1.length-1].getNumCarta() > jogador2[jogador2.length-1].getNumCarta())
			return 1;
		// Se o jogador 2 tiver a carta mais alta
		if(jogador1[jogador1.length-1].getNumCarta() < jogador2[jogador2.length-1].getNumCarta())
			return 2;
		// Se ambos tiverem o mesmo straight flush
		else
			return 0;
		
	}
	
	/* Calcular qual o melhor four of a kind entre dois
	 * Tem o melhor four of a kind quem tiver a primeira carta mais alta
	 */
	public int fourOfAKind(Cartas[] jogador1, Cartas[] jogador2)
	{
		//Se ambos tiverem um four of a kind de A's
		if(jogador1[0].getNumCarta() == 0 && jogador2[0].getNumCarta() == 0)
		{
			// Verificar se ambos tem o mesmo kicker
			if(jogador1[jogador1.length-1].getNumCarta() == jogador2[jogador2.length-1].getNumCarta())
				return 0;
			// Verificar se o jogador 1 tem um kicker maior
			if(jogador1[jogador1.length-1].getNumCarta() > jogador2[jogador2.length-1].getNumCarta() )
				return 1;
			// Verificar se o jogador 2 tem um kicker maior
			if(jogador1[jogador1.length-1].getNumCarta() < jogador2[jogador2.length-1].getNumCarta() )
				return 2;
		}
		
		//Se o jogador1 tiver um four of a kind de A's
		if(jogador1[0].getNumCarta() == 0)
			return 1;
		//Se o jogador2 tiver um four of a kind de A's
		if(jogador2[0].getNumCarta() == 0)
			return 2;
		// Se o jogador 1 tiver a carta mais alta
		if(jogador1[0].getNumCarta() > jogador2[0].getNumCarta())
			return 1;
		// Se o jogador 2 tiver a carta mais alta
		if(jogador1[0].getNumCarta() < jogador2[0].getNumCarta())
			return 2;
		// Se ambos tiverem o mesmo four of a kind
		else
		{
			//Verificar quem tem o kicker mais alto
			
			// Verificar se ambos tem o mesmo kicker
			if(jogador1[jogador1.length-1].getNumCarta() == jogador2[jogador2.length-1].getNumCarta() )
				return 0;
			// Verificar se o jogador 1 tem o A's como kicker
			if(jogador1[jogador1.length-1].getNumCarta() == 0)
				return 1;
			// Verificar se o jogador 2 tem o A's como kicker
			if(jogador2[jogador2.length-1].getNumCarta() == 0)
				return 2;
			// Verificar se o jogador 1 tem um kicker maior
			if(jogador1[jogador1.length-1].getNumCarta() > jogador2[jogador2.length-1].getNumCarta() )
				return 1;
			// Verificar se o jogador 2 tem um kicker maior
			if(jogador1[jogador1.length-1].getNumCarta() < jogador2[jogador2.length-1].getNumCarta() )
				return 2;
			
			return -1;
		}
	}

	/* Calcular qual o melhor full house 
	 * Tem o melhor full house quem tem: 
	 * 1 - O melhor trio
	 * 2 - O melhor par
	 */
	public int fullHouse(Cartas[] jogador1, Cartas[] jogador2)
	{
		// Se ambos os jogadores tiverem o mesmo trio
		if(jogador1[0].getNumCarta() == jogador2[0].getNumCarta())
		{
			//Se ambos os jogdores tiverem o mesmo par
			// Empate
			if(jogador1[3].getNumCarta() == jogador2[3].getNumCarta())
				return 0;
			//Se o jogador 1 tiver um par de A's
			if(jogador1[3].getNumCarta() == 0)
				return 1;
			//Se o jogador 2 tiver um par de A's
			if(jogador2[3].getNumCarta() == 0)
				return 2;
			// Se o jogador 1 tiver um par mais alto
			if(jogador1[3].getNumCarta() > jogador2[3].getNumCarta())
				return 1;
			// Se o jogador 2 tiver um par mais alto
			if(jogador1[3].getNumCarta() < jogador2[3].getNumCarta())
				return 2;
		}
		//Se o jogador 1 tiver um trio de A's
		if(jogador1[0].getNumCarta() == 0)
			return 1;
		//Se o jogador 2 tiver um trio de A's
		if(jogador2[0].getNumCarta() == 0)
			return 2;
		// Se o jogador 1 tiver um trio mais alto
		if(jogador1[0].getNumCarta() > jogador2[0].getNumCarta())
			return 1;
		// Se o jogador 2 tiver um trio mais alto
		if(jogador1[0].getNumCarta() < jogador2[0].getNumCarta())
			return 2;
		
		return -1;
	}

	/* Calcular qual o melhor flush
	 * Tem o melhor flush quem tiver as cartas mais altas ou um A's
	 */
	public int flush(Cartas[] jogador1, Cartas[] jogador2)
	{
		// Se ambos tiverem a mesma carta mais alta
		if(jogador1[jogador1.length-1].getNumCarta() == jogador2[jogador2.length-1].getNumCarta())
		{
			// Procurar até um jogador ter uma carta maior que o outro
			for(int i=jogador1.length-1; i >= 0; i--)
			{
				// Se o jogador 1 tiver uma carta maior
				if(jogador1[i].getNumCarta() > jogador2[i].getNumCarta())
					return 1;
				// Se o jogador 2 tiver uma carta maior
				if(jogador1[i].getNumCarta() < jogador2[i].getNumCarta())
					return 2;
			}
			// Se ambos os jogadores tiverem o mesmo jogo 
			return 0;
		}
		
		//Se o jogador 1 tiver flush com A's 
		if(jogador1[jogador1.length-1].getNumCarta() == 0)
			return 1;
		//Se o jogador 2 tiver um flush de A's
		if(jogador2[jogador2.length-1].getNumCarta() == 0)
			return 2;
		// Se o jogador 1 tiver um flush com carta mais alta
		if(jogador1[jogador1.length-1].getNumCarta() > jogador2[jogador2.length-1].getNumCarta())
			return 1;
		// Se o jogador 2 tiver um flush com carta mais alta
		if(jogador1[jogador1.length-1].getNumCarta() < jogador2[jogador2.length-1].getNumCarta())
			return 2;
			
		return -1;
	}

	/* Calcular qual o melhor straight
	 * Tem o melhor straight quem tiver as cartas mais altas
	 */
	public int straight(Cartas[] jogador1, Cartas[] jogador2)
	{
		// Verificar se ambos tem o mesmo straight
		if(jogador1[jogador1.length-1].getNumCarta() == jogador2[jogador2.length-1].getNumCarta())
			return 0;
		//Se o jogador 1 straight A's a 10
		if(jogador1[jogador1.length-1].getNumCarta() == 0)
			return 1;
		//Se o jogador 2 straight A's a 10
		if(jogador2[jogador2.length-1].getNumCarta() == 0)
			return 2;
		// Se o jogador 1 tiver um straight com carta mais alta
		if(jogador1[jogador1.length-1].getNumCarta() > jogador2[jogador2.length-1].getNumCarta())
			return 1;
		// Se o jogador 2 tiver um straight com carta mais alta
		if(jogador1[jogador1.length-1].getNumCarta() < jogador2[jogador2.length-1].getNumCarta())
			return 2;
		
		return -1;
	}

	/* Calcular qual o melhor trio
	 * Tem o melhor trio quem tiver o trio mais alto 
	 * Se tiverem o mesmo trio o desempate é feito pelo kicker mais alto
	 */
	public int threeOfAKind(Cartas[] jogador1, Cartas[] jogador2)
	{
		//Se ambos os jogadores tiverem o mesmo trio
		if(jogador1[0].getNumCarta() == jogador2[0].getNumCarta())
		{
			// Se ambos tiverem o kicker A's
			if(jogador1[3].getNumCarta() == 0 && jogador2[3].getNumCarta() == 0)
			{
				//Desempatar com o ultimo kicker
				// Se o jogador 1 tiver a carta mais alta
				if(jogador1[4].getNumCarta() > jogador2[4].getNumCarta())
					return 1;
				// Se o jogador 2 tiver a carta mais alta
				if(jogador1[4].getNumCarta() < jogador2[4].getNumCarta())
					return 2;
				// Se ambos tiverem o mesmo jogo
				else
					return 0;
			}
			
			//Se o jogador 1 tiver kicker A's 
			if(jogador1[3].getNumCarta() == 0)
				return 1;
			//Se o jogador 2 tiver kicker A's
			if(jogador2[3].getNumCarta() == 0)
				return 2;
			
			//Encontrar o kicker mais alto sem haver A's
			//Procurar até um jogador ter uma carta maior que o outro
			for(int i=3; i < jogador1.length; i++)
			{
				// Se o jogador 1 tiver uma carta maior
				if(jogador1[i].getNumCarta() > jogador2[i].getNumCarta())
					return 1;
				// Se o jogador 2 tiver uma carta maior
				if(jogador1[i].getNumCarta() < jogador2[i].getNumCarta())
					return 2;
			}
			// Se os jogos forem iguais retorna empate
			return 0;
		}
		
		//Se o jogador 1 tiver trio de A's
		if(jogador1[0].getNumCarta() == 0)
			return 1;
		//Se o jogador 2 trio de A's
		if(jogador2[0].getNumCarta() == 0)
			return 2;
		// Se o jogador 1 tiver um trio maior
		if(jogador1[0].getNumCarta() > jogador2[0].getNumCarta())
			return 1;
		// Se o jogador 2 tiver um trio maio
		if(jogador1[0].getNumCarta() < jogador2[0].getNumCarta())
			return 2;
		
		return -1;
	}

	/* Calcular quem têm os dois pares mais altos
	 * Tem os pares mais altos se:
	 * Um par for mais alto que qualquer par do oponente
	 * Em caso de ambos terem o mesmo par mais alto verifica-se o segundo par
	 * Em caso de empate no segundo par verifica-se o kicker
	 */
	public int doisPares(Cartas[] jogador1, Cartas[] jogador2)
	{
		//Se ambos os jogadores tiverem o mesmo par mais alto
		if(jogador1[0].getNumCarta() == jogador2[0].getNumCarta())
		{
			//Se ambos os jogadores tiverem o mesmo par mais baixo
			if(jogador1[2].getNumCarta() == jogador2[2].getNumCarta())
			{
				//Se ambos tiverem o mesmo kicker
				if(jogador1[4].getNumCarta() == jogador2[4].getNumCarta())
					return 0;
				
				//Se o jogador 1 tiver kicker A's
				if(jogador1[4].getNumCarta() == 0)
					return 1;
				//Se o jogador 2 kicker A's
				if(jogador2[4].getNumCarta() == 0)
					return 2;
				// Se o jogador 1 kicker maior
				if(jogador1[4].getNumCarta() > jogador2[4].getNumCarta())
					return 1;
				// Se o jogador 2 tiver kicker maior
				if(jogador1[4].getNumCarta() < jogador2[4].getNumCarta())
					return 2;
			}

			// Se o jogador 1 o segundo par maior
			if(jogador1[2].getNumCarta() > jogador2[2].getNumCarta())
				return 1;
			// Se o jogador 2  segundo par maior
			if(jogador1[2].getNumCarta() < jogador2[2].getNumCarta())
				return 2;
		}
		
		//Se o jogador 1 tiver um par de A's
		if(jogador1[0].getNumCarta() == 0)
			return 1;
		//Se o jogador 2 tiver um par de A's
		if(jogador2[0].getNumCarta() == 0)
			return 2;
		// Se o jogador 1 tiver um par maior
		if(jogador1[0].getNumCarta() > jogador2[0].getNumCarta())
			return 1;
		// Se o jogador 1 tiver um par maior
		if(jogador1[0].getNumCarta() < jogador2[0].getNumCarta())
			return 2;
		
		return -1;
	}

	/* Calcular quem têm o par mais alto 
	 * Se ambos tiverem o mesmo par verifica-se o kicker
	 */
	public int umPar(Cartas[] jogador1, Cartas[] jogador2)
	{
		//Se ambos os jogadores tiverem o mesmo par
		if(jogador1[0].getNumCarta() == jogador2[0].getNumCarta())
		{
			// Se ambos tiverem o kicker A's
			if(jogador1[2].getNumCarta() == 0 && jogador2[2].getNumCarta() == 0)
			{
				//Procurar até um jogador ter uma carta maior que o outro
				for(int i=3; i < jogador1.length; i++)
				{
					// Se o jogador 1 tiver uma carta maior
					if(jogador1[i].getNumCarta() > jogador2[i].getNumCarta())
						return 1;
					// Se o jogador 2 tiver uma carta maior
					if(jogador1[i].getNumCarta() < jogador2[i].getNumCarta())
						return 2;
				}
				// Se ambos tiverem o mesmo jogo
				return 0;
			}
			
			//Se o jogador 1 tiver kicker de A's
			if(jogador1[2].getNumCarta() == 0)
				return 1;
			//Se o jogador 2 tiver kicker de A's
			if(jogador2[2].getNumCarta() == 0)
				return 2;
			//Se não existir A's nos kicker's
			//Procurar até um jogador ter uma carta maior que o outro
			for(int i=2; i < jogador1.length; i++)
			{
				// Se o jogador 1 tiver uma carta maior
				if(jogador1[i].getNumCarta() > jogador2[i].getNumCarta())
					return 1;
				// Se o jogador 2 tiver uma carta maior
				if(jogador1[i].getNumCarta() < jogador2[i].getNumCarta())
					return 2;
			}
			// Se ambos tiverem o mesmo jogo
			return 0;
		}
		
		//Se o jogador 1 tiver par de A's
		if(jogador1[0].getNumCarta() == 0)
			return 1;
		//Se o jogador 2 par de A's
		if(jogador2[0].getNumCarta() == 0)
			return 2;
		// Se o jogador 1 tiver um par maior
		if(jogador1[0].getNumCarta() > jogador2[0].getNumCarta())
			return 1;
		// Se o jogador 2 tiver um par maio
		if(jogador1[0].getNumCarta() < jogador2[0].getNumCarta())
			return 2;
		return -1;
	}

	/* Calcular quem têm as cartas mais altas */
	public int cartaAlta(Cartas[] jogador1, Cartas[] jogador2)
	{
		// Se ambos tiverem o kicker A's
		if(jogador1[4].getNumCarta() == 0 && jogador2[4].getNumCarta() == 0)
		{
			//Procurar até um jogador ter uma carta maior que o outro
			for(int i=jogador1.length-1; i >= 0; i--)
			{
				// Se o jogador 1 tiver uma carta maior
				if(jogador1[i].getNumCarta() > jogador2[i].getNumCarta())
					return 1;
				// Se o jogador 2 tiver uma carta maior
				if(jogador1[i].getNumCarta() < jogador2[i].getNumCarta())
					return 2;
			}
			// Se ambos tiverem o mesmo jogo
			return 0;
		}
		//Se o jogador 1 carta alta A's
		if(jogador1[4].getNumCarta() == 0)
			return 1;
		//Se o jogador 2 carta alta A's
		if(jogador2[4].getNumCarta() == 0)
			return 2;
		
		//Se não existir A's nos jogos
		//Procurar até um jogador ter uma carta maior que o outro
		for(int i=jogador1.length-1; i >= 0; i--)
		{
			// Se o jogador 1 tiver uma carta maior
			if(jogador1[i].getNumCarta() > jogador2[i].getNumCarta())
				return 1;
			// Se o jogador 2 tiver uma carta maior
			if(jogador1[i].getNumCarta() < jogador2[i].getNumCarta())
				return 2;
		}
		
		// Se ambos tiverem o mesmo jogo
		return 0;
	}
}
