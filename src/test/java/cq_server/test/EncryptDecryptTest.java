package cq_server.test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import cq_server.util.QuestionFileDecryptor;
import cq_server.util.QuestionFileEnryptor;

public class EncryptDecryptTest {
	private static final String TEST_QUESTIONS_DECRYPT_TXT = "test-questions-decrypt.txt";
    private static final String TEST_QUESTIONS_ENCRYPT_TXT = "test-questions-encrypt.txt";
    private static final String TEST_QUESTIONS_TXT = "test-questions.txt";

    @Test
	public void test() {
		final Path in = Paths.get(TEST_QUESTIONS_TXT);
		final Path enc = Paths.get(TEST_QUESTIONS_ENCRYPT_TXT);
		final Path dec = Paths.get(TEST_QUESTIONS_DECRYPT_TXT);
		if (Files.exists(in) && Files.exists(enc) && Files.exists(dec)) {
	        final Charset encoding = Charset.forName("utf-8");
	        final String password = "1";
	        final QuestionFileEnryptor enryptor = new QuestionFileEnryptor(in, enc, encoding, password);
	        enryptor.encrypt();
	        final QuestionFileDecryptor decryptor = new QuestionFileDecryptor(enc, dec, encoding, password);
	        decryptor.decrypt();
        }
	}
}
