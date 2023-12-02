package Experimenting;

import java.util.ArrayList;

public class ArrayListCheck {
	public static void main(String[] args) {
		ArrayList<Integer> ints = new ArrayList<>();
		
		ints.add(0);
		ints.add(1);
		ints.add(5);
		ints.add(3);
		ints.add(4);
		ints.add(2);
		
		System.out.println("Chjeck");
		
		/*
		for (int i = 0; i < ints.size(); i++) {
			if (i < ints.size() - 1) {
				if (ints.get(i) <= ints.get(i + 1)) {
					if (i > 0) {
						if (ints.get(i) >= ints.get(i - 1)) {
							i++;
						} else {
							int temp = ints.get(i);
							ints.remove(i);
							ints.add(i - 1, temp);
							i--;
						}
					}
				} else {
					int temp = ints.get(i);
					ints.remove(i);
					ints.add(i + 1, temp);
					i--;
				}
			}
			
			System.out.println(ints.toString());
		}
		*/
		
		int pos;
		int temp;
		
		for (int i = 0; i < ints.size(); i++) {
			pos = i;
			for (int j = i + 1; j < ints.size(); j++) {
				if (ints.get(j) < ints.get(pos)) {
					pos = j;
				}
			}
			
			temp = ints.get(pos);
			ints.set(pos, ints.get(i));
			ints.set(i, temp);
		}
		
		for (int i : ints) {
			System.out.println(i);
		}
	}
}
