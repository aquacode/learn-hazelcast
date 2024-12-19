package com.aquacodesoftware.learn.hazelcast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;

@RestController
public class HzController {

    private final HazelcastInstance hazelcastInstance;

    @Autowired
    public HzController(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public String ping() {
        return "PONG";
    }

    @PutMapping(path = "/put/{map}")
    public String put(@PathVariable String map, @RequestBody KeyValue pair) {
        hazelcastInstance.getMap(map).put(pair.key(), pair.value());
        return "OK";
    }

    @GetMapping(path = "/get/{map}/{key}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String get(@PathVariable String map, @PathVariable String key) {
        return hazelcastInstance.getMap(map).get(key).toString();
    }

    @DeleteMapping(path = "/delete/map/{key}")
    public String delete(@PathVariable String key) {
        hazelcastInstance.getMap(key).clear();
        return "OK";
    }

    @GetMapping(path = "/hz")
    public String hz(@RequestParam String metricsSeconds) {
        hazelcastInstance.getConfig().setProperty("hazelcast.diagnostics.metrics.period.seconds", metricsSeconds);
        return "OK";
    }

}
