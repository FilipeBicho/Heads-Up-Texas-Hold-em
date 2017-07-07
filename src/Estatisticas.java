/* Descricao: Esta classe obtem, guarda e mostra as estatisticas do utilizador
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 5/06/2017
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Estatisticas extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Caminho das cartas
	Icon[][] cartaImg = new Icon[13][4];
	// Painel para a interface das estatisticas
	JPanel paginaEstatisticas = new JPanel();
	// Recebe a imagem de fundo do jogo
	JLabel background = new JLabel();
	// Recebe o tilulo do jogo
	JLabel titulo = new JLabel();
	// Texto anterior às estatisticas
	JLabel[] descricao = new JLabel[5];
	// Cartas jogadas
	JLabel[] cartasJogadas = new JLabel[2];
	// Cartas que ganharam mais jogos
	JLabel[] cartasGanhas = new JLabel[2];
	// Mão mais alta
	JLabel[] maoAltaLabel = new JLabel[6];
	// Jogadas efetuadas e jogadas ganhas
	JLabel[] totais = new JLabel[3];
	int maxJogadas = 1, maxGanhas = 1;
	int maxPosJogadas = 0, maxPosGanhas = 0;
	// Avaliar as mãos dos jogadores
	AvaliarMao avaliar = new AvaliarMao();
	// Botão para voltar 
	JButton voltar = new JButton("Voltar");
	
	// ArrayList para guardar as cartas
	
	// Cartas jogadas
	ArrayList <Integer> cartaJogadas1Num;
	ArrayList <Integer> cartaJogadas1Naipe;
	ArrayList <Integer> cartaJogadas2Num;
	ArrayList <Integer> cartaJogadas2Naipe;
	ArrayList <Integer> contadorJogadas;
	ArrayList <Cartas> cartaJogadas1;
	ArrayList <Cartas> cartaJogadas2;
	
	// Cartas ganhas
	ArrayList <Integer> cartaGanhas1Num;
	ArrayList <Integer> cartaGanhas1Naipe;
	ArrayList <Integer> cartaGanhas2Num;
	ArrayList <Integer> cartaGanhas2Naipe;
	ArrayList <Integer> contadorGanhas;
	ArrayList <Cartas> cartaGanhas1;
	ArrayList <Cartas> cartaGanhas2;
	
	// Mão mais alta
	Cartas[] maoAlta = new Cartas[5]; 
	ArrayList <Integer> maoNum;
	ArrayList <Integer> maoNaipe;
	Integer resultadoMaoAlta = 0;
	
	// Total de jogadas
	int jogadas, jogadasGanhas;

	
	// Construtor vazio que inicializa as ArrayList, usado em modo de texto
	Estatisticas(){
		super();
		cartaJogadas1Num = new ArrayList<Integer>();
		cartaJogadas1Naipe = new ArrayList<Integer>();
		cartaJogadas2Num = new ArrayList<Integer>();
		cartaJogadas2Naipe = new ArrayList<Integer>();
		contadorJogadas = new ArrayList<Integer>();
		cartaJogadas1 = new ArrayList<Cartas>();
		cartaJogadas2 = new ArrayList<Cartas>();
		
		cartaGanhas1Num = new ArrayList<Integer>();
		cartaGanhas1Naipe = new ArrayList<Integer>();
		cartaGanhas2Num = new ArrayList<Integer>();
		cartaGanhas2Naipe = new ArrayList<Integer>();
		contadorGanhas = new ArrayList<Integer>();
		cartaGanhas1 = new ArrayList<Cartas>();
		cartaGanhas2 = new ArrayList<Cartas>();
	};
	
	// Construtor usado para desenhar a página das estatisticas, usado em modo gráfico
	Estatisticas(JFrame janela, String nome) throws FileNotFoundException
	{
		super();
		
		// Inicializar os ArrayList
		cartaJogadas1Num = new ArrayList<Integer>();
		cartaJogadas1Naipe = new ArrayList<Integer>();
		cartaJogadas2Num = new ArrayList<Integer>();
		cartaJogadas2Naipe = new ArrayList<Integer>();
		contadorJogadas = new ArrayList<Integer>();
		cartaJogadas1 = new ArrayList<Cartas>();
		cartaJogadas2 = new ArrayList<Cartas>();	
		cartaGanhas1Num = new ArrayList<Integer>();
		cartaGanhas1Naipe = new ArrayList<Integer>();
		cartaGanhas2Num = new ArrayList<Integer>();
		cartaGanhas2Naipe = new ArrayList<Integer>();
		contadorGanhas = new ArrayList<Integer>();
		cartaGanhas1 = new ArrayList<Cartas>();
		cartaGanhas2 = new ArrayList<Cartas>();
		
		// Definir o layout para null para posicionar elementos
		paginaEstatisticas.setLayout(null);
		// Obter a imagem de fundo
		background = new JLabel(new ImageIcon("imagens/estatisticas.jpg"));
		// Definir a dimensão da imagem de fundo
		background.setBounds(0, 0, 1200, 700);
		
		 // Inicializar titulo
		titulo.setFont(new Font("Courier New", Font.BOLD, 30));
		titulo.setForeground(Color.white);	
		titulo.setBounds(300, 20, 800, 20);
		background.add(titulo); 
		
		// Inicializar botão para voltar
		voltar.setBounds(900, 570, 150, 50);
		voltar.setFont(new Font("Courier New", Font.BOLD, 20));
		voltar.setFocusPainted(false);
		voltar.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	@SuppressWarnings("unused")
				Main main = new Main(nome);
		    
		    }
		});
		voltar.setBackground(Color.WHITE);
		background.add(voltar);	
		
		//Verificar se o ficheiro existe
		if (!Files.isDirectory(Paths.get(nome))) 	
			titulo.setText("Não existe estatisticas de " + nome);
		else
		{
			titulo.setText("Estatisticas de " + nome);
			
			//Carregar as 52 cartas para JLabel
			for(int i = 0; i < 13; i++)
			{
				for(int j = 0; j < 4; j++)
					cartaImg[i][j] = new ImageIcon("imagens/Cartas/" + i + j + ".png");					
			}
			
			 // Cartas mais jogadas
			descricao[0] = new JLabel("Cartas mais jogadas: ");
			descricao[0].setFont(new Font("Courier New", Font.BOLD, 20));
			descricao[0].setForeground(Color.white);
			descricao[0].setBounds(50, 150, 300, 20);
			background.add(descricao[0]); 
			
			//Obter as cartas
			obterCartasJogadas(nome);
			
			// Obter as cartas mais jogadas 
			for(int i = 0; i < contadorJogadas.size(); i++)
			{
				if(contadorJogadas.get(i) > maxJogadas)
				{
					maxPosJogadas = i;
					maxJogadas = contadorJogadas.get(i);
				}
			}
						
			cartasJogadas[0] = new JLabel(new ImageIcon());
			cartasJogadas[0].setIcon(cartaImg[cartaJogadas1Num.get(maxPosJogadas)][cartaJogadas1Naipe.get(maxPosJogadas)]);
			cartasJogadas[0].setBounds(400, 100, 90, 130);
			background.add(cartasJogadas[0]);
			cartasJogadas[1] = new JLabel(new ImageIcon());
			cartasJogadas[1].setIcon(cartaImg[cartaJogadas2Num.get(maxPosJogadas)][cartaJogadas2Naipe.get(maxPosJogadas)]);
			cartasJogadas[1].setBounds(500, 100, 90, 130);
			background.add(cartasJogadas[1]);
			
			maoAltaLabel[5] = new JLabel(contadorJogadas.get(maxPosJogadas)+"");
			maoAltaLabel[5].setFont(new Font("Courier New", Font.BOLD, 20));
			maoAltaLabel[5].setForeground(Color.white);
			maoAltaLabel[5].setBounds(950, 150, 200, 20);
			background.add(maoAltaLabel[5]);
			
			 // Cartas que ganharam mais jogos
			descricao[1] = new JLabel("Cartas mais vitoriosas: ");
			descricao[1].setFont(new Font("Courier New", Font.BOLD, 20));
			descricao[1].setForeground(Color.white);
			descricao[1].setBounds(50, 300, 300, 20);
			background.add(descricao[1]); 
			
			
			// Obter as cartas que ganharam mais jogos
			obterCartasGanhas(nome);
			
			// Obter as cartas que ganharam mais jogos
			for(int i = 0; i < contadorGanhas.size(); i++)
			{
				if(contadorGanhas.get(i) > maxGanhas)
				{
					maxPosGanhas = i;
					maxGanhas = contadorGanhas.get(i);
				}
			}
			
			if(!contadorGanhas.isEmpty())
			{

				cartasGanhas[0] = new JLabel(new ImageIcon());
				cartasGanhas[0].setIcon(cartaImg[cartaGanhas1Num.get(maxPosGanhas)][cartaGanhas1Naipe.get(maxPosGanhas)]);
				cartasGanhas[0].setBounds(400, 250, 90, 130);
				background.add(cartasGanhas[0]);
				cartasGanhas[1] = new JLabel(new ImageIcon());
				cartasGanhas[1].setIcon(cartaImg[cartaGanhas2Num.get(maxPosGanhas)][cartaGanhas2Naipe.get(maxPosGanhas)]);
				cartasGanhas[1].setBounds(500, 250, 90, 130);
				background.add(cartasGanhas[1]);
				
				maoAltaLabel[5] = new JLabel(contadorGanhas.get(maxPosGanhas)+"");
				maoAltaLabel[5].setFont(new Font("Courier New", Font.BOLD, 20));
				maoAltaLabel[5].setForeground(Color.white);
				maoAltaLabel[5].setBounds(950, 300, 200, 20);
				background.add(maoAltaLabel[5]);
			}
					
			//Obter a mão mais alta
			obterMaoAlta(nome);
			
			 // Cartas mais jogadas
			descricao[2] = new JLabel("Mão mais alta: ");
			descricao[2].setFont(new Font("Courier New", Font.BOLD, 20));
			descricao[2].setForeground(Color.white);
			descricao[2].setBounds(50, 450, 300, 20);
			background.add(descricao[2]); 
			
			for(int i=0; i < maoAlta.length; i++)
			{
				maoAltaLabel[i] = new JLabel(new ImageIcon());
				maoAltaLabel[i].setIcon(cartaImg[maoAlta[i].getNumCarta()][maoAlta[i].getNaipe()]);
				background.add(maoAltaLabel[i]);
			}
			maoAltaLabel[0].setBounds(400, 400, 90, 130);
			maoAltaLabel[1].setBounds(500, 400, 90, 130);
			maoAltaLabel[2].setBounds(600, 400, 90, 130);
			maoAltaLabel[3].setBounds(700, 400, 90, 130);
			maoAltaLabel[4].setBounds(800, 400, 90, 130);
			
			maoAltaLabel[5] = new JLabel(avaliar.mostrarClassificacao(resultadoMaoAlta));
			maoAltaLabel[5].setFont(new Font("Courier New", Font.BOLD, 20));
			maoAltaLabel[5].setForeground(Color.white);
			maoAltaLabel[5].setBounds(950, 450, 200, 20);
			background.add(maoAltaLabel[5]);
			
			// Obter os totais
			obterTotais(nome);
			
			totais[0] = new JLabel(jogadas + " jogadas efetuadas até ao momento");
			totais[0] .setFont(new Font("Courier New", Font.BOLD, 20));
			totais[0] .setForeground(Color.white);
			totais[0] .setBounds(50, 550, 600, 20);
			background.add(totais[0]);
			totais[1] = new JLabel(jogadasGanhas + " jogadas ganhas até ao momento");
			totais[1] .setFont(new Font("Courier New", Font.BOLD, 20));
			totais[1] .setForeground(Color.white);
			totais[1] .setBounds(50, 600, 600, 20);
			background.add(totais[1]);
			totais[2] = new JLabel(nome + " ganhou " + (((float)jogadasGanhas/jogadas)*100) + "% dos jogos");
			totais[2] .setFont(new Font("Courier New", Font.BOLD, 20));
			totais[2] .setForeground(Color.white);
			totais[2] .setBounds(50, 650, 600, 20);
			background.add(totais[2]);

		}

		paginaEstatisticas.add(background);
		
		// Adicionar página Inicial à janela
		janela.getContentPane().add(paginaEstatisticas);	
		
	}
	
	/* Guarda as cartas do jogador no arrayList
	 * se a cartas já existirem então incrementa o contador da carta
	 */	
	public void cartasJogadas(Cartas[] cartas)
	{	
		boolean flag = false;
		// Fazer iterações sobre as cartas
		for(int i = 0; i < cartaJogadas1.size(); i++)
		{
			// Se a primeira carta for igual 
			if(cartas[0].getNumCarta() == cartaJogadas1Num.get(i) && 
				cartas[0].getNaipe() == cartaJogadas1Naipe.get(i))
			{
				// Se a segunda também for igual então incrementa o contador
				if(cartas[1].getNumCarta() == cartaJogadas2Num.get(i) && 
						cartas[1].getNaipe() == cartaJogadas2Naipe.get(i))
				{
					int inc = contadorJogadas.get(i);
					inc++;
					contadorJogadas.set(i, inc);
					flag = true;
					return;
				}
				
			}
			// O mesmo processo mas por ordem inversa
			else if(cartas[0].getNumCarta() == cartaJogadas2Num.get(i) && 
					cartas[0].getNaipe() == cartaJogadas2Naipe.get(i))
			{
				if(cartas[1].getNumCarta() == cartaJogadas1Num.get(i) && 
						cartas[1].getNaipe() == cartaJogadas1Naipe.get(i))
				{
					int inc = contadorJogadas.get(i);
					inc++;
					contadorJogadas.set(i, inc);
					flag = true;
					return;
				}
				
			}
		}
		
		// Se as cartas não existirem então adiciona-as
		if(!flag)
		{
			cartaJogadas1Num.add((int) cartas[0].getNumCarta());
			cartaJogadas1Naipe.add((int) cartas[0].getNaipe());
			cartaJogadas2Num.add((int) cartas[1].getNumCarta());
			cartaJogadas2Naipe.add((int) cartas[1].getNaipe());
			contadorJogadas.add(1);
			cartaJogadas1.add(new Cartas((short) cartas[0].getNumCarta(), (short)  cartas[0].getNaipe()));
			cartaJogadas2.add(new Cartas((short) cartas[1].getNumCarta(), (short) cartas[1].getNaipe()));
			
		}
	}

	/* Guarda as cartas ganhas do jogador no arrayList
	 * se a cartas já existirem então incremente o contador da carta
	 */	
	public void cartasGanhas(Cartas[] cartas)
	{	
		boolean flag = false;
		// Fazer iterações sobre as cartas
		for(int i = 0; i < cartaGanhas1.size(); i++)
		{
			// Se a primeira carta for igual 
			if(cartas[0].getNumCarta() == cartaGanhas1Num.get(i) && 
				cartas[0].getNaipe() == cartaGanhas1Naipe.get(i))
			{
				// Se a segunda também for igual então incrementa o contador
				if(cartas[1].getNumCarta() == cartaGanhas2Num.get(i) && 
						cartas[1].getNaipe() == cartaGanhas2Naipe.get(i))
				{
					int inc = contadorGanhas.get(i);
					inc++;
					contadorGanhas.set(i, inc);
					flag = true;
					return;
				}
				
			}
			// O mesmo processo mas por ordem inversa
			else if(cartas[0].getNumCarta() == cartaGanhas2Num.get(i) && 
					cartas[0].getNaipe() == cartaGanhas2Naipe.get(i))
			{
				if(cartas[1].getNumCarta() == cartaGanhas1Num.get(i) && 
						cartas[1].getNaipe() == cartaGanhas1Naipe.get(i))
				{
					int inc = contadorGanhas.get(i);
					inc++;
					contadorGanhas.set(i, inc);
					flag = true;
					return;
				}
				
			}
		}
		
		// Se as cartas não existirem então adiciona-as
		if(!flag)
		{
			cartaGanhas1Num.add((int) cartas[0].getNumCarta());
			cartaGanhas1Naipe.add((int) cartas[0].getNaipe());
			cartaGanhas2Num.add((int) cartas[1].getNumCarta());
			cartaGanhas2Naipe.add((int) cartas[1].getNaipe());
			contadorGanhas.add(1);
			cartaGanhas1.add(new Cartas((short) cartas[0].getNumCarta(), (short)  cartas[0].getNaipe()));
			cartaGanhas2.add(new Cartas((short) cartas[1].getNumCarta(), (short) cartas[1].getNaipe()));	
		}
	}
	
	// Guarda a mão mais alta no array de cartas
	public void maoMaisAlta(Cartas[] mao, int res)
	{
		// Guarda o resultado das mâos dos jogadores
		int resultado[] = new int[2];
		// Calcular a mão mais alta
		CalcularVencedor calcular = new CalcularVencedor();

		// Se ainda não existir mão alta
		if(maoAlta == null)
		{
			maoAlta[0] = mao[0];
			maoAlta[1] = mao[1];
			maoAlta[2] = mao[2];
			maoAlta[3] = mao[3];
			maoAlta[4] = mao[4];
			this.resultadoMaoAlta = res;
		}
		else
		{
			resultado[0] = resultadoMaoAlta;
			resultado[1] = res;
			
			// Se a mão passada em argumento for maior que a mão atual
			if(calcular.calcularVencedor(maoAlta, mao, resultado) == 2)
			{
				maoAlta[0] = mao[0];
				maoAlta[1] = mao[1];
				maoAlta[2] = mao[2];
				maoAlta[3] = mao[3];
				maoAlta[4] = mao[4];
				this.resultadoMaoAlta = res;
			}
		}
	}
	
	// Obter as cartas mais jogadas do utilizador guardadas no seu ficheiro
	public void obterCartasJogadas(String nome) throws FileNotFoundException
	{
		// Verificar se já existe um diretorio com o nome do utilizador
		if (Files.isDirectory(Paths.get(nome))) 
		{
			File f = new File(nome+"/" + "cartas_jogadas_bd.txt");
			
			// Verifica se o ficheiro existe
			if(f.exists())
			{
				Scanner s = new Scanner(new File(nome+"/" + "cartas_jogadas_bd.txt"));
			
				// Adicionar as cartas
				if(s != null)
				{
					while (s.hasNext()){
						
						int num1 = s.nextInt();
						int naipe1 = s.nextInt();
						int num2 = s.nextInt();
						int naipe2 = s.nextInt();
						int n = s.nextInt();
						
						cartaJogadas1Num.add(num1);
						cartaJogadas1Naipe.add(naipe1);
						cartaJogadas2Num.add(num2);
						cartaJogadas2Naipe.add(naipe2);
						contadorJogadas.add(n);
						cartaJogadas1.add(new Cartas((short) num1, (short) naipe1));
						cartaJogadas2.add(new Cartas((short) num2, (short) naipe2));
						
					}
				}
				
				s.close();
			}
		}
	}
	
	// Obter as cartas mais vitoriosas do utilizador guardadas no seu ficheiro
	public void obterCartasGanhas(String nome) throws FileNotFoundException
	{
		// Verificar se já existe um diretorio com o nome do utilizador

		if (Files.isDirectory(Paths.get(nome))) 
		{
			File f = new File(nome+"/" + "cartas_ganhas_bd.txt");
			
			// Verifica se o ficheiro existe
			if(f.exists())
			{
				Scanner s = new Scanner(new File(nome+"/" + "cartas_ganhas_bd.txt"));
			
				// Adicionar as cartas
				if(s != null)
				{
					while (s.hasNext()){
						
						int num1 = s.nextInt();
						int naipe1 = s.nextInt();
						int num2 = s.nextInt();
						int naipe2 = s.nextInt();
						int n = s.nextInt();
						
						cartaGanhas1Num.add(num1);
						cartaGanhas1Naipe.add(naipe1);
						cartaGanhas2Num.add(num2);
						cartaGanhas2Naipe.add(naipe2);
						contadorGanhas.add(n);
						cartaGanhas1.add(new Cartas((short) num1, (short) naipe1));
						cartaGanhas2.add(new Cartas((short) num2, (short) naipe2));
						
					}
				}
				
				s.close();
			}
		}
	}
	
	// Obter a mão alta do ficheiro
	public void obterMaoAlta(String nome) throws FileNotFoundException
	{
		if (Files.isDirectory(Paths.get(nome))) 
		{
			File f = new File(nome+"/" + "mao_alta.txt");
			
			// Verifica se o ficheiro existe
			if(f.exists())
			{
				Scanner s = new Scanner(new File(nome+"/" + "mao_alta.txt"));
				int i = 0, j = 0;
				// Adicionar as cartas
				if(s != null)
				{
					while (i < 9){
						
						int num = s.nextInt();
						i++;
						int naipe = s.nextInt();
						i++;
						maoAlta[j] = new Cartas((short) num, (short) naipe);
						j++;
						
					}
					try {
						resultadoMaoAlta =(int) s.nextInt();
					} catch (InputMismatchException e) {
					    System.out.print(e.getMessage()); //try to find out specific reason.
					}		
				}	
				s.close();
			}
		}
	}
	
	// Obter os totais do ficheiro
	public void obterTotais(String nome) throws FileNotFoundException
	{

		if (Files.isDirectory(Paths.get(nome))) 
		{
			File f = new File(nome+"/" + "totais.txt");
			
			// Verifica se o ficheiro existe
			if(f.exists())
			{
				Scanner s = new Scanner(new File(nome+"/" + "totais.txt"));
				// Adicionar as cartas
				if(s != null)
				{
					jogadas = s.nextInt();
					jogadasGanhas = s.nextInt();
				}	
				s.close();
			}
		}
	}
	
	// Guarda as cartas que sairam ao jogador no ficheiro
	public void guardarCartasJogadas(String nome) throws IOException
	{
		File theDir = new File(nome);

		// Criar directorio se não existir
		if (!theDir.exists()) 
		{
		    boolean result = false;
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        System.out.println("Erro a criar directorio!");
		    }        
		    if(result) 
		    {    
		    	// Variavel que escreve no ficheiro
				FileWriter escrever = new FileWriter(nome+"/" + "cartas_jogadas_bd.txt");
				String newLine = System.getProperty("line.separator");

				try
				{
					int i=0;
					while(i < cartaJogadas1Num.size())
					{
						escrever.write(cartaJogadas1Num.get(i) + " " + cartaJogadas1Naipe.get(i) + 
								" " + cartaJogadas2Num.get(i) + " " + cartaJogadas2Naipe.get(i) + " " + contadorJogadas.get(i) + newLine);		
						i++;
					}			
				}finally
				{
					if(escrever != null)
						escrever.close();
				}
		    }
		}
		else
		{
			// Variavel que escreve no ficheiro
			FileWriter escrever = new FileWriter(nome+"/" + "cartas_jogadas_bd.txt");
			String newLine = System.getProperty("line.separator");

			try
			{
				int i=0;
				while(i < cartaJogadas1Num.size())
				{
					escrever.write(cartaJogadas1Num.get(i) + " " + cartaJogadas1Naipe.get(i) + 
							" " + cartaJogadas2Num.get(i) + " " + cartaJogadas2Naipe.get(i) + " " + contadorJogadas.get(i) + newLine);		
					i++;
				}			
			}finally
			{
				if(escrever != null)
					escrever.close();
			}
		}
	}
	
	// Guarda as cartas que sairam ao jogador no ficheiro
	public void guardarCartasGanhas(String nome) throws IOException
	{
		File theDir = new File(nome);

		// Criar directorio se não existir
		if (!theDir.exists()) 
		{
		    boolean result = false;
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        System.out.println("Erro a criar directorio!");
		    }        
		    if(result) 
		    {    
		    	// Variavel que escreve no ficheiro
				FileWriter escrever = new FileWriter(nome+"/" + "cartas_ganhas_bd.txt");
				String newLine = System.getProperty("line.separator");

				try
				{
					int i=0;
					while(i < cartaGanhas1Num.size())
					{
						escrever.write(cartaGanhas1Num.get(i) + " " + cartaGanhas1Naipe.get(i) + 
								" " + cartaGanhas2Num.get(i) + " " + cartaGanhas2Naipe.get(i) + " " + contadorGanhas.get(i) + newLine);		
						i++;
					}			
				}finally
				{
					if(escrever != null)
						escrever.close();
				}
		    }
		}
		else
		{
			// Variavel que escreve no ficheiro
			FileWriter escrever = new FileWriter(nome+"/" + "cartas_ganhas_bd.txt");
			String newLine = System.getProperty("line.separator");

			try
			{
				int i=0;
				while(i < cartaGanhas1Num.size())
				{
					escrever.write(cartaGanhas1Num.get(i) + " " + cartaGanhas1Naipe.get(i) + 
							" " + cartaGanhas2Num.get(i) + " " + cartaGanhas2Naipe.get(i) + " " + contadorGanhas.get(i) + newLine);		
					i++;
				}			
			}finally
			{
				if(escrever != null)
					escrever.close();
			}
		}
	}

	// Guarda as cartas que sairam ao jogador no ficheiro
	public void guardarMaoAlta(String nome) throws IOException
	{
		File theDir = new File(nome);

		// Criar directorio se não existir
		if (!theDir.exists()) 
		{
		    boolean result = false;
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        System.out.println("Erro a criar directorio!");
		    }        
		    if(result) 
		    {    
		    	// Variavel que escreve no ficheiro
				FileWriter escrever = new FileWriter(nome+"/" + "mao_alta.txt");
				String newLine = System.getProperty("line.separator");

				try
				{
					int i=0;
					while(i < maoAlta.length)
					{
						escrever.write(maoAlta[i].getNumCarta() + " " + maoAlta[i].getNaipe() + " " + newLine);
						System.out.println(i);
						i++;
					}	
					escrever.write(new Integer(resultadoMaoAlta).toString());
				}finally
				{
					if(escrever != null)
						escrever.close();
				}
		    }
		}
		else
		{
			// Variavel que escreve no ficheiro
			FileWriter escrever = new FileWriter(nome+"/" + "mao_alta.txt");
			String newLine = System.getProperty("line.separator");

			try
			{
				int i=0;
				while(i < maoAlta.length)
				{
					escrever.write(maoAlta[i].getNumCarta() + " " + maoAlta[i].getNaipe() + " " + newLine);
					i++;
				}		
				escrever.write(new Integer(resultadoMaoAlta).toString());
			}finally
			{
				if(escrever != null)
					escrever.close();
			}
		}
	}
	
	public void guardarTotais(String nome) throws IOException
	{
		File theDir = new File(nome);

		// Criar directorio se não existir
		if (!theDir.exists()) 
		{
		    boolean result = false;
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        System.out.println("Erro a criar directorio!");
		    }        
		    if(result) 
		    {    
		    	// Variavel que escreve no ficheiro
				FileWriter escrever = new FileWriter(nome+"/" + "totais.txt");
				String newLine = System.getProperty("line.separator");

				try
				{
					escrever.write(new Integer(jogadas).toString() + " " + newLine);
					escrever.write(new Integer(jogadasGanhas).toString());
				}finally
				{
					if(escrever != null)
						escrever.close();
				}
		    }
		}
		else
		{
			// Variavel que escreve no ficheiro
			FileWriter escrever = new FileWriter(nome+"/" + "totais.txt");
			String newLine = System.getProperty("line.separator");

			try
			{
				escrever.write(new Integer(jogadas).toString() + " " + newLine);
				escrever.write(new Integer(jogadasGanhas).toString());
			}finally
			{
				if(escrever != null)
					escrever.close();
			}
		}
	}
	
	// Mostrar as estatisticas do utilizador
	public void mostrarEstatisticas()
	{
		int maxJogadas = 1, maxGanhas = 1;
		int maxPosJogadas = 0, maxPosGanhas = 0;
		// Avaliar as mãos dos jogadores
		AvaliarMao avaliar = new AvaliarMao();
		
		// Obter as cartas mais jogadas 
		for(int i = 0; i < contadorJogadas.size(); i++)
		{
			if(contadorJogadas.get(i) > maxJogadas)
			{
				maxPosJogadas = i;
				maxJogadas = contadorJogadas.get(i);
			}
		}
		
		// Obter as cartas que ganharam mais jogos
		for(int i = 0; i < contadorGanhas.size(); i++)
		{
			if(contadorGanhas.get(i) > maxGanhas)
			{
				maxPosGanhas = i;
				maxGanhas = contadorGanhas.get(i);
			}
		}
		
		if(!contadorJogadas.isEmpty())
			System.out.println("As cartas mais jogadas são: " + cartaJogadas1.get(maxPosJogadas) + " " + cartaJogadas2.get(maxPosJogadas) + ": " + maxJogadas); 
		if(!contadorGanhas.isEmpty())
			System.out.println("As cartas que ganharam mais jogos são: " + cartaGanhas1.get(maxPosGanhas) + " " + cartaGanhas2.get(maxPosGanhas) + ": " + maxGanhas); 
		if(maoAlta != null)
			System.out.println("A mão mais alta é: " + maoAlta[0] + " " + maoAlta[1] + " " + maoAlta[2] + " " + 
					maoAlta[3] + " " + maoAlta[4] + ": " + avaliar.mostrarClassificacao(resultadoMaoAlta)); 
		System.out.println("Total de jogadas: " + jogadas);
		System.out.println("Jogadas ganhas: " + jogadasGanhas + " (" + ((float)jogadasGanhas/jogadas) + ")");
	}
	
	// Calcular o numero de jogadas e jogadas ganhas
	public void totalJogadas(int jog, int jog_ganhas)
	{
		jogadas = jogadas + jog;
		jogadasGanhas = jogadasGanhas + jog_ganhas;
	}
}
