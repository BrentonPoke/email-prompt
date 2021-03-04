import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import htmlflow.HtmlView;
import htmlflow.StaticHtml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import models.ChildItem;
import models.Document;
import org.xmlet.htmlapifaster.Article;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Ol;

public class Main {
  public static void main(String[] args) {
    PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
    ObjectMapper mapper = new ObjectMapper();

    File file = new File("/home/brentonpoke/IdeaProjects/prompt/src/main/resources/email.json");
    Scanner sc = null;
    try {
      sc = new Scanner(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    StringBuffer string = new StringBuffer();
    while (sc.hasNextLine()) string.append(sc.nextLine());
    try {
      Document document = mapper.readValue(string.toString(), Document.class);

      normalize(document, mapper);
      System.out.println(render(document));
    } catch (IOException e) {
      e.printStackTrace();
    }
    //
  }

  private static String normalize(Document doc, ObjectMapper mapper) {
  
    ArrayList<ChildItem> children = doc.children;
    for (ChildItem i : children) {
      children.removeIf(a -> a.type.equals("list") && a.children.isEmpty());
    }
    for (int i = 0; i < children.size()-1; i++) {
      if (children.get(i).type.equals("blockquote")
          && children.get(i + 1).type.equals("blockquote")) {
        children.get(i).children.add(children.get(i + 1));
        children.remove(i + 1);
      }
    }
    
    for (int i = 0; i < children.size()-1; i++){
      if (children.get(i).type.equals("list")
          && children.get(i+1).type.equals("list")) {
        children.get(i).children.addAll(children.get(i + 1).children);
        children.remove(i + 1);
      }
    }
    
    for (int i = 0; i < children.size(); i++) {
      if(children.get(i).type.equals("link") && children.get(i).content.isEmpty()){
        children.removeIf(a -> a.type.equals("link") && a.content.isEmpty());
      }
    }

    String ans = "";
    try {
      ans = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(doc);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ans;
  }
  
  private static String render (Document doc){
    
    Article<Div<HtmlView<Object>>> email = StaticHtml.view().div().article().attrClass("email");
    StringBuilder scratchPad = new StringBuilder();
    
    for(ChildItem i: doc.children){

      if (i.type.equals("paragraph")) {
        // this is terrible
        for (ChildItem j : i.children) {
          // email.p().text(j.children).getParent().__();
          scratchPad.append(j.content);
        }
        email.p().text(scratchPad.toString()).__();
        }
      
    }
  
    scratchPad.delete(0,scratchPad.length());
    Ol<Article<Div<HtmlView<Object>>>> ol = email.ol();
    for (ChildItem i : doc.children) {
      if (i.type.equals("list")) {

        // this is terrible
        for (ChildItem j : i.children) {
          scratchPad.append(j.children.get(0).content);
          ol.li().text(scratchPad.toString()).__();
          scratchPad.delete(0,scratchPad.length()); //clear out buffer to take next list item
        }
      }
    }
    
      
      //email.__();
    
    
    
    
    return email.getParent().__().render();
  }
}
