package holdem.code_snipet;

import java.util.HashMap;

public class 개량된rank생성 {
	public static final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
	public static final int[] cards  = {
			//2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
			//2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
			//2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
			2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 
			};
	public static final int[] straight = {
			cards[0] * cards[ 1] * cards[ 2] * cards[ 3] * cards[ 4], 
			cards[1] * cards[ 2] * cards[ 3] * cards[ 4] * cards[ 5], 
			cards[2] * cards[ 3] * cards[ 4] * cards[ 5] * cards[ 6], 
			cards[3] * cards[ 4] * cards[ 5] * cards[ 6] * cards[ 7], 
			cards[4] * cards[ 5] * cards[ 6] * cards[ 7] * cards[ 8], 
			cards[5] * cards[ 6] * cards[ 7] * cards[ 8] * cards[ 9], 
			cards[6] * cards[ 7] * cards[ 8] * cards[ 9] * cards[10], 
			cards[7] * cards[ 8] * cards[ 9] * cards[10] * cards[11], 
			cards[8] * cards[ 9] * cards[10] * cards[11] * cards[12], 
			cards[9] * cards[10] * cards[11] * cards[12] * cards[ 0]
	};
	public static HashMap<Integer, Integer> rankmap = new HashMap<>();
	
	public static void main(String[] args) {
		initRankData();
	}
	
	public static HashMap<Integer, Integer> initRankData() {
		rankmap.clear();
		addStraightFlush();
		System.out.println(" sf : "+rankmap.size());
		addFourAKind();
		System.out.println(" fk : "+rankmap.size());
		addFullHouse();
		System.out.println(" fh : "+rankmap.size());
		addFlush();
		System.out.println(" fl : "+rankmap.size());
		addStraight();
		System.out.println(" st : "+rankmap.size());
		addThreeAKind();
		System.out.println(" tk : "+rankmap.size());
		addTwoPair();
		System.out.println(" tp : "+rankmap.size());
		addOnePair();
		System.out.println(" op : "+rankmap.size());
		addHighCard();
		System.out.println(" hc : "+rankmap.size());
		return rankmap;
	}

	private static void addStraightFlush() {
		for(int i : straight) {
			rankmap.putIfAbsent(i*43, rankmap.size());
		}
		
	}

	private static void addFourAKind() {
		for(int i = 0 ; i < 13; i++) {
			for( int j = 0; j < 13; j++) {
				if(i != j) {
					int fourcard = cards[i]*cards[i]*cards[i]*cards[i];
					rankmap.putIfAbsent(fourcard*cards[j], rankmap.size());
				}
			}
		}
		
	}

	private static void addFullHouse() {
		for(int i = 0 ; i < 13; i++) {
			for( int j = 0; j < 13; j++) {
				if(i != j) {
					int threecard = cards[i]*cards[i]*cards[i];
					int twocard = cards[j]*cards[j];
					rankmap.putIfAbsent(threecard*twocard, rankmap.size());
				}
			}
		}
		
	}

	private static void addFlush() {
		for(int a = 0; a < 13; a++) {
			for(int b = a+1; b < 13; b++) {
				for(int c = b+1; c < 13; c++) {
					for(int d = c+1; d < 13; d++) {
						for(int e = d+1; e < 13; e++) {
							int flushmade = 43 * cards[a] * cards[b] * cards[c] * cards[d] * cards[e] ;
							rankmap.putIfAbsent(flushmade, rankmap.size());
						}
					}
				}
			}
		}
	}

	private static void addStraight() {
		for(int i : straight) {
			rankmap.putIfAbsent( i , rankmap.size());
		}
	}

	private static void addThreeAKind() {
		for(int a = 0; a < 13; a++) {
			for(int b = 0; b < 13; b++) {
				if (a==b)
					continue;
				for(int c = 0; c < 13; c++) {
					if (a==c && b==c )
						continue;
					int threecard = cards[a] * cards[a] * cards[a] * cards[b] * cards[c] ;
					rankmap.putIfAbsent(threecard , rankmap.size());
				}
			}
		}
	}

	private static void addTwoPair() {
		for(int a = 0; a < 13; a++) {
			for(int b = a+1; b < 13; b++) {
				if(a == b)
					continue;
				for(int c = 0; c < 13; c++) {
					if (a==c && b==c )
						continue;
					int twopair = cards[a] * cards[a] * cards[b] * cards[b] * cards[c] ;
					rankmap.putIfAbsent(twopair, rankmap.size());
				}
			}
		}
	}

	private static void addOnePair() {
		for(int a = 0; a < 13; a++) {
			for(int b = 0; b < 13; b++) {
				if(a == b)
					continue;
				for(int c = b+1; c < 13; c++) {
					if (a==c && b==c )
						continue;
					for(int d = c+1; d < 13; d++) {
						if (a==d && b==d && c==d )
							continue;
						int onepair = cards[a] * cards[a] * cards[b] * cards[c] * cards[d] ;
						rankmap.putIfAbsent(onepair , rankmap.size());
					}
				}
			}
		}
	}

	private static void addHighCard() {
		for(int a = 0; a < 13; a++) {
			for(int b = a+1; b < 13; b++) {
				for(int c = b+1; c < 13; c++) {
					for(int d = c+1; d < 13; d++) {
						for(int e = d+1; e < 13; e++) {
							int highcard = cards[a] * cards[b] * cards[c] * cards[d] * cards[e] ;
							rankmap.putIfAbsent( highcard , rankmap.size());
						}
					}
				}
			}
		}
	}
}
