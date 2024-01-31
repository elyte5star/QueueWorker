package elyte.util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import elyte.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.sql.Connection;


@Setter
@Getter
@AllArgsConstructor
public class AppConfig {

    static SecureRandom rnd = new SecureRandom();

    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    private Properties properties;

    public  ObjectMapper mapper;

    public AppConfig() {
        this.mapper = new ObjectMapper();
    }

    public void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            log.error("DB Connection Exception: " + e.getCause());

        }
    }


    public String getConfigValue(String key) {
        String value = null;
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("config.yml");
            Properties prop = new Properties();
            prop.load(resource);
            value = prop.getProperty(key);
        } catch (FileNotFoundException e) {
            log.error("[+] Config Exception ", e.getCause());
        } catch (IOException e) {
            log.error("[+] Config Exception ", e.getCause());
        }
        return value;
    }


    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public String generateUuidString() {
        return UUID.randomUUID().toString();
    }

    public String timeNow() {
        LocalDateTime current = LocalDateTime.now();
        return current.format(dtf);
    }

    public long timeDiff(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return Math.abs(duration.toMillis());
    }

    public String convertObjectToJson(Object object) {
        String result = null;
        try {
            result =this.mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("[x] JsonProcessingException Exception ", e.getLocalizedMessage());

        }
        return result;
    }

    public Map<String, Object> objectToMap(Object object) {
        return this.mapper.convertValue(object,
                new TypeReference<Map<String, Object>>() {
                });

    }

    

    public long diff(String start, String end) {
        LocalDateTime dateTime1 = LocalDateTime.parse(start, dtf);
        LocalDateTime dateTime2 = LocalDateTime.parse(end, dtf);
        Duration duration = Duration.between(dateTime1, dateTime2);
        return Math.abs(duration.toMillis());
    }

    public Object entityToObject(Status taskStatus) {

        byte[] data = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(taskStatus);
            oos.flush();
            oos.close();
            baos.close();
            data = baos.toByteArray();
        } catch (IOException ex) {
            data = null;
            log.error("ERROR :" + ex.getLocalizedMessage());

        }

        return data;

    }

}
