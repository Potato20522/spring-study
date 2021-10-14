package extensions.com.potato.manifold.Person;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import com.potato.manifold.Person;

@Extension
public class PersonExtension {
  public static void helloWorld(@This Person thiz) {
    System.out.println("hello world!");

  }
}