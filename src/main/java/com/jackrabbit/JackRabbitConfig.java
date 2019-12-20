package com.jackrabbit;

import javax.jcr.Repository;
import javax.sql.DataSource;

import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBDataSourceFactory;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBDocumentNodeStoreBuilder;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JackRabbitConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(JackRabbitConfig.class);
	@Bean
	public Repository getRepository(
			@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.driver-class-name}") String drivername) {
		
		DataSource dataSource = RDBDataSourceFactory.forJdbcUrl(url, username, "", drivername);
		RDBOptions options = new RDBOptions().tablePrefix("JACK_RABBIT").dropTablesOnClose(false);
		
		
		DocumentNodeStore store = RDBDocumentNodeStoreBuilder.newRDBDocumentNodeStoreBuilder()
		    .setRDBConnection(dataSource, options).build();
		
		Repository repository = new Jcr(new Oak(store)).createRepository();

		LOGGER.info("Created Repo->>>>>>>>>>>---");
		return repository;
	}
}
