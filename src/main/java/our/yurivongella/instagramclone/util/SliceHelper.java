package our.yurivongella.instagramclone.util;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class SliceHelper {
    public static <T> Slice<T> getSlice(List<T> content, Pageable pageable) {
        boolean hasNext = false;

        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
