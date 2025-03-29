package com.example.SpringBootTurialVip.util;

import com.example.SpringBootTurialVip.enums.RelativeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RelativeTypeConverter implements Converter<String, RelativeType> {
//Class có tác dụng giúp convert enum sang string
    @Override
    public RelativeType convert(String source) {
        for (RelativeType type : RelativeType.values()) {
            if (type.getDisplay().equalsIgnoreCase(source.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Loại quan hệ không hợp lệ: " + source);
    }
}