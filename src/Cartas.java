/* Descricao: Criar estrutura de dados para guardar e aceder as 52 cartas do baralho
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, estudante nº 1300531
 * Ultima modificacao: 04/04/2017
 */
public class Cartas {
	
	//Dados short para poupar memoria
	private short carta, naipe;
	
	//Guarda o numero das cartas
	private static String[] cartaArray = {"A", "2", "3", "4", "5", "6", "7", "8", "9", 
			"10", "J", "Q", "K"};
	
	// Copas, Ouros, Paus, Espadas
	private static String[] naipeArray = {"\u2665", "\u2666", "\u2663", "\u2660"};
	
	
	// Construtor vazio
	Cartas(){}
	
	// Contrustor que recebe como argumentos a carta e o respetivo naipe
	Cartas(short carta, short naipe){
		this.carta=carta;
		this.naipe=naipe;
	}
	
	// Mostar uma carta
	public @Override String toString(){
		return cartaArray[carta] + naipeArray[naipe];
	}
	
	// Obter uma carta
	public short getNumCarta(){
		return carta;
	}
	
	// Obter um naipe
	public short getNaipe(){
		return naipe;
	}	
}
