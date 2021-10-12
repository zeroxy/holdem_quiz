package holdem.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class HoldemUtil {
	static int[][] dp = get_dp_table();
	static Node[] cards_to_rank = new Node[ dp[52][5] ];
	static final char[] mark = "sdhc".toCharArray();
	static final char[] numb = "A23456789TJQK".toCharArray();
	static final long div = 1_000_000_000L;
	static final long card_A = 1L<<(0*4);
	static final long card_2 = 1L<<(1*4);
	static final long card_3 = 1L<<(2*4);
	static final long card_4 = 1L<<(3*4);
	static final long card_5 = 1L<<(4*4);
	static final long card_6 = 1L<<(5*4);
	static final long card_7 = 1L<<(6*4);
	static final long card_8 = 1L<<(7*4);
	static final long card_9 = 1L<<(8*4);
	static final long card_T = 1L<<(9*4);
	static final long card_J = 1L<<(10*4);
	static final long card_Q = 1L<<(11*4);
	static final long card_K = 1L<<(12*4);
	static final long[] card_rank = {card_A, card_K, card_Q, card_J, card_T, 
									 card_9, card_8, card_7, card_6, card_5,
									 card_4, card_3, card_2, card_A, 0};
	static final long[] fourcard  = { 0b1111};
	static final long[] threecard = { 0b0111, 0b1011, 0b1101, 0b1110 };
	static final long[] twocard   = { 0b0011, 0b0101, 0b1001, 0b0110, 0b1010, 0b1100};
	static final long[] onecard   = { 0b0001, 0b0010, 0b0100, 0b1000 };
	static final long mark_s = 0;
	static final long mark_d = 1;
	static final long mark_h = 2;
	static final long mark_c = 3;
	static final int[] cumcount = {0, 10, 166, 322, 1599, 1609, 2467, 3325, 6185, (7462+10) };
								//    SF   FK   FH     F    ST    TK    2P    1P    HC
	public static final String[] rank_name = {"SF" , "FK" , "FH" , "F" , "ST" , "TK" , "2P" , "1P" , "HC"};

	static class Node implements Comparable<Node>{
		long cards;
		long rank;
		public Node(long cards, long rank) {
			this.cards = cards;
			this.rank = rank;
		}
		@Override
		public int compareTo(Node o) {
			return Long.compare(this.cards, o.cards);
		}
		
	}
	
	public static void init() throws IOException {
		File file = new File("handranking_db.txt");
		if(file.exists()) {
			System.out.println("족보 파일이 있음 "+ file.getAbsolutePath());
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringTokenizer st;
			for(int i = 0 ; i < dp[52][5] ; i++) {
				st = new StringTokenizer(br.readLine());
				cards_to_rank[i] = new Node(Long.parseLong(st.nextToken()), Long.parseLong(st.nextToken()));
				if((i % 10000) == 0) {
					System.out.print("\r"+i);
				}
			}
			
			br.close();
		} else { // 족보 데이터베이스 파일 없으면 만들어야지.
			System.out.println("족보 파일이 없다!!! "+ file.getAbsolutePath());
			for(int i = 1; i <= dp[52][5]; i++) {
				int temp = i;
				long result = 0;
				long anchor = 1;
				int b = 5;
				for(int a = 52; a > 0 ; a--) {
					if( dp[a-1][b-1] < temp) {
						temp -= dp[a-1][b-1];
					}else {
						result += (anchor << (52-a)) ;
						b--;
					}
					if(b== 0)
						break;
				}
				if((i % 10000) == 0) {
					System.out.print("\r "+dp[52][5]+" => "+i);
				}
				int resultrank = HoldemUtil.check_cards(result);
				cards_to_rank[i-1] = new Node(result, resultrank);
			}
			Arrays.sort(cards_to_rank);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0 ; i < cards_to_rank.length; i++) {
				bw.write(""+cards_to_rank[i].cards+" " +cards_to_rank[i].rank);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		}
		System.out.println("\nend");
	}
	
	public static void main(String[] args) throws IOException {
		HoldemUtil.init();
		//System.out.println("\n"+HoldemUtil.cards_to_rank[0].cards+" ### ");
		/*
		System.out.println("012345678901234567890123456789");
		System.out.println("4c Td  As 6s 8s Jh Kd");
		System.out.println("4c Td  As 6s 8s Jh Kd".length());
		long cards = string_to_long("4c Td  As 6s 8s Jh Kd");
		
		System.out.println(long_to_String(cards)+" "+cards);
		System.out.println(best_hand_ranking(cards));
		*/
	}
	
	public static int get_outs(long cards) {
		long outs=0;
		long[] flush_mask = {0x8888888888888L, 0x4444444444444L, 0x2222222222222L, 0x1111111111111L};
		for(long mask : flush_mask) {
			if(bitcount(mask&cards) == 4) {
				outs |= mask&(~cards);
			}
		}
		
		int straight_outs;
		int flush_outs;
		int full_house_outs;
		int four_card_outs;
		int triple_outs;
		int two_pair_outs;
		
		return bitcount(outs);
	}
	
	public static long cardset_to_rank(long cards) {
		int i = Arrays.binarySearch(cards_to_rank, new Node(cards, 0));
		return cards_to_rank[i].rank;
	}
	public static int rank_to_category(int rank) {
		for(int i = 0 ; i < cumcount.length ; i++) {
			if(rank <= cumcount[i+1]) {
				return i;
			}
		}
		return 9;
	}
	
	public static String long_to_String(long cardset) {
		StringBuilder sb = new StringBuilder();
		long temp = cardset;
		for(int n=0; n < 13; n++) {
			for(int m = 0; m < 4; m++) {
				if( (temp&1L) ==1) {
					sb.append(numb[n]).append(mark[m]).append(' ');
				}
				temp>>=1;
			}
		}
		return sb.toString();
	}
	
	public static long string_to_long(String cardset) {
		long result = 0;
		char[] str = cardset.toCharArray();
		for(int i = 0; i < str.length; i++) {
			for(int n = 0 ; n < numb.length; n++) {
				if(numb[n]==str[i]) {
					for(int m = 0 ; m < mark.length; m++) {
						if(mark[m]==str[i+1]) {
							result += (1L<<(n*4+m));
							i++;
							break;
						}
					}
				}
			}
		}
		return result;
	}
	public static int best_hand_ranking(long cardset) {
		int result = 10000;
		int cardcount = 0;
		long cards_5 = 0;
		long temp = cardset;
		long[] cards = new long[7];
		
		for(int i = 0 ; i < 52; i++) {
			if ((temp & (1L<<i)) > 0) {
				cards[cardcount++] = 1L<<i;
			}
		}
		
		if (cardcount <5) {
			return 0;
		}else if(cardcount == 5) {
			cards_5 = temp;
			result = Math.min(result, (int)HoldemUtil.cardset_to_rank(cards_5) );
		}else if (cardcount == 6) {
			for(int i = 0 ; i < cardcount ; i++) {
				cards_5 = temp - cards[i];
				result = Math.min(result, (int)HoldemUtil.cardset_to_rank(cards_5) );
			}
		} else {
			for(int i = 0 ; i < cardcount ; i++) {
				for (int j = i+1; j < cardcount; j++) {
					cards_5 = temp - cards[i] - cards[j];
					//System.out.println(long_to_String(cards_5)+" !@# "+(int)HoldemUtil.cardset_to_rank(cards_5));
					//System.out.println(check_cards(cards_5));
					result = Math.min(result, (int)HoldemUtil.cardset_to_rank(cards_5) );
				}
			}
		}
		
		return result;
	}
	
	public static int bitcount(long data) {
		long[] mask = { 0x5555555555555555L , 0x3333333333333333L 
					, 0x0f0f0f0f0f0f0f0fL , 0x00ff00ff00ff00ffL 
					, 0x0000ffff0000ffffL , 0x00000000ffffffffL};
		int shift = 1;
		for(long m : mask) {
			data = (data & m) + ( (data >> shift) & m );
			shift = shift*2;
			System.out.printf("%64s\n",Long.toBinaryString(data));	
		}
		return (int)data;
	}
	
	public static int check_cards(long cards) {
		int i= 0;
		int rank = 0;
		int result = 0;
		
		result = straigth_flush(cards);
		if(result >0) {
			return cumcount[0]+result;
		}
		
		result = four_of_a_kind(cards);
		if(result >0) {
			return cumcount[1]+result;
		}
		
		result = full_house(cards);
		if(result >0) {
			return cumcount[2]+result;
		}
		
		result = flush(cards);
		if(result >0) {
			return cumcount[3]+result;
		}
		
		result = straigth(cards);
		if(result >0) {
			return cumcount[4]+result;
		}
		
		result = three_of_a_kind(cards);
		if(result >0) {
			return cumcount[5]+result;
		}
		
		result = two_pair(cards);
		if(result >0) {
			return cumcount[6]+result;
		}
		
		result = one_pair(cards);
		if(result >0) {
			return cumcount[7]+result;
		}
		
		result = high_card(cards);
		if(result >0) {
			return cumcount[8]+result;
		}
		return 0;
	}
	
	static int[][] get_dp_table() {
		int[][] result = new int[53][6];
		result[0][0] = 1;
		for(int i = 1; i <53; i ++) {
			result[i][0] = 1;
			for(int j = 1 ; j <6; j++) {
				result[i][j] = result[i-1][j] + result[i-1][j-1];
				if(j>i)
					break;
			}
		}
		
		return result;
	}
	
	static int straigth_flush(long cards) { // 5*10 로열 플러쉬 포함
		int rank = 1;
		long mask = card_A+card_K+card_Q+card_J+card_T;
		for(int i = 0; i < 10; i++) {
			for(int m = 0; m < 4; m++) {
				if(cards == (mask<<m)) {
					return rank+i;
				}
			}
			mask -= card_rank[i];
			mask += card_rank[i+5];
		}
		return -1;
	}
	static int four_of_a_kind(long cards) {  // 1*13 * 12 겹치지만 않게 kicker 카드 
		int rank = 1;
		for(int a = 0 ; a< 13; a++) {
			for(int b = 0; b < 13; b++) {
				if(a != b) {
					for(int m = 0; m < 4; m++) {
						long mask = card_rank[a]*15 + (card_rank[b]<<m);
						if(cards == mask) {
							return rank;
						}
					}
					rank++;
				}
			}
		}
		return -1;
	}
	static int full_house(long cards) { // 13*12 겹치지만 않게 triple 과 pair 선정
		int rank =1;
		for(int a = 0 ; a< 13; a++) {
			for(int b = 0; b < 13; b++) {
				if(a != b) {
					for(long three: threecard) {
						for(long two : twocard) {
							long mask = card_rank[a]*three + card_rank[b]*two;
							if(cards == mask) {
								return rank;
							}
						}
					}
					rank++;
				}
			}
		}
		return -1;
	}
	static int flush(long cards) { // 4 * 13C5 그래서 13*12*11*10*9 를 5*4*3*2*1 로 나눠줘야함.
		int rank =1;
		for(int a = 0 ; a < 13; a++) {
			for(int b = a+1; b<13; b++) {
				for(int c = b+1; c< 13; c++) {
					for(int d = c+1; d< 13; d++) {
						for(int e = d+1; e< 13; e++) {
							long mask = card_rank[a] + card_rank[b] + card_rank[c] + card_rank[d] + card_rank[e];
							for(int m = 0; m < 4; m++) {
								if (cards == (mask<<m)) {
									return rank;
								}
							}
							rank++;
						}
					}
				}
			}
		}
		return -1;
	}
	static int straigth(long cards) { // 입력 받은 카드의 무늬를 임으로 spade로 바꾼뒤, 10개 케이스와 비교
		int rank =1;
		long temp = 0;
		long bit4 = 15;
		for(int i = 0 ; i <13; i++) {
			if( (cards&(bit4*card_rank[i])) > 0 )
			temp += card_rank[i];
		}
		long mask = card_A+card_K+card_Q+card_J+card_T;
		for(int i = 0; i < 10; i++) {
			if(temp == mask) {
				return rank+i;
			}
			mask -= card_rank[i];
			mask += card_rank[i+5];
		}
		return -1;
	}
	static int three_of_a_kind(long cards) {// 13가지 트리플 + 12C2 카드.
		int rank =1;
		for(int a = 0; a<13 ; a++) {
			for(int b = 0; b<13;b++) {
				if( a == b) continue;
				for(int c = b+1; c<13;c++) {
					if(a == c) continue;
					for(long aa : threecard) {
						for(long bb : onecard) {
							for(long cc : onecard) {
								if(cards == (aa*card_rank[a] + bb*card_rank[b] + cc*card_rank[c]) ){
									return rank;
								}
							}
						}
					}
					rank++;
				}
			}
		}
		return -1;
	}
	static int two_pair(long cards) {
		int rank =1;
		for(int a = 0; a<13 ; a++) {
			for(int b = a+1; b<13;b++) {
				for(int c = 0; c<13;c++) {
					if(a == c || b == c) continue;
					for(long aa : twocard) {
						for(long bb : twocard) {
							for(long cc : onecard) {
								if(cards == (aa*card_rank[a] + bb*card_rank[b] + cc*card_rank[c]) ){
									return rank;
								}
							}
						}
					}
					rank++;
				}
			}
		}
		return -1;
	}
	static int one_pair(long cards) {
		int rank =1;
		for(int a = 0; a<13 ; a++) {
			for(int b = 0; b<13 ; b++) {
				if(a == b) continue;
				for(int c = b+1; c<13 ; c++) {
					if(a == c) continue;
					for(int d = c+1; d<13 ; d++) {
						if(a == d) continue;
						for(long aa : twocard) {
							for(long bb : onecard) {
								for(long cc : onecard) {
									for(long dd : onecard) {
										if(cards == (aa*card_rank[a] + bb*card_rank[b] + cc*card_rank[c] + dd*card_rank[d]) ){
											return rank;
										}
									}
								}
							}
						}
						rank++;
					}
				}
			}
		}
		return -1;
	}
	static int high_card(long cards) {
		int rank =1;
		long temp = 0;
		long bit4 = 15;
		for(int i = 0 ; i <13; i++) {
			if( (cards&(bit4*card_rank[i])) > 0 )
			temp += card_rank[i];
		}
		int remain = 5;
		for(int a = 0; a<13; a++) {
			if( (temp & card_rank[a]) > 0) {
				remain--;
			}else {
				rank+=dp[12-a][remain-1];
			}
			if(remain == 0)
				break;
		}
		return rank;
	}
	
}
