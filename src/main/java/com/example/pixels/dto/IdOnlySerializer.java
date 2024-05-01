package com.example.pixels.dto;

import com.example.pixels.entity.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class IdOnlySerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof User) {
            gen.writeNumber(((User) value).getId());
        }
        else if(value instanceof Movie){
            gen.writeNumber(((Movie) value).getId());
        }
        else if(value instanceof Review){
            gen.writeNumber(((Review) value).getId());
        }
        else if(value instanceof Comment){
            gen.writeNumber(((Comment) value).getId());
        }
        else if(value instanceof ReviewLike){
            gen.writeNumber(((ReviewLike) value).getId());
        }
        else if(value instanceof ReviewDislike){
            gen.writeNumber(((ReviewDislike) value).getId());
        }
        else if(value instanceof CommentLike){
            gen.writeNumber(((CommentLike) value).getId());
        }
        else if(value instanceof CommentDislike){
            gen.writeNumber(((CommentDislike) value).getId());
        }
        else {
            gen.writeNull();
        }
    }
}
