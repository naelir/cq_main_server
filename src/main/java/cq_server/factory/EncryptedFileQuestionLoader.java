package cq_server.factory;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import cq_server.model.Questions;
import cq_server.util.QuestionFileDecryptor;

public class EncryptedFileQuestionLoader implements IQuestionsLoader {
	private final Path in;

	private final Charset encoding;

	private final String password;

	public EncryptedFileQuestionLoader(final Path in, final Charset encoding, final String password) {
		this.in = in;
		this.encoding = encoding;
		this.password = password;
	}

	@Override
	public Questions load() {
		final String uuid = UUID.randomUUID().toString();
		final Path to = Paths.get(uuid);
		final QuestionFileDecryptor decryptor = new QuestionFileDecryptor(this.in, to, this.encoding, this.password);
		decryptor.decrypt();
		final FileQuestionLoader loader = new FileQuestionLoader(to, this.encoding, true);
		final Questions load = loader.load();
		return load;
	}
}
