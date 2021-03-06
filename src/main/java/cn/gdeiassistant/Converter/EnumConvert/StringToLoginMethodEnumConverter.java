package cn.gdeiassistant.Converter.EnumConvert;

import cn.gdeiassistant.Enum.Method.LoginMethodEnum;
import org.springframework.core.convert.converter.Converter;

/**
 * 将String字符串类型转换为LoginMethodEnum枚举类型
 */
public class StringToLoginMethodEnumConverter implements Converter<String, LoginMethodEnum> {

    @Override
    public LoginMethodEnum convert(String source) {
        return LoginMethodEnum.getEnumByValue(source);
    }
}
