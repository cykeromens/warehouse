package com.cluster.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Cluster.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
public class ApplicationProperties {

	private final ApplicationProperties.Batch batch = new ApplicationProperties.Batch();

	public ApplicationProperties() {
	}

	public ApplicationProperties.Batch getBatch() {
		return this.batch;
	}

	public static class Batch {
		private final ApplicationProperties.Batch.Upload upload = new ApplicationProperties.Batch.Upload();

		public Batch() {
		}

		public ApplicationProperties.Batch.Upload getUpload() {
			return this.upload;
		}

		public static class Upload {
			private String dir = "./";
			private char delimiter = ';';
			private int batchSize = 1000;

			public Upload() {
			}

			public String getDir() {
				return dir;
			}

			public void setDir(String dir) {
				this.dir = dir;
			}

			public char getDelimiter() {
				return delimiter;
			}

			public void setDelimiter(char delimiter) {
				this.delimiter = delimiter;
			}

			public int getBatchSize() {
				return batchSize;
			}

			public void setBatchSize(int batchSize) {
				this.batchSize = batchSize;
			}
		}
	}
}
