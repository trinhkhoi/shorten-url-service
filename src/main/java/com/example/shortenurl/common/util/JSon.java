package com.example.shortenurl.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

/**
 * JSon utilities.
 *
 * @author khoitd
 *
 */
public class JSon {

	/** {@link ObjectWriter} JSON_WRITER. */
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
			.registerModule(new AfterburnerModule())
			// serialize and deserialize JodaTime
			.registerModule(
					new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME))
							.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME))
							.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE_TIME))
							.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_DATE_TIME)))
			.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

	/** {@link ObjectWriter} JSON_WRITER. */
	private static final ObjectWriter JSON_WRITER = JSON_MAPPER.writer();


	/**
	 * deprecated protected constructor.
	 *
	 * @deprecated
	 */
	@Deprecated
	protected JSon() {
		super();
	}

	/**
	 *
	 * @param object
	 * @param <T>
	 * @return
	 * @throws JsonProcessingException
	 */
	public static <T> String toJson(final T object) throws JsonProcessingException {
		return JSON_WRITER.writeValueAsString(object);
	}

}
