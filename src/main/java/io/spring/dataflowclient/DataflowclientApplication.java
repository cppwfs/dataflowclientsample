package io.spring.dataflowclient;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.dataflow.rest.client.DataFlowOperations;
import org.springframework.cloud.dataflow.rest.client.DataFlowTemplate;
import org.springframework.cloud.dataflow.rest.client.TaskOperations;
import org.springframework.cloud.dataflow.rest.client.dsl.DeploymentPropertiesBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataflowclientApplication {

	@Autowired
	private DataFlowOperations dataFlowOperations;

	public static void main(String[] args) {
		SpringApplication.run(DataflowclientApplication.class, args);
	}


	@Bean
	ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			@Override
			public void run(ApplicationArguments args) throws Exception {
				Map<String, String> taskLaunchProperties = new DeploymentPropertiesBuilder()
						.put("deployer.mytask.kubernetes.jobAnnotations", "validated.keys:key1,key2").build();
				TaskOperations taskOperations = dataFlowOperations.taskOperations();
				if(args.getSourceArgs().length != 1) {
					throw new IllegalStateException("Must specify one task definition name.");
				}
				taskOperations.launch(args.getSourceArgs()[0], taskLaunchProperties, Collections.emptyList());				
			}
		};
	}
}
