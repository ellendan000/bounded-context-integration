package top.bujiaban.rpcsub.common;

import org.redisson.codec.JsonJacksonCodec;

public class CustomizeJsonJacksonCodec extends JsonJacksonCodec {
    public CustomizeJsonJacksonCodec() {
        super();
        this.getObjectMapper().findAndRegisterModules();
    }
}
