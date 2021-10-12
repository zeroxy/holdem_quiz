package holdem.code_snipet;

public class 비트정보를카드로표기하기 {

	static final char[] mark = "sdhc".toCharArray();
	static final char[] numb = "A23456789TJQK".toCharArray();
	static final long div = 1_000_000_000L;
	public static void main(String[] args) {
		//for(long i = 1; i < 400; i++) {
		String cardset = long_to_String((1l<<52)-1);
		System.out.println(cardset);
		long temp = string_to_long(cardset);
		for(int e = 1; e<=52; e++) {
			System.out.print(e%10);
		}
		System.out.println();
		for(long bit= 1; bit < (1L<<52); bit<<=1) {
			if( (temp&bit) > 0) {
				System.out.print(1);
			}else {
				System.out.print(0);
			}
		}
		System.out.println();
		//}

	}
	static String long_to_String(long cardset) {
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
	
	static long string_to_long(String cardset) {
		long result = 0;
		char[] str = cardset.toCharArray();
		for(int i = 0; i < str.length; i++) {
			for(int n = 0 ; n < numb.length; n++) {
				if(numb[n]==str[i]) {
					for(int m = 0 ; m < mark.length; m++) {
						if(mark[m]==str[i+1]) {
							result += (1L<<(n*4+m));
							i++;
						}
					}
				}
			}
		}
		return result;
	}

}
