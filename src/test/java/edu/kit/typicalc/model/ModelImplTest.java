package edu.kit.typicalc.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModelImplTest {

    @Test
    void getTypeInferer() {
        ModelImpl model = new ModelImpl();
        Map<String, String> map = new HashMap<>();
        map.put("x", "int");
        Map<String, String> map2 = new HashMap<>();
        map2.put("a.x", "3.int");
        assertTrue(model.getTypeInferer("test.x.x.test", map2).isError());
        assertTrue(model.getTypeInferer("x", map2).isError());
        assertTrue(model.getTypeInferer("x", map).isOk());
    }
}