package holdem;

public class 비트카운트 {

	public static void main(String[] args) {
		long test = (long)(Math.random()*Long.MAX_VALUE);
		System.out.println(test);
		System.out.println("4321098765432109876543210987654321098765432109876543210987654321");
		System.out.printf("%64s\n",Long.toBinaryString(test));
		System.out.println(bitcount(test));
		System.out.printf("%64s\n",Long.toBinaryString(~test));
		
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

}
