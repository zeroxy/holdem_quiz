package holdem.code_snipet;

public class 족보맞추기 {

	public static final long card_A = 1L<<(0*4);
	public static final long card_2 = 1L<<(1*4);
	public static final long card_3 = 1L<<(2*4);
	public static final long card_4 = 1L<<(3*4);
	public static final long card_5 = 1L<<(4*4);
	public static final long card_6 = 1L<<(5*4);
	public static final long card_7 = 1L<<(6*4);
	public static final long card_8 = 1L<<(7*4);
	public static final long card_9 = 1L<<(8*4);
	public static final long card_T = 1L<<(9*4);
	public static final long card_J = 1L<<(10*4);
	public static final long card_Q = 1L<<(11*4);
	public static final long card_K = 1L<<(12*4);
	public static final long[] card_rank = {card_A, card_K, card_Q, card_J, card_T,	card_9, card_8,	card_7, card_6, card_5,	card_4, card_3,	card_2, card_A,0};
	public static final long[] fourcard  = { 0b1111};
	public static final long[] threecard = { 0b0111, 0b1011, 0b1101, 0b1110 };
	public static final long[] twocard   = { 0b0011, 0b0101, 0b1001, 0b0110, 0b1010, 0b1100};
	public static final long[] onecard   = { 0b0001, 0b0010, 0b0100, 0b1000 };
	public static final long mark_s = 0;
	public static final long mark_d = 1;
	public static final long mark_h = 2;
	public static final long mark_c = 3;
	public static final int[] cumcount = {0, 10,   166,  322,  1599, 1609, 2467, 3325, 6185, 7462 };  
			// SF , FK , FH , F  , S  , TK , 2P , 1P , HC 

	public static void main(String[] args) {

		long test = 0x11111;
		for(int i = 0 ; i < 10; i++)
			System.out.println(straigth_flush(test<<(4*i)));
		System.out.println("####################");
		System.out.println(four_of_a_kind(card_A*15+card_K*1));
		System.out.println(four_of_a_kind(card_A*15+card_Q*2));
		System.out.println(four_of_a_kind(card_A*15+card_2*4));
		System.out.println(four_of_a_kind(card_K*15+card_Q*8));
		System.out.println(four_of_a_kind(card_K*15+card_A*1));
		System.out.println(four_of_a_kind(card_K*15+card_J*1));
		System.out.println(four_of_a_kind(card_2*14+card_3*1));
		System.out.println("####################");
		for(long temp : fourcard)	System.out.printf(" %d ", temp);
		System.out.println();
		for(long temp : threecard)	System.out.printf(" %d ", temp);
		System.out.println();
		for(long temp : twocard)	System.out.printf(" %d ", temp);
		System.out.println();
		for(long temp : onecard)	System.out.printf(" %d ", temp);
		System.out.println();
		System.out.println("####################");
		test = 0b1000100110001L;
		System.out.println(straigth_flush(test)+" SF");
		System.out.println(four_of_a_kind(test)+" 4k");
		System.out.println(full_house(test)+" fh");
		System.out.println(flush(test)+" F");
		System.out.println(straigth(test)+" S");
		System.out.println(three_of_a_kind(test)+" 3k");
		System.out.println(two_pair(test)+" 2p");
		System.out.println(one_pair(test)+" 1p");
		System.out.println(high_card(test)+" hc");
		System.out.println(비트정보를카드로표기하기.long_to_String(test));
		
	}

	static int check_cards(long cards) {
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
		long bit4 = 0xfL;
		for(int i = 0 ; i <13; i++) {
			if( (cards&(bit4<<(i*4))) > 0 )
			temp += 1<<(i*4);
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
		for(int a =0 ; a<13; a++) {
			for(int b =a+1 ; b<13; b++) {
				for(int c =b+1 ; c<13; c++) {
					for(int d =c+1 ; d<13; d++) {
						for(int e =d+1 ; e<13; e++) {
							if(temp == (card_rank[a] + card_rank[b] + card_rank[c] + card_rank[d] + card_rank[e]))
								return rank;
							rank++;
						}
					}
				}
			}
		}
		return -1;
	}
}
