package cq_server.factory;

import java.io.IOException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import cq_server.model.Option;
import cq_server.model.Questions;
import cq_server.model.RawQuestion;
import cq_server.model.RawTip;
import cq_server.model.Selectable;
import cq_server.model.Tipable;

public class OnlineQuestionsLoader implements IQuestionsLoader {
	private static final Logger LOG = LoggerFactory.getLogger(OnlineQuestionsLoader.class);

	private static final String DEFAULT_USER_MD5 = "b43a6b9024591ba199aca71c587444a3";

	private static final String USER_NAME_MD5_HASH_HEADER = "user_name_md5_hash";

	private final String questionsApiEndpoint;

	private final String tipsApiEndpoint;

	public OnlineQuestionsLoader(final String questionsApiEndpoint, final String tipsApiEndpoint) {
		this.questionsApiEndpoint = questionsApiEndpoint;
		this.tipsApiEndpoint = tipsApiEndpoint;
	}

	@Override
	public Questions load() {
		configureUnirest();
		long start = System.currentTimeMillis();
		while (true) {
			try {
				Questions questions = getQuestions();
				if (isEmpty(questions) == false || (System.currentTimeMillis() - start > 300000)) {
					return questions;
				}
				Thread.sleep(60000);
			} catch (final InterruptedException e) {
				LOG.error("error:", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	private boolean isEmpty(Questions questions) {
		return questions.getRawQuestions().isEmpty() || questions.getTips().isEmpty();
	}

	private Questions getQuestions() {
		Questions questions = new Questions(new RawQuestion[] {}, new RawTip[] {});
		try {
			final RawQuestion[] selectables = adapt(retrieveSelectables());
			final RawTip[] tips = adapt(retrieveTips());
			questions = new Questions(selectables, tips);
		} catch (Exception e) {
			LOG.error("error when loading questions:", e);
		}
		return questions;
	}

	private RawTip[] adapt(Tipable[] t) {
	    RawTip[] tips = new RawTip[t.length];

	    for (int i = 0; i < t.length; i++) {
            Tipable tipable = t[i];
            tips[i] = new RawTip(tipable.getQuestion(), tipable.getAnswer());
        }
        return tips;
    }

    private RawQuestion[] adapt(Selectable[] selectables) {
        RawQuestion[] questions = new RawQuestion[selectables.length];
        for (int i = 0; i < selectables.length; i++) {
            Selectable selectable = selectables[i];
            Option[] so = selectable.getOptions();
            String[] options = new String[so.length];
            int trueAnswer = 1;
            for (int j = 0; j < so.length; j++) {
                Option option = so[j];
                options[j] = option.getText();
                if (option.isCorrect()) {
                    trueAnswer = j + 1;
                }
            }
            String question = selectable.getQuestion();
            questions[i] = new RawQuestion(question, options, trueAnswer);
        }
        return questions;
    }

    private Tipable[] retrieveTips() throws UnirestException {
		final HttpResponse<Tipable[]> tipResponse =
				Unirest
				.get(this.tipsApiEndpoint)
				.header(USER_NAME_MD5_HASH_HEADER, DEFAULT_USER_MD5)
		        .asObject(Tipable[].class);
		final Tipable[] tips = tipResponse.getBody();
		return tips;
	}

	private Selectable[] retrieveSelectables() throws UnirestException {
		final HttpResponse<Selectable[]> questionsResponse =
				Unirest
				.get(this.questionsApiEndpoint)
                .header(USER_NAME_MD5_HASH_HEADER, DEFAULT_USER_MD5)
				.asObject(Selectable[].class);
		final Selectable[] questions = questionsResponse.getBody();
		return questions;
	}

	private void configureUnirest() {
		Unirest.setObjectMapper(new ObjectMapper() {
			private final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

			@Override
			public <T> T readValue(final String value, final Class<T> valueType) {
				try {
					return this.jacksonObjectMapper.readValue(value, valueType);
				} catch (final IOException e) {
					LOG.error(MessageFormat.format("error while reading value {0}, type {1}:", value, valueType), e);
					throw new RuntimeException(e);
				}
			}

			@Override
			public String writeValue(final Object value) {
				try {
					return this.jacksonObjectMapper.writeValueAsString(value);
				} catch (final JsonProcessingException e) {
					LOG.error(MessageFormat.format("error while writing value {0}:", value), e);
					throw new RuntimeException(e);
				}
			}
		});
	}
}
