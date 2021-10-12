package holdem.code_snipet;

import java.util.Arrays;

import holdem.util.HoldemUtil;

public class dp테이블만들기 {

	public static void main(String[] args) {
		long starttime = System.currentTimeMillis();
		long[] timetable = new long[10];
		int[]  timecount = new int [10];
		int[][] dp = get_dp_table();
		int[] casecount = new int[7473];
		//for(int i = 0 ; i < 53; i ++) {
		//	System.out.println(Arrays.toString(dp[i]));
		//}
		
		System.out.println(52*51*50*49*48/5/4/3/2);
		System.out.println(dp[52][5]);
		for(int i = 1; i <= dp[52][5]; i++) {
			int temp = i;
			long result = 0;
			long anchor = 1;
			int a = 52;
			int b = 5;
			while(temp>0) {
				if( dp[a-1][b-1] < temp) {
					temp -= dp[a-1][b-1];
				}else {
					result+=anchor;
					b--;
				}
				if(b== 0)
					break;
				a--;
				anchor *= 2;
				
			}
			long subtime = System.currentTimeMillis();
			int resultrank = HoldemUtil.check_cards(result);
			int category = HoldemUtil.rank_to_category(resultrank);
			timetable[category] += ( System.currentTimeMillis() - subtime );
			timecount[category] += 1;
			if (0 >= resultrank|| i%20000 == 0) {
				System.out.println(HoldemUtil.long_to_String(result) + " " +resultrank+ " -> "+ i);
				//System.out.printf("%52s  \n", Long.toBinaryString(result));
			}
			casecount[resultrank]++;
			if(i>200000)
				break;
		}
		System.out.println(System.currentTimeMillis() - starttime );
		for(int t = 0; t< 10; t++) {
			System.out.println(timetable[t] +" , "+ timecount[t]);
		}
		//System.out.println(Arrays.toString(casecount));

		
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

}
