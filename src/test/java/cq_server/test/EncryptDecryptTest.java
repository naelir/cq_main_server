package cq_server.test;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import cq_server.util.QuestionFileDecryptor;
import cq_server.util.QuestionFileEnryptor;

public class EncryptDecryptTest {
	@Test
	public void test() {
		final Path in = Paths.get("test-questions.txt");
		final Path enc = Paths.get("test-questions-encrypt.txt");
		final Path dec = Paths.get("test-questions-decrypt.txt");
		final Charset encoding = Charset.forName("utf-8");
		final String password = "1";
		final QuestionFileEnryptor enryptor = new QuestionFileEnryptor(in, enc, encoding, password);
		enryptor.encrypt();
		final QuestionFileDecryptor decryptor = new QuestionFileDecryptor(enc, dec, encoding, password);
		decryptor.decrypt();
	}
}
