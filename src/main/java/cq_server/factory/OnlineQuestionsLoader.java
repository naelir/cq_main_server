package cq_server.factory;

import java.io.IOException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import cq_server.model.Questions;
import cq_server.model.RawQuestion;
import cq_server.model.RawTip;

//@formatter:off
public class OnlineQuestionsLoader implements IQuestionsLoader {
	private static final Logger LOG = LoggerFactory.getLogger(OnlineQuestionsLoader.class);

	private static final String DEFAULT_USER_MD5 = "b43a6b9024591ba199aca71c587444a3";

	private static final String USER_NAME_MD5_HASH_HEADER = "user_name_md5_hash";

	private static final String TIME_MD5_HASH_HEADER = "time_md5_hash";

	private final String questionsApiEndpoint;

	private final String tipsApiEndpoint;

	private final String userMd5;

	private final String userNameMd5HashHeader;

	private final String timeMd5HashHeader;

	public OnlineQuestionsLoader(final String questionsApiEndpoint, final String tipsApiEndpoint) {
		this.questionsApiEndpoint = questionsApiEndpoint;
		this.tipsApiEndpoint = tipsApiEndpoint;
		this.userMd5 = DEFAULT_USER_MD5;
		this.userNameMd5HashHeader = USER_NAME_MD5_HASH_HEADER;
		this.timeMd5HashHeader = TIME_MD5_HASH_HEADER;
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
			final long second = System.currentTimeMillis() / 1000;
			final String timeHash = DigestUtils.md5DigestAsHex(String.valueOf(second).getBytes());
			LOG.debug("current second: {}, current timehash: {}", second, timeHash);
			final RawQuestion[] selectables = retrieveSelectables(timeHash);
			final RawTip[] tips = retrieveTips(timeHash);
			questions = new Questions(selectables, tips);
		} catch (Exception e) {
			LOG.error("error when loading questions:", e);
		}
		return questions;
	}

	private RawTip[] retrieveTips(final String timeHash) throws UnirestException {
		final HttpResponse<RawTip[]> tipResponse =
				Unirest
				.get(this.tipsApiEndpoint)
				.header(this.userNameMd5HashHeader, this.userMd5)
		        .header(this.timeMd5HashHeader, timeHash)
		        .asObject(RawTip[].class);
		final RawTip[] tips = tipResponse.getBody();
		return tips;
	}

	private RawQuestion[] retrieveSelectables(final String timeHash) throws UnirestException {
		final HttpResponse<RawQuestion[]> questionsResponse =
				Unirest
				.get(this.questionsApiEndpoint)
				.header(this.userNameMd5HashHeader, this.userMd5)
		        .header(this.timeMd5HashHeader, timeHash)
				.asObject(RawQuestion[].class);
		final RawQuestion[] questions = questionsResponse.getBody();
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
