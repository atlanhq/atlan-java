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
@JsonSerialize(using = AdminEventResponse.AdminEventResponseSerializer.class)
@JsonDeserialize(using = AdminEventResponse.AdminEventResponseDeserializer.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AdminEventResponse extends ApiResource implements Iterable<AdminEvent> {
    private static final long serialVersionUID = 2L;

    private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED;

    /** Connectivity to the Atlan tenant where the search was run. */
    @Setter
    @JsonIgnore
    AtlanClient client;

    /** Request that produced this response. */
    @Setter
    @JsonIgnore
    AdminEventRequest request;

    /** Admin events returned by the request. */
    List<AdminEvent> events;

    private AdminEventResponse(List<AdminEvent> events) {
        this.events = events;
    }

    /**
     * Retrieve the next page of results from this response.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    public AdminEventResponse getNextPage() throws AtlanException {
        int from = request.getOffset() < 0 ? 0 : request.getOffset();
        int page = request.getSize() < 0 ? 100 : request.getSize();
        AdminEventRequest next = request.toBuilder().offset(from + page).build();
        return next.search(client);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<AdminEvent> iterator() {
        return new AdminEventResponseIterator(this);
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<AdminEvent> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), CHARACTERISTICS), false);
    }

    /**
     * Allow results to be iterated through without managing paging retrievals.
     */
    private static class AdminEventResponseIterator implements Iterator<AdminEvent> {

        private AdminEventResponse response;
        private int i;

        public AdminEventResponseIterator(AdminEventResponse response) {
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
        public AdminEvent next() {
            return response.getEvents().get(i++);
        }
    }

    public static final class AdminEventResponseDeserializer extends StdDeserializer<AdminEventResponse> {
        private static final long serialVersionUID = 2L;

        public AdminEventResponseDeserializer() {
            this(null);
        }

        public AdminEventResponseDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public AdminEventResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            List<AdminEvent> events = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new AdminEventResponse(events);
        }
    }

    public static final class AdminEventResponseSerializer extends StdSerializer<AdminEventResponse> {
        private static final long serialVersionUID = 2L;

        public AdminEventResponseSerializer() {
            this(null);
        }

        public AdminEventResponseSerializer(Class<AdminEventResponse> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(AdminEventResponse response, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            if (response != null) {
                gen.writeStartArray();
                for (AdminEvent event : response.getEvents()) {
                    gen.writeObject(event);
                }
                gen.writeEndArray();
            }
        }
    }
}
