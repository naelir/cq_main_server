package cq_server.game;

import java.io.IOException;

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
		Unirest.setObjectMapper(new ObjectMapper() {
			private final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

			@Override
			public <T> T readValue(final String value, final Class<T> valueType) {
				try {
					return this.jacksonObjectMapper.readValue(value, valueType);
				} catch (final IOException e) {
					LOG.error("{}", e);
					throw new RuntimeException(e);
				}
			}

			@Override
			public String writeValue(final Object value) {
				try {
					return this.jacksonObjectMapper.writeValueAsString(value);
				} catch (final JsonProcessingException e) {
					LOG.error("{}", e);
					throw new RuntimeException(e);
				}
			}
		});
		//@formatter:off
		try { 
			final long second = System.currentTimeMillis() / 1000;
			final String timeHash = DigestUtils.md5DigestAsHex(String.valueOf(second).getBytes());
			LOG.debug("current second: {}, current timehash: {}", second, timeHash); 
			final HttpResponse<RawQuestion[]> questionsResponse = 
					Unirest
					.get(this.questionsApiEndpoint)
					.header(this.userNameMd5HashHeader, this.userMd5)
			        .header(this.timeMd5HashHeader, timeHash)
					.asObject(RawQuestion[].class);
			final RawQuestion[] questions = questionsResponse.getBody();
			
			final HttpResponse<RawTip[]> tipResponse = 
					Unirest
					.get(this.tipsApiEndpoint)
					.header(this.userNameMd5HashHeader, this.userMd5)
			        .header(this.timeMd5HashHeader, timeHash)
			        .asObject(RawTip[].class);
			final RawTip[] tips = tipResponse.getBody();
			return new Questions(questions, tips);
		} catch (final UnirestException e) {
			LOG.error("{}", e);
		}
		//@formatter:on
		return new Questions(new RawQuestion[] {}, new RawTip[] {});
	}
}
