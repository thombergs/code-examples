package com.reflectoring.library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.library.client.LibraryClient;
import com.reflectoring.library.interceptor.BasicAuthInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(ClientConfigProperties.class)
public class RestClientConfiguration {

    @Bean
    public LibraryClient libraryClient(ClientConfigProperties props) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(props.getUsername(), props.getPassword()))
                .connectTimeout(props.getConnectionTimeout(), TimeUnit.SECONDS)
                .readTimeout(props.getReadWriteTimeout(), TimeUnit.SECONDS);

        return new Retrofit.Builder().client(httpClientBuilder.build())
                .baseUrl(props.getEndpoint())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build().create(LibraryClient.class);

    }

}
