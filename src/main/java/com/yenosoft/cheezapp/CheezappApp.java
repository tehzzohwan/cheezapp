package com.yenosoft.cheezapp;

import com.yenosoft.cheezapp.config.CRLFLogConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
public class CheezappApp {
    private static final Logger LOG = LoggerFactory.getLogger(CheezappApp.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(CheezappApp.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }
        private static void logApplicationStartup(Environment env){
            String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
            String applicationName = env.getProperty("spring.application.name");
            String serverPort = env.getProperty("server.port");
            String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
                .filter(StringUtils::isNotBlank)
                .orElse("/");
            String hostAddress = "localhost";
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                LOG.warn("The host name could not be determined, using `localhost` as fallback");
            }
            LOG.info(
                CRLFLogConverter.CRLF_SAFE_MARKER,
                """

                    ----------------------------------------------------------
                    \tApplication '{}' is running! Access URLs:
                    \tLocal: \t\t{}://localhost:{}{}
                    \tExternal: \t{}://{}:{}{}
                    \tProfile(s): \t{}
                    ----------------------------------------------------------""",
                applicationName,
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
            );

            String configServerStatus = env.getProperty("configserver.status");
            if (configServerStatus == null) {
                configServerStatus = "Not found or not setup for this application";
            }
            LOG.info(
                CRLFLogConverter.CRLF_SAFE_MARKER,
                "\n----------------------------------------------------------\n\t" +
                    "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus
            );
        }
    }
