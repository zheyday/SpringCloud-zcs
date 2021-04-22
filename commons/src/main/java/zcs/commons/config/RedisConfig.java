package zcs.commons.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Configuration
//@EnableCaching//开启缓存
public class RedisConfig extends CachingConfigurationSelector {
//    @Resource
//    private RedisClusterConfigProperties clusterProperties;
//
//    @Bean
//    public RedisClusterConfiguration getClusterConfig(){
//        RedisClusterConfiguration rcc = new RedisClusterConfiguration(clusterProperties.getNodes());
////        rcc.setPassword(clusterProperties.getPassword());
//        rcc.setMaxRedirects(clusterProperties.getMaxRedirects());
//        return rcc;
//    }
//
//    @Bean
//    public JedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration){
//        return new JedisConnectionFactory(redisClusterConfiguration);
//    }
//
//    @Bean
//    public JedisCluster getJedisCluster() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        // 截取集群节点
//        String[] cluster = clusterProperties.getNodes().toArray(new String[0]);
//        // 创建set集合
//        Set<HostAndPort> nodes = new HashSet<>();
//        // 循环数组把集群节点添加到set集合中
//        for (String node : cluster) {
//            String[] host = node.split(":");
//            //添加集群节点
//            nodes.add(new HostAndPort(host[0], Integer.parseInt(host[1])));
//        }
//        return new JedisCluster(nodes, clusterProperties.getConnectionTimeout(), clusterProperties.getSoTimeout(), clusterProperties.getMaxRedirects(),poolConfig);
//    }

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(redisConnectionFactory).build();
//    }


    @Bean
//    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(serializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
}

