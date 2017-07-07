/* Descricao: Class main que cria a pagina inicial e pagina dos menus
 * Projeto: Simulador Heads-Up Texas Hold'em 
 * Autor: Filipe Andre de Matos Bicho, aluno nr 1300531
 * Ultima modificacao: 12/05/2017
 */

import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main extends JFrame implements ActionListener {
	
	// Declarar variaveis 
	// Criar a janela onde se irá desenhar
	JFrame janela = new JFrame("Filipe Bicho - 1300531");
	// Criar o panel para a página incial
	JPanel paginaInicial = new JPanel();
	// Criar o panel para a pagina dos menus
	JPanel paginaMenus = new JPanel();
	// Label que recebe o background
	JLabel background = new JLabel();
	// Label que recebe o titulo
	JLabel titulo = new JLabel();
	// Recebe o input do user
	JTextField utilizador = new JTextField();
	// Escreve no panel
	JLabel texto = new JLabel();
	// Usado para editar posicionamento e dimensão
	GridBagConstraints c = new GridBagConstraints();
	
	// Construtor da classe onde a janela é inicializada
	Main() 
	{		
		// Chamar o panel da página inicial
		try {
			paginaInicial();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  	
 		// Sair ao clicar na cruz
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Reconstruir a janela
 		janela.pack();
 		// Definir a janela para visivel
 		janela.setVisible(true); 
	}
	
	// Construtor com o argumento nome usado por outras classes para ir para a página dos menus
	Main(String nome) 
	{		
		// Chamar o panel da página inicial
		try {
			paginaInicial();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		utilizador.setText(nome);
		
		paginaInicial.setVisible(false);
		paginaMenus();
		  	
 		// Sair ao clicar na cruz
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Reconstruir a janela
 		janela.pack();
 		// Definir a janela para visivel
 		janela.setVisible(true); 
	}
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException{
	
		Jogar jogo = new Jogar();
		// Recebe o input do utilizador
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		int opcao=0;
		
		System.out.println();
		System.out.println("\t************** Simulador Head's Up Texas Hold'em ****************\n");
		System.out.println("\t               1- Jogar em modo de texto.\n");
		System.out.println("\t               2- Jogar em modo gráfico.\n");
		System.out.print("\t               Introduza a sua opção: ");
		opcao = input.nextInt();
		
		switch(opcao)
		{
			case 1:
				// Recebe o input do utilizador
				Scanner user = new Scanner(System.in);
				System.out.println("\n\nBemVindo. Introduza o nome: ");
				String nome = user.nextLine();
				// Recebe o input do utilizador
				Scanner input2 = new Scanner(System.in);
				int opcao2=0;
				System.out.println(" ************** SIMULADOR HEADS UP TEXAS HOLD'EM ************");
				System.out.println("\nEscolha o modo de jogo: ");
				System.out.println("1. Jogador vs Jogador");
				System.out.println("2. Jogador vs Computador (bot certo)");
				System.out.println("3. Jogador vs Computador (bot agressivo)");
				System.out.println("4. Jogador vs Computador (bot misto)");
				System.out.println("5. Jogador vs Computador (bot formula)");
				System.out.println("6. Computador (bot certo) vs Computador(bot agressivo)");
				System.out.println("7. Computador (bot certo) vs Computador(bot misto)");
				System.out.println("8. Computador (bot certo) vs Computador(bot formula)");
				System.out.println("9. Computador (bot agressivo) vs Computador(bot misto)");
				System.out.println("10. Computador (bot agressivo) vs Computador(bot formula)");
				System.out.println("11. Computador (bot misto) vs Computador(bot formula)");
				opcao2 = input2.nextInt();
				
				switch(opcao2)
				{
					case 1:
						jogo.jogadorVSjogador(1,nome);
						break;
					case 2:
						jogo.jogadorVScomputador(2,nome);
						break;
					case 3:
						jogo.jogadorVScomputador(3,nome);
						break;
					case 4:
						jogo.jogadorVScomputador(4,nome);
						break;
					case 5:
						jogo.jogadorVScomputador(5,nome);
						break;
					case 6:
						jogo.computadorVScomputador(6);
						break;
					case 7:
						jogo.computadorVScomputador(7);
						break;
					case 8:
						jogo.computadorVScomputador(8);
						break;
					case 9:
						jogo.computadorVScomputador(9);
						break;
					case 10:
						jogo.computadorVScomputador(10);
						break;
					case 11:
						jogo.computadorVScomputador(11);
						break;
				}
				break;
			case 2:
				new Main();
				break;
			default:
				System.out.println("Opcao errada. Volte a escolher:");
				opcao = input.nextInt();
		} 
	}
	
	// Esta é a primeira janela apresentada ao utilizador
	public void paginaInicial() throws IOException
	{
		// Centrar na janela
		paginaInicial.setLayout(new GridBagLayout());
		
		// Obter a imagem
		background = new JLabel(new ImageIcon("imagens/paginaInicial.jpg"));
		
		// Criar um botão
		JButton botao = new JButton("Entrar", new ImageIcon("imagens/start.png"));
				
		// Centrar na pagina inicial
		background.setLayout(new GridBagLayout());
		// Definir a dimensão da imagem de fundo
		background.setPreferredSize(new Dimension(1200, 700)); 
	   
	   // Definir o titulo
	    titulo.setText("Simulador Head's up Texas Hold'em");
	    // Definir a font do titulo
		titulo.setFont(new Font("Courier New", Font.BOLD, 40));
		// Definir a cor da font do titulo
		titulo.setForeground(Color.white);
		
		 // Label com a messagem para o utilizador introduzir o seu nome
		texto = new JLabel("Introduza o seu nome");
		// Alterar a fonte
		texto.setFont(new Font("Courier New", Font.BOLD, 20));
		// Font de cor branca
		texto.setForeground(Color.white);
		
		// Alterar a cor de fundo par branco
		utilizador.setBackground(Color.white);
		utilizador.setFont(new Font("Courier New", Font.BOLD, 16));
		utilizador.setPreferredSize(new Dimension(200, 25));
			
		// Alterar a cor de fundo para branco
		botao.setBackground(Color.WHITE);
		// Adicionar a acção do botão
		botao.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	// Verificar se o nome do utilizador não está vazio
				if(!utilizador.getText().equals(""))
				{
					paginaInicial.setVisible(false);
					paginaMenus();
				}
				else
					utilizador.setBackground(new Color(255,150,150));
		    }
		});
		botao.setFocusPainted(false);
		botao.setFont(new Font("Courier New", Font.BOLD, 16));

		// Preencher horizontalmente
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// Definir posicionamento do titulo
		c.insets = new Insets(-400,0,0,0);  
		c.gridwidth = 10;   				 // 10 Colunas usadas
		background.add(titulo,c);	
		
		// Definir posicionamento do texto
		c.gridy = 0;					
		c.gridx = 0;					
		c.insets = new Insets(-200,170,0,0);  // Espaçamento 
		c.gridwidth = 5;   					  // 5 Colunas usadas
		background.add(texto,c);
		
		// Definir posicionamento da caixa de texto
		c.insets = new Insets(-200,450,0,0);  // Espaçamento 
		c.gridwidth = 5;   					  // 5 Colunas usadas
		background.add(utilizador,c);
		
		// Definir posicionamento do botão
		c.gridy = 1;
		c.insets = new Insets(-50,400,0,0);  // Espaçamento
		c.gridwidth = 2;   					  // 2 colunas usadas
		background.add(botao,c);
		
		// Adicionar o background à página inicial
		paginaInicial.add(background);
		
		// Adicionar página Inicial à janela
		 janela.getContentPane().add(paginaInicial);
	}
	
	// Págna que apresenta os menus ao utilizador
	public void paginaMenus()
	{
		 // Obter a imagem
 		background = new JLabel(new ImageIcon("imagens/menus.jpg"));
 		// Centrar na pagina inicial
 		background.setLayout(new GridBagLayout());
 		// Definir a dimensão da imagem de fundo
 		background.setPreferredSize(new Dimension(1200, 700)); 
 		
		// Obter nome do utilizador
		String nome = utilizador.getText();
		// Definir o titulo
	    titulo.setText("Bem vindo " + nome);
	    
	    // Label com a messagem para o utilizador introduzir o seu nome
		texto.setText("Escolha o modo de jogo: ");
		// Alterar a fonte
		texto.setFont(new Font("Courier New", Font.BOLD, 24));
		
	    // Declarar as 11 opções de jogo
		JButton[] menu = new JButton[11];
		
		menu[0] = new JButton(nome + " vs bot certo");
		menu[1] = new JButton(nome + " vs bot agressivo");
		menu[2] = new JButton(nome + " vs bot misto");
		menu[3] = new JButton(nome + " vs bot probabilidades");
		menu[4] = new JButton("bot certo vs bot agressivo");
		menu[5] = new JButton("bot certo vs bot misto");
		menu[6] = new JButton("bot certo vs bot formula");
		menu[7] = new JButton("bot agressivo vs bot misto");
		menu[8] = new JButton("bot agressivo vs bot probabilidades");
		menu[9] = new JButton("bot misto vs bot probabilidades");
		menu[10] = new JButton("Estatisticas");
 
 		// Definir posicionamento do titulo
		c.insets = new Insets(-600,0,0,0);  
		c.gridwidth = 10;   				 // 10 Colunas usadas
		background.add(titulo,c);
		
		// Definir posicionamento do texto
		c.insets = new Insets(-450,-300,0,0);  
		c.gridwidth = 10;   				 // 10 Colunas usadas
		background.add(texto,c);
		
		int top = -350;
		// Definir posicionamento dos botoes
		for(int i=0; i < 11 ; i++)
		{
			// Alterar a cor de fundo para branco
			menu[i].setBackground(Color.WHITE);
			menu[i].setFocusPainted(false);
			menu[i].setFont(new Font("Courier New", Font.BOLD, 16));
			c.insets = new Insets(top,-300,0,0);  
			c.gridwidth = 2;   				 // 2 Colunas usadas
			background.add(menu[i],c);	
			top += 90;
		}
		
		// Jogador vs bot certo
		menu[0].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		
		// Jogador vs bot agressivo
		menu[1].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		// Jogador vs bot misto
		menu[2].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		// Jogador vs bot probabilidades
		menu[3].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		// bot certo vs bot agressivo
		menu[4].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		
		// bot certo vs bot misto
		menu[5].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 6);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		
		// bot certo vs probabilidades
		menu[6].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 7);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		
		// bot agressivo vs bot misto
		menu[7].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 8);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		
		// bot agressivo vs bot probabilidades
		menu[8].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 9);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		
		// bot misto vs bot probabilidades
		menu[9].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					InterfaceGrafica teste = new InterfaceGrafica(janela);
					teste.inicio(nome, 10);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
		
		// bot misto vs bot probabilidades
		menu[10].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	paginaMenus.setVisible(false);
				try {
					@SuppressWarnings("unused")
					Estatisticas ver = new Estatisticas(janela,nome);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		});
	 	// Adicionar o background à página inicial
		paginaMenus.add(background);
		
		// Adicionar página Inicial à janela
		janela.getContentPane().add(paginaMenus);
	    
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
	

}
