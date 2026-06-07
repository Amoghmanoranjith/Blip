package mp.org.blip.model.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mp.org.blip.enumeration.HttpMethodTypes;
import mp.org.blip.enumeration.HttpResponseTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpConfig {
    @NotBlank(message = "{http.url.required}")
    private String url;

    @NotNull(message = "{http.method.required}")
    private HttpMethodTypes method;

    @NotNull(message = "{http.response_type.required}")
    @JsonProperty("response_type")
    private HttpResponseTypes responseType;

    private String body;

    private String headers;
}
