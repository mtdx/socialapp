package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProxySchedulerService {

    private final Logger log = LoggerFactory.getLogger(ProxySchedulerService.class);

    private final ProxyService proxyService;

    public ProxySchedulerService(ProxyService proxyService){
            this.proxyService = proxyService;
    }

    /**
     * We check for new proxies from the buyproxies.org API and we update ours (should update monthly)
     * <p>
     * This is scheduled to get fired every 5 minutes.
     * </p>
     */
    @Async
    @Scheduled(cron = "0 */1 * * * *")
    public void updateProxies() {
        log.debug("Run scheduled update proxies {}");
        final String USERNAME = "ninja";
        final String PASSWORD = "fifaninja10";
        final String API_URL = "http://api.buyproxies.org/?a=showProxies&pid=39606&key=16f11daa3e601378b177eb03e567b9d3";
        String response = new RestTemplate().getForEntity(API_URL, String.class).getBody();
        if (!response.isEmpty()) {
            List<Proxy> proxies = proxyService.findAllByUsernameAndPasswordOrderById(USERNAME, PASSWORD);
            String lines[] = response.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line[] = lines[i].split(":");
                Proxy proxy = new Proxy();
                if (! proxies.isEmpty() && proxies.size() > i) {
                    proxy = proxies.get(i);
                    if (proxySkip(proxy, line))
                        continue; // no point saving
                }

                proxy.setHost(line[0]);
                proxy.setPort(Integer.valueOf(line[1]));
                proxy.setUsername(line[2]);
                proxy.setPassword(line[3]);
                proxyService.save(proxy);
            }
        }
    }

    /**
     * Small utility function
     */
    private boolean proxySkip(Proxy proxy, String line[]) {
        return (proxy.getHost() != null && proxy.getHost().equals(line[0]) &&
            proxy.getPort() != null && Objects.equals(proxy.getPort(), Integer.valueOf(line[1])) &&
            proxy.getUsername() != null && proxy.getUsername().equals(line[2]) &&
            proxy.getPassword() != null && proxy.getPassword().equals(line[3]));
    }
}
