package com.example.demo.config;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.demo.problem.NotFoundProblem;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {

    return (response.getStatusCode().series() == Series.CLIENT_ERROR
        || response.getStatusCode().series() == Series.SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    // handle CLIENT_ERROR
    if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
        && response.getStatusCode() == HttpStatus.NOT_FOUND) {
      // handle NOT_FOUND
      ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
      URI uri = builder.build().toUri();
      Path path = Paths.get(uri.getPath());
      // the entity id is the last name in the path
      String id = path.getName(path.getNameCount() - 1).toString();
      throw new NotFoundProblem(id, uri);
    }
  }
}