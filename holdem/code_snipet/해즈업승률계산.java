package holdem.code_snipet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class ÇØÁî¾÷½Â·ü°è»ê {

	public static void main(String[] args) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		int[] hero = {1,2};
		int[] villain = {32, 14};
		int[] all_cards = new int[52-4];
		int[] board = new int[5];
		int cnt = 0;
		for(int i = 0; i < 52; i++) {
			if (i != hero[0] && i != hero[1] && i != villain[0] && i != villain[1]) {
				all_cards[cnt++] = i;
			}
		}
		long count= 0;
		for(int a = 0 ; a < all_cards.length; a++) {
			board[0] = all_cards[a];
			for(int b = a+1; b< all_cards.length; b++) {
				board[1] = all_cards[b];
				for(int c = b+1; c< all_cards.length; c++) {
					board[2] = all_cards[c];
					for(int d = c+1; d< all_cards.length; d++) {
						board[3] = all_cards[d];
						for(int e = d+1; e< all_cards.length; e++) {
							board[4] = all_cards[e];
							count++;
							bw.write(Arrays.toString(board));
							bw.newLine();
						}
					}
				}
			}
		}
		bw.write(""+count);
		bw.flush();

	}

}

/*



169 
50 49 / 2 = 1225

207025

2 47 46 45 44 / 5 = 
(46*46 -1) * (45*45 -1 ) / 2
2115 * 1012

2140380




*/
