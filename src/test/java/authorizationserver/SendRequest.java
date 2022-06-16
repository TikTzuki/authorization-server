package authorizationserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SendRequest {
    public static void test_call() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Integer> counter = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            executor.execute(new RequestCaller(counter));
        }
        executor.shutdown();
        boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(finished);
        System.out.println(counter);
        System.out.println(counter.stream().reduce(0, Integer::sum));
    }

    static class RequestCaller implements Runnable {
        List<Integer> counter;

        public RequestCaller(List<Integer> counter) {
            this.counter = counter;
        }

        @SneakyThrows
        @Override
        public void run() {
            ObjectMapper mapper = new ObjectMapper();

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(60, TimeUnit.SECONDS);
            client.setReadTimeout(60, TimeUnit.SECONDS);
            client.setWriteTimeout(60, TimeUnit.SECONDS);
            BufferedReader reader = new BufferedReader(new FileReader("src/test/java/authorizationserver/body.json"));
            String json = reader.lines().collect(Collectors.joining());

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    json);

            Request request = new Request.Builder()
                    .url("http://192.168.73.135:9001/api/v2/normal-loan/product-group?position=U1")
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6IjVkZWI1ZDMzN2M4YWU4NTU2NDcxN2RkZTY1ZjQ4NjE5MzBhZTVjNzUiLCJ1c2VyX2luZm8iOnsidXNlcl9pZCI6IjE3MjQyIiwidXNlcl9uYW1lIjoiR0lBTkdOVEg3IiwiZnVsbF9uYW1lIjoiTmd1eVx1MWVjNW4gVHJcdTFlYTduIEhcdTAxYjBcdTAxYTFuZyBHaWFuZyIsImF2YXRhciI6Ii9jZG4tcHJvZmlsZS90aHVtYi8xNzI0Ml9OZ3V5ZW4gVHJhbiBIdW9uZyBHaWFuZy5qcGciLCJicmFuY2giOnsiYnJhbmNoX2NvZGUiOiIwMDEiLCJicmFuY2hfbmFtZSI6IlNDQiBDXHUxZWQxbmcgUXVcdTFlZjNuaCIsImJyYW5jaF9hZGRyZXNzIjpudWxsLCJicmFuY2hfcGFyZW50X2NvZGUiOm51bGwsImJyYW5jaF90YXhfY29kZSI6bnVsbCwiYnJhbmNoX3Bob25lIjpudWxsLCJicmFuY2hfc3RhdHVzIjpudWxsLCJicmFuY2hfcmVnaW9uX2NvZGUiOm51bGwsImJyYW5jaF9yZWdpb25fbmFtZSI6bnVsbH0sImRlcGFydG1lbnQiOnsiaWQiOiIxMjYwMCIsImNvZGUiOiJBOCIsIm5hbWUiOiJCXHUxZWQ5IHBoXHUxZWFkbiBLVEdEIn0sImhybV90aXRsZV9pZCI6IjA0Mi0yODQiLCJocm1fdGl0bGVfY29kZSI6IjA0Mi0yODQiLCJocm1fdGl0bGVfbmFtZSI6IkdpYW8gZFx1MWVjYmNoIHZpXHUwMGVhbiBsXHUwMWIwdSBcdTAxMTFcdTFlZDluZyIsImhybV9wb3NpdGlvbl9pZCI6IjA0MiIsImhybV9wb3NpdGlvbl9jb2RlIjoiIiwiaHJtX3Bvc2l0aW9uX25hbWUiOiIifSwia2V5X3JlZGlzIjoiR0lBTkdOVEg3IiwiZXhwIjoxNjU1NDc2NTQ2fQ.TcDhbtOCiJjcUj0U6M-JoW31WcgWUbHff6RE0JszGvA")
                    .post(body)
                    .build(); // defaults to GET

            int startTime = (int) new Date().getTime() / 1000;
            Response response = client.newCall(request).execute();
            int endTime = (int) new Date().getTime() / 1000;
            counter.add(endTime - startTime);
            System.out.println(
                    response.body().string()
            );
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        test_call();
//        Path currentRelativePath = Paths.get("");
//        String s = currentRelativePath.toAbsolutePath().toString();
//        System.out.println("Current absolute path is: " + s);
    }
}
