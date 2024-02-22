package co.mannit.commonservice.exception;

import java.util.UUID;

public class IdGenerator {

	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
//		System.out.println(UUID.fromString("nir-vim"));
		System.out.println(UUID.nameUUIDFromBytes(new byte[] {10,15}));
	}
}
