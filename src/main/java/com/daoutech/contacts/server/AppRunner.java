package com.daoutech.contacts.server;

import com.daoutech.contacts.server.domain.CGroup;
import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.domain.User;
import com.daoutech.contacts.server.repository.CGroupRepository;
import com.daoutech.contacts.server.repository.UserRepository;
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

	private static final String QUERY = "INSERT INTO contacts (`name`, `tel`, `email`, `cgroup_id`) VALUES (?, ?, ?, ?)";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CGroupRepository cGroupRepository;

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
		List<Long> elapsedTimes = new ArrayList<>();
		List<Contact> dummy = new ArrayList<>();
		Stopwatch stopwatch = Stopwatch.createStarted();

		for (int j = 0; j < 100; j++) {
			User user = new User();
			userRepository.save(user);

			CGroup cGroup = new CGroup();
			cGroup.setUser(user);
			cGroupRepository.save(cGroup);

			stopwatch.reset().start();
			log.info("count: {}", j);
			// 100_000_000
			// 100_000 * 100
			for (int i = 0; i < 100_000; i++) {

				int rand = randomNo();
				Contact.ContactBuilder cb = Contact.builder().name("최경림" + rand);
				if (i % 2 == 0) {
					cb = cb.email("TEST" + rand + "@email.net");
				} else {
					cb = cb.tel(String.format("%s%04d%04d", firstNo(), randomNo(), randomNo()));
				}
				dummy.add(cb.cGroup(cGroup).build());
			}

			batchInsert(dummy);
			dummy.clear();
			stopwatch.stop();
			elapsedTimes.add(stopwatch.elapsed(TimeUnit.MINUTES));
		}


		log.info("total {}m", elapsedTimes.stream().mapToLong(t -> t).sum());
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
						ps.setInt(4, c.getCGroup().getId());
					}

					@Override
					public int getBatchSize() {
						return items.size();
					}
				});
	}
}

