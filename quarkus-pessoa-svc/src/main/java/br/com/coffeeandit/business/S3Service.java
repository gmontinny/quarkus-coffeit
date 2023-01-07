package br.com.coffeeandit.business;

import br.com.coffeeandit.model.InputData;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class S3Service {
    private final static String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @ConfigProperty(name = "aws.bucket.name")
    String bucketName;

    @ConfigProperty(name = "aws.access.key")
    String accessKey;

    @ConfigProperty(name = "aws.secret.key")
    String secretKey;

    public CompletableFuture<PutObjectResponse> putObject(InputData formData) {
        return s3AsyncClient().putObject(buildPutRequest(formData), AsyncRequestBody.fromFile(uploadToTemp(formData.data)));
    }

    public CompletableFuture<GetObjectResponse> getObject(String objectKey, File tempFile) {
       return s3AsyncClient().getObject(buildGetRequest(objectKey), AsyncResponseTransformer.toFile(tempFile));
    }

    public CompletableFuture<ListObjectsResponse> listObjects() {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        return s3AsyncClient().listObjects(listRequest);
    }

    protected PutObjectRequest buildPutRequest(InputData formData) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(formData.fileName)
                .contentType(formData.mimeType)
                .build();
    }

    protected GetObjectRequest buildGetRequest(String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

    public File tempFilePath() {
        return new File(TEMP_DIR, new StringBuilder().append("s3AsyncDownloadedTemp")
                .append((new Date()).getTime()).append(UUID.randomUUID())
                .append(".").append(".tmp").toString());
    }

    protected File uploadToTemp(InputStream data) {
        File tempPath;
        try {
            tempPath = File.createTempFile("uploadS3Tmp", ".tmp");
            Files.copy(data, tempPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return tempPath;
    }

    private S3AsyncClient s3AsyncClient() {
        AwsCredentialsProvider provider = new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
                return AwsBasicCredentials.create(accessKey, secretKey);
            }
        };
        return S3AsyncClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(provider).build();
    }

}