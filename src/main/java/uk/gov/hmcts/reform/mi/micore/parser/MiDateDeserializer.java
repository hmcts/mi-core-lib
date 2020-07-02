package uk.gov.hmcts.reform.mi.micore.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class MiDateDeserializer extends StdDeserializer<OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    public MiDateDeserializer() {
        super(OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isNumber()) {
            return OffsetDateTime.ofInstant(Instant.ofEpochMilli(node.asLong()), ZoneOffset.UTC);
        } else {
            return OffsetDateTime.parse(node.asText(),DateTimeFormatter.ISO_DATE_TIME);
        }
    }
}
