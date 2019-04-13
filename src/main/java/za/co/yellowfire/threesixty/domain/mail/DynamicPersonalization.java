package za.co.yellowfire.threesixty.domain.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Personalization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DynamicPersonalization extends Personalization {

    private Map<String, Object> dynamicTemplateData;

    @JsonProperty("dynamic_template_data")
    public Map<String, Object> getDynamicTemplateData() {
        return this.dynamicTemplateData == null ? Collections.emptyMap() : this.dynamicTemplateData;
    }

    public void addDynamicTemplateData(
            final String key,
            final String value) {

        if (this.dynamicTemplateData == null) {
            this.dynamicTemplateData = new HashMap();
            this.dynamicTemplateData.put(key, value);
        } else {
            this.dynamicTemplateData.put(key, value);
        }

    }
}
