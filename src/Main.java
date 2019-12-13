public class Main {
	public static boolean atWar = false; //atWar is false to begin with, war hasn't been called
	public static int roundCount = 0;
	public static void main(String[] args) {
		deal();
		playWar();
	}
	static FIFOQueue<Card>[] hand = new FIFOQueue[2]; //Creates an empty array with two slots
	static Deck gameDeck = new Deck(); //creates a new deck which can be dealt to 
	static FIFOQueue<Card>[] warHand = new FIFOQueue[1]; //creates warHand, where the cards bet in war are placed
	public static void deal(){
		hand[0] = new FIFOQueue();  //insert queue into each slot of hand
		hand[1] = new FIFOQueue();  //insert queue into each slot of hand
		warHand[0] = new FIFOQueue(); //inserts queue into warHand

		for (int i=0; i<26; i++) { 
			//i<26 is half the deck. since the deck is shuffled when .deal() is called, these cards are randomly given to p1 and p2
			hand[0].put(gameDeck.deal()); //place card in p1's hand
			hand[1].put(gameDeck.deal()); //place card in p2's hand
		}
	}
	public static void playWar() { //plays war until one hand is empty
		while(!hand[0].isEmpty() && !hand[1].isEmpty()) { 
			playRound();
			//if neither is empty, play a round. stops code when one is empty
		}
		if(hand[0].isEmpty()) {
			System.out.println("Player 2 Wins the game! Good game!"); 
			System.out.println(roundCount +" rounds were played");//p1 empty, p2 wins
		}
		if(hand[1].isEmpty()) {
			System.out.println("Player 1 Wins the game! Good game!");
			System.out.println(roundCount +" rounds were played");//p2 empty, p1 wins
		}
		if(hand[0].isEmpty() && hand[1].isEmpty()) {
			System.out.println("This is a very very rare outcome, it's a tie!"); //both are empty, tie
		}
	}

	public static void warCards() { //takes three cards off the top of each deck and puts them into a warHand

		warHand[0].put(hand[0].peek());//makes sure that next card is compared to during war by placing it into warHand
		warHand[0].put(hand[1].peek());
		hand[0].remove();
		hand[1].remove();
		// error fixed by code above: would do "1A vs 1H" then war, but one of the two cards would be reused
		// example: next comparison would be "2A vs 1H"
		//no more infinite war matchups 	
		if (hand[0].size() < 3) { //war case for if hand[0] has less than three cards
			for (int i=0; i < hand[0].size(); i++) { 
				warHand[0].put(hand[0].peek()); //put as many cards as possible into the warHand
				hand[0].remove();
			}
			for(int i=0; i<3; i++) {
				warHand[0].put(hand[1].peek()); //put three cards in from the other hand, as it has more than 3 cards
				hand[1].remove();
			}	
		}
		else if (hand[1].size() < 3) { //war case for if hand[1] has les than three cards
			for (int i=0; i < hand[1].size(); i++) {
				warHand[0].put(hand[1].peek()); //put as many cards as possible into warHand
				hand[1].remove();
			}
			for(int i=0; i<3; i++) {
				warHand[0].put(hand[0].peek()); //put three cards in from the other hand, as it has more than 3 cards
				hand[0].remove();
			}

		}
		else if (hand[0].size() == 3) { //if size is equal to 3
			for (int i=0; i < hand[0].size(); i++) { 
				warHand[0].put(hand[0].peek()); //put as many cards as possible into the warHand
				hand[0].remove();
			}
			for(int i=0; i<3; i++) {
				warHand[0].put(hand[1].peek()); //put three cards in from the other hand
				hand[1].remove();
			}
		}
		else if (hand[1].size() == 3) { //if size is equal to 3
			for (int i=0; i < hand[1].size(); i++) {
				warHand[0].put(hand[1].peek()); //put as many cards as possible into warHand
				hand[1].remove();
			}
			for(int i=0; i<3; i++) {
				warHand[0].put(hand[0].peek()); //put three cards in from the other hand
				hand[0].remove();
			}
		}
		else if (hand[1].size() > 3) { //if size is greater than 3 case 		

			for (int i=0; i<3; i++) { //put three cards from each hand into warHand and remove when done
				warHand[0].put(hand[0].peek());
				hand[0].remove();
				warHand[0].put(hand[1].peek());
				hand[1].remove();			
			}
			playRound(); //if war, a round is played to see who gets all 6 cards

		}	
		else {
			System.out.println(warHand[0].size());
			for (int i=0; i<3; i++) { //put three cards from each hand into warHand and remove when done
				warHand[0].put(hand[0].peek());
				hand[0].remove();
				warHand[0].put(hand[1].peek());
				hand[1].remove();
			}
			playRound();//if war, a round is played to see who gets all 6 cards	
		}	
	}
	public static void playRound() {
		System.out.println(hand[0].peek().toString() + " versus " + hand[1].peek().toString());
		if (hand[0].peek().compareTo(hand[1].peek()) > 0)  { //p1 is greater, p1 wins
			System.out.println("Player 1 wins the round!");
			roundCount++;
			if(atWar) { //atWar becomes true when the else statement is called (only in a tie)
				atWar=false;  //atWar becomes false, otherwise the war case would infinitely loop
				int whSize = warHand[0].size();
				//warHand changes within the for loop, making the size an int before running fixes error where cards would disappear
				//disappear meaning that less cards than the amount in warHand were given
				for (int i=0; i<whSize; i++) { //put the cards won from war into p1's hand and empties warHand
					hand[0].put(warHand[0].peek());
					warHand[0].remove();	
				}
			}
			hand[0].put(hand[0].peek()); //puts top card of p1 at bottom of p1
			hand[0].remove(); //removes top card from p1
			hand[0].put(hand[1].peek()); //puts top card of p2 at bottom of p1
			hand[1].remove(); //removes top card from p2

			System.out.println("Player 1 has " + hand[0].size() + " cards and Player 2 has " + hand[1].size() + " cards");
		}
		else if(hand[0].peek().compareTo(hand[1].peek()) < 0) { //p2 is greater, p2 wins
			System.out.println("Player 2 wins the round!");
			roundCount++;
			if(atWar) {
				atWar=false;
				int whSize = warHand[0].size(); 
				//warHand changes within the for loop, making the size an int before running fixes error where cards would disappear
				//disappear meaning that less cards than the amount in warHand were given
				for (int i=0; i<whSize; i++) { //put the cards won from war into p2's hand and empties warHand 
					hand[1].put(warHand[0].peek());
					warHand[0].remove();
				}
			}
			hand[1].put(hand[0].peek()); //puts top card of p1 at bottom of p2
			hand[0].remove(); //removes top card from p1
			hand[1].put(hand[1].peek()); //puts top card of p2 at bottom of p2
			hand[1].remove(); //removes top card from p2

			System.out.println("Player 1 has "  + hand[0].size() + " cards and Player 2 has " + hand[1].size() + " cards");
		}
		else {
			System.out.println("!!!!!!!War!!!!!!!");
			atWar=true;//if the compareTo method returns 0, the cards are the same and warCards() is called;
			roundCount++;
			warCards(); //method warCards() is called to 
		}	
	}	
}



