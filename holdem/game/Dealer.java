package holdem.game;

import java.util.ArrayList;
import java.util.Collections;

public class Dealer {
	int players_no ;
	ArrayList<Integer> all_cards;
	
	public Dealer() {
		this.all_cards = new ArrayList<Integer>();
		for( int i = 0 ; i < 52; i ++)
			all_cards.add(i);
	}
	public void newGame(int p_no) {
		players_no = p_no;
		Collections.shuffle(all_cards);
	}
	public int[][] preflop(){
		int[][] result = new int[this.players_no][2];
		for(int i = 0 ; i < 2; i++) {
			for(int p = 0; p < players_no; p++) {
				result[p][i] = all_cards.get( i * players_no + p);
			}
		}
		return result;
	}
	public int[] flip() {
		int[] result = new int[3];
		for(int i = 0 ; i < 3; i++) {
			result[i] = all_cards.get(2 * players_no + 1 + i);
		}
		return result;
	}
	public int[] turn() {
		int[] result = new int[1];
		result[0] = all_cards.get(2 * players_no + 5 );
		return result;
	}
	public int[] river() {
		int[] result = new int[1];
		result[0] = all_cards.get(2 * players_no + 7 );
		return result;
	}
}
