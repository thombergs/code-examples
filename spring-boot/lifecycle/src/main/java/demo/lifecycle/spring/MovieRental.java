package demo.lifecycle.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieRental {

    @Value("MovieRentalUser1")
    private String name;
    @Value("c:/MovieRental")
    private String filePath;
    private BufferedWriter bufferedWriter;

    @PostConstruct
    public void openMovieRentalFile() throws IOException {
        System.out.println("openMovieRentalFile");
        File file = new File(filePath, name + ".txt");
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
    }

    public void movieCheckout(List<String> movieList) throws IOException {
        bufferedWriter.write(movieList.stream().collect(Collectors.joining(",")) + " , " + LocalDateTime.now());
    }

    @PreDestroy
    public void closeMovieRentalFile() throws IOException {
        bufferedWriter.close();
        System.out.println("closeMovieRentalFile");
    }

}
