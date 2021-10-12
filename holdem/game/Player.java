package holdem.game;

import holdem.util.HoldemUtil;

public class Player {
	
	long[] cards = new long[7];
	long card_sum = 0;
	int cardcount = 0;
	int id ;
	public Player(int id) {
		this.id = id;
	}
	
	public void set_Hands(int[] hands) {
		cards[0] = 1L << hands[0];
		cards[1] = 1L << hands[1];
		card_sum = cards[0] + cards[1];
		cardcount  = 2;
	}
	public void set_flop(int[] flop) {
		cards[2] = 1L << flop[0];
		cards[3] = 1L << flop[1];
		cards[4] = 1L << flop[2];
		card_sum = card_sum + cards[2] + cards[3] + cards[4];
		cardcount  = 5;
	}
	public void set_turn(int[] turn) {
		cards[5] = 1L << turn[0];
		card_sum = card_sum + cards[5];
		cardcount  = 6;
	}
	public void set_river(int[] river) {
		cards[6] = 1L << river[0];
		card_sum = card_sum + cards[6];
		cardcount  = 7;
	}
	public int best_hand_ranking() {
		return HoldemUtil.best_hand_ranking(card_sum);
	}
	@Override
	public String toString() {
		String hands_card_str = HoldemUtil.long_to_String(cards[0]+cards[1]);
		String board_card_str = HoldemUtil.long_to_String(card_sum-cards[0]-cards[1]);
		int best_rank = best_hand_ranking();
		int category = HoldemUtil.rank_to_category(best_rank);
		String rank_name = HoldemUtil.rank_name[category];
		return String.format("%2d player has [ %s ] [ %s ] => rank : %4d ( %2s )",
							   id, hands_card_str, board_card_str, best_rank , rank_name);
	}
	public int get_outs() {
		int outs = 0;
		long temp = cards[0]+cards[1]+cards[2]+cards[3]+cards[4];
		int[] cardcount = new int[13];
		int[] cardcolor = new int[4];
		for(int i = 0; i < 13; i++) {
			for(int j = 0; j < 4; j++) {
				if(temp%2 == 1) {
					cardcolor[j]++;
					cardcount[i]++;
					temp = temp /2;
				}
			}
		}
		return outs;
	}
}
