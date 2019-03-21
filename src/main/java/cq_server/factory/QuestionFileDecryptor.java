package cq_server.factory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.model.Questions;
import cq_server.model.RawQuestion;
import cq_server.model.RawTip;

public class QuestionFileDecryptor implements IQuestionsLoader {
	private static final Logger LOG = LoggerFactory.getLogger(QuestionFileDecryptor.class);

	private final Random generator;

	private final String questionsFile;

	private final Charset encoding;

	private final String password;

	public QuestionFileDecryptor(final String questionsFile, final Charset encoding, final String password) {
		this.questionsFile = questionsFile;
		this.encoding = encoding;
		this.password = password;
		this.generator = new Random();
	}

	@Override
	public Questions load() {
		final List<RawQuestion> rawSelectables = new ArrayList<>();
		final List<RawTip> rawTips = new ArrayList<>();
		final Questions questions = new Questions(rawSelectables, rawTips);
		final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(this.password);
		final URL url = this.getClass().getClassLoader().getResource(this.questionsFile);
		if (url != null) {
			try (
				InputStream is = url.openStream();
				InputStreamReader isr = new InputStreamReader(is, this.encoding);
				BufferedReader br = new BufferedReader(isr)
			) {
				for (String line = br.readLine(); line != null; line = br.readLine()) {
					final String decoded = textEncryptor.decrypt(line);
					final String[] split = decoded.split("\\|");
					if (split.length == 4) {
						final RawQuestion selectable = this.readSelectable(split);
						rawSelectables.add(selectable);
					} else {
						final RawTip tip = this.readTip(split);
						rawTips.add(tip);
					}
				}
			} catch (final Exception e) {
				LOG.error("error during loading questions", e);
			}
			LOG.info("questions loaded");
		} else
			LOG.info("question file not found");
		return questions;
	}

	private RawQuestion readSelectable(final String[] line) {
		final String[] options = new String[4];
		System.arraycopy(line, 1, options, 0, 4);
		final int trueAnswer = this.shuffle(options);
		return new RawQuestion(line[0], options, trueAnswer);
	}

	private RawTip readTip(final String[] line) {
		return new RawTip(line[0], Integer.parseInt(line[1]));
	}

	private int shuffle(final String[] array) {
		final String trueAnswer = array[0];
		for (int i = array.length - 1; i > 0; i--) {
			final int index = this.generator.nextInt(i + 1);
			final String a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(trueAnswer))
				return i + 1;
		return 0;
	}
}
