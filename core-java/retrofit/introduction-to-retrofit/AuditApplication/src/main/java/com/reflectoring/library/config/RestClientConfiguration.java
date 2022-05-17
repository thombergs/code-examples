package com.reflectoring.library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.library.client.LibraryClient;
import com.reflectoring.library.interceptor.BasicAuthInterceptor;
import com.reflectoring.library.interceptor.CacheInterceptor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(ClientConfigProperties.class)
public class RestClientConfiguration {

    @Bean
    public LibraryClient libraryClient(ClientConfigProperties props) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Cache cache = new Cache(new File("cache"), 10 * 1024 * 1024);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(props.getUsername(), props.getPassword()))
                .cache(cache)
                .addNetworkInterceptor(new CacheInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(props.getConnectionTimeout(), TimeUnit.SECONDS)
                .readTimeout(props.getReadWriteTimeout(), TimeUnit.SECONDS);

        return new Retrofit.Builder().client(httpClientBuilder.build())
                .baseUrl(props.getEndpoint())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build().create(LibraryClient.class);

    }

}
