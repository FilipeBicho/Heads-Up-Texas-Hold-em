/* Descricao: Cria a interface gráfica do Simulador de poker
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 26/06/2017
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;


/* Classe principal que cria a interface 2D do jogo de poker
 * 
 */
public class InterfaceGrafica extends JFrame{
	
	private static final long serialVersionUID = 1L;

	// Panel da janela
	JPanel paginaJogo = new JPanel();
	// Recebe a imagem de fundo do jogo
	JLabel background = new JLabel();
	// Recebe o tilulo do jogo
	JLabel titulo = new JLabel();
	// Caminho das cartas
	Icon[][] cartaImg = new Icon[13][4];
	// Guarda as cartas dos jogadores
	JLabel[][] jogadorLabel = new JLabel[2][2];
	// Guarda as cartas da mesa
	JLabel[] mesaLabel = new JLabel[5];
	// Mostra o nome dos jogadores
	JLabel[] nomeJogador = new JLabel[2];
	// Guarda as probabilidades dos jogadores
	JLabel[] probabilidadeLabel = new JLabel[2];
	// Imagem da ficha do dealer
	JLabel fichaDealerImg = new JLabel();
	// Botôes do jogo
	// 0 - Fold
	// 1 - Check
	// 2 - Apostar blind
	// 3 - Apostar valor
	// 4 - All in
	JButton[] botao = new JButton[6];
	// Recebe o valor da aposta
	JTextField valorAposta = new JTextField();
	// Recebe o resumo do jogo
	JLabel resumo = new JLabel();
	JTextArea resumoTexto = new JTextArea();
	JScrollPane scroll = new JScrollPane(resumoTexto);
	// Mostra a descrição da ultima ação
	JLabel jogada = new JLabel();
	// Mostra as apostas dos jogadores
	JLabel[] apostaLabel = new JLabel[2];
	// Mostra as fichas na mesa
	// 0 - Fichas do jogador
	// 1 - Fichas do oponente
	// 2 - Pote
	JLabel[] fichasLabel = new JLabel[3];
	// Mostrar o ranking da mão do jogador
	JLabel[] ranking = new JLabel[2];
	// Mostra qual o jogador que venceu o jogo
	JLabel vitoria = new JLabel("");
	// Botão para verificar se o jogador quer jogar novamente
	JButton[] botaoFinal = new JButton[2];
	
	// Guardar as estatisticas do jogador
	Estatisticas guardar = new Estatisticas();
	// Guardar as estatisticas do oponente
	Estatisticas guardarOponente = new Estatisticas();
	// Distribuir as cartas pelos jogadores
	Dealer distribuir = new Dealer();
	// Escolhe o tipo de simulador
	Simulador computador = new Simulador();
	// Guarda as cartas de ambos os jogadores
	Cartas jogador[][] = new Cartas[2][2];
	// Guarda as mãos de ambos os jogadores
	Cartas maoJogador[][] = new Cartas[2][5];
	// Guarda as cartas da mesa
	Cartas[] mesa = new Cartas[5];
	// Contonar o erro das duas ultimas cartas
	Cartas[] erro = new Cartas[2]; 
	// Avaliar as mãos dos jogadores
	AvaliarMao avaliar = new AvaliarMao();
	// Calcular o vencedor entre os jogadores
	CalcularVencedor calcular = new CalcularVencedor();
	// Guarda o resultado das mâos dos jogadores
	int resultado[] = new int[2];
	//Calcular as probabilidades das mâos dos jogadores
	Probabilidades prob = new Probabilidades();
	// Guarda as probabilidades dos jogadores 
	float[] probabilidades = new float[2];
	// Obtem as probabilidades após o flop
	float[][] probTemp = new float[2][3];
	// Define a ronda atual
	int ronda;
	// Se um jogador fez a aposta inicial
	private boolean apostaInicial = true;
	// Se um jogador fez o check inicial
	private boolean checkInicial = false;	
	/* Apostas realizadas pelos jogadores
	 * 0 - Aposta do jogador 1
	 * 1 - Aposta do jogador 2
	 */
	float[] aposta = new float[2];
	/* Confirmacao que pode passar a proxima fase
	 * Passa a proxima fase se ambos os jogadores confirmarem (ambos com 1 )
	 * 0 - confirmacao do jogador 1
	 * 1 - confirmacao do jogador 2
	 */
	int[] confirmacao = new int[2];
	/* 0 - Fichas do jogador 1
	 * 1 - Fichas do jogador 2 
	 * 2 - Fichas no pote
	 * 3 - Define se jogada continua ou nao
	 */
	private float[] fichas = new float[4];
	// Guarda o valor das fichas de ambos os jogadores
	// É usado apenas para apostar blinds
	private float[] fichasIniciais = new float[2];
	// Dealer
	int dealer;
	private int smallBlind = 10, 	// Valor da small blind
			bigBlind = 20,			// Valor da big blind 
			pote = 2;				// Posicao onde fica guardado o pote em fichas
	// Recebe o modo de jogo
	int modoJogo;
	// Recebe o nome do utilizador
	String nome;
	// Usado para simular um jogador de poker
	// 0 - opcão de aposta
	// 1 - valor da aposta (usado apenas em apostaValor)
	int[] opcao = new int[2];
	float tempPote = 0;
	String texto = "";
	// Verifica que menu está ativo
	Boolean[] menu = new Boolean[3];
	// Conta o numero de jogos
	int i = 0;
	// Variavel para evitar sobrecontagem quando termina a jogada
	Boolean finalPagamento;
	// Variavel para evitar duas esperas quando termina a jogada
	Boolean espera;
	// Definir o tempo de espera 
	int tempo = 0;
	// Incrementa as jogadas ganhas do utilizador
	int jogadasGanhas = 0;
	int jogadasGanhasOponente = 0;
	// Usado para criar um atraso 
	Timer timer;
	// Guarda o nome do oponente;
	String nomeOponente;
	String tempNome;
	
