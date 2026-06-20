package model.dao;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 비밀번호 해싱 — 외부 라이브러리 없이 JDK PBKDF2 사용.
 * 저장 형식: "pbkdf2$&lt;iterations&gt;$&lt;saltBase64&gt;$&lt;hashBase64&gt;".
 * 기존 평문 계정 호환을 위해 matches()는 해시가 아닌 저장값은 평문으로 비교한다(점진 마이그레이션).
 */
public final class PasswordHasher {
	private static final String PREFIX = "pbkdf2$";
	private static final int ITERATIONS = 120000;
	private static final int KEY_LEN_BITS = 256;
	private static final int SALT_LEN = 16;
	private static final SecureRandom RANDOM = new SecureRandom();

	private PasswordHasher() {
	}

	/** 평문을 PBKDF2 해시 문자열로 변환 */
	public static String hash(String plain) {
		if (plain == null) {
			plain = "";
		}
		byte[] salt = new byte[SALT_LEN];
		RANDOM.nextBytes(salt);
		byte[] dk = pbkdf2(plain.toCharArray(), salt, ITERATIONS, KEY_LEN_BITS);
		return PREFIX + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt) + "$"
				+ Base64.getEncoder().encodeToString(dk);
	}

	/** 이미 해시면 그대로, 평문이면 해시 (UPDATE 시 이중 해시 방지) */
	public static String ensureHashed(String value) {
		if (value != null && value.startsWith(PREFIX)) {
			return value;
		}
		return hash(value);
	}

	/** 평문이 저장값과 일치하는지. 저장값이 해시면 PBKDF2 검증, 레거시 평문이면 그대로 비교. */
	public static boolean matches(String plain, String stored) {
		if (stored == null) {
			return false;
		}
		if (!stored.startsWith(PREFIX)) {
			return stored.equals(plain); // 레거시 평문 폴백 (기존 계정 호환)
		}
		try {
			String[] parts = stored.split("\\$"); // [0]=pbkdf2 [1]=iter [2]=salt [3]=hash
			int iter = Integer.parseInt(parts[1]);
			byte[] salt = Base64.getDecoder().decode(parts[2]);
			byte[] expected = Base64.getDecoder().decode(parts[3]);
			byte[] actual = pbkdf2((plain == null ? "" : plain).toCharArray(), salt, iter, expected.length * 8);
			return constantTimeEquals(expected, actual);
		} catch (Exception ex) {
			return false;
		}
	}

	private static byte[] pbkdf2(char[] pw, byte[] salt, int iter, int keyLenBits) {
		try {
			KeySpec spec = new PBEKeySpec(pw, salt, iter, keyLenBits);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			return factory.generateSecret(spec).getEncoded();
		} catch (Exception ex) {
			throw new RuntimeException("비밀번호 해시 실패", ex);
		}
	}

	private static boolean constantTimeEquals(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}
		int result = 0;
		for (int i = 0; i < a.length; i++) {
			result |= a[i] ^ b[i];
		}
		return result == 0;
	}
}
