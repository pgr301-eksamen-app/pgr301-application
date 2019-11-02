package no.kristiania.pgr301.exam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

  @Test
  public void contextLoads() {}
}
