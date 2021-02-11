package edu.kit.typicalc.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelImplTest {

    @Test
    void getTypeInferer() {
        ModelImpl model = new ModelImpl();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("x", "int");
        Map<String, String> map2 = new LinkedHashMap<>();
        map2.put("a.x", "3.int");
        assertTrue(model.getTypeInferer("test.x.x.test", map2).isError());
        assertTrue(model.getTypeInferer("x", map2).isError());
        assertTrue(model.getTypeInferer("x", map).isOk());
    }
}