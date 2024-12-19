package com.aquacodesoftware.learn.hazelcast;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.RestEndpointGroup;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.hazelcast.spring.context.SpringManagedContext;

@Configuration
@EnableCaching
public class HzSetup {
  @Bean
  public CacheManager cacheManager(final HazelcastInstance hazelcastInstance) {
    return new HazelcastCacheManager(hazelcastInstance);
  }

  @Bean
  public SpringManagedContext managedContext() {
    return new SpringManagedContext();
  }

  @Bean
  public Config hazelCastConfig() {
    final Config config = new Config();
    config.setManagedContext(managedContext());
    config.setProperty("hazelcast.diagnostics.enabled", "true");
    config.setProperty("hazelcast.diagnostics.stdout", "STDOUT");
    config.setProperty("hazelcast.shutdownhook.enabled", "false"); // spring's context will shutdown
    config.setProperty("hazelcast.diagnostics.metric.level", "off");
    config.getCPSubsystemConfig().setSessionTimeToLiveSeconds(30); // free node's session after 30 secs
    config.setClusterName("hazelcast-mcb");
    config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true).addMember("192.168.64.1");
    config.getNetworkConfig().getRestApiConfig().setEnabled(true).setEnabledGroups(List.of(RestEndpointGroup.HEALTH_CHECK));
    return config;
  }

}
