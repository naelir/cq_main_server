package cq_server.factory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.model.Questions;
import cq_server.model.RawQuestion;
import cq_server.model.RawTip;

public class FileQuestionLoader implements IQuestionsLoader {
	private static final Logger LOG = LoggerFactory.getLogger(FileQuestionLoader.class);

	private final Path from;

	private final Charset encoding;

	private final Random generator;

	private final boolean deleteOnClose;

	public FileQuestionLoader(final Path from, final Charset encoding, final boolean deleteOnClose) {
		this.from = from;
		this.encoding = encoding;
		this.deleteOnClose = deleteOnClose;
		this.generator = new Random();
	}

	@Override
	public Questions load() {
		final List<RawQuestion> rawSelectables = new ArrayList<>();
		final List<RawTip> rawTips = new ArrayList<>();
		final Questions questions = new Questions(rawSelectables, rawTips);
		StandardOpenOption[] options;
		if (this.deleteOnClose)
			options = new StandardOpenOption[] { StandardOpenOption.READ, StandardOpenOption.DELETE_ON_CLOSE };
		else
			options = new StandardOpenOption[] { StandardOpenOption.READ };
		try (
			InputStream is = Files.newInputStream(this.from, options);
			InputStreamReader isr = new InputStreamReader(is, this.encoding);
			BufferedReader br = new BufferedReader(isr);
		) {
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				final String[] split = line.split("\\|");
				if (split.length == 5) {
					final RawQuestion selectable = this.readSelectable(split);
					rawSelectables.add(selectable);
				} else {
					final RawTip tip = this.readTip(split);
					rawTips.add(tip);
				}
			}
		} catch (final Exception e) {
			LOG.error("error during writing questions", e);
		}
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