	// Inicializar as variaveis 
	InterfaceGrafica(JFrame janela) throws IOException
	{
		// Definir o layout para null para posicionar elementos
		paginaJogo.setLayout(null);
		// Obter a imagem de fundo
		background = new JLabel(new ImageIcon("imagens/jogo.png"));
		// Definir a dimensão da imagem de fundo
		background.setBounds(0, 0, 1200, 700);
		
		//Carregar as 52 cartas para JLabel
		for(int i = 0; i < 13; i++)
		{
			for(int j = 0; j < 4; j++)
				cartaImg[i][j] = new ImageIcon("imagens/Cartas/" + i + j + ".png");					
		}
			
		for(int i = 0; i < 2; i++)
		{
			// Inicialiar as cartas dos jogadores
			jogadorLabel[0][i]= new JLabel(new ImageIcon("imagens/Cartas/back.png"));
			jogadorLabel[1][i]= new JLabel(new ImageIcon("imagens/Cartas/back.png"));
			
			// Inicializar o nome dos jogadores
			nomeJogador[i] = new JLabel();
			nomeJogador[i].setText("");
			nomeJogador[i].setFont(new Font("Courier New", Font.BOLD, 20));
			nomeJogador[i].setForeground(Color.white);
			nomeJogador[i].setBounds(500, 630+(i*-600), 250, 20);
			background.add(nomeJogador[i]);	
			
			// Inicializar as probabilidades
			probabilidadeLabel[i] = new JLabel();
			probabilidadeLabel[i].setText(""); 
			probabilidadeLabel[i].setFont(new Font("Courier New", Font.BOLD, 20));
			probabilidadeLabel[i].setForeground(Color.white);
			probabilidadeLabel[i].setBounds(700, 525+(i*-385), 250, 20);
			background.add(probabilidadeLabel[i]);	
			
			// Inicializar as fichas
			fichasLabel[i] = new JLabel(1500 + "€");
			fichasLabel[i].setFont(new Font("Courier New", Font.BOLD, 20));
			fichasLabel[i].setForeground(Color.white);
			fichasLabel[i].setBounds(400, 570+(i*-480), 100, 20);
			background.add(fichasLabel[i]);
		}
		
		// Inicializar pote
		fichasLabel[2] = new JLabel("Pote: " + 0 + "€");
		fichasLabel[2].setFont(new Font("Courier New", Font.BOLD, 20));
		fichasLabel[2].setForeground(Color.white);
		fichasLabel[2].setBounds(500, 400, 200, 20);
		background.add(fichasLabel[2]);
		
		// Inicializar label que mostra o vencedor
		vitoria.setFont(new Font("Courier New", Font.BOLD, 30));
		vitoria.setForeground(Color.white);
		vitoria.setBounds(400, 400, 600, 40);
		vitoria.setVisible(false);
		background.add(vitoria);
		
		// Inicializar as cartas da mesa
		for(int i = 0; i < 5; i++)
			mesaLabel[i] = new JLabel(new ImageIcon("imagens/Cartas/back.png"));

	    // Inicializar titulo
		titulo.setFont(new Font("Courier New", Font.BOLD, 20));
		titulo.setForeground(Color.white);
		titulo.setText("Modificar o texto aqui");
		titulo.setBounds(10, 10, 400, 20);
		background.add(titulo); 
		
		 // Inicializar ranking
		ranking[0] = new JLabel("");
		ranking[0].setFont(new Font("Courier New", Font.BOLD, 16));
		ranking[0].setForeground(Color.white);
		ranking[0].setBounds(950, 520, 400, 50);
		background.add(ranking[0]); 
		ranking[1] = new JLabel("");
		ranking[1].setFont(new Font("Courier New", Font.BOLD, 16));
		ranking[1].setForeground(Color.white);
		ranking[1].setBounds(950, 100, 400, 50);
		background.add(ranking[1]); 
	
		// Posicionar as cartas do jogador
		jogadorLabel[0][0].setBounds(500, 490, 90, 130);			// Carta do lado esquerdo
		background.add(jogadorLabel[0][0]);				
		jogadorLabel[0][1].setBounds(600, 490, 90, 130);			// Carta do lado direito
		background.add(jogadorLabel[0][1]);
		
		// Posicionar as cartas do oponente
		jogadorLabel[1][0].setBounds(500, 60, 90, 130);			// Carta do lado esquerdo
		background.add(jogadorLabel[1][0]);
		jogadorLabel[1][1].setBounds(600, 60, 90, 130);			// Carta do lado direito
		background.add(jogadorLabel[1][1]);
		
		// Posicionar as cartas da mesa
		mesaLabel[0].setBounds(350, 250, 90, 130);				// 1 carta do flop
		background.add(mesaLabel[0]);
		mesaLabel[1].setBounds(450, 250, 90, 130);				// 2 carta do flop
		background.add(mesaLabel[1]);
		mesaLabel[2].setBounds(550, 250, 90, 130);				// 3 carta do flop
		background.add(mesaLabel[2]);
		mesaLabel[3].setBounds(670, 250, 90, 130);				// Turn
		background.add(mesaLabel[3]);
		mesaLabel[4].setBounds(790, 250, 90, 130);				// River
		background.add(mesaLabel[4]);
		
		// Inicializar textField para valor das apostas
		valorAposta.setBackground(Color.white);
		valorAposta.setFont(new Font("Courier New", Font.BOLD, 16));
		valorAposta.setVisible(false);
		valorAposta.setBounds(910, 630, 60, 50);
		background.add(valorAposta);
		
		// Inicializar botões
		botao[0] = new JButton("Fold");
		botao[1] = new JButton("Check");
		botao[2] = new JButton("Call");
		botao[3] = new JButton("Blind");
		botao[4] = new JButton("Valor:");
		botao[5] = new JButton("All in");
		
		botao[0].setBounds(800, 570, 90, 50);
		botao[1].setBounds(800, 570, 100, 50);
		botao[2].setBounds(900, 570, 90, 50);
		botao[3].setBounds(1000, 570, 110, 50);
		botao[4].setBounds(800, 630, 110, 50);
		botao[5].setBounds(1000, 630, 110, 50);
		
		// Esconder os botões
		for(int i = 0; i < 6; i++)
			botao[i].setVisible(false);
		
		// Estilo dos botões
		for(int i = 0; i < 6; i++)
		{
			botao[i].setFont(new Font("Courier New", Font.BOLD, 20));
			botao[i].setFocusPainted(false);
			botao[i].addActionListener(listener);
			botao[i].setBackground(new Color(192, 192, 192));
			background.add(botao[i]);	
		}
		
		// Botões finais
		botaoFinal[0] = new JButton("Sair");	
		botaoFinal[0].setBounds(800, 570, 90, 50);
		botaoFinal[0].setFont(new Font("Courier New", Font.BOLD, 20));
		botaoFinal[0].setFocusPainted(false);
		botaoFinal[0].addActionListener(listener);
		botaoFinal[0].setBackground(new Color(192, 192, 192));
		background.add(botaoFinal[0]);	
		
		botaoFinal[1] = new JButton("Novo Jogo");
		botaoFinal[1].setBounds(900, 570, 150, 50);
		botaoFinal[1].setFont(new Font("Courier New", Font.BOLD, 20));
		botaoFinal[1].setFocusPainted(false);
		botaoFinal[1].addActionListener(listener);
		botaoFinal[1].setBackground(new Color(192, 192, 192));
		background.add(botaoFinal[1]);	
		
		// Adicionar ficha do dealer
		fichaDealerImg = new JLabel(new ImageIcon("imagens/Cartas/dealer.png"));		
		background.add(fichaDealerImg);
		
		// Inicializar secção do resumo do jogo
		resumo.setFont(new Font("Courier New", Font.BOLD, 20));
		resumo.setForeground(Color.white);
		resumo.setText("Resumo: ");
		resumo.setBounds(10, 520, 100, 20);
		background.add(resumo); 
		resumoTexto.setBackground(Color.white);
		resumoTexto.setFont(new Font("Courier New", Font.BOLD, 12));		
		resumoTexto.setEditable(false);
		
		// Resumo do jogo com scroll
		 JScrollPane scroll = new JScrollPane ( resumoTexto );
		 scroll.setBounds(10, 550, 270, 140);
		    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		    background.add(scroll);
		
		// Inicializar label das apostas
		for(int i = 0; i < 2; i++)
		{
			apostaLabel[i] = new JLabel();
			apostaLabel[i].setFont(new Font("Courier New", Font.BOLD, 17));
			apostaLabel[i].setForeground(Color.white);
			apostaLabel[i].setBounds(500, 460 - (i*260), 200, 20);
			background.add(apostaLabel[i]); 
		}
		
		// Inicializar label que mostra a ultima jogada
		jogada.setFont(new Font("Courier New", Font.BOLD, 17));
		jogada.setForeground(Color.white);
		jogada.setBounds(800, 20, 400, 20);
		background.add(jogada); 
		
		// Adicionar o background à página inicial
		paginaJogo.add(background);
		
		// Adicionar página Inicial à janela
		janela.getContentPane().add(paginaJogo);		
}
	 
