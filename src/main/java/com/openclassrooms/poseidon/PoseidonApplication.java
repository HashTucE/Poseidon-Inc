package com.openclassrooms.poseidon;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PoseidonApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PoseidonApplication.class, args);
	}

	@Override
	public void run(String... args) {

		System.out.println(" ");
		System.out.println("  _____               _     _               _____              ");
		System.out.println(" |  __ \\             (_)   | |             |_   _|             ");
		System.out.println(" | |__) |__  ___  ___ _  __| | ___  _ __     | |  _ __   ___   ");
		System.out.println(" |  ___/ _ \\/ __|/ _ \\ |/ _` |/ _ \\| '_ \\    | | | '_ \\ / __|  ");
		System.out.println(" | |  | (_) \\__ \\  __/ | (_| | (_) | | | |  _| |_| | | | (__ _ ");
		System.out.println(" |_|   \\___/|___/\\___|_|\\__,_|\\___/|_| |_| |_____|_| |_|\\___(_)");
		System.out.println(" ==============================================================");
		System.out.println(" :: API ::                                          (v1.0.0)");
		System.out.println(" ");
	}
}
