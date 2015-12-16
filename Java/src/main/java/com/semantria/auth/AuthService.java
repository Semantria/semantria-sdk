package com.semantria.auth;

import com.google.gson.Gson;
import com.semantria.utils.AuthRequest;
import com.semantria.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private String appKey;
    private String cookieDir;
    private String key;
    private String secret;
    private String authHost;
    private String cookieFileName = "session.dat";

    public AuthService(String appKey, String authHost) {
        this.appKey = appKey;
        this.authHost = authHost;
        this.cookieDir = System.getProperty("user.home");
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

    public void authWithUsernameAndPassword(String username, String password, boolean reuseExisting) throws CredentialException {
        Map<String, String> cookie = reuseExisting ? this.loadCookieData() : null;
        String requestData = this.getRequestData(username, password);
        AuthRequest req;

        if (cookie != null && cookie.containsKey("credHash") && Utils.getHashCode(username + password).equals(cookie.get("credHash"))) {
            String url = authHost + "/auth/session/" + cookie.get("id") +".json?appkey=" + this.appKey;
            req = AuthRequest.getInstance(url, "GET");
        } else {
            String url = authHost + "/auth/session.json?appkey=" + this.appKey;
            req = AuthRequest.getInstance(url, "POST").body(requestData);
        }

        req.doRequest();
        if (req.getStatus() == 200) {
            Gson gson = new Gson();
            AuthSessionData sessionData = gson.fromJson(req.getResponse(), AuthSessionData.class);
            String sessionId = sessionData.id;
            this.key = sessionData.custom_params.get("key");
            this.secret = sessionData.custom_params.get("secret");

            this.saveCookieData(sessionId, username, password);
        } else if (cookie != null && req.getStatus() == 404) {
            // Probably session expired, lets try again and create new one
            this.authWithUsernameAndPassword(username, password, false);
        } else {
            this.removeCookieData();
            throw new CredentialException("Provided username and password are incorrect");
        }
    }

    private String getRequestData(String username, String password) {
        Map<String, String> requestDataMap = new HashMap<String, String>();
        Gson gson = new Gson();

        requestDataMap.put("password", password);
        if (Utils.isEmail(username)) {
            requestDataMap.put("email", username);
        } else {
            requestDataMap.put("username", username);
        }

        return gson.toJson(requestDataMap);
    }

    private Map<String, String> loadCookieData() {
        String fullPath = this.cookieDir + "/" + this.cookieFileName;
        String sessionData = null;
        File sessionFile = new File(fullPath);

        try {
            if (sessionFile.exists()) {
                sessionData = new String(Files.readAllBytes(sessionFile.toPath()));
            }
        } catch (IOException e) {
            // yes, we will ignore it. and yes, I'm ashamed
        }

        if (sessionData != null && !sessionData.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(sessionData, Map.class);
        } else {
            return null;
        }
    }

    private void saveCookieData(String sessionId, String username, String password) {
        String fullPath = this.cookieDir + "/" + this.cookieFileName;
        File sessionFile = new File(fullPath);
        Gson gson = new Gson();
        Map<String, String> sessionDataMap = new HashMap<String, String>();

        sessionDataMap.put("id", sessionId);
        sessionDataMap.put("credHash", Utils.getHashCode(username + password));

        String sessionData = gson.toJson(sessionDataMap);

        try {
            if (!sessionFile.exists()) {
                sessionFile.createNewFile();
            }

            Files.write(sessionFile.toPath(), sessionData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            // yes, we will ignore it. and yes, I'm ashamed
        }
    }

    private void removeCookieData() {
        String fullPath = this.cookieDir + "/" + this.cookieFileName;
        File sessionFile = new File(fullPath);

        try {
            Files.deleteIfExists(sessionFile.toPath());
        } catch (IOException e) {
            // yes, we will ignore it. and yes, I'm ashamed
        }
    }
}