	// Definir as ações dos botões
	ActionListener listener = new ActionListener() 
	{
        @Override
        public void actionPerformed(ActionEvent e) 
        {   	
        	// Se o utilizador carregar no botão fold 
            if (e.getSource() == botao[0]) 
            {      	
            	// Fold_Call
            	if(menu[0] == true)
            	{
            		opcao[0] = 1;			
            		menuFold_Call(0, true);
            	}
            	// Fold_Allin
            	else if(menu[2] == true)
            	{
            		opcao[0] = 1;			
            		menuFold_Allin(0, true);
            	}		
            }
            // Se o utilizaodr carregar no botão check
            if (e.getSource() == botao[1]) 
            {
            	//Check
            	if(menu[1] == true)
            	{
            		opcao[0] = 1;			
            		menuCheck(0, true);
            	}
            }
            // Se o utilizaodr carregar no botão call
            if (e.getSource() == botao[2]) 
            {
            	//Fold call
            	if(menu[0] == true)
            	{
            		opcao[0] = 2;			
            		menuFold_Call(0, true);;
            	}
            	//Fold Allin
            	else if(menu[2] == true)
            	{
            		opcao[0] = 2;			
            		menuFold_Call(0, true);;
            	}
            }         
	         // Se o utilizaodr carregar no botão blind
	        if (e.getSource() == botao[3]) 
	        {
	        	//Fold call
	        	if(menu[0] == true)
	        	{
	        		opcao[0] = 3;			
	        		menuFold_Call(0, true);;
	        	}	
	        	//Check
	        	else if(menu[1] == true)
	        	{
	        		opcao[0] = 2;			
	        		menuCheck(0, true);;
	        	}	
	        }   
	        // Se o utilizador carregar no botão valor
	        if (e.getSource() == botao[4]) 
	        {        	
	        	//Fold call
	        	if(menu[0] == true)
	        	{
	        		try 
	        		{	        		 	
	        			opcao[1] = Integer.parseInt(valorAposta.getText());
	        		 	opcao[0] = 4;
		        		menuFold_Call(0,true);
	        		}
	        		catch (NumberFormatException e1) {
	        		     System.out.println(e1);
	        		}
	        		
	        	}
	        	//Check
	        	else if(menu[1] == true)
	        	{
	        		try
	        		{
	        		    	opcao[1] = Integer.parseInt(valorAposta.getText());
	        		    	opcao[0] = 3;
	        		    	menuCheck(0,true);
	        		}
	        		catch (NumberFormatException e2) {
	        		     System.out.println(e2);
	        		}
	        	}
	        }      
	        // Se o utilizaodr carregar no botão allin
	        if (e.getSource() == botao[5]) 
	        {
	        	// Fold call
	        	if(menu[0] == true)
	        	{
	        		opcao[0] = 5;
	        		menuFold_Call(0,true);
	        	}
	        	// Check
	        	else if(menu[1] == true)
	        	{
	        		opcao[0] = 4;
	        		menuCheck(0,true);
	        	}
	        }
	        
	        // Se o utilizador carregar no botão para sair
	        if(e.getSource() == botaoFinal[0])
	        {
	        	@SuppressWarnings("unused")
				Main main = new Main(tempNome);
	        }
	        
	        // Se o utilizador carregar no botão para jogar novamente
	        if(e.getSource() == botaoFinal[1])
	        {
	        	try {
					inicio(nome,modoJogo);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	        }		
        }
    };
	
	// Metodo inicial para inicializar valores
	public void inicio(String nomeStr, int modo) throws FileNotFoundException
	{
		// Inicializar menus
		menu[0] = false;
		menu[1] = false;
		menu[2] = false;
		
		this.modoJogo = modo;
		this.nome = nomeStr;
		tempNome = nomeStr;

		// Esconder os botões finais e Label final
		botaoFinal[0].setVisible(false);
		botaoFinal[1].setVisible(false);
		vitoria.setVisible(false);
		
		// Mostrar o pote
		fichasLabel[2].setVisible(true);
			
		// Definir o titulo e nome dos jogadores
		if(modoJogo == 1)
		{
			nomeOponente = "Bot certo";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);		
		}		
		if(modoJogo == 2)
		{
			nomeOponente = "Bot agressivo";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);		
		}		
		if(modoJogo == 3)
		{
			nomeOponente = "Bot misto";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);		
		}		
		if(modoJogo == 4)
		{
			nomeOponente = "Bot formula";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);		
		}
		if(modoJogo == 5)
		{
			nome = "Bot certo";
			nomeOponente = "Bot agressivo";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);		
		}
		if(modoJogo == 6)
		{
			nome = "Bot certo";
			nomeOponente = "Bot misto";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);		
		}
		if(modoJogo == 7)
		{
			nome = "Bot certo";
			nomeOponente = "Bot formula";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);	
		}		
		if(modoJogo == 8)
		{
			nome = "Bot agressivo";
			nomeOponente = "Bot misto";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);	
		}
		if(modoJogo == 9)
		{
			nome = "Bot agressivo";
			nomeOponente = "Bot formula";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);	
		}		
		if(modoJogo == 10)
		{
			nome = "Bot misto";
			nomeOponente = "Bot formula";
			titulo.setText(nome  + " vs " + nomeOponente);
			nomeJogador[0].setText(nome);
			nomeJogador[1].setText(nomeOponente);	
		}

		// Fazer o upload das estatisticas do utilizador
		guardar.obterCartasJogadas(nome);
		guardar.obterCartasGanhas(nome);
		guardar.obterMaoAlta(nome);
		// Fazer o upload das estatisticas do utilizador
		guardarOponente.obterCartasJogadas(nomeOponente);
		guardarOponente.obterCartasGanhas(nomeOponente);
		guardarOponente.obterMaoAlta(nomeOponente);
		
		Random dealerRandom = new Random();
		
		// Escolhe o dealer
		dealer = dealerRandom.nextInt((1-0)+1) + 0;
		
		// Mudar a posição do dealer
		if(dealer == 0)
			fichaDealerImg.setBounds(440, 140, 50, 50);
		else
			fichaDealerImg.setBounds(440, 495, 50, 50);
		
		// Inicializar valores
		fichas[0] = 1500; 
		fichas[1] = 1500;
		fichas[pote] = 0;
		tempPote = 0;
		aposta[0] = 0;
		aposta[1] = 0;
		
		// Começar o jogo
		preFlop();

	}
	
	// Ronda antes do flop
	public void preFlop() throws FileNotFoundException
	{					
		// Fazer reset ao ranking
		ranking[0].setText("");
		ranking[1].setText("");
		
		// Esconder as cartas do adversário
		jogadorLabel[1][0].setIcon(new ImageIcon("imagens/Cartas/back.png"));
		jogadorLabel[1][1].setIcon(new ImageIcon("imagens/Cartas/back.png"));
		
		// Baralhar as cartas
		Baralho baralho = new Baralho();
		
		// Distribuir as duas cartas pelos jogadores
		distribuir.darCartas(baralho, jogador[0], jogador[1]);
		
		// Guardar cartas jogadas
		guardar.cartasJogadas(jogador[0]);
		guardarOponente.cartasJogadas(jogador[1]);
		
		// Inicializar cartas do jogador
		jogadorLabel[0][0].setIcon(cartaImg[jogador[0][0].getNumCarta()][jogador[0][0].getNaipe()]);
		jogadorLabel[0][1].setIcon(cartaImg[jogador[0][1].getNumCarta()][jogador[0][1].getNaipe()]);
	
		// Inicializar cartas do oponente em computador VS computador
		if(modoJogo > 4)
		{
			jogadorLabel[1][0].setIcon(cartaImg[jogador[1][0].getNumCarta()][jogador[1][0].getNaipe()]);
			jogadorLabel[1][1].setIcon(cartaImg[jogador[1][1].getNumCarta()][jogador[1][1].getNaipe()]);	
		}
		
		// Dar o flop 
		distribuir.darFlop(baralho, mesa);
		// Dar o turn 
		distribuir.darTurn(baralho, mesa);
		// Dar o flop 
		distribuir.darRiver(baralho, mesa);

		// Esconder as cartas da mesa
		for(int i = 0; i < 5; i++)
			mesaLabel[i].setVisible(false);
		
		// Inicializar cartas da mesa
		mesaLabel[0].setIcon(cartaImg[mesa[0].getNumCarta()][mesa[0].getNaipe()]);
		mesaLabel[1].setIcon(cartaImg[mesa[1].getNumCarta()][mesa[1].getNaipe()]);
		mesaLabel[2].setIcon(cartaImg[mesa[2].getNumCarta()][mesa[2].getNaipe()]);
		mesaLabel[3].setIcon(cartaImg[mesa[3].getNumCarta()][mesa[3].getNaipe()]);
		mesaLabel[4].setIcon(cartaImg[mesa[4].getNumCarta()][mesa[4].getNaipe()]);
					
		// Obter as probabilidades de vitoria do computador no preflop
		probabilidades[1] = (float) prob.probabilidadesPreFlop(jogador[1]);
		
		// Para o caso de o jogo ser computador vs computador
		if(modoJogo > 4)
		{
			probabilidades[0] = (float) prob.probabilidadesPreFlop(jogador[0]);
			probabilidadeLabel[0].setText(probabilidades[0]+"%");
			probabilidadeLabel[1].setText(probabilidades[1]+"%");
		}
		// Ronda pré flop
		ronda = 0;
		
		// Primeira ronda de apostas
		apostasPreFlop();
	}
	
	// Ronda do flop
	public void flop()
	{
		// Atualizar o valor do pote
		fichas[pote]+=tempPote;
		
		// Mostrar as cartas do flop
		for(int i = 0; i < 3; i++)
			mesaLabel[i].setVisible(true);
		
		// Mostrar o ranking do jogador
		resultado[0] = avaliar.avaliar(jogador[0],mesa, 5);
		ranking[0].setText(avaliar.mostrarClassificacao(resultado[0]));
		
		if(modoJogo > 4)
		{
			// Mostrar o ranking do jogador
			resultado[1] = avaliar.avaliar(jogador[1],mesa, 5);
			ranking[1].setText(avaliar.mostrarClassificacao(resultado[1]));
		}
	
		erro[0] = mesa[3];
		erro[1] = mesa[4];
		// Para o caso de o jogo ser computador vs computador
		if(modoJogo > 4)
			probTemp[0] = prob.probabilidadesFlop(jogador[0], mesa);
		// Obter as probabilidades do computador
		probTemp[1] = prob.probabilidadesFlop(jogador[1], mesa);
		mesa[3] = erro[0];
		mesa[4] = erro[1];
		probabilidades[1] = probTemp[1][1];
			
		// Para o caso de o jogo ser computador vs computador
		if(modoJogo > 4)
		{
			probabilidades[0] = probTemp[0][1];
			probabilidadeLabel[0].setText(probabilidades[0]+"%");
			probabilidadeLabel[1].setText(probabilidades[1]+"%");
		}
		ronda = 1;
		
		// Fazer uma pausa quando for computador vs computador
		if(modoJogo > 4)
		{
			try {
				Thread.sleep(2000);					// 2 segundos
				System.out.println("Agora");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Efetuar apostas
		apostas();
	}
	
	// Ronda do turn
	public void turn()
	{
		// Atualizar o valor do pote
		fichas[pote]+=tempPote;
		
		//Mostrar a carta do turn
		mesaLabel[3].setVisible(true);
		
		// Mostrar o ranking do jogador
		resultado[0] = avaliar.avaliar(jogador[0],mesa, 6);
		ranking[0].setText(avaliar.mostrarClassificacao(resultado[0]));
		
		
		if(modoJogo > 4)
		{
			// Mostrar o ranking do jogador
			resultado[1] = avaliar.avaliar(jogador[1],mesa, 6);
			ranking[1].setText(avaliar.mostrarClassificacao(resultado[1]));
		}
		
		erro[0] = mesa[3];
		erro[1] = mesa[4];
		// Para o caso de o jogo ser computador vs computador
		if(modoJogo > 4)
		{
			probTemp[0] = prob.probabilidadesTurn(jogador[0], mesa);
			mesa[3] = erro[0];
			mesa[4] = erro[1];
		}	
		// Obter as probabilidades do computador
		probTemp[1] = prob.probabilidadesTurn(jogador[1], mesa);
		mesa[3] = erro[0];
		mesa[4] = erro[1];
		
		probabilidades[1] = probTemp[1][1];
		// Para o caso de o jogo ser computador vs computador
		if(modoJogo > 4)
		{
			probabilidades[0] = probTemp[0][1];
			probabilidadeLabel[0].setText(probabilidades[0]+"%");
			probabilidadeLabel[1].setText(probabilidades[1]+"%");
		}
		
		// Fazer uma pausa quando for computador vs computador
		if(modoJogo > 4)
		{
			try {
				Thread.sleep(2000);					// 2 segundos
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Ronda do turn
		ronda = 2;
		// Efetuar apostas
		apostas();
	}
	
	// Ultima ronda, river
	public void river()
	{
		
		// Atualizar o valor do pote
		fichas[pote]+=tempPote;
		
		// Mostrar a carta do river
		mesaLabel[4].setVisible(true);
		
		// Mostrar o ranking do jogador
		resultado[0] = avaliar.avaliar(jogador[0],mesa, 7);
		ranking[0].setText(avaliar.mostrarClassificacao(resultado[0]));
		
		
		if(modoJogo > 4)
		{
			// Mostrar o ranking do jogador
			resultado[1] = avaliar.avaliar(jogador[1],mesa, 7);
			ranking[1].setText(avaliar.mostrarClassificacao(resultado[1]));
		}
		
		erro[0] = mesa[3];
		erro[1] = mesa[4];
		// Para o caso de o jogo ser computador vs computador
		if(modoJogo > 4)
		{
			probTemp[0] = prob.probabilidadesRiver(jogador[0], mesa);
			mesa[3] = erro[0];
			mesa[4] = erro[1];
		}
		// Obter as probabilidades do computador
		probTemp[1] = prob.probabilidadesRiver(jogador[1], mesa);
		mesa[3] = erro[0];
		mesa[4] = erro[1];
		probabilidades[1] = probTemp[1][1];
		// Para o caso de o jogo ser computador vs computador
		if(modoJogo > 4)
		{
			probabilidades[0] = probTemp[0][1];
			probabilidadeLabel[0].setText(probabilidades[0]+"%");
			probabilidadeLabel[1].setText(probabilidades[1]+"%");
		}
		
		// Fazer uma pausa quando for computador vs computador
		if(modoJogo > 4)
		{
			try {
				Thread.sleep(2000);					// 2 segundos
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Ronda do river
		ronda = 3;
		// Efetuar apostas
		apostas();
	}
	
	// Método para realizar as apostas antes do flop
	public void apostasPreFlop()
	{
		// Mudar a ficha do dealer
		if(dealer == 0)
		{
			fichaDealerImg.setBounds(440, 140, 50, 50);
			dealer = 1;
		}
		else
		{
			fichaDealerImg.setBounds(440, 495, 50, 50);
			dealer = 0;
		}
		
		// Atualizar o número do jogo
		texto +="Jogo " + (i+1) + ": \n";
		i++;
		resumoTexto.setText(texto);
		
		// Inicializar valores 
		aposta[0] = 0;
		aposta[1] = 0;
		confirmacao[0] = 0;
		confirmacao[1] = 0;
		fichas[pote] = 0;
		tempPote = 0;
		finalPagamento = true;
		espera = true;
		
		// Aposta da smallblind
		apostaInicial = true;
		
		// Fichas iniciais de cada jogador
		fichasIniciais[0] = fichas[0];
		fichasIniciais[1] = fichas[1];
		
		int blind;
		
		// Se o jogador 1 for o dealer então o jogador 2 é a big blind e vice-versa
		blind = (dealer == 0) ? 1 : 0;
		
		// Se a blind não tiver dinheiro para pagar a bigBlind então faz all in automaticamente
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
				
				atualizarLabels();
				
				// O jogo terminou
				calcularVencedor();
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
				
				atualizarLabels();
				
				// A blind tem que fazer allin
				allIn(blind);
				
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
			
			atualizarLabels();
			
			// O jogo terminou
			calcularVencedor();
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
			
			texto += "O jogador " + (dealer+1) + " paga " + aposta[dealer] + "€\n";
			texto += "O jogador " + (blind+1) + " paga " + aposta[blind] + "€\n";
						
			atualizarLabels();
			
			//A small blind tem que fazer fold, call ou apostar
			menuFold_Call(dealer,false);
		}
	}
	
	// Método para realizar as apostas após o flop
	public void apostas()
	{
		// Inicializar as variáveis
		tempPote = fichas[pote];
		aposta[0] = 0;
		aposta[1] = 0;
		confirmacao[0] = 0;
		confirmacao[1] = 0;
		fichas[pote] = 0;
		fichas[3] = 0;
		
		atualizarLabels();
		
		// Não existe aposta inicial
		apostaInicial = false;
		// Se o primeiro jogador fizer check ou outro ainda têm que responder
		checkInicial = true;	
		
		// Usado apenas para apostar o valor de uma blind
		fichasIniciais[0] = fichas[0];
		fichasIniciais[1] = fichas[1];
		
		int jogador = (dealer == 0) ? 1 : 0;
		
		//O jogador pode fazer check ou apostar
		menuCheck(jogador, false);
	}
	
	/* Opcoes nas apostas:
	 * Fold - O jogador desiste
	 * Call - Iguala a aposta do adversário
	 * Aposta blind - Faz uma aposta no valor da blind
	 * Aposta valor - Faz uma aposta num valor definido pelo utilizador
	 * All-in - Aposta o dinheiro todo
	 */
	public void menuFold_Call(int jogador, Boolean jogar)
	{				
		// Menu ativado
		menu[0] = true;
		
		// Mostra o menu apenas quando utilizador jogar
		if((modoJogo == 1 && jogador == 0) || (modoJogo == 2 && jogador == 0)
				|| (modoJogo == 3 && jogador == 0) || (modoJogo == 4 && jogador == 0))
		{
			// Mostra os botões ao utilizador
			if(!jogar)
			{
				// Mostrar os botões
				botao[0].setVisible(true);
				botao[2].setVisible(true);
				botao[3].setVisible(true);
				botao[4].setVisible(true);
				botao[5].setVisible(true);
				valorAposta.setVisible(true);
			}
			// Se o jogador já escolheu a opção
			else
			{
				// Esconder os botões
				esconderBotoes();
				
				// Menu desativado
				menu[0] = false;
				
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
				}
			}
		}
		// A vez do computador jogar
		else
		{
			// Recebe a opção 
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
			
			}
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
			menuCheck(jogador,false);
		}	
	
		// Proximo método 
		if(ronda == 0 && confirmacao[0] == 1 && confirmacao[1] == 1)
			flop();
		if(ronda == 1 && confirmacao[0] == 1 && confirmacao[1] == 1)
			turn();
		if(ronda == 2 && confirmacao[0] == 1 && confirmacao[1] == 1)
			river();
		if(ronda == 3 && confirmacao[0] == 1 && confirmacao[1] == 1)
			calcularVencedor();		
	}
	
	/* Opcoes nas apostas:
	 * Check - As apostas dos dois jogadores estão iguais e passa à proxima fase
	 * Aposta blind - Faz uma aposta no valor da blind
	 * Aposta valor - Faz uma aposta num valor definido pelo utilizador
	 * All-in - Aposta o dinheiro todo
	 */
	public void menuCheck(int jogador, boolean jogar)
	{			
		// Menu ativado
		menu[1] = true;
		
		// Mostra o menu apenas quando utilizador jogar
		if((modoJogo == 1 && jogador == 0) || (modoJogo == 2 && jogador == 0)
				|| (modoJogo == 3 && jogador == 0) || (modoJogo == 4 && jogador == 0))
		{
			// Mostra os botões ao utilizador
			if(!jogar)
			{
				// Mostrar os botões
				botao[1].setVisible(true);
				botao[3].setVisible(true);
				botao[4].setVisible(true);
				botao[5].setVisible(true);
				valorAposta.setVisible(true);
			}
			// Se o jogador já escolheu a opcao
			else
			{
				// Esconder os botões
				esconderBotoes();
				
				// Menu desativado
				menu[1] = false;
				
				switch(opcao[0])
				{
					// Em check o jogador apenas confirma e a jogada continua 
					case 1:
						// O jogador confirma a sua jogada
						confirmacao[jogador] = 1;
						texto += "O jogador " + (jogador+1) + " fez check." + "\n";
						jogada.setText("O jogador " + (jogador+1) + " fez check.");
						resumoTexto.setText(texto);
					
						// Se foi o primeiro check então o oponente então ainda fala
						if(checkInicial == true && apostaInicial == false)
						{
							checkInicial = false;
							
							// Troca de jogador
							jogador = (jogador == 0) ? 1 : 0;
							//Volta a chamar este menu com o novo jogador
							menuCheck(jogador,false);
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
				}	
			}
		}
		// A vez do computador jogar
		else
		{
			// Recebe a opção
			opcao = modoJogo(jogador,modoJogo,4);
			
			switch(opcao[0])
			{
				// Em check o jogador apenas confirma e a jogada continua 
				case 1:
					// O jogador confirma a sua jogada
					confirmacao[jogador] = 1;
					texto += "O jogador " + (jogador+1) + " fez check." + "\n";
					resumoTexto.setText(texto);
					// Se foi o primeiro check então o oponente então ainda fala
					if(checkInicial == true && apostaInicial == false)
					{
						checkInicial = false;
						
						// Troca de jogador
						jogador = (jogador == 0) ? 1 : 0;
						//Volta a chamar este menu com o novo jogador
						menuCheck(jogador,false);
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
			}
		}
		
		// Proximo método
		if(ronda == 0 && confirmacao[0] == 1 && confirmacao[1] == 1)
			flop();
		if(ronda == 1 && confirmacao[0] == 1 && confirmacao[1] == 1)
			turn();
		if(ronda == 2 && confirmacao[0] == 1 && confirmacao[1] == 1)
			river();
		if(ronda == 3 && confirmacao[0] == 1 && confirmacao[1] == 1)
			calcularVencedor();
		
	}
	
	/* Opcoes nas apostas:
	 * Check - As apostas dos dois jogadores estão iguais e passa à proxima fase
	 * Aposta blind - Faz uma aposta no valor da blind
	 * Aposta valor - Faz uma aposta num valor definido pelo utilizador
	 * All-in - Aposta o dinheiro todo
	 */
	public void menuFold_Allin(int jogador, boolean jogar)
	{		
		// Menu ativado
		menu[2] = true;

		// Mostra o menu apenas quando utilizador jogar
		if((modoJogo == 1 && jogador == 0) || (modoJogo == 2 && jogador == 0)
				|| (modoJogo == 3 && jogador == 0) || (modoJogo == 4 && jogador == 0))
		{

			// Mostra os botões ao utilizador
			if(!jogar)
			{				
				// Mostrar os botões
				botao[0].setVisible(true);
				botao[2].setVisible(true);
			}
			// Se o jogador já escolheu a opcao
			else
			{
				// Esconder os botões
				esconderBotoes();
				
				// Menu desativado
				menu[2] = false;
				
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
				}
				
			}
		}
		// A vez do computador
		else
		{
			// Recebe a opção
			opcao = modoJogo(jogador,modoJogo,2);
			
			switch(opcao[0])
			{
				case 1: 
					fold(jogador);
					break;
				case 2: 
					call(jogador);
					break;
				default:
					System.out.println("Opcao Errada - Volte a escolher novamente");
			}	
		}
		
		// Proximo método
		if(ronda == 0 && confirmacao[0] == 1 && confirmacao[1] == 1)
			flop();
		if(ronda == 1 && confirmacao[0] == 1 && confirmacao[1] == 1)
			turn();
		if(ronda == 2 && confirmacao[0] == 1 && confirmacao[1] == 1)
			river();
		if(ronda == 3 && confirmacao[0] == 1 && confirmacao[1] == 1)
			calcularVencedor();		
	}
	
	/* Método quando um jogador faz fold
	 * Se um jogador fizer fold o outro ganha o pote e termina a jogada
	 */
	public void fold(int jogador)
	{		
		texto += "O jogador " + (jogador+1) + " fez fold" + "\n";
		
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		// Jogador fez fold
		// Oponente ganha o pote
		fichas[oponente] += fichas[pote] + tempPote;
		
		texto += "O jogador " + (oponente+1) + " ganha " + (fichas[pote]+tempPote) + "€\n";
		jogada.setText("O jogador " + (oponente+1) + " ganha " + (fichas[pote]+tempPote) + "€");

		// O ciclo de apostas termina
		confirmacao[0] = 1;
		confirmacao[1] = 1;
		
		// O pote faz reset 
		fichas[pote] = 0;
		
		// Verificar se o jogador venceu a jogada
		if(oponente == 0)
			jogadasGanhas++;
		else
			jogadasGanhasOponente++;
		
		atualizarLabels();
			
		// Acabou as apostas
		confirmacao[0] = 1;
		confirmacao[1] = 1;
		finalPagamento = false;
		espera = true;
		tempo = 2000;
		ronda=3;
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
			
			texto += "O jogador " + (jogador+1) + " fez All in com " + aposta[jogador] + "€\n";
			jogada.setText("O jogador " + (jogador+1) + " fez All in com " + aposta[jogador] + "€");
			
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
			
			texto += "O jogador " + (jogador+1) + " fez Call " + aposta[jogador] + "€\n";
			jogada.setText("O jogador " + (jogador+1) + " fez Call " + aposta[jogador] + "€");

			// Atualizar as fichas do jogador
			fichas[jogador] -= aposta[jogador];
			
			// Atualizar o valor da aposta do jogador
			aposta[jogador] += tempAposta;
			
			// Atualizar o valor do pote
			fichas[pote]= aposta[jogador] + aposta[oponente];	
					
		}
		
		atualizarLabels();
		
		checkInicial = false;
		
		// O ciclo de apostas termina
		confirmacao[jogador] = 1;
		
		// Se algum dos jogadores não tiver dinheiro acaba as apostas
		if(fichas[jogador] == 0 || fichas[oponente] == 0)
		{
			confirmacao[oponente] = 1;
			ronda=3;
			opcao[0]=0;
		}
	}
	
	/* Método para fazer uma aposta no valor da big blind */
	public void apostaBlind(int jogador)
	{	
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		//flag é ativa se entrar numa condição
		int flag = 0;
		
		// Se for a ronda incial a aposta é de smallBlind + bigBlind
		if(apostaInicial == true && (bigBlind+smallBlind) < fichas[jogador] )
		{
			
			texto += "O jogador " + (jogador+1) + " apostou " + (bigBlind+smallBlind) + "€\n";
			jogada.setText("O jogador " + (jogador+1) + " apostou " + (bigBlind+smallBlind) + "€");
			
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
			
			texto += "O jogador " + (jogador+1) + " apostou " + (int)(bigBlind+aposta[oponente]) + "€\n";
			jogada.setText("O jogador " + (jogador+1) + " apostou " + (int)(bigBlind+aposta[oponente]) + "€");
			
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
		
		atualizarLabels();
		
		// Jogador confirma mas oponente tem que responder
		confirmacao[jogador] = 1;
		confirmacao[oponente] = 0;
		
		// Já não conta como ronda inicial
		apostaInicial=false;
		
		// Trocar de jogador
		jogador = (jogador == 0) ? 1 : 0;
		
		// Chamar menu fold e call
		menuFold_Call(jogador,false);	
	}
	
	/* Método para fazer uma aposta no valor da big blind */
	public void apostaValor(int jogador)
	{
		// Calcular quem é o oponente
		int oponente = (jogador == 0) ? 1 : 0;
		
		opcao[1] = (int) (opcao[1] + aposta[oponente]);
		
		// Se as fichas do jogador forem menores que duas big blinds então aposta tudo
		if(fichas[jogador] < (2*bigBlind))
			opcao[1] = (int)fichas[jogador];	
		// Se a aposta for menor que duas big blinds então passa para duas big blinds
		else if(opcao[1] < (2*bigBlind))
			opcao[1] = (2*bigBlind);
		// Se a aposta for maior que as fichas do jogador então faz allin
		else if(opcao[1] > fichas[jogador])
			opcao[1] = (int) fichas[jogador];
		
		// Guarda o valor antigo da aposta
		float tempAposta = aposta[jogador];
		
		// Aposta definida pelo jogador
		aposta[jogador] = opcao[1]; 

		texto += "O jogador " + (jogador+1) + " apostou " + opcao[1] + "€\n";
		jogada.setText("O jogador " + (jogador+1) + " apostou " + opcao[1] + "€");
		
		// Jogador paga a aposta
		fichas[jogador] -= aposta[jogador];
		
		aposta[jogador] += tempAposta;
		
		// Atualizar o valor do pote
		fichas[pote] = aposta[jogador]+ aposta[oponente];
		
		atualizarLabels();
		
		// Jogador confirma mas oponente tem que responder
		confirmacao[jogador] = 1;
		confirmacao[oponente] = 0;
		
		// Já não conta como ronda inicial
		apostaInicial=false;
		
		valorAposta.setText(null);
		
		// Trocar de jogador
		jogador = (jogador == 0) ? 1 : 0;
		
		// Se o oponente não tiver fichas para cobrir a aposta tem que fazer fold ou all in
		if(fichas[jogador] <=  opcao[1])
			menuFold_Allin(jogador,false);
		else
			menuFold_Call(jogador,false);
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
		
		texto += "O jogador " + (jogador+1) + " fez all in com " + aposta[jogador] + "€\n";
		jogada.setText("O jogador " + (jogador+1) + " fez all in com " + aposta[jogador] + "€");
		
		// Jogador paga a aposta
		fichas[jogador] -= (aposta[jogador]-tempAposta);
		
		// Atualizar as fichas do pote
		fichas[pote] = (aposta[jogador]+ aposta[oponente]);
		
		atualizarLabels();
		
		// Jogador confirma mas oponente tem que responder
		confirmacao[jogador] = 1;
		confirmacao[oponente] = 0;
		
		// Já não conta como ronda inicial
		apostaInicial=false;
		
		// Trocar de jogador
		jogador = (jogador == 0) ? 1 : 0;
			
		// Se o oponente ainda tiver fichas
		if(fichas[jogador] > 0)
			menuFold_Allin(jogador,false);
		else
		{
			confirmacao[0] = 1;
			confirmacao[1] = 1;
			ronda = 3;
			
		}

	}

	/* Método para calcular o vencedor e mostrar o resultado */
	public void calcularVencedor()
	{
		// Verificar que o pagamento é efetuado apenas uma vez a cada jogada
		if(finalPagamento)
		{
			
			tempo = 4000; 		// 4 segundos a ver o resultado
			
			// Atualizar o valor do pote
			fichas[pote]+=tempPote;

			// resultado do jogador 1
			resultado[0] = avaliar.avaliar(jogador[0],mesa, 7);
			// resultado do jogador 2
			resultado[1] = avaliar.avaliar(jogador[1],mesa, 7);
			
			// Obter a mão final de cadajogador
			maoJogador[0] = avaliar.obterMaoFinal(jogador[0], mesa,7);
			maoJogador[1] = avaliar.obterMaoFinal(jogador[1], mesa,7);
			
			// Verificar se existe uma nova mão alta do utilizador
			try {
				guardar.obterMaoAlta(nome);
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			guardar.maoMaisAlta(maoJogador[0], resultado[0]);
			
			// Verificar se existe uma nova mão alta do oponente
			try {
				guardarOponente.obterMaoAlta(nomeOponente);
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			guardarOponente.maoMaisAlta(maoJogador[1], resultado[1]);
			
			// Verificar o vencedor
			// Se o jogadores empataram
			if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 0)
			{		
				// Dividir o pote pelos 2 jogadores
				fichas[0] += fichas[pote] / 2;
				fichas[1] += fichas[pote] / 2;
				texto += "Empate com " + avaliar.mostrarClassificacao(resultado[0]) + "\n";
				jogada.setText("Empate com " + avaliar.mostrarClassificacao(resultado[0]));
			}
			// Se o jogador ganhou
			else if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 1)
			{
				guardar.cartasGanhas(jogador[0]);
				jogadasGanhas++;
				// Jogador recebe o pote
				fichas[0] += fichas[pote];
				texto += "Jogador ganhou com " + avaliar.mostrarClassificacao(resultado[0]) + "\n";
				jogada.setText("Jogador ganhou com " + avaliar.mostrarClassificacao(resultado[0]));
			}
			// Se o computador ganhou
			else if(calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 2)
			{
				// Computador recebe o pote
				guardarOponente.cartasGanhas(jogador[1]);
				jogadasGanhasOponente++;
				fichas[1] += fichas[pote];
				texto += "Computador ganhou com " + avaliar.mostrarClassificacao(resultado[1]) + "\n";
				jogada.setText("Computador ganhou com " + avaliar.mostrarClassificacao(resultado[1]));
			}
			
			// Verificar que as cartas da mesa estão visiveis
			for(int i = 0; i < 5; i++)
				mesaLabel[i].setVisible(true);
			
			// Obter as imagens das cartas do oponente
			jogadorLabel[1][0].setIcon(cartaImg[jogador[1][0].getNumCarta()][jogador[1][0].getNaipe()]);
			jogadorLabel[1][1].setIcon(cartaImg[jogador[1][1].getNumCarta()][jogador[1][1].getNaipe()]);
			
			// Mostrar a classificação da mão de cada jogador
			apostaLabel[0].setText(avaliar.mostrarClassificacao(resultado[0]));
			apostaLabel[1].setText(avaliar.mostrarClassificacao(resultado[1]));
			
			esconderBotoes();
			
			// Guardar as estatisticas do jogador e do oponente
			try {
				guardar.obterTotais(nome);
				guardarOponente.obterTotais(nomeOponente);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			guardar.totalJogadas(i, jogadasGanhas);
			guardarOponente.totalJogadas(i,jogadasGanhasOponente);

			try {
				guardar.guardarCartasJogadas(nome);
				guardarOponente.guardarCartasJogadas(nomeOponente);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				guardar.guardarCartasGanhas(nome);
				guardarOponente.guardarCartasGanhas(nomeOponente);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				guardar.guardarMaoAlta(nome);
				guardarOponente.guardarMaoAlta(nomeOponente);
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				guardar.guardarTotais(nome);
				guardarOponente.guardarTotais(nomeOponente);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		finalPagamento = false;

		// Mostra os resultados durante um x de tempo
		mostrar();
	}
	
	// Este método têm como função fazer uma pausa para mostrar os resultados ao utilizador
	public void mostrar()
	{
		
		// Verificar que apenas é efetuado uma unica vez
		if(espera)
		{
			// Verificar se os jogadores tem fichas para continuar em jogo
			if(fichas[0] == 0 || fichas[1] == 0)
			{
				// Mostrar a mensagem da vitória
				vitoria.setVisible(true);
				fichasLabel[2].setVisible(false);
				
				if(fichas[0] == 0 && calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 2)
				{
					if(modoJogo == 1)
						vitoria.setText("Bot certo ganhou o jogo!");
					if(modoJogo == 2 || modoJogo == 5)
						vitoria.setText("Bot agressivo ganhou o jogo!");
					if(modoJogo == 3 || modoJogo == 6 || modoJogo == 8)
						vitoria.setText("Bot misto ganhou o jogo!");
					if(modoJogo == 4 || modoJogo == 7 || modoJogo == 9 || modoJogo == 10)
						vitoria.setText("Bot probabilidades ganhou o jogo!");
					
					fichasLabel[1].setText("3000€");

				}
				if(fichas[1] == 0 && calcular.calcularVencedor(maoJogador[0], maoJogador[1], resultado) == 1)
				{
					if(modoJogo >= 1 && modoJogo < 5)
						vitoria.setText(nome + " ganhou o jogo!");
					if(modoJogo >= 5 && modoJogo < 8)
						vitoria.setText("Bot certo ganhou o jogo!");
					if(modoJogo == 8  || modoJogo == 9)
						vitoria.setText("Bot agressivo ganhou o jogo!");
					if(modoJogo == 10)
						vitoria.setText("Bot misto ganhou o jogo!");
					
					fichasLabel[0].setText("3000€");
				}			
				
				// Mostrar o botões finais
				botaoFinal[0].setVisible(true);
				botaoFinal[1].setVisible(true);
			}	
			// Se o jogo continuar
			else
			{
				//Pre Flop é chamado após algum tempo
				ActionListener task = new ActionListener() {
				        public void actionPerformed(ActionEvent e) {
				    	try {
							preFlop();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}		 
				    }
				};
				
				Timer timer = new Timer(tempo , task);
				timer.setRepeats(false);
				timer.start();
			}
		}
		espera = false;

		
	}

	// Retorna a opcão consoante o modo de jogo
	public int[] modoJogo(int nJogador, int modoJogo, int menu)
	{
		erro[0] = mesa[3];
		erro[1] = mesa[4];
		
		switch(modoJogo)
		{
		// Jogador VS Computador (bot com apostas certas)
		case 1:
				opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, tempPote, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
		break;
		
		// Jogador VS Computador (bot agressivo)
		case 2:
				
				opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, tempPote, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
		break;
		
		// Jogador VS Computador (bot agressivo)
		case 3:
				
			Random botRandom = new Random();
			
			// Escolhe o bot
			int bot= botRandom.nextInt((1-0)+1) + 0;
			
			if(bot == 0)
				opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, tempPote, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
			else
				opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, tempPote, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
		break;
		
		// Jogador VS Computador (bot probabilidades)
		case 4:	
			opcao = computador.MFS(nJogador, fichas,tempPote,probabilidades,aposta,menu);
		break;
		
		// Computador VS Computador (certo vs agressivo)
		case 5:
			if(nJogador == 0)
				opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, 0, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
			else
				opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, 0, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
		break;
		// Computador VS Computador (certo vs misto)
		case 6:
			if(nJogador == 0)
				opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, 0, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
			else
			{
				Random botRandom1 = new Random();
				
				// Escolhe o bot
				int bot1= botRandom1.nextInt((1-0)+1) + 0;
				
				if(bot1 == 0)
					opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, 0, ronda, 
							menu,jogador[nJogador],mesa, probabilidades);
				else
					opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, 0, ronda, 
							menu,jogador[nJogador],mesa, probabilidades);
			}
		break;
		
		// Computador VS Computador (certo vs probabilidades)
		case 7:
			if(nJogador == 0)
				opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, 0, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
			else
				opcao = computador.MFS(nJogador, fichas,tempPote,probabilidades,aposta,menu);
		break;
		
		// Computador VS Computador (agressivo vs misto)
		case 8:
			if(nJogador == 0)
				opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, 0, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
			else
			{
				Random botRandom1 = new Random();
				
				// Escolhe o bot
				int bot1= botRandom1.nextInt((1-0)+1) + 0;
				
				if(bot1 == 0)
					opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, 0, ronda, 
							menu,jogador[nJogador],mesa, probabilidades);
				else
					opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, 0, ronda, 
							menu,jogador[nJogador],mesa, probabilidades);
			}
		break;
		
		// Computador VS Computador (agressivo vs probabilidades)
		case 9:
			if(nJogador == 0)
				opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, 0, ronda, 
						menu,jogador[nJogador],mesa, probabilidades);
			else
				opcao = computador.MFS(nJogador, fichas,tempPote,probabilidades,aposta,menu);
		break;
		
		// Computador VS Computador (misto vs probabilidades)
		case 10:
			if(nJogador == 0)
			{
				Random botRandom1 = new Random();
				
				// Escolhe o bot
				int bot1= botRandom1.nextInt((1-0)+1) + 0;
				
				if(bot1 == 0)
					opcao = computador.bot_certo(nJogador, dealer, fichas, aposta, 0, ronda, 
							menu,jogador[nJogador],mesa, probabilidades);
				else
					opcao = computador.bot_agressivo(nJogador, dealer, fichas, aposta, 0, ronda, 
							menu,jogador[nJogador],mesa, probabilidades);
			}
			else
				opcao = computador.MFS(nJogador, fichas,tempPote,probabilidades,aposta,menu);
		break;
		}
		mesa[3] = erro[0];
		mesa[4] = erro[1];
		return opcao;
		
	}

	// Esconde os botões
	public void esconderBotoes()
	{
		for(int i = 0; i< 6; i++)
			botao[i].setVisible(false);
		
		valorAposta.setVisible(false);
	}
	
	// Atualizar os labels
	public void atualizarLabels()
	{
		fichasLabel[pote].setText("Pote: " + (fichas[pote] + tempPote)+"€");
		fichasLabel[0].setText(fichas[0]+"");
		fichasLabel[1].setText(fichas[1]+"");
		apostaLabel[0].setText("Aposta: " + aposta[0]);
		apostaLabel[1].setText("Aposta: " + aposta[1]);
		resumoTexto.setText(texto);
	}

}
