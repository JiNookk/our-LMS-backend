package jinookk.ourlms.models.vos;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class VideoUrl {
    private String value;

    public VideoUrl() {
    }

    public VideoUrl(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public void update(String value) {
        this.value = value;
    }

    public void delete() {
        this.value = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object other) {
        return other != null &&
                other.getClass().equals(VideoUrl.class) &&
                ((VideoUrl) other).value.equals(this.value);
    }

    @Override
    public String toString() {
        return "VideoUrl value: " + value;
    }
}
