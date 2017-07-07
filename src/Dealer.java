/* Descricao: Esta classe é constituida por 4 metodos
 * distribui as cartas pelos 2 jogadores
 * queima uma carta e dá o flop, 
 * queima outra carta e dá o turn 
 * e por fim volta a queimar outra carta e dá o river
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 05/04/2017
 */

public class Dealer {

	/* Construtor vazio */
	Dealer()
	{
	}
	
	/* Método que recebe um baralho e dois jogadores 
	 * e distribui duas cartas pelos dois jogadores de forma alternada */
	public void darCartas(Baralho baralho, Cartas[] jog1, Cartas[] jog2)
	{
		jog1[0] = baralho.tirarCarta();
		jog2[0] = baralho.tirarCarta();
		jog1[1] = baralho.tirarCarta();
		jog2[1] = baralho.tirarCarta();
	}
	
	/* Método que recebe um baralho e a mesa e distribui três cartas pela mesa */
	public void darFlop(Baralho baralho, Cartas[] mesa)
	{
		/* Queimar uma carta */
		baralho.tirarCarta();
		
		mesa[0] = baralho.tirarCarta();
		mesa[1] = baralho.tirarCarta();
		mesa[2] = baralho.tirarCarta();
	}
	
	/* Método que recebe um baralho e a mesa e distribui uma carta pela mesa */
	public void darTurn(Baralho baralho, Cartas[] mesa)
	{
		/* Queimar uma carta */
		baralho.tirarCarta();
		
		mesa[3] = baralho.tirarCarta();
	}
	
	/* Método que recebe um baralho e a mesa e distribui uma carta pela mesa */
	public void darRiver(Baralho baralho, Cartas[] mesa)
	{
		/* Queimar uma carta */
		baralho.tirarCarta();
		
		mesa[4] = baralho.tirarCarta();
	}
}
