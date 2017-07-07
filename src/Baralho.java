/* Descricao: Esta classe recebe as 52 cartas do baralho e baralha-as
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 05/04/2017
 */

import java.util.Random;
import java.util.ArrayList;

public class Baralho {

	/* Inicializar lista de cartas */
	private ArrayList <Cartas> cartas;
	
	/* Construtor que recebe as 52 cartas do baralho e as baralha de forma aleatoria */
	Baralho()
	{
		cartas = new ArrayList<Cartas>();
		Random gerar = new Random();
		
		
		/* Adicionar as cartas ao baralho */
		for(short i=0;i<13;i++)
		{
			for(short j=0;j<4;j++)
				cartas.add(new Cartas(i,j));
		}
			
		/* Baralhar pelo algoritmo de Fisher–Yates */
		for(int i = cartas.size()-1; i > 0; i--)
		{
			int indice = gerar.nextInt(i+1);	//Obter o indice1 entre 0 e 52
			
			// temp fica com a carta da posição do indice
			Cartas temp = (Cartas) cartas.get(indice); 
			// Trocar a carta que estava na posição do indice com a carta da posicao do i
			cartas.set(indice, cartas.get(i));
			// Trocar a carta que estava na posição i fica com a carta temp
			cartas.set(i, temp);
		}
	}
	
	/* Metodo que tira a carta que está topo do baralho */
	public Cartas tirarCarta(){
		return cartas.remove(cartas.size()-1);	
	}
	
	/* Metodo que mostra o numero de cartas no baralho */
	public int totalCartas(){
		return cartas.size();
	}
}
