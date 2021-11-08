package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.config.OauthProvider;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProviderRepository {
    private final Map<String, OauthProvider> providers;

    public InMemoryProviderRepository(Map<String, OauthProvider> providers) {
        this.providers = new HashMap<>(providers);
    }

    public OauthProvider findByProviderName(String name) {
        return providers.get(name);
    }
}
