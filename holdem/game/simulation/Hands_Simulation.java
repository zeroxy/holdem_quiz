package holdem.game.simulation;

import java.io.IOException;
import java.util.Arrays;

import holdem.game.Dealer;
import holdem.game.Player;
import holdem.util.HoldemUtil;

public class Hands_Simulation {
	static final int players_cnt = 4;  // 플레이어 수를 변경할때마다 핸드차트가 바뀐다.
	static final int all_card = 52;
	static final int multiply = 10;
	static final Dealer dealer = new Dealer();
	static final HoldemUtil util = new HoldemUtil();
	static Player[] players = new Player[players_cnt ];
	static int[]    p_rank  = new int[players_cnt ];
	static float[][][] hand_range = new float[2][13][13];
	public static void main(String[] args) throws IOException {
		util.init();
		System.out.println("");
		for(int i = 0 ; i < players_cnt; i++) {
			players[i] = new Player(i);
		}
		
		for(int game = 1 ; game <= 300000; game++) {
			dealer.newGame(players_cnt);
			int best_score = 10000;
			int best_player = 0;
			int[][] user_handcard = dealer.preflop();
			int[]   flop          = dealer.flip();
			int[]   turn          = dealer.turn();
			int[]   river         = dealer.river();
			for(int i = 0; i< players_cnt; i++) {
				players[i].set_Hands(user_handcard[i]);
				players[i].set_flop(flop);
				players[i].set_turn(turn);
				players[i].set_river(river);
				p_rank[i] = players[i].best_hand_ranking();
				if (best_score > p_rank[i]) {
					best_score = p_rank[i];
					best_player = i;
				}
				//System.out.println(players[i]);
				//System.out.println(user_handcard[i][0]+" "+user_handcard[i][1]);
			}
			for( int i = 0 ; i < players_cnt; i++) {
				int[][] number = new int[2][2];
				for(int k = 0; k < 2; k++) {
					//System.out.println(i+" " +k + " "+j+ " "+user_handcard[i][k]);
					number[k][0] = (user_handcard[i][k]/4)-1;
					if (number[k][0] <0) {
						number[k][0] = 12;
					}
					number[k][1] = user_handcard[i][k]%4;
				}
				if (number[0][0] > number[1][0]) {
					int temp = number[0][0];
					number[0][0] = number[1][0];
					number[1][0] = temp;
					temp = number[0][1];
					number[0][1] = number[1][1];
					number[1][1] = temp;
				}
				if( number[0][1] != number[1][1] ) {
					int temp = number[0][0];
					number[0][0] = number[1][0];
					number[1][0] = temp;
				}
				hand_range[0][number[1][0]][number[0][0]] += 1;
				if( i == best_player) {
					hand_range[1][number[1][0]][number[0][0]] += 1;
				}
			}
			System.out.print("\r "+game);
			if(game %100000 == 0 ) {
				System.out.println();
				for(int i = 0 ; i < 2; i++) {
					for(int j = 12 ; j >= 0; j--) {
						for(int k = 12 ; k >= 0; k--) {
							System.out.printf(" %7.1f",hand_range[i][j%13][k%13]);
							//System.out.printf(" %6.1f",hand_range[i][j-1][k-1]);
						}
						System.out.println();
					}
					System.out.println();
				}
				System.out.println("#########################");
			}
		}
		float total_hands=0, total_wins=0;
		for(int i = 12 ; i >= 0; i--) {
			for(int j = 12 ; j >= 0; j--) {
				total_hands += hand_range[0][i][j];
				total_wins +=  hand_range[1][i][j];
				System.out.printf(" %7.4f",(hand_range[1][i%13][j%13]/hand_range[0][i%13][j%13]));
			}
			System.out.println();
		}
		float winrate = total_wins/total_hands;
		System.out.println(total_hands+ " : "+ total_wins+ " : " + winrate );
		
		float[] threshold = { 0.11f, 0.12f, 0.13f, 0.14f, 0.15f, 0.18f , 0.2f , 0.3f , 0.4f , 0.45f , 0.5f , 0.55f , 0.6f , 0.65f };
		char[] coord = "23456789TJQKA".toCharArray();
		int[][] hand_chart_result = new int[13][13];
		for(float th : threshold) {
			float th_hands=0, th_wins=0, th_winrate = 0;
			System.out.println("#########################");
			System.out.println("   A  K  Q  J  T  9  8  7  6  5  4  3  2");
			for(int i = 12 ; i >= 0; i--) {
				System.out.print(coord[i%13]);
				for(int j = 12 ; j >= 0; j--) {
					if( (hand_range[1][i%13][j%13]/hand_range[0][i%13][j%13])>=th ) {
						th_hands += hand_range[0][i%13][j%13];
						th_wins += hand_range[1][i%13][j%13];
						hand_chart_result[i%13][j%13] += 1;
						System.out.printf(" %2d", hand_chart_result[i%13][j%13]);
					}else {
						System.out.printf(" %2d", hand_chart_result[i%13][j%13]);
					}
				}
				System.out.println();
			}
			th_winrate = th_wins/th_hands;
			System.out.println(th+"  => "+ th_hands+ " : "+ th_wins+ " : " + th_winrate + "  => "+ (th_hands/total_hands));
		}
		
		

	}

}

