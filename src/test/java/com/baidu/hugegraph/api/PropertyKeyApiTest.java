/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.baidu.hugegraph.api;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.baidu.hugegraph.structure.constant.AggregateType;
import com.baidu.hugegraph.structure.constant.Cardinality;
import com.baidu.hugegraph.structure.constant.DataType;
import com.baidu.hugegraph.structure.schema.PropertyKey;
import com.baidu.hugegraph.testutil.Assert;
import com.baidu.hugegraph.testutil.Utils;
import com.google.common.collect.ImmutableList;

public class PropertyKeyApiTest extends BaseApiTest {

    @After
    public void teardown() throws Exception {
        propertyKeyAPI.list().forEach(pk -> propertyKeyAPI.delete(pk.name()));
    }

    @Test
    public void testCreate() {
        PropertyKey propertyKey = schema().propertyKey("name")
                                          .asText()
                                          .valueSingle()
                                          .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("name", propertyKey.name());
        Assert.assertEquals(DataType.TEXT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
    }

    @Test
    public void testCreateWithDataType() {
        PropertyKey propertyKey = schema().propertyKey("name")
                                          .dataType(DataType.LONG)
                                          .valueSingle()
                                          .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("name", propertyKey.name());
        Assert.assertEquals(DataType.LONG, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
    }

    @Test
    public void testCreateWithCardinality() {
        PropertyKey propertyKey = schema().propertyKey("name")
                                          .asText()
                                          .cardinality(Cardinality.SET)
                                          .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("name", propertyKey.name());
        Assert.assertEquals(DataType.TEXT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SET, propertyKey.cardinality());
    }

    @Test
    public void testCreateWithAggregateType() {
        PropertyKey propertyKey = schema().propertyKey("name")
                                          .asText().valueSingle()
                                          .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("name", propertyKey.name());
        Assert.assertEquals(DataType.TEXT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
        Assert.assertEquals(AggregateType.NONE, propertyKey.aggregateType());
        Assert.assertTrue(propertyKey.aggregateType().isNone());
        Assert.assertFalse(propertyKey.aggregateType().isNumber());
        Assert.assertTrue(propertyKey.aggregateType().isIndexable());

        propertyKey = schema().propertyKey("no")
                              .asText().valueSingle()
                              .calcOld()
                              .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("no", propertyKey.name());
        Assert.assertEquals(DataType.TEXT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
        Assert.assertEquals(AggregateType.OLD, propertyKey.aggregateType());
        Assert.assertTrue(propertyKey.aggregateType().isOld());
        Assert.assertTrue(propertyKey.aggregateType().isIndexable());
        Assert.assertFalse(propertyKey.aggregateType().isNumber());

        propertyKey = schema().propertyKey("max")
                              .asInt().valueSingle()
                              .calcMax()
                              .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("max", propertyKey.name());
        Assert.assertEquals(DataType.INT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
        Assert.assertEquals(AggregateType.MAX, propertyKey.aggregateType());
        Assert.assertTrue(propertyKey.aggregateType().isMax());
        Assert.assertTrue(propertyKey.aggregateType().isIndexable());
        Assert.assertTrue(propertyKey.aggregateType().isNumber());

        propertyKey = schema().propertyKey("min")
                              .asInt().valueSingle()
                              .calcMin()
                              .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("min", propertyKey.name());
        Assert.assertEquals(DataType.INT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
        Assert.assertEquals(AggregateType.MIN, propertyKey.aggregateType());
        Assert.assertTrue(propertyKey.aggregateType().isMin());
        Assert.assertTrue(propertyKey.aggregateType().isIndexable());
        Assert.assertTrue(propertyKey.aggregateType().isNumber());

        propertyKey = schema().propertyKey("sum")
                              .asInt().valueSingle()
                              .calcSum()
                              .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("sum", propertyKey.name());
        Assert.assertEquals(DataType.INT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
        Assert.assertEquals(AggregateType.SUM, propertyKey.aggregateType());
        Assert.assertTrue(propertyKey.aggregateType().isSum());
        Assert.assertFalse(propertyKey.aggregateType().isIndexable());
        Assert.assertTrue(propertyKey.aggregateType().isNumber());

        propertyKey = schema().propertyKey("total")
                              .asInt().valueSingle()
                              .aggregateType(AggregateType.SUM)
                              .build();

        propertyKey = propertyKeyAPI.create(propertyKey);

        Assert.assertEquals("total", propertyKey.name());
        Assert.assertEquals(DataType.INT, propertyKey.dataType());
        Assert.assertEquals(Cardinality.SINGLE, propertyKey.cardinality());
        Assert.assertEquals(AggregateType.SUM, propertyKey.aggregateType());
        Assert.assertTrue(propertyKey.aggregateType().isSum());
        Assert.assertFalse(propertyKey.aggregateType().isIndexable());
        Assert.assertTrue(propertyKey.aggregateType().isNumber());

        propertyKey = schema().propertyKey("nameV46")
                              .asText().valueSingle()
                              .build();
        PropertyKey.PropertyKeyV46 pk = propertyKey.switchV46();
        Assert.assertEquals("nameV46", pk.name());
        Assert.assertEquals(DataType.TEXT, pk.dataType());
        Assert.assertEquals(Cardinality.SINGLE, pk.cardinality());
    }

    @Test
    public void testCreateWithInvalidName() {
        Utils.assertResponseError(400, () -> {
            propertyKeyAPI.create(new PropertyKey(""));
        });
        Utils.assertResponseError(400, () -> {
            propertyKeyAPI.create(new PropertyKey(" "));
        });
        Utils.assertResponseError(400, () -> {
            propertyKeyAPI.create(new PropertyKey("    "));
        });
    }

    @Test
    public void testCreateExistedPropertyKey() {
        PropertyKey propertyKey = new PropertyKey("name");
        propertyKeyAPI.create(propertyKey);

        Utils.assertResponseError(400, () -> {
            propertyKeyAPI.create(new PropertyKey("name"));
        });
    }

    @Test
    public void testGet() {
        PropertyKey propertyKey1 = schema().propertyKey("name")
                                           .asText()
                                           .valueSingle()
                                           .build();

        propertyKey1 = propertyKeyAPI.create(propertyKey1);

        PropertyKey propertyKey2 = propertyKeyAPI.get("name");

        Assert.assertEquals(propertyKey1.name(), propertyKey2.name());
        Assert.assertEquals(propertyKey1.dataType(), propertyKey2.dataType());
        Assert.assertEquals(propertyKey1.cardinality(),
                            propertyKey2.cardinality());
    }

    @Test
    public void testGetNotExist() {
        Utils.assertResponseError(404, () -> {
            propertyKeyAPI.get("not-exist-pk");
        });
    }

    @Test
    public void testList() {
        PropertyKey propertyKey1 = schema().propertyKey("name")
                                           .asText()
                                           .valueSingle()
                                           .build();
        propertyKey1 = propertyKeyAPI.create(propertyKey1);

        PropertyKey propertyKey2 = schema().propertyKey("age")
                                           .asInt()
                                           .valueSingle()
                                           .build();
        propertyKey2 = propertyKeyAPI.create(propertyKey2);

        List<PropertyKey> propertyKeys = propertyKeyAPI.list();
        Assert.assertEquals(2, propertyKeys.size());
        assertContains(propertyKeys, propertyKey1);
        assertContains(propertyKeys, propertyKey2);
    }

    @Test
    public void testListByNames() {
        PropertyKey name = schema().propertyKey("name").asText().build();
        name = propertyKeyAPI.create(name);

        PropertyKey age = schema().propertyKey("age").asInt().build();
        age = propertyKeyAPI.create(age);

        List<PropertyKey> propertyKeys;

        propertyKeys = propertyKeyAPI.list(ImmutableList.of("name"));
        Assert.assertEquals(1, propertyKeys.size());
        assertContains(propertyKeys, name);

        propertyKeys = propertyKeyAPI.list(ImmutableList.of("age"));
        Assert.assertEquals(1, propertyKeys.size());
        assertContains(propertyKeys, age);

        propertyKeys = propertyKeyAPI.list(ImmutableList.of("name", "age"));
        Assert.assertEquals(2, propertyKeys.size());
        assertContains(propertyKeys, name);
        assertContains(propertyKeys, age);
    }

    @Test
    public void testDelete() {
        PropertyKey propertyKey = schema().propertyKey("name")
                                          .asText()
                                          .valueSingle()
                                          .build();
        propertyKeyAPI.create(propertyKey);
        propertyKeyAPI.delete("name");

        Utils.assertResponseError(404, () -> {
            propertyKeyAPI.get("name");
        });
    }

    @Test
    public void testDeleteNotExist() {
        Utils.assertResponseError(404, () -> {
            propertyKeyAPI.delete("not-exist-pk");
        });
    }

    @Test
    public void testAddPropertyKeyWithUserData() {
        PropertyKey age = schema().propertyKey("age")
                                  .userdata("min", 0)
                                  .userdata("max", 100)
                                  .build();
        propertyKeyAPI.create(age);
        Assert.assertEquals(2, age.userdata().size());
        Assert.assertEquals(0, age.userdata().get("min"));
        Assert.assertEquals(100, age.userdata().get("max"));

        PropertyKey id = schema().propertyKey("id")
                                 .userdata("length", 15)
                                 .userdata("length", 18)
                                 .build();
        propertyKeyAPI.create(id);
        // The same key user data will be overwritten
        Assert.assertEquals(1, id.userdata().size());
        Assert.assertEquals(18, id.userdata().get("length"));

        PropertyKey sex = schema().propertyKey("sex")
                                  .userdata("range",
                                            ImmutableList.of("male", "female"))
                                  .build();
        propertyKeyAPI.create(sex);
        Assert.assertEquals(1, sex.userdata().size());
        Assert.assertEquals(ImmutableList.of("male", "female"),
                            sex.userdata().get("range"));
    }
}
