package leandromangieri.portfolio.authentication.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CognitoServiceConfig {

    @Value("${application.accessKey}")
    private String accessKey;

    @Value("${application.secretKey}")
    private String secretKey;

    // TODO: Regions está como campo estático :(

    @Bean
    public AWSCognitoIdentityProvider createAWSCognitoIdentityProvider() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(Regions.US_EAST_2)
                .build();
    }
}
