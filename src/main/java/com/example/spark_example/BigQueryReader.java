package com.example.spark_example;

/**
 * Hello world!
 *
 */
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class BigQueryReader {
    public static void main(String[] args) {
        // Set your GCP project and table
        String projectId = "airflow-sample-project";
        String dataset = "testdb";
        String table = "employee-details";
        String serviceAccountKeyPath = "src/resources/serviceAccountKey.json";

        String kafkaBootstrapServers = "your-kafka-broker:9092";
        String kafkaTopic = "your-kafka-topic";
        
        System.out.println("Service account path :: "+serviceAccountKeyPath);
        
        
        SparkSession spark = SparkSession.builder()
                .appName("BigQueryToKafka")
                .master("local[*]") // For local testing
                .config("spark.sql.catalogImplementation", "in-memory")
                .config("temporaryGcsBucket", "your_temp_gcs_bucket") // must exist
                .config("spark.hadoop.google.cloud.auth.service.account.enable", "true")
                .config("spark.hadoop.google.cloud.auth.service.account.json.keyfile", serviceAccountKeyPath)
                .config("spark.hadoop.hadoop.security.group.mapping", "org.apache.hadoop.security.LdapGroupsMapping")
                .getOrCreate();
        System.out.println("After Spark Session:: ");
        Dataset<Row> bqData = spark.read()
                .format("bigquery")
                .option("project", projectId)
                .option("dataset", dataset)
                .option("table", table)
                .load();
        System.out.println("After Data set read:: ");
        bqData.show(); // Display result
        System.out.println(" bqData.columns :: "+ bqData.columns()); 

        spark.stop();
    }
}

