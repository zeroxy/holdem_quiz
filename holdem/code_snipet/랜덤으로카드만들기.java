package holdem.code_snipet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class 랜덤으로카드만들기 {

	static final int all_card = 52;
	static final int multiply = 10;
	public static void main(String[] args) {
		int[] total = new int[all_card+1];
		//int[] cards = new int[all_card];
		ArrayList<Integer> cards = new ArrayList<>();
		for(int i = 0 ; i < all_card; i++) {
			cards.add(i+1);
		}
		int result[] = new int[5];
		for(int i = 0 ; i < 1000000; i++) {
			Collections.shuffle(cards);
			for(int j = 0 ; j< 5; j++) {
				total[cards.get(j)]++;
				result[j] = cards.get(j);
			}
			if(i%100 == 0)
				System.out.println(i + " " +Arrays.toString(result) );
		}
		/*
		for(int i = 0; i < 10000; i ++) {
			int[] result = Generate_card(5);
			for(int j : result) {
				total[j]++;
			}
			System.out.println(i + " " +Arrays.toString(result) );
			
		}
*/
		
		System.out.println(Arrays.toString(total));
		int sum = 0;
		for(int temp : total) {
			sum+=temp;
		}
		int mean = sum/52;
		System.out.println("총합 :"+sum+" 평균 : "+mean);
		total[0] = mean;
		sum = 0;
		for(int i = 0 ; i <53; i++) {
			total[i] -= mean;
			total[i] = total[i]*total[i];
			sum += total[i] ;
		}
		System.out.println("편차 제곱 : ");
		System.out.println(Arrays.toString(total));
		System.out.println(Math.pow(sum, 0.5)/52);
	}
	
	static int[] Generate_card(int 카드개수) {
		HashSet<Integer> temppot = new HashSet<>(); 
		int[] result = new int[카드개수];
		int temp = 0;
		final int multiply = 10;
		for(int i = 0; i < 카드개수; i++){
			do {
				temp = (int)(Math.random()*(all_card-i)*multiply)/multiply;
			}while (temppot.contains(temp));
			temppot.add(temp);
			result[i] = temp+1;
		}
		return result;
	}

}
