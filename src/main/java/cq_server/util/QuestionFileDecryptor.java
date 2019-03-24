package cq_server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionFileDecryptor {
	private static final Logger LOG = LoggerFactory.getLogger(QuestionFileDecryptor.class);

	private final Path in;

	private final Charset encoding;

	private final String password;

	private final Path to;

	public QuestionFileDecryptor(final Path in, final Path to, final Charset encoding, final String password) {
		this.in = in;
		this.to = to;
		this.encoding = encoding;
		this.password = password;
	}

	public void decrypt() {
		final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(this.password);
		try (
			InputStream is = Files.newInputStream(this.in, StandardOpenOption.READ);
			InputStreamReader isr = new InputStreamReader(is, this.encoding);
			BufferedReader br = new BufferedReader(isr);
			OutputStream os = Files.newOutputStream(this.to, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			OutputStreamWriter osw = new OutputStreamWriter(os, this.encoding);
			BufferedWriter bw = new BufferedWriter(osw);
		) {
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				final String decoded = textEncryptor.decrypt(line);
				bw.write(decoded);
				bw.newLine();
			}
		} catch (final Exception e) {
			LOG.error("error:", e);
		}
	}
}
