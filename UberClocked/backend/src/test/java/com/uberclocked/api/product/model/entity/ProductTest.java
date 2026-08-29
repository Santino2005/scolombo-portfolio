package com.uberclocked.api.product.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.uberclocked.api.component.model.entity.Component;
import com.uberclocked.api.component.model.entity.field.FieldType;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ProductTest {

  @Test
  void initializeAttributesFromComponent_withProvidedAttributes() {
    Component component = new Component("GPU", "Graphics Card");
    component.addField("chipset", FieldType.STRING, true, null);
    component.addField("vram", FieldType.STRING, true, "8GB");

    Product product = new Product("GPU1", "RTX 3070", component, 500.0, 10);
    product.initializeAttributesFromComponent(Map.of("chipset", "GA104"));

    assertEquals("GA104", product.getAttributes().get("chipset"));
    assertEquals("8GB", product.getAttributes().get("vram"));
  }

  @Test
  void initializeAttributesFromComponent_missingRequiredWithoutDefault_throwsException() {
    Component component = new Component("GPU", "Graphics Card");
    component.addField("chipset", FieldType.STRING, true, null);

    Product product = new Product("GPU1", "RTX 3070", component, 500.0, 10);

    assertThrows(
        IllegalArgumentException.class, () -> product.initializeAttributesFromComponent(Map.of()));
  }

  @Test
  void updateAttribute_validField_updatesValue() {
    Component component = new Component("GPU", "Graphics Card");
    component.addField("chipset", FieldType.STRING, true, null);

    Product product = new Product("GPU1", "RTX 3070", component, 500.0, 10);
    product.initializeAttributesFromComponent(Map.of("chipset", "GA104"));

    product.updateAttribute("chipset", "GA104-300");

    assertEquals("GA104-300", product.getAttributes().get("chipset"));
  }

  @Test
  void updateAttribute_invalidField_throwsException() {
    Component component = new Component("GPU", "Graphics Card");
    Product product = new Product("GPU1", "RTX 3070", component, 500.0, 10);

    assertThrows(
        IllegalArgumentException.class, () -> product.updateAttribute("unknownField", "value"));
  }

  @Test
  void setImage_tooLarge_throwsException() {
    Product product = new Product();
    byte[] largeImage = new byte[401 * 1024];

    assertThrows(IllegalArgumentException.class, () -> product.setImage(largeImage));
  }

  @Test
  void clearAttributes_clearsMap() {
    Component component = new Component("GPU", "Graphics Card");
    component.addField("chipset", FieldType.STRING, true, "GA104");

    Product product = new Product("GPU1", "RTX 3070", component, 500.0, 10);
    product.initializeAttributesFromComponent(Map.of());

    assertEquals(1, product.getAttributes().size());
    product.clearAttributes();
    assertEquals(0, product.getAttributes().size());
  }
}
