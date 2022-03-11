package com.viettel.etc.kafka.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConsumerModel {
	//	private String bootstrapServers;
	private String applicationId;
	private String groupId;
	private String autoCommitIntervalMs;
	private String maxPollIntervalMs;
	private String consumerPollTime;
	private String sessionTimeoutMs;
	private String autoOffsetReset;
	private String enableAutoCommit;
	private String missingTopicsFatal;
	private String maxPollRecords;
	private String keySerializer;
	private String valueSerializer;
	private String tableName;
	private String tableMapping;
	private String sizeBatch;
	private String url;
	private String driverClassName;
	private String usernameDb;
	private String passwordDb;
	private Boolean isUseCustomSql;
	private Boolean isUseSequence;
	//===================config for the producer==================
	private Boolean isPushBaseOnCurrentTopic;
	private String pushToTopic;
	private String batchSizeConfig;
	private String lingerMsConfig;
	private String clientIdConfig;
	//===================Input message mapping====================
	private String fileMappingPath;
	//image file path property
	private String prefixFilePath;

	@JsonCreator
	public ConsumerModel(@JsonProperty("bootstrapServers") String bootstrapServers,
						 @JsonProperty("groupId") String groupId,
						 @JsonProperty("autoCommitIntervalMs") String autoCommitIntervalMs,
						 @JsonProperty("maxPollIntervalMs") String maxPollIntervalMs,
						 @JsonProperty("consumerPollTime") String consumerPollTime,
						 @JsonProperty("sessionTimeoutMs") String sessionTimeoutMs,
						 @JsonProperty("autoOffsetReset") String autoOffsetReset,
						 @JsonProperty("enableAutoCommit") String enableAutoCommit,
						 @JsonProperty("maxPollRecords")String maxPollRecords,
						 @JsonProperty("keySerializer")String keySerializer,
						 @JsonProperty("valueSerializer")String valueSerializer,
						 @JsonProperty("tableName")String tableName,
						 @JsonProperty("topicExceptionOut")String topicExceptionOut,
						 @JsonProperty("applicationId") String applicationId,
						 @JsonProperty("sizeBatch")String sizeBatch,
						 @JsonProperty("url")String url,
						 @JsonProperty("driverClassName")String driverClassName,
						 @JsonProperty("usernameDb")String usernameDb,
						 @JsonProperty("passwordDb")String passwordDb,
						 @JsonProperty("isUseCustomSql")Boolean isUseCustomSql,
						 @JsonProperty("isUseSequence")Boolean isUseSequence,
						 @JsonProperty("isPushBaseOnCurrentTopic") Boolean isPushBaseOnCurrentTopic,
						 @JsonProperty("pushToTopic")String pushToTopic,
						 @JsonProperty("batchSizeConfig")String batchSizeConfig,
						 @JsonProperty("lingerMsConfig")String lingerMsConfig,
						 @JsonProperty("clientIdConfig")String clientIdConfig,
						 @JsonProperty("fileMappingPath")String fileMappingPath,
						 @JsonProperty("prefixFilePath") String prefixFilePath) {
		super();
//		this.bootstrapServers = bootstrapServers;
		this.groupId = groupId;
		this.autoCommitIntervalMs = autoCommitIntervalMs;
		this.maxPollIntervalMs=maxPollIntervalMs;
		this.consumerPollTime=consumerPollTime;
		this.sessionTimeoutMs = sessionTimeoutMs;
		this.autoOffsetReset = autoOffsetReset;
		this.enableAutoCommit = enableAutoCommit;
		this.maxPollRecords = maxPollRecords;
		this.keySerializer = keySerializer;
		this.valueSerializer = valueSerializer;
		this.tableName=tableName;
		this.tableMapping = topicExceptionOut;
		this.applicationId = applicationId;
		this.sizeBatch = sizeBatch;
		this.url = url;
		this.driverClassName = driverClassName;
		this.usernameDb = usernameDb;
		this.passwordDb = passwordDb;
		this.isUseCustomSql=isUseCustomSql;
		this.isUseSequence=isUseSequence;
		this.isPushBaseOnCurrentTopic=isPushBaseOnCurrentTopic;
		this.pushToTopic=pushToTopic;
		this.batchSizeConfig=batchSizeConfig;
		this.lingerMsConfig=lingerMsConfig;
		this.clientIdConfig=clientIdConfig;
		this.fileMappingPath=fileMappingPath;
		this.prefixFilePath=prefixFilePath;
	}
}
