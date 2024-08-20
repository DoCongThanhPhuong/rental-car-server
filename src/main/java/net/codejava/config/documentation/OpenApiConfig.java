package net.codejava.config.documentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@EnableWebMvc
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {
    @Value("${openapi.dev-url}")
    private String devUrl;

    @Value("${openapi.prod-url}")
    private String prodUrl;

    @Value("${openapi.contact-email}")
    private String contactEmail;

    @Value("${openapi.contact-name}")
    private String contactName;

    @Value("${openapi.contact-url}")
    private String contactUrl;

    @Value("${openapi.title}")
    private String title;

    @Value("${openapi.version}")
    private String version;

    @Value("${openapi.description}")
    private String description;

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail(contactEmail);
        contact.setName(contactName);
        contact.setUrl(contactUrl);

        Info info = new Info().title(title).version(version).contact(contact).description(description);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
