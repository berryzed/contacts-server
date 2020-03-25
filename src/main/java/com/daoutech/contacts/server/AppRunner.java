package com.daoutech.contacts.server;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AppRunner implements ApplicationRunner {

	private static final String QUERY = "INSERT INTO contacts (`name`, `tel`, `email`, `user_id`) VALUES (?, ?, ?, ?)";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void run(ApplicationArguments args) {
//		initData();
	}

	public static int randomNo() {
		return ThreadLocalRandom.current().nextInt(10000);
	}

	public static String firstNo() {
		List<String> first = Arrays.asList("011", "016", "019", "018", "017");
		Collections.shuffle(first);
		return first.get(0);
	}

	/**
	 * innodb_buffer_pool_size = 1G
	 * bulk_insert_buffer_size = 256MB
	 */
	@SuppressWarnings("UnstableApiUsage")
	@Transactional
	protected void initData() {
		User user = new User();
		userRepository.save(user);

		List<Long> elapsedTimes = new ArrayList<>();
		List<Contact> dummy = new ArrayList<>();
		Stopwatch stopwatch = Stopwatch.createStarted();

		for (int j = 0; j < 200; j++) {
			stopwatch.reset().start();
			log.info("count: {}", j);
			// 100_000_000
			// 500_000 * 200
			for (int i = 0; i < 50_000; i++) {
				int num = ThreadLocalRandom.current().nextInt(10000);
				Contact c;
				if (num % 2 == 0) {
					c = Contact.builder().name("NAME" + num).email("TEST" + num + "@email.net").user(user).build();
				} else {
					c = Contact.builder().name("NAME" + num).tel(String.format("%s%04d%04d", firstNo(), randomNo(), randomNo())).user(user).build();
				}
				dummy.add(c);
			}

			batchInsert(dummy);
			dummy.clear();
			stopwatch.stop();
			elapsedTimes.add(stopwatch.elapsed(TimeUnit.MILLISECONDS));
		}


		log.info("total {}ms", elapsedTimes.stream().mapToLong(t -> t).sum());
		log.info("avg {}ms", elapsedTimes.stream().mapToLong(t -> t).average().orElse(0.0));
	}

	/**
	 * GeneratedValue의 전략이 IDENTITY인 경우 hibernate의 batch insert가 동작하지 않는다.
	 * 아래와 같이 JDBC 수준에서 처리해야 한다.
	 */
	private void batchInsert(List<Contact> items) {

		jdbcTemplate.batchUpdate(QUERY,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
						Contact c = items.get(i);
						ps.setString(1, c.getName());
						ps.setString(2, c.getTel());
						ps.setString(3, c.getEmail());
						ps.setInt(4, c.getUser().getId());
					}

					@Override
					public int getBatchSize() {
						return items.size();
					}
				});
	}
}

