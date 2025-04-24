/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@JsonSerialize(using = KeycloakEventResponse.KeycloakEventResponseSerializer.class)
@JsonDeserialize(using = KeycloakEventResponse.KeycloakEventResponseDeserializer.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class KeycloakEventResponse extends ApiResource implements Iterable<KeycloakEvent> {
    private static final long serialVersionUID = 2L;

    private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED;

    /** Connectivity to the Atlan tenant where the search was run. */
    @Setter
    @JsonIgnore
    AtlanClient client;

    /** Request that produced this response. */
    @Setter
    @JsonIgnore
    KeycloakEventRequest request;

    /** Admin events returned by the request. */
    List<KeycloakEvent> events;

    private KeycloakEventResponse(List<KeycloakEvent> events) {
        this.events = events;
    }

    /**
     * Retrieve the next page of results from this response.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    public KeycloakEventResponse getNextPage() throws AtlanException {
        int from = request.getOffset() < 0 ? 0 : request.getOffset();
        int page = request.getSize() < 0 ? 100 : request.getSize();
        KeycloakEventRequest next = request.toBuilder().offset(from + page).build();
        return next.search(client);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<KeycloakEvent> iterator() {
        return new EventResponseIterator(this);
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<KeycloakEvent> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), CHARACTERISTICS), false);
    }

    /**
     * Allow results to be iterated through without managing paging retrievals.
     */
    private static class EventResponseIterator implements Iterator<KeycloakEvent> {

        private KeycloakEventResponse response;
        private int i;

        public EventResponseIterator(KeycloakEventResponse response) {
            this.response = response;
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getEvents() != null && response.getEvents().size() > i) {
                return true;
            } else {
                try {
                    response = response.getNextPage();
                    i = 0;
                    return response.getEvents() != null && response.getEvents().size() > i;
                } catch (AtlanException e) {
                    throw new RuntimeException("Unable to iterate through all pages of search results.", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public KeycloakEvent next() {
            return response.getEvents().get(i++);
        }
    }

    public static final class KeycloakEventResponseDeserializer extends StdDeserializer<KeycloakEventResponse> {
        private static final long serialVersionUID = 2L;

        public KeycloakEventResponseDeserializer() {
            this(null);
        }

        public KeycloakEventResponseDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public KeycloakEventResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            List<KeycloakEvent> events = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new KeycloakEventResponse(events);
        }
    }

    public static final class KeycloakEventResponseSerializer extends StdSerializer<KeycloakEventResponse> {
        private static final long serialVersionUID = 2L;

        public KeycloakEventResponseSerializer() {
            this(null);
        }

        public KeycloakEventResponseSerializer(Class<KeycloakEventResponse> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(KeycloakEventResponse response, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            if (response != null) {
                gen.writeStartArray();
                for (KeycloakEvent event : response.getEvents()) {
                    gen.writeObject(event);
                }
                gen.writeEndArray();
            }
        }
    }
}
