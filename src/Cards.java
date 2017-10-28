import java.util.Comparator;

/*
 * Description: Define a data structure to save and get the 52 cards of the deck
 * Name: Filipe Andre de Matos Bicho
 * Last update: 27/10/2017
 */
public class Cards{

	private int rank, suit;
	
	/* Array because the size of the array is already known
	 * It is final because this array can�t be changed
	 */
	final String[] rankArray = {"A", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "J", "Q", "K"};
	
	// Hearts, Diamonds, Clubs, Spades
	final String[] suitArray = {"\u2665", "\u2666", "\u2663", "\u2660"};
	
	// Initialize card
	Cards(int rank, int suit)
	{
		this.rank = rank;
		this.suit = suit;
	}
	
	// Get a card in string format
	@Override
	public String toString()
	{
		return rankArray[rank] + suitArray[suit];
	}
	
	// Get card rank
	public int getRank() { return rank; }
	
	//Get card suit 
	public int getSuit() { return suit; }

	/*Comparator to sort card by rank*/
    public static Comparator<Cards> sortRank = new Comparator<Cards>() {

    	public int compare(Cards card1, Cards card2) {

	   int rank1 = card1.getRank();
	   int rank2 = card2.getRank();

	   /*For ascending order*/
	   return rank1-rank2;

	   /*For descending order*/
	   //rank2-rank1;
    	}
    };
   
   /*Comparator to sort card by suit*/
   public static Comparator<Cards> sortSuit = new Comparator<Cards>() {

	   public int compare(Cards card1, Cards card2) {

	   int suit1 = card1.getSuit();
	   int suit2 = card2.getSuit();

	   /*For ascending order*/
	   return suit1-suit2;

	   /*For descending order*/
	   //suit2-suit1;
	   }
   };
}
