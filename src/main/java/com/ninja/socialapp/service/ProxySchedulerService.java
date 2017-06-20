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
     * This is scheduled to get fired every 15 minutes.
     * </p>
     */
    @Async
    @Scheduled(cron = "0 */15 * * * *")
    public void updateProxies() {
        log.debug("Run scheduled update proxies {}");
        final String USERNAME = "ninja";
        final String PASSWORD = "fifaninja10";
        final String API_URL = "http://api.buyproxies.org/?a=showProxies&pid=39606&key=16f11daa3e601378b177eb03e567b9d3";
        String response = new RestTemplate().getForEntity(API_URL, String.class).getBody();
        if (!response.isEmpty()) {
            List<Proxy> proxies = proxyService.findAllByUsernameAndPassword(USERNAME, PASSWORD);
            String lines[] = response.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line[] = lines[i].split(":");
                Proxy proxy = proxies.size() > i ? proxies.get(i) : new Proxy();
                proxy.setHost(line[0]);
                proxy.setPort(Integer.valueOf(line[1]));
                proxy.setUsername(line[2]);
                proxy.setPassword(line[3]);
                proxyService.save(proxy);
            }
        }
    }
}
