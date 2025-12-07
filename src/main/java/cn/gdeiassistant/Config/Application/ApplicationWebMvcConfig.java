package cn.gdeiassistant.Config.Application;

import cn.gdeiassistant.Constant.SettingConstantUtils;
import cn.gdeiassistant.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class ApplicationWebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 登录拦截器不进行拦截的URL列表
     *
     * @return
     */
    @Bean
    public List<String> loginInterceptorExceptionList() {
        return Arrays.asList(SettingConstantUtils.LOGIN_INTERCEPTOR_EXCEPTION_LIST);
    }

    /**
     * 登录拦截器
     *
     * @return
     */
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(loginInterceptorExceptionList());
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor());
    }

    /**
     * 类型转换器
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //字符串值转换为对应的枚举类值
    }

    /**
     * 添加静态资源处理器
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射Vue应用打包后的静态资源
        // 假设Vue的构建输出（dist目录）位于 src/main/webapp/dist
        // 将所有以 /dist/ 开头的请求映射到 /dist/ 目录
        registry.addResourceHandler("/dist/**").addResourceLocations("/dist/");

        // 映射公共静态资源
        registry.addResourceHandler("/static/css**").addResourceLocations("/static/css");
        registry.addResourceHandler("/static/js**").addResourceLocations("/static/js");
        registry.addResourceHandler("/static/img**").addResourceLocations("/static/img");
        registry.addResourceHandler("/doc/**").addResourceLocations("/doc/");
        registry.addResourceHandler("/mp3/**").addResourceLocations("/mp3/");
        registry.addResourceHandler("/font/**").addResourceLocations("/font/");
        // 映射特定文件
        registry.addResourceHandler("/MP_verify_i9vujYHtkV4q7Kgx.txt").addResourceLocations("/MP_verify_i9vujYHtkV4q7Kgx.txt");
    }

    /**
     * 配置默认的Servlet处理器
     * 允许Spring MVC将所有未处理的请求转发给Servlet容器的默认Servlet
     * 这对于处理静态资源（如位于webapp根目录下的index.html）非常重要
     * 并且可以避免DispatcherServlet拦截所有请求导致静态资源无法访问的问题
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 将根路径和所有未匹配的路径都转发到Vue应用的index.html
        // 确保 /dist/index.html 能够被访问到，并且 Vue Router 可以接管路由
        // 这里的 forward:/dist/index.html 会根据 WebApp 根目录下的 /dist/index.html 查找
        registry.addViewController("/").setViewName("forward:/dist/index.html");
        registry.addViewController("/index").setViewName("forward:/dist/index.html");
        registry.addViewController("/{path:[^\\.]*}").setViewName("forward:/dist/index.html");
        registry.addViewController("/**/{path:[^\\.]*}").setViewName("forward:/dist/index.html");

        // 移除或注释掉旧的VIEW_CONTROLLER_NAME_MAP，因为现在Vue Router处理前端路由
        // for (Map.Entry<String, String> entry : SettingConstantUtils.VIEW_CONTROLLER_NAME_MAP.entrySet()) {
        //     registry.addViewController(entry.getKey()).setViewName(entry.getValue());
        // }
        // super.addViewControllers(registry); // 如果不再需要父类实现，可以移除
    }
}
