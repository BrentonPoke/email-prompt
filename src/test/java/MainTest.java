import static net.javacrumbs.jsonunit.assertj.JsonAssert.assertThatJson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import models.Document;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
  ObjectMapper mapper;
  Document document, document1;
  StringBuffer stringBuffer, string;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();

    File file = new File("src/main/resources/emptylist.json");
    File file2 = new File("src/main/resources/email.json");
    Scanner sc = null, sc1 = null;
    try {
      sc = new Scanner(file);
      sc1 = new Scanner(file2);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    string = new StringBuffer();
    stringBuffer = new StringBuffer();
    while (sc.hasNextLine()) string.append(sc.nextLine());
    while (sc1.hasNextLine()) stringBuffer.append(sc1.nextLine());
    // System.out.println(string.toString());

    try {
      document = mapper.readValue(string.toString(), Document.class);
      document1 = mapper.readValue(stringBuffer.toString(), Document.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void normalize() {
    File file = new File("src/test/resources/emptylisttest.json");
    Scanner sc = null;
    try {
      sc = new Scanner(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    StringBuffer testString = new StringBuffer();
    while (sc.hasNextLine()) testString.append(sc.nextLine());
    assertThatJson(Main.normalize(document, mapper)).isEqualTo(testString.toString());
  }

  @Test
  void render() {
  
  }
}
