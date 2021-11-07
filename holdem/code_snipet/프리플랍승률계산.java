package holdem.code_snipet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class 프리플랍승률계산 {
	public static final int[] card  = {
			2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
			2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
			2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
			2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 
			};
	public static final int[] mark  = {
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
			5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
			};
	public static final int[] flush_made  = {
			2* 2* 2* 2* 2, 
			3* 3* 3* 3* 3, 
			5* 5* 5* 5* 5, 
			7* 7* 7* 7* 7
			};
	public static HashSet<Integer> flush_sets = new HashSet<Integer>();
	public static HashMap<Integer, Integer> rankmap = 개량된rank생성.initRankData();
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for(int fm :flush_made) {
			flush_sets.add(fm);
		}
		int[] hero = new int[2];
		int[] vill = new int[2];
		long[][][][][] winrate = new long[13][13][13][13][3];
		for(int i = 0 ; i < 13 ; i++) {
			for( int j = 0 ; j < 13 ; j++) {
				hero[0] = i;
				hero[1] = j;
				if(i<=j) {
					hero[0] += 13;
				}
				for(int a = 0; a < 52 ;a++) {
					if(a == hero[0] || a == hero[1])
						continue;
					vill[0] = a;
					for(int b = a+1; b < 52; b++) {
						if (b %4 == 3)
							System.out.print("\r"+a+" "+b+" "+ ((System.currentTimeMillis()-start)/1000));
						if(b == hero[0] || b == hero[1])
							continue;
						vill[1] = b;
						int k = a%13;
						int l = b%13;
						int kmark = a/13;
						int lmark = b/13;
						if(k>l) {
							int temp = k;
							k = l;
							l = temp;
						}
						if(kmark != lmark) {
							int temp = k;
							k = l;
							l = temp;
						}
						getWinrate(hero, vill, winrate[i][j][k][l]);
					}
				}
				System.out.println("\r"+i+" "+j+ " " + (System.currentTimeMillis()-start));
			}
		}
	}

	private static void getWinrate(int[] hero, int[] vill, long[] winrate) {
		boolean[] used_card = new boolean[52];
		int[] board = new int[7];
		used_card[hero[0]] = true;
		used_card[hero[1]] = true;
		used_card[vill[0]] = true;
		used_card[vill[1]] = true;
		for( int a = 0; a < 52; a++) { if ( used_card[a] ) continue;
			for( int b = a+1; b < 52; b++) { if ( used_card[b] ) continue;
				for( int c = b+1; c < 52; c++) { if ( used_card[c] ) continue;
					for( int d = c+1; d < 52; d++) { if ( used_card[d] ) continue;
						for( int e = d+1; e < 52; e++) { if ( used_card[e] ) continue;
							board[0] = a;
							board[1] = b;
							board[2] = c;
							board[3] = d;
							board[4] = e;
							board[5] = hero[0];
							board[6] = hero[1];
							int hero_best_score = check_score( board);
							board[5] = vill[0];
							board[6] = vill[1];
							int vill_best_score = check_score( board);
							winrate[Integer.compare(hero_best_score, vill_best_score)+1] +=1;
						}
					}
				}
			}
		}
		
	}

	private static int check_score( int[] board) {
		int[] marks = new int[7];
		int[] numbs = new int[7];
		long tempnumbs = 1;
		int tempmarks = 1;
		int result = 99999;
		
		for(int i = 0 ; i < 7; i++) {
			marks[i] = mark[ board[i] ];
			numbs[i] = card[ board[i] ];
			tempnumbs *= numbs[i];
			tempmarks *= marks[i];
		}
		//System.out.println(Arrays.toString(board)+ " => " +tempnumbs);
		try {
			for(int a = 0; a < 7; a++) {
				for(int b = a+1; b < 7 ; b++) {
					if(flush_sets.contains(tempmarks / marks[a] / marks[b]) ) {
						result = Math.min(result, rankmap.get((int)(tempnumbs / numbs[a] / numbs[b] * 43)) );
					} else {
						result = Math.min(result, rankmap.get((int)(tempnumbs / numbs[a] / numbs[b])) );
					}
					
				}
			}
		} catch (Exception e){
		    //에러시 수행
			System.out.println(Arrays.toString(board)+ " => " +tempnumbs);
			e.printStackTrace(); //오류 출력(방법은 여러가지)
			throw e; //최상위 클래스가 아니라면 무조건 던져주자
		}
		return result;
	}

}
