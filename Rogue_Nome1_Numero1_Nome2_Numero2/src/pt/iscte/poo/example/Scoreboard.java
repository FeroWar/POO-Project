package pt.iscte.poo.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Scoreboard {
	static void teste(String name, int score) {
		try {
			File scoreboard=new File("scoreboard.txt");
			Scanner sc = new Scanner(scoreboard);
			String[] file = new String[5];
			for (int i = 0; i != 5; i++) {
				if (sc.hasNextLine()) {
					file[i] = sc.nextLine();
				} else {
					file[i] = "empty";
				}
			}
			for (int i = 0; i != 5; i++) {
				if (file[i].equals("empty")) {
					file[i] = new String(name + ":" + score);
					break;
				} else {
					String s = file[i];
					String[] split = s.split(":");
					if (split.length>=2 && Integer.parseInt(split[1]) < score) {
						for (int j = i; j != 4; j++) {
							file[4-j] = file[3-j];
						}
						file[i] = new String(name + ":" + score);
						break;
					}
				}
			}
			PrintWriter pw = new PrintWriter(scoreboard);
			for (int i = 0; i != 5; i++) {
				pw.write(file[i]+"\n");
			}
			pw.close();
			sc.close();
		} catch (IOException e) {
			System.out.println("error");
		}
	}
}
