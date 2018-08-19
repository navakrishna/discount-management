package com.discount.mgt.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Kieconfig configures kiesession bean to Spring application context.
 * 
 * 
 * @author thotanav
 * @see Configuration
 * 
 * 
 *
 */
@Configuration
public class KieConfig {

	@Value("${rule.groupId}")
	private String groupId;
	@Value("${rule.artifactId}")
	private String artifactId;
	@Value("${rule.version}")
	private String version;

	/**
	 * Responsibility of kieSession method is to add Kiesession object to
	 * application context.
	 * 
	 * Kieservices API intiates knowledge services. Release Id, KieContainer and
	 * KieScanner are created from Kieservices. Release Id expects maven components
	 * like group Id, artifact Id and version of a business rules jar (eg:
	 * groupId=com.discount, artifactId=rules and version=1.0).
	 * 
	 * Kieservices provides multiple instance methods to get kieContainer, for eg:
	 * 
	 * {@code kieServices.newClassPathKieContainer()} creates from rules based on
	 * classpath of current project 
	 * {@code kieServices.newKieContainer(releaseId)} creates from maven artifactory.
	 * 
	 * KieContainer provide multiple instance methods to get kieSession, for eg:
	 * 
	 * {@code kieServices.newKieSession()} creates default one.
	 * {@code kieServices.newKiession("rulesSession")} creates from kie base passsed to it.
	 * 
	 * KieScanner scans the maven repository for new updates in regular intervals of time.
	 * 
	 * 
	 * 
	 * @return
	 */
	@Bean
	public KieSession kieSession() {
		KieServices kieServices = KieServices.Factory.get();
		ReleaseId releaseId = kieServices.newReleaseId(groupId, artifactId, version);

		KieContainer kieContainer = kieServices.newKieContainer(releaseId);
		KieSession kSession = kieContainer.newKieSession("rulesSession");

		KieScanner kieScanner = kieServices.newKieScanner(kieContainer);
		kieScanner.start(30000L);

		return kSession;
	}
}
